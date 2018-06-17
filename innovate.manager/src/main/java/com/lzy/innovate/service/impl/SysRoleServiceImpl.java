package com.lzy.innovate.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lzy.innovate.entity.SysRole;
import com.lzy.innovate.mapper.SysRoleMapper;
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
 * @since 2017-02-27
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    /**
     * 查询角色列表
     * @param params 参数集合
     * @return
     */
    @Override
    public List<SysRole> findRolesByPagin(Map<String, Object> params) {
        return baseMapper.findRolesByPagin(params);
    }

    /**
     * 检索总数
     * @param params
     * @return
     */
    @Override
    public int findRoleCount(Map<String, Object> params) {
        return baseMapper.findRoleCount(params);
    }
}
