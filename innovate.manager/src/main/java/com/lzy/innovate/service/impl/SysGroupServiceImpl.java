package com.lzy.innovate.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lzy.innovate.entity.SysGroup;
import com.lzy.innovate.mapper.SysGroupMapper;
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
 * @since 2017-02-27
 */
@Service
public class SysGroupServiceImpl extends ServiceImpl<SysGroupMapper, SysGroup> implements ISysGroupService {

    /**
     * 检索列表
     * @param params 参数集合
     * @return
     */
    @Override
    public List<SysGroup> findGroupListByPagin(Map<String, Object> params) {
        return baseMapper.findGroupListByPagin(params);
    }

    /**
     * 检索总数
     * @param params
     * @return
     */
    @Override
    public int findGroupCount(Map<String, Object> params) {
        return baseMapper.findGroupCount(params);
    }
}
