package com.huiyu.service.core.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.common.core.result.R;
import com.huiyu.service.core.model.dto.PicShareAuditDto;
import com.huiyu.service.core.model.query.PicShareQuery;
import com.huiyu.service.core.model.vo.PicShareAdminVo;
import com.huiyu.service.core.service.PicShareService;
import com.huiyu.service.core.service.business.PicBusiness;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Naccl
 * @date 2023-08-21
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/picShare")
public class PicShareAdminController {
    private final PicShareService picShareService;
    private final PicBusiness picBusiness;

    /**
     * 后台管理分页查询
     */
    @GetMapping("/{pageNum}/{pageSize}")
    public R<IPage<PicShareAdminVo>> adminPageQuery(PicShareQuery query, @PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        IPage<PicShareAdminVo> pageInfo = picShareService.adminPageQuery(new Page<>(pageNum, pageSize), query);
        return R.ok(pageInfo);
    }

    /**
     * 审核
     */
    @PostMapping("/audit")
    public R audit(@RequestBody PicShareAuditDto dto) {
        boolean res = picBusiness.audit(dto.getPicIdList(), dto.getStatus());
        return R.status(res);
    }

    /**
     * 重新审核
     */
    @PostMapping("/reAudit")
    public R reAudit(@RequestBody List<Long> picIdList) {
        boolean res = picShareService.reAudit(picIdList);
        return R.status(res);
    }

}
