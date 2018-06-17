package com.lzy.innovate.dubbo.system;

import com.lzy.innovate.dubbo.base.BaseService;
import com.lzy.innovate.entity.SysMenu;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author laizy
 * @since 2017-02-28
 */
public interface ISysMenuServiceSoa extends BaseService<SysMenu> {

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

    /**
     * 根据条件查列表
     * @param params
     * @return
     */
    public List<SysMenu> findMenusByCondition(Map<String, Object> params);
	
}
