package com.huiyu.service.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyu.service.core.entity.Spellbook;
import com.huiyu.service.core.mapper.SpellbookMapper;
import com.huiyu.service.core.model.vo.SpellbookVo;
import com.huiyu.service.core.service.SpellbookService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * (Spellbook)服务实现类
 *
 * @author Naccl
 * @date 2023-08-03
 */
@Service
public class SpellbookServiceImpl extends ServiceImpl<SpellbookMapper, Spellbook> implements SpellbookService {

    private List<SpellbookVo> spellbookVoList;

    private Map<String, String> spellbookPromptMap;

    @Override
    public List<SpellbookVo> listVo() {
        if (spellbookVoList == null) {
            List<Spellbook> spellbookList = super.lambdaQuery()
                    .eq(Spellbook::getVisible, true)
                    .orderByAsc(Spellbook::getPriority, Spellbook::getId)
                    .list();
            Map<String, List<Spellbook>> groupByTitle = spellbookList.stream().collect(Collectors.groupingBy(Spellbook::getTitle, LinkedHashMap::new, Collectors.toList()));

            spellbookVoList = new ArrayList<>(groupByTitle.size());
            groupByTitle.forEach((k, v) -> {
                List<SpellbookVo.SubTab> subTabs = new ArrayList<>(v.size());
                v.forEach(spellbook -> {
                    SpellbookVo.SubTab subTab = SpellbookVo.SubTab.builder()
                            .name(spellbook.getName())
                            .prompt(spellbook.getPrompt())
                            .build();
                    subTabs.add(subTab);
                });
                SpellbookVo spellbookVo = SpellbookVo.builder()
                        .title(k)
                        .subTabs(subTabs)
                        .build();
                spellbookVoList.add(spellbookVo);
            });
        }
        return spellbookVoList;
    }

    @Override
    public String getPromptByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        if (spellbookPromptMap == null) {
            spellbookPromptMap = super.lambdaQuery()
                    .select(Spellbook::getName, Spellbook::getPrompt)
                    .list()
                    .stream().collect(Collectors.toMap(Spellbook::getName, Spellbook::getPrompt, (k1, k2) -> k1));
        }
        return spellbookPromptMap.get(name);
    }
}
