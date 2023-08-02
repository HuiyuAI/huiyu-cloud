package com.huiyu.service.core.model.vo;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * (Spellbook)视图类
 *
 * @author Naccl
 * @date 2023-08-03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpellbookVo {

    /**
     * 标签
     */
    private String title;
    /**
     * 选项
     */
    private List<SubTab> subTabs;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubTab {
        /**
         * 中文描述
         */
        private String name;
        /**
         * 英文prompt
         */
        private String prompt;
    }
}
