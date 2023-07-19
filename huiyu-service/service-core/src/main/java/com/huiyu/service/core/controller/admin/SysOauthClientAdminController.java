package com.huiyu.service.core.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.service.core.model.query.SysOauthClientQuery;
import com.huiyu.service.core.service.SysOauthClientService;
import com.huiyu.service.api.entity.SysOauthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.huiyu.common.core.result.R;

/**
 * (SysOauthClient)表控制层
 *
 * @author Naccl
 * @date 2022-03-07
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/sysOauthClient")
public class SysOauthClientAdminController {
    private final SysOauthClientService sysOauthClientService;

    /**
     * 分页查询
     *
     * @param query    筛选条件
     * @param pageNum  页码
     * @param pageSize 每页个数
     * @return 查询结果
     */
    @GetMapping("/{pageNum}/{pageSize}")
    public R<IPage<SysOauthClient>> queryByPage(SysOauthClientQuery query, @PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        IPage<SysOauthClient> pageInfo = sysOauthClientService.queryPage(new Page<>(pageNum, pageSize), query);
        return R.ok(pageInfo);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param clientId 主键
     * @return 单条数据
     */
    @GetMapping("/{clientId}")
    public R<SysOauthClient> queryByClientId(@PathVariable("clientId") String clientId) {
        return R.ok(sysOauthClientService.queryByClientId(clientId));
    }

    /**
     * 新增数据
     *
     * @param sysOauthClient 实体
     * @return 新增结果
     */
    @PostMapping
    public R<SysOauthClient> add(@RequestBody SysOauthClient sysOauthClient) {
        return R.ok(sysOauthClientService.insert(sysOauthClient));
    }

    /**
     * 编辑数据
     *
     * @param sysOauthClient 实体
     * @return 编辑结果
     */
    @PutMapping
    public <T> R<T> edit(@RequestBody SysOauthClient sysOauthClient) {
        sysOauthClientService.update(sysOauthClient);
        return R.ok();
    }

    /**
     * 通过主键删除数据
     *
     * @param clientId 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/{clientId}")
    public <T> R<T> deleteByClientId(@PathVariable("clientId") String clientId) {
        sysOauthClientService.deleteByClientId(clientId);
        return R.ok();
    }
}
