package com.huiyu.service.core.sd;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.huiyu.service.core.sd.constant.SDAPIConstant;
import com.huiyu.service.core.sd.dto.Txt2ImgDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * SD HTTP请求
 *
 * @author Naccl
 * @date 2023-06-11
 */
@Slf4j
@Component
public class SDHttpRestTemplate {
    @Autowired
    private RestTemplate restTemplate;

    public void txt2img(Txt2ImgDto dto) {
        String url = SDAPIConstant.BASE_URL + SDAPIConstant.TXT2IMG;
        ResponseEntity<String> response = restTemplate.postForEntity(url, dto, String.class);
        String body = response.getBody();
        JSONObject jsonObject = new JSONObject(body);
        JSONArray imageUuidList = jsonObject.getJSONArray("image_uuid_list");
        for (Object uuid : imageUuidList) {
            String imgUrl = "https://huiyucdn.naccl.top/gen/" + uuid + ".jpg";
            log.info("image url: {}", imgUrl);
        }
    }
}
