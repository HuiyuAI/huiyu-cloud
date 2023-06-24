package com.huiyu.service.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huiyu.common.core.result.R;
import com.huiyu.service.core.model.dto.PicDto;
import com.huiyu.service.core.model.vo.PicVo;
import com.huiyu.service.core.service.PicService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 图片表(Pic)控制层
 *
 * @author Naccl
 * @date 2023-06-25
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/pic")
public class PicController {

    private final PicService picService;

    /**
     * 画夹分页
     */
    @GetMapping("/page")
    public R<IPage<PicVo>> list(PicDto dto, Integer pageNum, Integer pageSize) {
        return R.ok();
    }

    /**
     * 详情
     */
    @GetMapping("/get")
    public R<PicVo> get(PicDto dto) {
        return R.ok();
    }

    /**
     * 批量删除
     */
    @PostMapping("/removeByIds")
    public R<?> remove(@RequestBody List<Long> ids) {
        return R.ok();
    }

}
