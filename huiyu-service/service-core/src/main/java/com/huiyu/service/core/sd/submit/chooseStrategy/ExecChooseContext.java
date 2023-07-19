package com.huiyu.service.core.sd.submit.chooseStrategy;

import com.huiyu.service.core.hconfig.config.AIExampleConfig;
import com.huiyu.service.core.config.executor.MonitorLinkedBlockingQueue;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wAnG
 * @Date 2023-06-28  00:54
 */
public class ExecChooseContext {

    public static final Map<AIExampleConfig.ExampleItem, BigInteger> examplePoint = new ConcurrentHashMap<>();

    public static final Map<String, MonitorLinkedBlockingQueue<Byte>> submitQueueList = new HashMap<>();

}
