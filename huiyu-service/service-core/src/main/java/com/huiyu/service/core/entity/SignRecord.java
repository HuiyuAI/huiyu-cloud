package com.huiyu.service.core.entity;

import com.huiyu.service.core.enums.SignRecordStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 签到时间
     */
    private LocalDate signTime;
    /**
     * 签到状态
     */
    private SignRecordStatusEnum status;
    /**
     * 是否删除1是0否
     */
    private Integer isDelete;
}
