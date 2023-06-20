package com.huiyu.service.core.model.query;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 模型表(Model)查询对象实体类
 *
 * @author Naccl
 * @date 2023-06-20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelQuery {

    /**
     * 模型id
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
}
