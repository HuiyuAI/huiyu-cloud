package com.huiyu.service.core.service.submit.chooseStrategy;

import com.huiyu.service.core.Hconfig.config.AIExampleConfig;
import com.huiyu.service.core.model.cmd.Cmd;
import com.huiyu.service.core.service.submit.chooseStrategy.constant.ExecStrategyTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wAnG
 * @Date 2023-06-28  00:51
 */
@Component
@Slf4j
public class PointExecChooseStrategy<T extends Cmd> implements ExecChooseStrategy<T> {

    @Resource
    private AIExampleConfig aiExampleConfig;

    Map<AIExampleConfig.ExampleItem, BigInteger> examplePoint = ExecChooseContext.examplePoint;


    @Override
    public String chooseExecSource(T t) {
        List<AIExampleConfig.ExampleItem> exampleItems = aiExampleConfig.getExampleItems();
        AIExampleConfig.ExampleItem exampleItem = exampleItems.stream()
                .min(Comparator.comparing(item -> examplePoint.getOrDefault(item, BigInteger.ZERO)))
                .orElseGet(null);
        if (Objects.isNull(exampleItem)) {
            log.error("执行源为空:userId:{}", t.getUserId());
            return null;
        }
        synchronized (exampleItem) {
            BigInteger point = examplePoint.getOrDefault(exampleItem, BigInteger.ZERO);

            examplePoint.put(exampleItem, point.add(new BigInteger(String.valueOf(t.getPoint() * exampleItem.getEfficiency()))));
        }
        return exampleItem.getSource();
    }

    @Override
    public ExecStrategyTypeEnum getType() {
        return ExecStrategyTypeEnum.POINT;
    }
}
