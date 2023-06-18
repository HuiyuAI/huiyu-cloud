package com.huiyu.service.core.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Naccl
 * @date 2023-06-18
 */
@Component
public class HuiyuConstant {

    public static String callbackToken;

    public static String cdnUrlGen;

    public static final String imageSuffix = ".jpg";

    @Value("${huiyu.sd.callback-token}")
    public void setCallbackToken(String callbackToken) {
        HuiyuConstant.callbackToken = callbackToken;
    }

    @Value("${huiyu.cdn.url-gen}")
    public void setCdnUrlGen(String cdnUrlGen) {
        HuiyuConstant.cdnUrlGen = cdnUrlGen;
    }
}
