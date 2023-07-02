package com.huiyu.service.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * (UserIdSender)实体类
 *
 * @author Naccl
 * @date 2023-07-03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_id_sender")
public class UserIdSender implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 200000~999999
     */
    @TableField("user_id")
    private Long userId;
}
