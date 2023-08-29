package com.huiyu.service.core.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huiyu.service.api.entity.User;
import com.huiyu.service.core.entity.PointRecord;
import com.huiyu.service.core.enums.PointOperationSourceEnum;
import com.huiyu.service.core.enums.PointOperationTypeEnum;
import com.huiyu.service.core.enums.PointTypeEnum;
import com.huiyu.service.core.model.dto.PointRecordPageDto;
import com.huiyu.service.core.model.vo.PointRecordPageVo;
import com.huiyu.service.core.model.vo.UserVo;
import org.springframework.web.multipart.MultipartFile;

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
     * 用户积分修改（修改积分的唯一入口）
     *
     * @param userId      用户id
     * @param pointDiff   要修改的积分值
     * @param source      积分来源
     * @param operation   积分修改方式
     * @param requestUuid 请求uuid
     * @param pointType   积分类型(null则根据PointOperationSourceEnum判断是GENERATE_PIC / BACK)
     * @return true/false
     */
    boolean updatePoint(Long userId, Integer pointDiff, PointOperationSourceEnum source, PointOperationTypeEnum operation, String requestUuid, PointTypeEnum pointType);

    /**
     * 用户积分记录分页查询
     *
     * @param page 分页对象
     * @param dto  筛选条件
     * @return 查询结果
     */
    IPage<PointRecordPageVo> pagePointRecord(IPage<PointRecord> page, PointRecordPageDto dto);

    /**
     * 用户访问我的页面
     *
     * @param userId 用户id
     * @return 用户信息
     */
    UserVo getUserInfo(Long userId);

    /**
     * 用户修改头像
     *
     * @param userId 用户id
     * @param file   头像文件
     * @return 是否成功
     */
    boolean updateAvatar(Long userId, MultipartFile file);

    /**
     * 用户修改昵称
     *
     * @param userId   用户id
     * @param nickname 昵称
     * @return 是否成功
     */
    boolean updateNickname(Long userId, String nickname);

    /**
     * 用户签到
     *
     * @param userId 用户id
     * @return 是否成功
     */
    boolean signIn(Long userId);
}
