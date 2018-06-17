package com.lzy.innovate.dubbo.impl.system;


import com.lzy.innovate.dubbo.base.impl.BaseServiceImpl;
import com.lzy.innovate.dubbo.system.ISysRoleServiceSoa;
import com.lzy.innovate.entity.SysRole;
import com.lzy.innovate.service.ISysRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author laizy
 * @since 2017-02-28
 */
@Service
public class SysRoleServiceSoaImpl extends BaseServiceImpl<ISysRoleService, SysRole> implements ISysRoleServiceSoa {

    /**
     * 查询角色列表
     * @param params 参数集合
     * @return
     */
    @Override
    public List<SysRole> findRolesByPagin(Map<String, Object> params) {
        return e.findRolesByPagin(params);
    }

    /**
     * 检索总数
     * @param params
     * @return
     */
    @Override
    public int findRoleCount(Map<String, Object> params) {
        return e.findRoleCount(params);
    }
}
