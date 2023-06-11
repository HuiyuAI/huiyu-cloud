package com.huiyu.service.core.entity;

import com.huiyu.service.core.constant.StateEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PicExt implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 图片id
     */
    private Long picId;
    /**
     * 点击次数
     */
    private Integer views;
    /**
     * 是否可用
     */
    private StateEnum enable;
    /**
     * 图片地址
     */
    private String path;
}
