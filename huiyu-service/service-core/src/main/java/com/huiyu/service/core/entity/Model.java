package com.huiyu.service.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 模型表(Model)实体类
 *
 * @author Naccl
 * @date 2023-06-20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("model")
public class Model implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 模型id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 名称
     */
    @TableField("name")
    private String name;
    /**
     * 分类
     */
    @TableField("category")
    private String category;
    /**
     * 描述
     */
    @TableField("description")
    private String description;
    /**
     * 封面图片url
     */
    @TableField("cover_url")
    private String coverUrl;
    /**
     * 编码
     */
    @TableField("code")
    private String code;
    /**
     * 首选vae
     */
    @TableField("vae")
    private String vae;
    /**
     * 首选采样器
     */
    @TableField("sampler")
    private String sampler;
    /**
     * 首选高清化算法
     */
    @TableField("hr_upscaler")
    private String hrUpscaler;
    /**
     * 搭配lora
     */
    @TableField("lora")
    private String lora;
    /**
     * 排序
     */
    @TableField("priority")
    private Integer priority;
    /**
     * 是否启用1是0否
     */
    @TableField("enabled")
    private Integer enabled;
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
