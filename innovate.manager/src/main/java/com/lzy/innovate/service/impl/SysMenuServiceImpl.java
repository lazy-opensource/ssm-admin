package com.lzy.innovate.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.lzy.innovate.entity.SysMenu;
import com.lzy.innovate.mapper.SysMenuMapper;
import com.lzy.innovate.service.ISysMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author laizy
 * @since 2017-02-27
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    /**
     * 通过用户id查出有该用户拥有的所有菜单
     * @param userId  用户ID
     * @return 当前用户拥有的所有菜单列表
     */
    @Override
    public List<SysMenu> findMenusByUserId(String userId) {

        if (StringUtils.isEmpty(userId)){
            return new ArrayList<SysMenu>();
        }

        return baseMapper.findMenusByUserId(userId);
    }

    /**
     * 通过用户id查出该用户用户的所有的一级菜单
     * @param userId 用户ID
     * @return 当前用户拥有的一级菜单
     */
    @Override
    public List<SysMenu> findOneMenusByUserId(String userId) {

        if (StringUtils.isEmpty(userId)){
            return new ArrayList<SysMenu>();
        }

        return baseMapper.findOneMenusByUserId(userId);
    }

    /**
     * 通过父菜单id查出当前用户拥有的所有子菜单
     * @param parentId 父菜单ID
     * @param userId 用户ID
     * @return 当前用户某个菜单下的子菜单
     */
    @Override
    public List<SysMenu> findChildMenusByParentId(String parentId, String userId) {

        if (StringUtils.isEmpty(parentId) || StringUtils.isEmpty(userId)){
            return new ArrayList<SysMenu>();
        }
        return baseMapper.findChildMenusByParentId(parentId , userId);
    }

    @Override
    public List<SysMenu> findMenusByCondition(Map<String, Object> params) {
        return baseMapper.findMenusByCondition(params);
    }
}
