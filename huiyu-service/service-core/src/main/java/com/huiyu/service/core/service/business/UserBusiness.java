package com.huiyu.service.core.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huiyu.service.api.entity.User;
import com.huiyu.service.core.entity.PointRecord;
import com.huiyu.service.core.enums.PointOperationSourceEnum;
import com.huiyu.service.core.enums.PointOperationTypeEnum;
import com.huiyu.service.core.model.dto.PointRecordPageDto;
import com.huiyu.service.core.model.vo.PointRecordPageVo;

/**
 * @author Naccl
 * @date 2023-08-11
 */
public interface UserBusiness {
    /**
     * 修改用户数据
     *
     * @param user 实例对象
     * @return true/false
     */
    boolean updateUser(User user);

    /**
     * 用户积分修改
     *
     * @param userId      用户id
     * @param point       积分值
     * @param source      积分来源
     * @param operation   积分修改方式
     * @param requestUuid 请求uuid
     * @return
     */
    boolean updatePoint(Long userId, Integer point, PointOperationSourceEnum source, PointOperationTypeEnum operation, String requestUuid);

    /**
     * 用户积分记录分页查询
     *
     * @param page 分页对象
     * @param dto  筛选条件
     * @return 查询结果
     */
    IPage<PointRecordPageVo> pagePointRecord(IPage<PointRecord> page, PointRecordPageDto dto);
}
