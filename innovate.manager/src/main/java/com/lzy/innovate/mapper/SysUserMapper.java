package com.lzy.innovate.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lzy.innovate.entity.SysUser;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  系统用户Mapper 接口
 * </p>
 *
 * @author laizy
 * @since 2017-02-27
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 检索列表
     * @param map
     * @return
     */
    public List<SysUser> findUserListByPagin(Map<String, Object> map);

    /**
     * 检索总数
     * @param map
     * @return
     */
    public int findUserCount(Map<String, Object> map);

}