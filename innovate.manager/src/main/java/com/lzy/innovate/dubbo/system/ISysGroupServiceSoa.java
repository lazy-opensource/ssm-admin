package com.lzy.innovate.dubbo.system;

import com.lzy.innovate.dubbo.base.BaseService;
import com.lzy.innovate.entity.SysGroup;

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
public interface ISysGroupServiceSoa extends BaseService<SysGroup> {

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
