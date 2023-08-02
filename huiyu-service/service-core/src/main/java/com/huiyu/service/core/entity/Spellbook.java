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
 * (Spellbook)实体类
 *
 * @author Naccl
 * @date 2023-08-03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("spellbook")
public class Spellbook implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 中文描述
     */
    @TableField("name")
    private String name;
    /**
     * 英文prompt
     */
    @TableField("prompt")
    private String prompt;
    /**
     * 标签
     */
    @TableField("title")
    private String title;
    /**
     * 子标签
     */
    @TableField("subtitle")
    private String subtitle;
    /**
     * 排序
     */
    @TableField("priority")
    private String priority;
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
