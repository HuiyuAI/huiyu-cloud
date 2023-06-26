package com.huiyu.service.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
public class PicPageDto extends PageDto {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 查询截止时间
     */
    @NotNull(message = "queryDeadline不能为空")
    private LocalDateTime queryDeadline;
}
