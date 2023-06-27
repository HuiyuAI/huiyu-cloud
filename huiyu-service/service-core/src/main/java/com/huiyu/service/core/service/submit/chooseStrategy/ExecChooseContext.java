package com.huiyu.service.core.service.submit.chooseStrategy;

import com.huiyu.service.core.Hconfig.AIExampleConfig;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wAnG
 * @Date 2023-06-28  00:54
 */
public class ExecChooseContext {

    public static final Map<AIExampleConfig.ExampleItem, BigInteger> examplePoint = new ConcurrentHashMap<>();

}
