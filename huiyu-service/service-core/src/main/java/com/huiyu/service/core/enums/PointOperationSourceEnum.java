package com.huiyu.service.core.enums;

import com.huiyu.common.core.util.JacksonUtils;
import com.huiyu.common.web.exception.BizException;
import com.huiyu.service.api.entity.User;
import com.huiyu.service.core.entity.PointRecord;
import com.huiyu.service.core.model.bo.UpdatePointHandlerBO;
import com.huiyu.service.core.service.PointRecordService;
import com.huiyu.service.core.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Getter
@ToString
@AllArgsConstructor
public enum PointOperationSourceEnum implements BaseEnum<String> {
    GENERATE_PIC("generatePic", "生成图片") {
        @Override
        public void checkParam(PointOperationTypeEnum operation, PointTypeEnum pointType) {
            if (operation != PointOperationTypeEnum.REDUCE) {
                throw new IllegalArgumentException("参数错误");
            }
        }

        @Override
        public UpdatePointHandlerBO updatePointHandler(Long userId, Integer pointDiff, PointOperationTypeEnum operationType, PointTypeEnum pointType, String requestUuid) {
            User user = userService.queryByUserId(userId);
            Integer dailyPoint = user.getDailyPoint();
            Integer point = user.getPoint();

            // 要增减的每日积分、永久积分
            Integer targetDailyPointDiff = 0;
            Integer targetPointDiff = 0;

            // 判断用户积分是否足够
            if (dailyPoint + point < pointDiff) {
                throw new BizException("积分不足");
            }

            // 到这里时，总积分肯定足够
            // 先判断每日积分是否为0
            if (dailyPoint > 0) {
                // 每日积分不为0，判断每日积分是否足够
                if (dailyPoint >= pointDiff) {
                    // 每日积分足够，直接扣除每日积分
                    pointType = PointTypeEnum.DAILY_POINT;
                    targetDailyPointDiff = -pointDiff;
                } else {
                    // 每日积分不够，扣除每日积分后，再扣除永久积分
                    pointType = PointTypeEnum.MIX_POINT;
                    targetDailyPointDiff = -dailyPoint;
                    targetPointDiff = -(pointDiff - dailyPoint);
                }
            } else {
                // 每日积分为0，直接扣除永久积分
                pointType = PointTypeEnum.POINT;
                targetPointDiff = -pointDiff;
            }

            return new UpdatePointHandlerBO(targetDailyPointDiff, targetPointDiff, operationType, pointType);
        }
    },
    FAIL_RETURN("failReturn", "任务失败返还") {
        @Override
        public void checkParam(PointOperationTypeEnum operation, PointTypeEnum pointType) {
            if (operation != PointOperationTypeEnum.ADD) {
                throw new IllegalArgumentException("参数错误");
            }
        }

        @Override
        public UpdatePointHandlerBO updatePointHandler(Long userId, Integer pointDiff, PointOperationTypeEnum operationType, PointTypeEnum pointType, String requestUuid) {
            // 要增减的每日积分、永久积分
            Integer targetDailyPointDiff = 0;
            Integer targetPointDiff = 0;

            // 返还积分，先根据requestUuid查询积分流水表
            PointRecord pointRecord = pointRecordService.getByRequestUuid(requestUuid);
            if (pointRecord == null) {
                // 按代码执行顺序，这里肯定能查到，查不到就是task执行前代码异常了
                log.error("任务失败返还, 积分流水表查询失败, userId: {}, pointDiff: {}, requestUuid: {}", userId, pointDiff, requestUuid);
                throw new BizException("异常错误");
            }
            // 判断是否使用了永久积分，且永久积分是否还有剩余返还数值，优先返还永久积分
            if (pointRecord.getPoint() > 0 && pointRecord.getPoint() - pointRecord.getReturnPoint() > 0) {
                // 还有可返还的永久积分，判断剩余数值是否足够
                if (pointRecord.getPoint() - pointRecord.getReturnPoint() >= pointDiff) {
                    // 剩余返还数值足够，直接返还永久积分
                    pointType = PointTypeEnum.POINT;
                    targetPointDiff = pointDiff;
                    // 更新积分流水表，记录返还数值
                    pointRecord.setReturnPoint(pointRecord.getReturnPoint() + pointDiff);
                    pointRecordService.updatePointRecord(pointRecord);
                } else {
                    // 剩余返还数值不够，先返还永久积分，再返还每日积分
                    pointType = PointTypeEnum.MIX_POINT;
                    targetPointDiff = pointRecord.getPoint() - pointRecord.getReturnPoint();
                    targetDailyPointDiff = pointDiff - (pointRecord.getPoint() - pointRecord.getReturnPoint());
                    // 更新积分流水表，记录返还数值
                    pointRecord.setReturnPoint(pointRecord.getReturnPoint() + targetPointDiff);
                    pointRecord.setReturnDailyPoint(pointRecord.getReturnDailyPoint() + targetDailyPointDiff);
                    pointRecordService.updatePointRecord(pointRecord);
                }
            } else {
                // 无可返还的永久积分，判断每日积分是否还有剩余返还数值
                if (pointRecord.getDailyPoint() > 0 && pointRecord.getDailyPoint() - pointRecord.getReturnDailyPoint() > 0) {
                    // 还有可返还的每日积分，判断剩余数值是否足够
                    if (pointRecord.getDailyPoint() - pointRecord.getReturnDailyPoint() >= pointDiff) {
                        // 剩余返还数值足够，直接返还每日积分
                        pointType = PointTypeEnum.DAILY_POINT;
                        targetDailyPointDiff = pointDiff;
                        // 更新积分流水表，记录返还数值
                        pointRecord.setReturnDailyPoint(pointRecord.getReturnDailyPoint() + pointDiff);
                        pointRecordService.updatePointRecord(pointRecord);
                    } else {
                        // 到这里肯定是代码有问题
                        log.error("积分流水表中剩余可返回每日积分不足, userId: {}, pointDiff: {}, requestUuid: {}, pointType: {}, pointRecord: {}", userId, pointDiff, requestUuid, pointType, JacksonUtils.toJsonStr(pointRecord));
                        throw new BizException("异常错误");
                    }
                } else {
                    // 每日积分和永久积分都没有剩余返还数值，到这里肯定是代码有问题
                    log.error("积分流水表中没有可返还的积分, userId: {}, pointDiff: {}, requestUuid: {}, pointType: {}, pointRecord: {}", userId, pointDiff, requestUuid, pointType, JacksonUtils.toJsonStr(pointRecord));
                    throw new BizException("异常错误");
                }
            }

            return new UpdatePointHandlerBO(targetDailyPointDiff, targetPointDiff, operationType, pointType);
        }
    },
    REGISTER("register", "注册") {
        @Override
        public void checkParam(PointOperationTypeEnum operation, PointTypeEnum pointType) {
            if (operation != PointOperationTypeEnum.ADD) {
                throw new IllegalArgumentException("参数错误");
            }
            if (pointType != PointTypeEnum.POINT) {
                throw new IllegalArgumentException("参数错误");
            }
        }

        @Override
        public UpdatePointHandlerBO updatePointHandler(Long userId, Integer pointDiff, PointOperationTypeEnum operationType, PointTypeEnum pointType, String requestUuid) {
            return new UpdatePointHandlerBO(0, pointDiff, operationType, pointType);
        }
    },
    SIGN_IN("signIn", "每日签到") {
        @Override
        public void checkParam(PointOperationTypeEnum operation, PointTypeEnum pointType) {
            if (operation != PointOperationTypeEnum.ADD) {
                throw new IllegalArgumentException("参数错误");
            }
            if (pointType != PointTypeEnum.DAILY_POINT) {
                throw new IllegalArgumentException("参数错误");
            }
        }

        @Override
        public UpdatePointHandlerBO updatePointHandler(Long userId, Integer pointDiff, PointOperationTypeEnum operationType, PointTypeEnum pointType, String requestUuid) {
            // 在每日签到中，pointDiff是目标值，不是差值，要根据用户当前每日积分计算差值
            User user = userService.queryByUserId(userId);
            Integer userDailyPoint = user.getDailyPoint();

            Integer targetDailyPointDiff = pointDiff - userDailyPoint;
            if (targetDailyPointDiff <= 0) {
                // 热配的签到赠送积分数量可能小于用户当前每日积分，这种情况也不需要签到重置
                log.info("用户每日积分已满，不需要签到重置, userId: {}, pointDiff: {}, userDailyPoint: {}", userId, pointDiff, userDailyPoint);
                return null;
            }
            return new UpdatePointHandlerBO(targetDailyPointDiff, 0, operationType, pointType);
        }
    },
    DAILY_TASK("dailyTask", "每日任务") {
        @Override
        public void checkParam(PointOperationTypeEnum operation, PointTypeEnum pointType) {
            if (operation != PointOperationTypeEnum.ADD) {
                throw new IllegalArgumentException("参数错误");
            }
            if (pointType != PointTypeEnum.POINT) {
                throw new IllegalArgumentException("参数错误");
            }
        }

        @Override
        public UpdatePointHandlerBO updatePointHandler(Long userId, Integer pointDiff, PointOperationTypeEnum operationType, PointTypeEnum pointType, String requestUuid) {
            return new UpdatePointHandlerBO(0, pointDiff, operationType, pointType);
        }
    },
    INVITE_USER("inviteUser", "邀请用户") {
        @Override
        public void checkParam(PointOperationTypeEnum operation, PointTypeEnum pointType) {
            if (operation != PointOperationTypeEnum.ADD) {
                throw new IllegalArgumentException("参数错误");
            }
            if (pointType != PointTypeEnum.POINT) {
                throw new IllegalArgumentException("参数错误");
            }
        }

        @Override
        public UpdatePointHandlerBO updatePointHandler(Long userId, Integer pointDiff, PointOperationTypeEnum operationType, PointTypeEnum pointType, String requestUuid) {
            return new UpdatePointHandlerBO(0, pointDiff, operationType, pointType);
        }
    },
    ADMIN_UPDATE("adminUpdate", "管理员修改") {
        @Override
        public void checkParam(PointOperationTypeEnum operation, PointTypeEnum pointType) {

        }

        @Override
        public UpdatePointHandlerBO updatePointHandler(Long userId, Integer pointDiff, PointOperationTypeEnum operationType, PointTypeEnum pointType, String requestUuid) {
            // 在管理员修改中，pointDiff是目标值，不是差值，要根据用户当前永久积分计算差值
            User oldUser = userService.queryByUserId(userId);

            int diffPoint = pointDiff - oldUser.getPoint();

            if (diffPoint == 0) {
                return null;
            } else if (diffPoint > 0) {
                operationType = PointOperationTypeEnum.ADD;
            } else {
                operationType = PointOperationTypeEnum.REDUCE;
            }
            return new UpdatePointHandlerBO(0, pointDiff, operationType, pointType);
        }
    },
    ;

    private String dictKey;

    private String desc;

    public abstract void checkParam(PointOperationTypeEnum operation, PointTypeEnum pointType);

    public abstract UpdatePointHandlerBO updatePointHandler(Long userId, Integer pointDiff, PointOperationTypeEnum operationType, PointTypeEnum pointType, String requestUuid);


    @Setter
    private static UserService userService;
    @Setter
    private static PointRecordService pointRecordService;

    @RequiredArgsConstructor
    @Component
    public static class MailHandleEnumComponent {
        private final UserService userService;
        private final PointRecordService pointRecordService;

        @PostConstruct
        public void init() {
            PointOperationSourceEnum.setUserService(userService);
            PointOperationSourceEnum.setPointRecordService(pointRecordService);
        }
    }
}
