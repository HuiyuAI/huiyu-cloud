package com.huiyu.service.core.service.business.impl;

import com.huiyu.service.core.hconfig.config.HotFileConfig;
import com.huiyu.service.core.enums.PointOperationTypeEnum;
import com.huiyu.service.core.enums.PointOperationSourceEnum;
import com.huiyu.service.core.enums.SignRecordStatusEnum;
import com.huiyu.service.core.entity.SignRecord;
import com.huiyu.service.core.service.SignRecordService;
import com.huiyu.service.core.service.business.PointBusiness;
import com.huiyu.service.core.service.business.SignRecordBusiness;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;

@Service
public class SignRecordBusinessImpl implements SignRecordBusiness {

    @Resource
    private SignRecordService signRecordService;

    @Resource
    private PointBusiness pointBusiness;

    @Resource
    private HotFileConfig hotFileConfig;

    @Override
    public boolean signIn(Long userId) {
        // 判断用户是否已签到
        if (!this.isSignIn(userId)) {
            return false;
        }
        // 签到积分奖励
        return pointBusiness.updatePoint(userId, hotFileConfig.getSignInPoint(), PointOperationSourceEnum.SIGN_IN, PointOperationTypeEnum.ADD, null);
    }

    private boolean isSignIn(Long userId) {
        // 判断用户是否已签到
        SignRecord signRecord = signRecordService.getTodayByUserId(userId);
        if (signRecord != null && signRecord.getStatus() == SignRecordStatusEnum.SIGN_IN) {
            return false;
        }
        if (signRecord == null) {
            signRecord = SignRecord.builder()
                    .signTime(LocalDate.now())
                    .isDelete(0)
                    .status(SignRecordStatusEnum.SIGN_IN)
                    .userId(userId).build();
            return signRecordService.insert(signRecord);
        }
        signRecord.setStatus(SignRecordStatusEnum.SIGN_IN);
        return signRecordService.updateToday(signRecord);
    }
}
