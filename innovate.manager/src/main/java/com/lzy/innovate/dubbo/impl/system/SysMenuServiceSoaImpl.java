package com.lzy.innovate.dubbo.impl.system;

import com.lzy.innovate.dubbo.base.impl.BaseServiceImpl;
import com.lzy.innovate.dubbo.system.ISysMenuServiceSoa;
import com.lzy.innovate.entity.SysMenu;
import com.lzy.innovate.service.ISysMenuService;
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
public class SysMenuServiceSoaImpl extends BaseServiceImpl<ISysMenuService, SysMenu> implements ISysMenuServiceSoa {

    /**
     * 通过用户id查出有该用户拥有的所有菜单
     * @param userId  用户ID
     * @return 当前用户拥有的所有菜单列表
     */
    @Override
    public List<SysMenu> findMenusByUserId(String userId) {


        return e.findMenusByUserId(userId);
    }

    /**
     * 通过用户id查出该用户用户的所有的一级菜单
     * @param userId 用户ID
     * @return 当前用户拥有的一级菜单
     */
    @Override
    public List<SysMenu> findOneMenusByUserId(String userId) {
        return e.findOneMenusByUserId(userId);
    }

    /**
     * 通过父菜单id查出当前用户拥有的所有子菜单
     * @param parentId 父菜单ID
     * @param userId 用户ID
     * @return 当前用户某个菜单下的子菜单
     */
    @Override
    public List<SysMenu> findChildMenusByParentId(String parentId, String userId) {
        return e.findChildMenusByParentId(parentId , userId);
    }

    /**
     * 根据条件查列表
     * @param params
     * @return
     */
    @Override
    public List<SysMenu> findMenusByCondition(Map<String, Object> params) {
        return e.findMenusByCondition(params);
    }
}
