package com.huiyu.service.core.utils.upload;

import cn.hutool.core.util.IdUtil;
import com.huiyu.service.core.config.UpyunProperties;
import com.huiyu.common.web.exception.BizException;
import com.upyun.RestManager;
import okhttp3.Response;
import org.springframework.stereotype.Component;

/**
 * 又拍云存储
 *
 * @author Naccl
 * @date 2022-05-26
 */
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
        Response response = manager.writeFile(fileAbsolutePath, image.getData(), null);
        if (!response.isSuccessful()) {
            throw new BizException("又拍云上传失败");
        }
        return upyunProperties.getDomain() + fileAbsolutePath;
    }
}
