package com.huiyu.service.core.sd.validator;

import com.huiyu.service.core.model.cmd.Cmd;

/**
 * @Author: wAnG
 * @Date: 2023-06-11  22:04
 */
public interface ICmdValidator<T extends Cmd> {

    boolean validate(T t);

}
