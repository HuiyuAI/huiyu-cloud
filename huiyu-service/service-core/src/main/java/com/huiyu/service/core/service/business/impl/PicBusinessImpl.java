package com.huiyu.service.core.service.business.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.service.core.convert.PicConvert;
import com.huiyu.service.core.entity.Model;
import com.huiyu.service.core.model.dto.PicPageDto;
import com.huiyu.service.core.model.vo.PicPageVo;
import com.huiyu.service.core.model.vo.PicVo;
import com.huiyu.service.core.service.ModelService;
import com.huiyu.service.core.service.PicService;
import com.huiyu.service.core.service.business.PicBusiness;
import com.huiyu.service.core.constant.StateEnum;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.entity.PicExt;
import com.huiyu.service.core.service.PicExtService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class PicBusinessImpl implements PicBusiness {

    @Resource
    private PicExtService picExtService;

    @Resource
    private PicService picService;

    @Resource
    private ModelService modelService;

    @Override
    public IPage<PicPageVo> queryVoPage(IPage<Pic> page, PicPageDto dto) {
        Assert.notNull(dto.getUserId(), "异常错误");
        IPage<Pic> picPage = picService.queryPage(page, dto);
        Page<PicPageVo> picVoPage = PicConvert.INSTANCE.toVOPage(picPage);
        return picVoPage;
    }

    @Override
    public PicVo getPicVo(String uuid, Long userId) {
        Pic pic = picService.getByUuidAndUserId(uuid, userId);
        Assert.notNull(pic, "图片不存在");
        PicVo picVo = PicConvert.INSTANCE.toVO(pic);

        Model model = modelService.getById(pic.getModelId());
        if (model == null) {
            picVo.setModelName("模型已下架");
        } else {
            picVo.setModelName(model.getName());
        }

        picVo.setQuality(pic.getQuality().getDesc());
        return picVo;
    }

    /**
     * 分享投稿图片
     *
     * @param pic   图片信息
     * @param state 需要的状态
     * @return 是否操作成功
     */
    @Override
    public boolean share(Pic pic, StateEnum state) {
        if (pic.getUserId() == null || pic.getId() == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        // 判断是否存在
        PicExt picExt = picExtService.getByPicId(pic.getId());
        if (picExt != null) {
            // 已分享
            picExt.setEnable(state);
            picExt.setUpdateTime(now);
            return picExtService.update(picExt);
        } else {
            // 未分享
            picExt = PicExt.builder()
                    .picId(pic.getId())
                    .views(0)
                    .path(pic.getPath())
                    .enable(state)
                    .updateTime(now)
                    .isDelete(0)
                    .createTime(now)
                    .build();
            return picExtService.insert(picExt);
        }
    }
}
