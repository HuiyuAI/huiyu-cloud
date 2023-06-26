package com.huiyu.service.core.model.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Naccl
 * @date 2023-06-26
 */
@Data
public class PageDto {
    @Min(value = 1, message = "pageNum不能小于1")
    @NotNull(message = "pageNum不能为空")
    Integer pageNum;

    @Max(value = 500, message = "pageSize不能大于500")
    @Min(value = 1, message = "pageSize不能小于1")
    @NotNull(message = "pageSize不能为空")
    Integer pageSize;
}
