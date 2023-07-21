package com.huiyu.service.core.controller.admin;

import com.huiyu.common.core.result.R;
import com.huiyu.service.core.enums.BaseEnum;
import com.huiyu.service.core.enums.PointOperationSourceEnum;
import com.huiyu.service.core.enums.PointOperationTypeEnum;
import com.huiyu.service.core.enums.TaskStatusEnum;
import com.huiyu.service.core.enums.TaskTypeEnum;
import com.huiyu.service.core.model.vo.EnumVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private List<EnumVo> enum2EnumVoList(Class<? extends BaseEnum> enumClass) {
        BaseEnum<?>[] enums = enumClass.getEnumConstants();
        List<EnumVo> enumVoList = new ArrayList<>(enums.length);
        Arrays.asList(enums).stream().map(
                e -> EnumVo.builder()
                        .key(e.name())
                        .desc(e.getDesc())
                        .build()
        ).forEach(enumVoList::add);
        return enumVoList;
    }
}
