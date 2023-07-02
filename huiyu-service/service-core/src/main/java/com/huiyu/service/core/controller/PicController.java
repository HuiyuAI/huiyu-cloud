package com.huiyu.service.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.common.core.result.R;
import com.huiyu.service.core.model.dto.PicPageDto;
import com.huiyu.service.core.model.vo.PicPageVo;
import com.huiyu.service.core.model.vo.PicVo;
import com.huiyu.service.core.service.business.PicBusiness;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
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

    private final PicBusiness picBusiness;

    /**
     * 画夹分页
     */
    @GetMapping("/page")
    public R<IPage<PicPageVo>> page(@Valid PicPageDto dto) {
//        Long userId = JwtUtils.getUserId();
        Long userId = 1L;
        dto.setUserId(userId);
        IPage<PicPageVo> picVoPage = picBusiness.queryVoPage(Page.of(dto.getPageNum(), dto.getPageSize()), dto);
        return R.ok(picVoPage);
    }

    /**
     * 详情
     */
    @GetMapping("/get")
    public R<PicVo> get(String uuid) {
//        Long userId = JwtUtils.getUserId();
        Long userId = 1L;
        PicVo picVo = picBusiness.getPicVo(uuid, userId);
        return R.ok(picVo);
    }

    /**
     * 批量删除
     */
    @PostMapping("/removeByIds")
    public R<?> remove(@RequestBody List<Long> ids) {
        return R.ok();
    }

}
