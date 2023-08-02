package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.core.entity.Spellbook;
import com.huiyu.service.core.model.vo.SpellbookVo;

import java.util.List;

/**
 * (Spellbook)服务接口
 *
 * @author Naccl
 * @date 2023-08-03
 */
public interface SpellbookService extends IService<Spellbook> {

    List<SpellbookVo> listVo();
}
