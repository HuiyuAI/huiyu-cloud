package com.huiyu.service.core.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigChangeEvent;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.config.listener.impl.AbstractConfigChangeListener;
import com.huiyu.service.core.Hconfig.HConfigOnChange;
import com.huiyu.service.core.Hconfig.annotation.HConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author wAnG
 * @Date 2023-06-23  16:24
 */
@Slf4j
@Configuration
public class NacosConfigListener implements InitializingBean, ApplicationContextAware {

    @Resource
    private NacosConfigManager nacosConfigManager;

    private ApplicationContext context;

    @Value("${spring.cloud.nacos.config.server-addr}")
    private String addr;

    //    @Value("${spring.cloud.nacos.config.namespace}")
    private String namespace = "public";

    @Value("${spring.cloud.nacos.config.username}")
    private String username;

    @Value("${spring.cloud.nacos.config.password}")
    private String password;

    private ConfigService configService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        configService = getConfigService();
        Map<String, Object> beanMap = context.getBeansWithAnnotation(HConfig.class);
        beanMap.values().stream()
                .map(bean -> (HConfigOnChange<?>) bean)
                .forEach(bean -> {
                    Class<? extends HConfigOnChange> aClass = bean.getClass();
                    HConfig annotation = aClass.getAnnotation(HConfig.class);
                    String dataId = annotation.dataId();
                    String group = annotation.group();
                    try {
                        log.info("加载自定义配置文件dataId:{},group:{}", dataId, group);
                        changeConfig(aClass, bean);
                        AbstractConfigChangeListener listener = new AbstractConfigChangeListener() {
                            @Override
                            public void receiveConfigChange(ConfigChangeEvent configChangeEvent) {
                                try {
                                    log.info("自定义配置文件变更dataId:{},group:{}", dataId, group);
                                    changeConfig(aClass, bean);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        };
                        this.nacosConfigManager
                                .getConfigService()
                                .addListener(dataId, group, listener);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void changeConfig(Class<? extends HConfigOnChange> aClass, HConfigOnChange bean) throws Exception {
        HConfig annotation = aClass.getAnnotation(HConfig.class);
        String dataId = annotation.dataId();
        String group = annotation.group();
        String config = configService.getConfig(dataId, group, 3000);
        Method[] methods = aClass.getMethods();
        Method onChangeMethod = Arrays.stream(methods)
                .filter(method -> method.getName().equals("onChange"))
                .findFirst().orElse(null);
        if (Objects.nonNull(onChangeMethod)) {
            String jsonStr = null;
            Class<?> parameterTypes = onChangeMethod.getParameterTypes()[0];
            if (dataId.endsWith(".yaml") && StringUtils.isNotEmpty(config)) {
                Yaml yaml = new Yaml();
                Map<String, Object> map = yaml.load(config);
                jsonStr = JSONUtil.toJsonStr(map);
            } else if (dataId.endsWith(".json") && StringUtils.isNotEmpty(config)) {
                jsonStr = config;
            }
            Object param = JSONUtil.toBean(jsonStr, parameterTypes);
            bean.onChange(param);
        }
    }

    private ConfigService getConfigService() throws NacosException {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", addr);
        properties.setProperty("namespace", namespace);
        properties.setProperty("username", username);
        properties.setProperty("password", password);
        return NacosFactory.createConfigService(properties);
    }
}
