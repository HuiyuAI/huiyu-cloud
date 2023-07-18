package com.huiyu.service.core.model.cmd;

import lombok.Data;

/**
 * @author wAnG
 * @Date 2023-06-11  22:04
 */
@Data
public class Cmd {
    private Long userId;

    /**
     * 积分
     */
    private Integer point;
}
