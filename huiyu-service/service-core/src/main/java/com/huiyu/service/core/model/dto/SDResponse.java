package com.huiyu.service.core.model.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * SD通用响应实体
 *
 * @author Naccl
 * @date 2023-06-18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SDResponse<T> implements Serializable {
    private Integer code;
    private String msg;
    private JsonNode data;
}
