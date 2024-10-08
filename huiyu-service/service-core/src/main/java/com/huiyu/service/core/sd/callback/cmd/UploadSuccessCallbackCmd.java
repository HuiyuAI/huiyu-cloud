package com.huiyu.service.core.sd.callback.cmd;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 图片上传成功回调指令
 *
 * @author Naccl
 * @date 2023-06-14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadSuccessCallbackCmd {
    /**
     * 图片uuid
     */
    @NotBlank(message = "verification failed")
    @JsonProperty("res_image_uuid")
    private String resImageUuid;
    /**
     * 图片地址uuid
     */
    @NotBlank(message = "verification failed")
    @JsonProperty("res_image_url_uuid")
    private String resImageUrlUuid;
    /**
     * 通信验证token
     */
    @NotBlank(message = "verification failed")
    private String token;
}
