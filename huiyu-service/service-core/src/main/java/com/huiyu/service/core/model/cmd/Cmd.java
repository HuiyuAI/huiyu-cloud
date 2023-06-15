package com.huiyu.service.core.model.cmd;

import com.huiyu.service.core.sd.dto.Dto;

/**
 * @author wAnG
 * @Date 2023-06-11  22:04
 */
public abstract class Cmd {
    public abstract Dto toDto();
}
