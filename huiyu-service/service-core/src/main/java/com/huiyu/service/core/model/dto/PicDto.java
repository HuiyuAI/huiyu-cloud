package com.huiyu.service.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Naccl
 * @date 2023-06-25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PicDto {
    /**
     * uuid
     */
    private String uuid;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 查询截止时间
     */
    @NotNull(message = "异常错误")
    private LocalDateTime queryDeadline;
}
