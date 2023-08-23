package com.huiyu.service.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.common.core.result.R;
import com.huiyu.common.web.util.JwtUtils;
import com.huiyu.service.api.entity.User;
import com.huiyu.service.core.aspect.annotation.RequestLimiter;
import com.huiyu.service.core.aspect.annotation.RequestLogger;
import com.huiyu.service.core.convert.UserConvert;
import com.huiyu.service.core.exception.BizException;
import com.huiyu.service.core.model.dto.PointRecordPageDto;
import com.huiyu.service.core.model.vo.PointRecordPageVo;
import com.huiyu.service.core.model.vo.UserVo;
import com.huiyu.service.core.service.UserService;
import com.huiyu.service.core.service.business.UserBusiness;
import com.huiyu.service.core.utils.upload.FileUploadChannel;
import com.huiyu.service.core.utils.upload.UploadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * @author Naccl
 * @date 2023-07-20
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    private final UserBusiness userBusiness;

    /**
     * 查询当前登录用户信息
     */
    @RequestLogger
    @RequestLimiter(seconds = 60, maxCount = 120)
    @GetMapping("/getMyUserInfo")
    public R<UserVo> getMyUserInfo() {
        Long userId = JwtUtils.getUserId();
        User user = userService.queryByUserId(userId);
        UserVo userVo = UserConvert.INSTANCE.toVo(user);
        return R.ok(userVo);
    }

    /**
     * 分页查询当前登录用户积分记录
     */
    @RequestLogger
    @RequestLimiter(seconds = 60, maxCount = 60)
    @GetMapping("/pagePointRecord")
    public R<IPage<PointRecordPageVo>> pagePointRecord(@Valid PointRecordPageDto dto) {
        Long userId = JwtUtils.getUserId();
        dto.setUserId(userId);
        IPage<PointRecordPageVo> picVoPage = userBusiness.pagePointRecord(Page.of(dto.getPageNum(), dto.getPageSize()), dto);
        return R.ok(picVoPage);
    }

    /**
     * 用户修改个人信息
     * 7天内最多修改5次
     */
    @RequestLogger
    @RequestLimiter(seconds = 604800, maxCount = 5, msg = "本周修改次数已用完，请下周再试")
    @GetMapping("/updateUserInfo")
    public R<?> updateUserInfo(@RequestPart("file") MultipartFile file, @RequestPart("nickname") String nickname) {
        Long userId = JwtUtils.getUserId();
        User user = userService.queryByUserId(userId);
        log.info("用户修改个人信息前, user: {}", user);

        User updateUser = new User();
        try {
            // TODO 压缩 文件大小限制
            String avatarUrl = UploadUtils.upload(new FileUploadChannel.ImageResource(file.getBytes(), "/avatar", "jpg"));
            updateUser.setAvatar(avatarUrl);
        } catch (Exception e) {
            log.error("上传头像失败", e);
            throw new BizException("上传头像失败");
        }

        nickname = StringUtils.trimToEmpty(nickname);
        if (nickname.length() == 0 || nickname.length() > 8) {
            throw new BizException("昵称长度错误");
        }
        updateUser.setNickname(nickname);

        updateUser.setUserId(userId);
        updateUser.setUpdateTime(user.getUpdateTime());

        log.info("用户修改个人信息后, updateUser: {}", updateUser);
        boolean isOK = userService.update(updateUser);

        return R.status(isOK);
    }
}
