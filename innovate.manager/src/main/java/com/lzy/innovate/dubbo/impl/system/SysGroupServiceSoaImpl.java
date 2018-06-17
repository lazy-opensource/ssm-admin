package com.lzy.innovate.dubbo.impl.system;

import com.lzy.innovate.dubbo.base.impl.BaseServiceImpl;
import com.lzy.innovate.dubbo.system.ISysGroupServiceSoa;
import com.lzy.innovate.entity.SysGroup;
import com.lzy.innovate.service.ISysGroupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author laizy
 * @since 2017-02-28
 */
@Service
public class SysGroupServiceSoaImpl extends BaseServiceImpl<ISysGroupService, SysGroup> implements ISysGroupServiceSoa {

    /**
     * 检索列表
     * @param params 参数集合
     * @return
     */
    @Override
    public List<SysGroup> findGroupListByPagin(Map<String, Object> params) {
        return e.findGroupListByPagin(params);
    }

    /**
     * 检索总数
     * @param params
     * @return
     */
    @Override
    public int findGroupCount(Map<String, Object> params) {
        return e.findGroupCount(params);
    }
}
