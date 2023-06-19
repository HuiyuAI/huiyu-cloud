package com.huiyu.service.core.service.bussiness.impl;

import com.huiyu.service.core.constant.IntegralOperationRecordEnum;
import com.huiyu.service.core.constant.IntegralSourceRecordEnum;
import com.huiyu.service.core.constant.SignRecordStatusEnum;
import com.huiyu.service.core.entity.SignRecord;
import com.huiyu.service.core.service.SignRecordService;
import com.huiyu.service.core.service.bussiness.IntegralRecordBussiness;
import com.huiyu.service.core.service.bussiness.SignRecordBussiness;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;

@Service
public class SignRecordBussinessImpl implements SignRecordBussiness {

    @Resource
    private SignRecordService signRecordService;

    @Resource
    private IntegralRecordBussiness integralRecordBussiness;

    @Override
    public boolean signIn(Long userId) {
        // 判断用户是否已签到
        if (!this.isSignIn(userId)) {
            return false;
        }
        // 签到积分奖励
        return integralRecordBussiness.updateIntegral(userId, 1, IntegralSourceRecordEnum.SING_IN, IntegralOperationRecordEnum.ADD);
    }

    private boolean isSignIn(Long userId) {
        // 判断用户是否已签到
        SignRecord signRecord = signRecordService.getTodayByUserId(userId);
        if (signRecord != null && signRecord.getStatus() == SignRecordStatusEnum.GENERATED) {
            return false;
        }
        if (signRecord == null) {
            signRecord = SignRecord.builder()
                    .signTime(LocalDate.now())
                    .isDelete(0)
                    .status(SignRecordStatusEnum.GENERATED)
                    .userId(userId).build();
            return signRecordService.insert(signRecord);
        }
        signRecord.setStatus(SignRecordStatusEnum.GENERATED);
        return signRecordService.updateToday(signRecord);
    }
}
