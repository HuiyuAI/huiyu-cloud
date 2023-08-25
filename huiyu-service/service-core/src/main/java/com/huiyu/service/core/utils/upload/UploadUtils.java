package com.huiyu.service.core.utils.upload;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

    /**
     * 缩放压缩并上传
     *
     * @param file 需要保存的图片
     * @return 访问图片的URL
     * @throws Exception IO异常
     */
    public static String uploadWithResizeCompress(MultipartFile file) throws Exception {
        byte[] bytes = resizeAndCompress(file.getInputStream(), 150, 0.8f);

        FileUploadChannel.ImageResource image = new FileUploadChannel.ImageResource(bytes, "/avatar", "jpg");
        return upload(image);
    }

    /**
     * 缩放并压缩图片
     *
     * @param is   图片输入流
     * @param size 缩放后的图片大小
     * @return 压缩后的图片字节数组
     * @throws IOException IO异常
     */
    private static byte[] resizeAndCompress(InputStream is, int size, double quality) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnails.of(is)
                .size(size, size)
                .keepAspectRatio(false)
                .outputQuality(quality)
                .toOutputStream(os);
        return os.toByteArray();
    }

}
