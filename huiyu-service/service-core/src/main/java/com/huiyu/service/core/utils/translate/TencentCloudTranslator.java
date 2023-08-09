package com.huiyu.service.core.utils.translate;

import com.huiyu.service.core.config.TencentCloudConfig;
import com.huiyu.service.core.config.executor.ThreadPoolExecutorDecorator;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tmt.v20180321.TmtClient;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateRequest;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 腾讯云服务工具类
 *
 * @author Naccl
 * @date 2023-08-01
 */
@Slf4j
@Component
public class TencentCloudTranslator {
    private static List<TmtClient> clientList;

    private static List<Long> projectIdList;

    private static AtomicLong count = new AtomicLong(0L);

    private static ThreadPoolExecutorDecorator translateExecutor;

    public TencentCloudTranslator(TencentCloudConfig tencentCloudConfig, @Qualifier("translateExecutor") ThreadPoolExecutorDecorator translateExecutor) {
        TencentCloudTranslator.clientList = new ArrayList<>();
        List<String> secretIdList = tencentCloudConfig.getSecretId();
        List<String> secretKeyList = tencentCloudConfig.getSecretKey();
        TencentCloudTranslator.projectIdList = tencentCloudConfig.getProjectId();
        TencentCloudTranslator.translateExecutor = translateExecutor;

        for (int i = 0; i < secretIdList.size(); i++) {
            Credential cred = new Credential(secretIdList.get(i), secretKeyList.get(i));
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("tmt.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            TencentCloudTranslator.clientList.add(new TmtClient(cred, "ap-shanghai", clientProfile));
        }
    }

    /**
     * 负载均衡多个腾讯云翻译账户
     *
     * @param sourceTextList 待翻译文本List
     * @return 翻译后的文本
     */
    public static List<String> en2ZhTranslateBatch(List<String> sourceTextList) {
        List<CompletableFuture<String>> futureList = new ArrayList<>(sourceTextList.size());
        List<String> resultList = Collections.synchronizedList(Arrays.asList(new String[sourceTextList.size()]));

        for (int i = 0; i < sourceTextList.size(); i++) {
            int finalIndex = i;
            CompletableFuture<String> completableFuture = CompletableFuture
                    .supplyAsync(() -> en2ZhTranslate(sourceTextList.get(finalIndex)), translateExecutor.getThreadPoolExecutor())
                    .whenComplete((result, throwable) -> {
                        if (result != null) {
                            resultList.set(finalIndex, result);
                        }
                    });
            futureList.add(completableFuture);
        }

        try {
            CompletableFuture[] futureArray = futureList.toArray(new CompletableFuture[0]);
            CompletableFuture.allOf(futureArray).get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("腾讯云翻译失败, CompletableFuture.allOf error", e);
        }
        List<String> resList = new ArrayList<>(resultList);
        return resList;
    }

    /**
     * 负载均衡多个腾讯云翻译账户
     *
     * @param sourceText 待翻译文本
     * @return 翻译后的文本
     */
    public static String en2ZhTranslate(String sourceText) {
        sourceText = CustomTranslator.preProcessText(sourceText);
        if (StringUtils.isEmpty(sourceText)) {
            return "";
        }
        int i = (int) (count.incrementAndGet() % clientList.size());
        return en2ZhTranslate(sourceText, clientList.get(i), projectIdList.get(i));
    }

    /**
     * 调用腾讯云翻译接口
     *
     * @param sourceText 待翻译文本
     * @param client     腾讯云翻译客户端
     * @param projectId  腾讯云翻译项目id
     * @return 翻译后的文本
     */
    private static String en2ZhTranslate(String sourceText, TmtClient client, Long projectId) {
        try {
            TextTranslateRequest req = new TextTranslateRequest();
            req.setProjectId(projectId);
            req.setSource("zh");
            req.setTarget("en");
            req.setSourceText(sourceText);

            TextTranslateResponse resp = client.TextTranslate(req);
            HashMap<String, String> respMap = new HashMap<>(8);
            resp.toMap(respMap, "");

            log.info("腾讯云翻译接口调用成功, RequestId: {}, SecretId: {}, SourceText: {}, TargetText: {}, resp: {}", respMap.get("RequestId"), client.getCredential().getSecretId(), sourceText, respMap.get("TargetText"), TextTranslateResponse.toJsonString(resp));
            return respMap.get("TargetText");
        } catch (TencentCloudSDKException e) {
            log.error("腾讯云翻译接口调用失败", e);
            return "";
        }
    }


}
