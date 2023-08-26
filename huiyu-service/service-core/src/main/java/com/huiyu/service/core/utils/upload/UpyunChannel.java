package com.huiyu.service.core.utils.upload;

import cn.hutool.core.util.IdUtil;
import com.huiyu.service.core.config.UpyunProperties;
import com.huiyu.common.web.exception.BizException;
import com.upyun.RestManager;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.stereotype.Component;

/**
 * 又拍云存储
 *
 * @author Naccl
 * @date 2022-05-26
 */
@Slf4j
@Component
public class UpyunChannel implements FileUploadChannel {
    private RestManager manager;
    private UpyunProperties upyunProperties;

    public UpyunChannel(UpyunProperties upyunProperties) {
        this.upyunProperties = upyunProperties;
        this.manager = new RestManager(upyunProperties.getBucketName(), upyunProperties.getUsername(), upyunProperties.getPassword());
    }

    @Override
    public String upload(ImageResource image) throws Exception {
        String fileAbsolutePath = "/upload" + image.getPathPrefix() + "/" + IdUtil.fastUUID() + "." + image.getType();
        log.info("又拍云上传文件: {}", fileAbsolutePath);
        Response response = manager.writeFile(fileAbsolutePath, image.getData(), null);
        if (!response.isSuccessful()) {
            throw new BizException("又拍云上传失败");
        }
        String res = upyunProperties.getDomain() + fileAbsolutePath;
        log.info("又拍云上传成功: {}", res);
        return res;
    }
}
