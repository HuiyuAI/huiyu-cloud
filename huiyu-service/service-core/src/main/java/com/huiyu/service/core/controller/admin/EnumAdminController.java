package com.huiyu.service.core.controller.admin;

import com.huiyu.common.core.result.R;
import com.huiyu.service.core.enums.BaseEnum;
import com.huiyu.service.core.enums.PicShareStatusEnum;
import com.huiyu.service.core.enums.PicStatusEnum;
import com.huiyu.service.core.enums.PointOperationSourceEnum;
import com.huiyu.service.core.enums.PointOperationTypeEnum;
import com.huiyu.service.core.enums.PointTypeEnum;
import com.huiyu.service.core.enums.TaskStatusEnum;
import com.huiyu.service.core.enums.TaskTypeEnum;
import com.huiyu.service.core.model.vo.EnumVo;
import com.huiyu.service.core.sd.constant.ImageQualityEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Naccl
 * @date 2023-07-20
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/enum")
public class EnumAdminController {

    @GetMapping("/getTaskTypeEnum")
    public R<List<EnumVo>> getTaskTypeEnum() {
        return R.ok(enum2EnumVoList(TaskTypeEnum.class));
    }

    @GetMapping("/getTaskStatusEnum")
    public R<List<EnumVo>> getTaskStatusEnum() {
        return R.ok(enum2EnumVoList(TaskStatusEnum.class));
    }

    @GetMapping("/getPointOperationTypeEnum")
    public R<List<EnumVo>> getPointOperationTypeEnum() {
        return R.ok(enum2EnumVoList(PointOperationTypeEnum.class));
    }

    @GetMapping("/getPointOperationSourceEnum")
    public R<List<EnumVo>> getPointOperationSourceEnum() {
        return R.ok(enum2EnumVoList(PointOperationSourceEnum.class));
    }

    @GetMapping("/getPicStatusEnum")
    public R<List<EnumVo>> getPicStatusEnum() {
        return R.ok(enum2EnumVoList(PicStatusEnum.class));
    }

    @GetMapping("/getImageQualityEnum")
    public R<List<EnumVo>> getImageQualityEnum() {
        return R.ok(enum2EnumVoList(ImageQualityEnum.class));
    }

    @GetMapping("/getPointTypeEnum")
    public R<List<EnumVo>> getPointTypeEnum() {
        return R.ok(enum2EnumVoList(PointTypeEnum.class));
    }

    @GetMapping("/getPicShareStatusEnum")
    public R<List<EnumVo>> getPicShareStatusEnum() {
        return R.ok(enum2EnumVoList(PicShareStatusEnum.class));
    }

    private List<EnumVo> enum2EnumVoList(Class<? extends BaseEnum<?>> enumClass) {
        BaseEnum<?>[] enums = enumClass.getEnumConstants();
        List<EnumVo> enumVoList = Arrays.asList(enums).stream().map(
                e -> EnumVo.builder()
                        .key(e.name())
                        .desc(e.getDesc())
                        .build()
        ).collect(Collectors.toList());
        return enumVoList;
    }
}
