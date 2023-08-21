package com.huiyu.service.core.model.vo;

import com.huiyu.service.api.entity.User;
import com.huiyu.service.core.entity.Model;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.entity.PicShare;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Naccl
 * @date 2023-08-21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PicShareAdminVo {
    private PicShare picShare;

    private Pic pic;

    private Model model;

    private User user;
}
