package com.huiyu.service.core.sd.validator.propertyValidator;

import com.huiyu.service.core.model.cmd.Cmd;
import com.huiyu.service.core.model.cmd.Img2ImgCmd;
import com.huiyu.service.core.sd.validator.base.CmdValidator;
import com.huiyu.service.core.sd.validator.constant.CmdType;

/**
 * @author wAnG
 * @Date 2023-06-12  00:28
 */
@CmdValidator(name = "model_id", dealType = CmdType.IMG2IMG)
public class modelIdValidator implements IPropertyValidator<Img2ImgCmd> {

    @Override
    public boolean validatorProperty(Img2ImgCmd img2ImgCmd) {
        return false;
    }
}
