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
 * @date 2023-08-20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PicSharePageDto extends PageDto {
    /**
     * 查询截止时间
     */
    @NotNull(message = "queryDeadline不能为空")
    private LocalDateTime queryDeadline;
}
