package com.huiyu.service.core.utils.upload;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 文件上传工具类
 *
 * @author Naccl
 * @date 2021-11-11
 */
@Component
public class UploadUtils {
    private static FileUploadChannel uploadChannel;

    public UploadUtils(@Qualifier("upyunChannel") FileUploadChannel uploadChannel) {
        UploadUtils.uploadChannel = uploadChannel;
    }

    /**
     * 通过指定方式上传文件
     *
     * @param image 需要保存的图片
     * @return 访问图片的URL
     * @throws Exception
     */
    public static String upload(FileUploadChannel.ImageResource image) throws Exception {
        return uploadChannel.upload(image);
    }

}
