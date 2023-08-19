package com.huiyu.service.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huiyu.service.core.enums.PicShareStatusEnum;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 图片分享表(PicShare)实体类
 *
 * @author Naccl
 * @date 2023-08-19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("pic_share")
public class PicShare implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;
    /**
     * 图片id
     */
    @TableField("pic_id")
    private Long picId;
    /**
     * 图片uuid
     */
    @TableField("uuid")
    private String uuid;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 作品标题
     */
    @TableField("title")
    private String title;
    /**
     * 点击量
     */
    @TableField("hits")
    private Integer hits;
    /**
     * 点赞量
     */
    @TableField("like_count")
    private Integer likeCount;
    /**
     * 画同款次数
     */
    @TableField("draw_count")
    private Integer drawCount;
    /**
     * 状态
     */
    @TableField("status")
    private PicShareStatusEnum status;
    /**
     * 审核时间
     */
    @TableField("audit_time")
    private LocalDateTime auditTime;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
    /**
     * 是否删除1是0否
     */
    @TableLogic
    @TableField("is_delete")
    private Integer isDelete;
}
