package com.lzy.innovate.service;


import com.baomidou.mybatisplus.service.IService;
import com.lzy.innovate.entity.SysRole;

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
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 查村角色列表，可分页，排序
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
