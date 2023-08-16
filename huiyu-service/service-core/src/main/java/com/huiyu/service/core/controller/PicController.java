package com.huiyu.service.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.common.core.result.R;
import com.huiyu.common.web.util.JwtUtils;
import com.huiyu.service.core.aspect.annotation.RequestLimiter;
import com.huiyu.service.core.aspect.annotation.RequestLogger;
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
    @RequestLimiter(seconds = 60, maxCount = 60)
    @RequestLogger
    @GetMapping("/page")
    public R<IPage<PicPageVo>> page(@Valid PicPageDto dto) {
        Long userId = JwtUtils.getUserId();
        dto.setUserId(userId);
        IPage<PicPageVo> picVoPage = picBusiness.queryVoPage(Page.of(dto.getPageNum(), dto.getPageSize()), dto);
        return R.ok(picVoPage);
    }

    /**
     * 详情
     */
    @RequestLimiter(seconds = 60, maxCount = 60)
    @RequestLogger
    @GetMapping("/get")
    public R<PicVo> get(String uuid) {
        Long userId = JwtUtils.getUserId();
        PicVo picVo = picBusiness.getPicVo(uuid, userId);
        return R.ok(picVo);
    }

    /**
     * 批量删除
     */
    @RequestLimiter(seconds = 20, maxCount = 10)
    @RequestLogger
    @PostMapping("/deleteByUuidList")
    public R<?> deleteByUuidList(@RequestBody List<String> uuidList) {
        Long userId = JwtUtils.getUserId();
        boolean res = picBusiness.userDeleteByUuidList(userId, uuidList);
        return R.status(res);
    }

}
