package com.huiyu.service.core.utils.upload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件上传方式
 *
 * @author Naccl
 * @date 2022-01-23
 */
public interface FileUploadChannel {
    /**
     * 通过指定方式上传文件
     *
     * @param image 需要保存的图片
     * @return 访问图片的URL
     * @throws Exception
     */
    String upload(ImageResource image) throws Exception;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class ImageResource {
        byte[] data;
        /**
         * 图片保存路径前缀
         */
        String pathPrefix;
        /**
         * 图片拓展名 jpg png
         */
        String type;
    }
}
