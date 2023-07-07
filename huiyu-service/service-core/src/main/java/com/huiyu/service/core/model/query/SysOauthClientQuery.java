package com.huiyu.service.core.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (SysOauthClient)查询对象实体类
 *
 * @author Naccl
 * @date 2023-07-07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysOauthClientQuery {

    /**
     * 客户端ID
     */
    private String clientId;
}
