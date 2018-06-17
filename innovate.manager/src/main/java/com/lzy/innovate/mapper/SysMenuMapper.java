package com.lzy.innovate.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lzy.innovate.entity.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author laizy
 * @since 2017-02-27
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 通过用户id查出有该用户拥有的所有菜单
     * @param userId  用户ID
     * @return 当前用户拥有的所有菜单列表
     */
    public List<SysMenu> findMenusByUserId(@Param("userId") String userId);

    /**
     * 通过用户id查出该用户用户的所有的一级菜单
     * @param userId 用户ID
     * @return 当前用户拥有的一级菜单
     */
    public List<SysMenu> findOneMenusByUserId(@Param("userId") String userId);


    /**
     * 通过父菜单id查出当前用户拥有的所有子菜单
     * @param parentId 父菜单ID
     * @param userId 用户ID
     * @return 当前用户某个菜单下的子菜单
     */
    public List<SysMenu> findChildMenusByParentId(@Param("parentId") String parentId,
                                                  @Param("userId") String userId);

    /**
     * 根据条件差列表
     * @param params
     * @return
     */
    public List<SysMenu> findMenusByCondition(Map<String, Object> params);

}