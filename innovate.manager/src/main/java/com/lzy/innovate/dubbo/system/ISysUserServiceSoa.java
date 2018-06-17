package com.lzy.innovate.dubbo.system;

import com.lzy.innovate.dubbo.base.BaseService;
import com.lzy.innovate.entity.SysUser;

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
public interface ISysUserServiceSoa extends BaseService<SysUser> {

    /**
     * 通过用户名、密码查出用户
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public SysUser selectByNameAndPwd(String username, String password);

    /**
     * 检索列表
     * @param param
     * @return
     */
    public List<SysUser> findUserListByPagin(Map<String, Object> param);

    /**
     * 检索总数
     * @param param
     * @return
     */
    public int findUserCount(Map<String, Object> param);

	
}
