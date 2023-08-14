package com.huiyu.service.core.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huiyu.service.api.entity.User;
import com.huiyu.service.core.entity.PointRecord;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.enums.PointOperationSourceEnum;
import com.huiyu.service.core.enums.PointOperationTypeEnum;
import com.huiyu.service.core.enums.PointTypeEnum;
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
     * @param dailyPoint  要修改的每日积分值
     * @param point       要修改的永久积分值
     * @param source      积分来源
     * @param operation   积分修改方式
     * @param requestUuid 请求uuid
     * @param pointType   积分类型(null则自动判断)
     * @param task        任务(扣减积分时自动设置积分类型)
     * @return true/false
     */
    boolean updatePoint(Long userId, Integer dailyPoint, Integer point, PointOperationSourceEnum source, PointOperationTypeEnum operation, String requestUuid, PointTypeEnum pointType, Task task);

    /**
     * 用户积分记录分页查询
     *
     * @param page 分页对象
     * @param dto  筛选条件
     * @return 查询结果
     */
    IPage<PointRecordPageVo> pagePointRecord(IPage<PointRecord> page, PointRecordPageDto dto);
}
