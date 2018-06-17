package com.lzy.innovate.service;

import com.baomidou.mybatisplus.service.IService;
import com.lzy.innovate.entity.SysOper;

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
public interface ISysOperService extends IService<SysOper> {

    /**
     * 查询当前用户的所有的操作列表
     * @param params 参数集合
     * @return
     */
    public List<SysOper> findOpersByPagin(Map<String, Object> params);


    /**
     * 查询当前用户某个菜单下的操作/按钮
     * @param userId 用户id
     * @param menuId 菜单id
     * @return
     */
    public List<SysOper> findOpersByMenuId(String userId, String menuId);

    /**
     * 检索总数
     * @param params
     * @return
     */
    public int findOperCount(Map<String, Object> params);

    /**
     * 生成操作对应的菜单
     * @param operIds
     * @return
     */
    public Map<String, List<SysOper>> generateOperBelongMenuMap(List<String> operIds);
	
}
