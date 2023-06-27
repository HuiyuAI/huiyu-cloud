package com.huiyu.service.core.service.submit.chooseStrategy;

import com.huiyu.service.core.model.cmd.Cmd;
import com.huiyu.service.core.service.submit.chooseStrategy.constant.ExecStrategyTypeEnum;

/**
 * @author wAnG
 * @Date 2023-06-28  00:49
 */
public interface ExecChooseStrategy<T extends Cmd> {

    String chooseExecSource(T t);

    ExecStrategyTypeEnum getType();

}
