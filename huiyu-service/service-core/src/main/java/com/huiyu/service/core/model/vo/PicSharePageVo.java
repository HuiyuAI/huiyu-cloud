package com.huiyu.service.core.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Naccl
 * @date 2023-08-21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PicSharePageVo {
    /**
     * 图片uuid
     */
    private String uuid;
    /**
     * 图片地址
     */
    private String path;
    /**
     * 宽
     */
    private Integer width;
    /**
     * 高
     */
    private Integer height;
    /**
     * 投稿人昵称
     */
    private String userNickname;
    /**
     * 投稿人头像
     */
    private String userAvatar;
    /**
     * 作品标题
     */
    private String title;
    /**
     * 点赞量
     */
    private Integer likeCount;
}
