package com.lzy.innovate.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lzy.innovate.entity.SysRole;

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
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 查询角色列表，可分页，可排序
     * @param params 参数集合
     * @return
     */
    public List<SysRole> findRolesByPagin(Map<String, Object> params);

    /**
     * 检索总数
     * @param params
     * @return
     */
    public int findRoleCount(Map<String, Object> params);
}