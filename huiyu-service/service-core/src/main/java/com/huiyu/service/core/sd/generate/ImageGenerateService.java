package com.huiyu.service.core.sd.generate;

import com.huiyu.service.core.model.vo.SDResponseVo;
import com.huiyu.service.core.model.cmd.Cmd;

/**
 * @author wAnG
 * @Date 2023-06-12  01:31
 */
public interface ImageGenerateService<T extends Cmd> {

    void generate(T t, SDResponseVo sdResponseVo);

}
