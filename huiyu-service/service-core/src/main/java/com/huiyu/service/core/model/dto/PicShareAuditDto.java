package com.huiyu.service.core.model.dto;

import com.huiyu.service.core.enums.PicShareStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Naccl
 * @date 2023-08-22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PicShareAuditDto {
    /*
     * 图片id列表
     */
    private List<Long> picIdList;
    /**
     * 审核状态
     */
    private PicShareStatusEnum status;
}
