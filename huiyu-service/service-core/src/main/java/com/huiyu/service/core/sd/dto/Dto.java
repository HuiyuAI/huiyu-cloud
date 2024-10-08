package com.huiyu.service.core.sd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wAnG
 * @Date 2023-06-12  23:48
 */
@Data
public class Dto implements Serializable {
    /**
     * 结果图uuid
     */
    @JsonProperty("res_image_uuid")
    private String resImageUuid;
    /**
     * 结果图地址uuid
     */
    @JsonProperty("res_image_url_uuid")
    private String resImageUrlUuid;
}
