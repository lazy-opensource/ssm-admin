package com.lzy.innovate.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lzy.innovate.entity.SysGroup;

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
public interface SysGroupMapper extends BaseMapper<SysGroup> {

    /**
     * 检索列表
     * @param params 参数集合
     * @return
     */
    public List<SysGroup> findGroupListByPagin(Map<String, Object> params);

    /**
     * 检索总数
     * @param params
     * @return
     */
    public int findGroupCount(Map<String, Object> params);

}