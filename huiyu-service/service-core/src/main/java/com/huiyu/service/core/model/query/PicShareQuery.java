package com.huiyu.service.core.model.query;

import com.huiyu.service.core.enums.PicStatusEnum;
import com.huiyu.service.core.enums.TaskTypeEnum;
import com.huiyu.service.core.sd.constant.ImageQualityEnum;
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
     * 创建时间开始
     */
    private LocalDateTime createTimeStart;
    /**
     * 创建时间结束
     */
    private LocalDateTime createTimeEnd;
}
