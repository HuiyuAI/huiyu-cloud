package com.huiyu.service.core.model.query;

import com.huiyu.service.core.enums.PicShareStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Naccl
 * @date 2023-08-21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PicShareQuery {
    /**
     * 图片uuid
     */
    private String uuid;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 模型id
     */
    private Long modelId;
    /**
     * 审核状态
     */
    private PicShareStatusEnum status;
    /**
     * 投稿时间开始
     */
    private LocalDateTime createTimeStart;
    /**
     * 投稿时间结束
     */
    private LocalDateTime createTimeEnd;
}
