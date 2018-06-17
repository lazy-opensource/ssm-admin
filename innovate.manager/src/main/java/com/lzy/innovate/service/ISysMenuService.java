package com.lzy.innovate.service;


import com.baomidou.mybatisplus.service.IService;
import com.lzy.innovate.entity.SysMenu;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author laizy
 * @since 2017-02-27
 */
public interface ISysMenuService extends IService<SysMenu> {


    /**
     * 通过用户id查出有该用户拥有的所有菜单
     * @param userId  用户ID
     * @return
     */
    public List<SysMenu> findMenusByUserId(String userId);

    /**
     * 通过用户id查出该用户用户的所有的一级菜单
     * @param userId 用户ID
     * @return
     */
    public List<SysMenu> findOneMenusByUserId(String userId);


    /**
     * 通过父菜单id查出当前用户拥有的所有子菜单
     * @param parentId 父菜单ID
     * @return
     */
    public List<SysMenu> findChildMenusByParentId(String parentId, String userId);

    public List<SysMenu> findMenusByCondition(Map<String, Object> params);

}
