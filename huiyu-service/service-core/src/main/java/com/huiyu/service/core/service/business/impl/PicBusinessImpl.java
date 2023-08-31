package com.huiyu.service.core.service.business.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.service.core.constant.HuiyuConstant;
import com.huiyu.service.core.constant.RedisKeyEnum;
import com.huiyu.service.core.convert.PicConvert;
import com.huiyu.service.core.entity.Model;
import com.huiyu.service.core.entity.PicShare;
import com.huiyu.service.core.enums.DailyTaskEnum;
import com.huiyu.service.core.enums.PicShareStatusEnum;
import com.huiyu.service.core.enums.PicStatusEnum;
import com.huiyu.service.core.model.dto.PicPageDto;
import com.huiyu.service.core.model.dto.PicShareDto;
import com.huiyu.service.core.model.dto.PicSharePageDto;
import com.huiyu.service.core.model.vo.PicPageVo;
import com.huiyu.service.core.model.vo.PicSharePageVo;
import com.huiyu.service.core.model.vo.PicShareVo;
import com.huiyu.service.core.model.vo.PicVo;
import com.huiyu.service.core.model.vo.RedrawVo;
import com.huiyu.service.core.sd.constant.ImageQualityEnum;
import com.huiyu.service.core.service.PicShareService;
import com.huiyu.service.core.service.ModelService;
import com.huiyu.service.core.service.PicService;
import com.huiyu.service.core.service.business.PicBusiness;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.service.extension.SecureCheckService;
import com.huiyu.service.core.utils.IdUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PicBusinessImpl implements PicBusiness {
    private final PicService picService;
    private final PicShareService picShareService;
    private final ModelService modelService;
    private final SecureCheckService secureCheckService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void picGeneratedCallback(String resImageUuid, String resImageUrlUuid) {
        String imgUrl = HuiyuConstant.cdnUrlGen + resImageUrlUuid + HuiyuConstant.imageSuffix;
        log.info("图片上传成功 url: {}", imgUrl);

        PicStatusEnum picStatus = PicStatusEnum.GENERATED;

        // 缩略图
        String checkUrl = imgUrl + "!/fw/720";
        log.info("图片上传成功, 调用微信图片审核接口");
        boolean checkRes = secureCheckService.checkImage(checkUrl);
        if (!checkRes) {
            log.info("图片审核不通过, imgUrl: {}", checkUrl);
            picStatus = PicStatusEnum.RISKY;
        }

        LocalDateTime now = LocalDateTime.now();
        // 更新图片状态
        Pic updatePicDto = Pic.builder()
                .uuid(resImageUuid)
                .path(imgUrl)
                .status(picStatus)
                .updateTime(now)
                .build();
        picService.updateByUuid(updatePicDto);

        // 每日任务计数
        Pic pic = picService.getByUuidOnly(resImageUuid);
        String dailyTaskRedisKey = RedisKeyEnum.DAILY_TASK_MAP.getFormatKey(now.toLocalDate().toString(), String.valueOf(pic.getUserId()));
        redisTemplate.opsForHash().increment(dailyTaskRedisKey, DailyTaskEnum.GENERATE_PIC.getDictKey(), 1);

        // 通知审核群
        picService.sendMsgByPicGenerated(resImageUuid);
    }

    @Override
    public IPage<PicPageVo> queryVoPage(IPage<Pic> page, PicPageDto dto) {
        Assert.notNull(dto.getUserId(), "异常错误");
        IPage<Pic> picPage = picService.queryPage(page, dto);
        Page<PicPageVo> picVoPage = PicConvert.INSTANCE.toVOPage(picPage);

        // 违规的图片不返回图片url
        picVoPage.getRecords().stream()
                .filter(picPageVo -> picPageVo.getStatus() == PicStatusEnum.RISKY)
                .forEach(picPageVo -> picPageVo.setPath(null));
        return picVoPage;
    }

    @Override
    public PicVo getPicVo(String uuid, Long userId) {
        Pic pic = picService.getByUuidAndUserId(uuid, userId);
        Assert.notNull(pic, "图片不存在");
        PicVo picVo = PicConvert.INSTANCE.toVO(pic);

        // 违规的图片不返回图片url
        if (pic.getStatus() == PicStatusEnum.RISKY) {
            picVo.setPath(null);
        }

        Model model = modelService.getById(pic.getModelId(), true);
        if (model == null) {
            picVo.setModelId(null);
            picVo.setModelName("模型已下架");
        } else {
            picVo.setModelId(model.getId());
            picVo.setModelName(model.getName());
        }

        // 设置投稿状态
        if (pic.getStatus() == PicStatusEnum.GENERATED) {
            PicShare picShare = picShareService.getByPicId(pic.getId());
            if (picShare == null) {
                picVo.setShareStatus(PicShareStatusEnum.NONE);
            } else {
                picVo.setShareStatus(picShare.getStatus());
            }
        } else {
            picVo.setShareStatus(PicShareStatusEnum.NONE);
        }

        picVo.setQuality(pic.getQuality().getDesc());
        return picVo;
    }

    @Override
    public boolean share(Long userId, PicShareDto picShareDto) {
        Assert.notNull(userId, "异常错误");

        List<PicShare> picShareList = picShareService.getByUserIdAndUuidList(userId, Collections.singletonList(picShareDto.getUuid()));
        if (CollUtil.isNotEmpty(picShareList)) {
            // 已经有投稿记录
            return false;
        }

        // 未分享，检查图片状态
        Pic pic = picService.getByUuidAndUserIdAndStatus(picShareDto.getUuid(), userId, PicStatusEnum.GENERATED);
        if (pic == null) {
            return false;
        }

        // 可分享，提交审核
        LocalDateTime now = LocalDateTime.now();
        PicShare picShare = PicShare.builder()
                .id(IdUtils.nextSnowflakeId())
                .picId(pic.getId())
                .uuid(pic.getUuid())
                .userId(userId)
                .title(StringUtils.trimToEmpty(picShareDto.getTitle()))
                .hits(0)
                .likeCount(0)
                .drawCount(0)
                .status(PicShareStatusEnum.AUDITING)
                .createTime(now)
                .updateTime(now)
                .isDelete(0)
                .build();
        boolean flag = picShareService.save(picShare);
        if (!flag) {
            return false;
        }

        // 通知审核群
        picShareService.sendMsgByPicShare(pic, picShare, now);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean userDeleteByUuidList(Long userId, List<String> uuidList) {
        if (CollectionUtils.isEmpty(uuidList)) {
            return false;
        }
        boolean res = picService.userDeleteByUuidList(userId, uuidList);
        if (!res) {
            return false;
        }

        // 如果图片正在投稿审核中，取消投稿
        // 暂时先不取消投稿
//        List<Long> picIdList = picShareService.getByUserIdAndUuidList(userId, uuidList).stream()
//                .filter(picShare -> picShare.getStatus() == PicShareStatusEnum.AUDITING)
//                .map(PicShare::getPicId)
//                .collect(Collectors.toList());
//        picShareService.audit(picIdList, PicShareStatusEnum.CANCEL);

        // 已分享的图片不删除
        return true;
    }

    @Override
    public IPage<PicSharePageVo> picSharePage(IPage<PicShare> page, PicSharePageDto dto) {
        IPage<PicSharePageVo> picSharePage = picShareService.queryPage(page, dto);
        return picSharePage;
    }

    @Override
    public PicShareVo getPicShareVoByUuid(String uuid) {
        PicShareVo picShareVo = picShareService.getPicShareVoByUuid(uuid);

        Model model = modelService.getById(picShareVo.getModelId(), true);
        if (model == null) {
            picShareVo.setModelId(null);
            picShareVo.setModelName("模型已下架");
        } else {
            picShareVo.setModelName(model.getName());
        }

        picShareVo.setQuality(ImageQualityEnum.getEnumByCode(Integer.valueOf(picShareVo.getQuality())).getDesc());

        picShareService.addHitsByUuid(uuid);
        return picShareVo;
    }

    @Override
    public RedrawVo redraw(Long userId, String uuid) {
        RedrawVo redrawVo = picShareService.redraw(uuid);

        picShareService.addRedrawCountByUuid(uuid);
        return redrawVo;
    }
}
