package com.huiyu.service.core.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户表(User)视图类
 *
 * @author Naccl
 * @date 2023-07-20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像url
     */
    private String avatar;
    /**
     * 每日积分
     */
    private Integer dailyPoint;
    /**
     * 积分
     */
    private Integer point;
    /**
     * 每日任务
     */
    private List<DailyTask> dailyTaskList;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DailyTask {
        private String key;
        private String title;
        private String desc;
        private Integer point;
        private Boolean status;
        private String action;
    }
}
