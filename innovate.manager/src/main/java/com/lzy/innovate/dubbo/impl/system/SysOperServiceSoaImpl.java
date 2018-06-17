package com.lzy.innovate.dubbo.impl.system;


import com.lzy.innovate.dubbo.base.impl.BaseServiceImpl;
import com.lzy.innovate.dubbo.system.ISysOperServiceSoa;
import com.lzy.innovate.entity.SysOper;
import com.lzy.innovate.service.ISysOperService;
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
public class SysOperServiceSoaImpl extends BaseServiceImpl<ISysOperService, SysOper> implements ISysOperServiceSoa {

    /**
     * 查询当前用户的所有的操作列表
     * @param params 参数集合
     * @return 当前用户的所有按钮列表
     */
    @Override
    public List<SysOper> findOpersByPagin(Map<String, Object> params) {
        return e.findOpersByPagin(params);
    }

    /**
     * 查询当前用户某个菜单下的操作/按钮
     * @param userId 用户ID
     * @param menuId 菜单ID
     * @return 当前用户某个菜单下的按钮列表
     */
    @Override
    public List<SysOper> findOpersByMenuId(String userId, String menuId) {
        return e.findOpersByMenuId(userId , menuId);
    }

    /**
     * 检索总数
     * @param params
     * @return
     */
    @Override
    public int findOperCount(Map<String, Object> params) {
        return e.findOperCount(params);
    }

    /**
     * 生成操作对于的菜单Map
     * @param operIds
     * @return
     */
    @Override
    public Map<String, List<SysOper>> generateOperBelongMenuMap(List<String> operIds) {
        return e.generateOperBelongMenuMap(operIds);
    }
}
