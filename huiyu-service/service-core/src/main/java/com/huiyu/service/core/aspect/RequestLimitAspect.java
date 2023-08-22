package com.huiyu.service.core.aspect;

import com.huiyu.common.core.result.R;
import com.huiyu.common.core.util.JacksonUtils;
import com.huiyu.common.redis.constant.RedisKeyConstant;
import com.huiyu.common.web.util.JwtUtils;
import com.huiyu.service.core.aspect.annotation.RequestLimiter;
import com.huiyu.service.core.entity.RequestLimitLog;
import com.huiyu.service.core.service.RequestLimitLogService;
import com.huiyu.service.core.utils.IdUtils;
import com.huiyu.service.core.utils.IpAddressUtils;
import com.huiyu.service.core.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Order(1)
@Aspect
@Component
public class RequestLimitAspect {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RequestLimitLogService requestLimitLogService;

    @Around("@annotation(requestLimiter)")
    public Object limitRequestFrequency(ProceedingJoinPoint joinPoint, RequestLimiter requestLimiter) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        int seconds = requestLimiter.seconds();
        int maxCount = requestLimiter.maxCount();
        String ip = IpAddressUtils.getIpAddress(request);
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String redisKey = RedisKeyConstant.REQUEST_LIMITER + ":" + ip + ":" + method + ":" + requestURI;
        Object redisResult = redisTemplate.opsForValue().get(redisKey);
        Integer count = JacksonUtils.toBean(redisResult, Integer.class);
        if (count == null) {
            // 在规定周期内第一次访问，存入redis
            redisTemplate.opsForValue().set(redisKey, 1, seconds, TimeUnit.SECONDS);
        } else {
            if (count >= maxCount) {
                log.warn("用户IP: {}, 请求: {} 的次数超过了限制 {}", ip, requestURI, maxCount);

                Map<String, Object> requestParams = RequestUtils.getRequestParams(joinPoint);
                String param = StringUtils.substring(JacksonUtils.toJsonStr(requestParams), 0, 2000);

                RequestLimitLog requestLimitLog = RequestLimitLog.builder()
                        .id(IdUtils.nextSnowflakeId())
                        .userId(JwtUtils.getUserId())
                        .ip(ip)
                        .method(method)
                        .uri(requestURI)
                        .param(param)
                        .createTime(LocalDateTime.now())
                        .build();
                requestLimitLogService.insert(requestLimitLog);

                return R.error(requestLimiter.msg());
            } else {
                // 没超出访问限制次数
                // fix 如果刚好此时key过期了，increment会设置一个永久的key，所以需要设置后判断一下过期时间，永不过期或不存在则重置key TODO 后面做成滑动窗口
                redisTemplate.opsForValue().increment(redisKey, 1);
                Long expireSeconds = redisTemplate.opsForValue().getOperations().getExpire(redisKey);
                if (expireSeconds == -1 || expireSeconds == -2) {
                    redisTemplate.opsForValue().set(redisKey, 1, seconds, TimeUnit.SECONDS);
                }
            }
        }
        return joinPoint.proceed();
    }
}
