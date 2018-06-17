package com.lzy.innovate.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lzy.innovate.entity.SysOper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  操作（按钮）Mapper 接口
 * </p>
 *
 * @author laizy
 * @since 2017-02-27
 */
public interface SysOperMapper extends BaseMapper<SysOper> {

    /**
     * 查询当前用户的所有的操作列表
     * @param params 参数map
     * @return 当前用户的所有按钮列表
     */
    public List<SysOper> findOpersByPagin(Map<String, Object> params);


    /**
     * 查询当前用户某个菜单下的操作/按钮
     * @param userId 用户ID
     * @param menuId 菜单ID
     * @return 当前用户某个菜单下的按钮列表
     */
    public List<SysOper> findOpersByMenuId(@Param("userId") String userId,
                                           @Param("menuId") String menuId);

    /**
     * 检索总数
     * @param params
     * @return
     */
    public int findOperCount(Map<String, Object> params);

}