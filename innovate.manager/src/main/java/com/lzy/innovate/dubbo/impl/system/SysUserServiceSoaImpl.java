package com.lzy.innovate.dubbo.impl.system;

import com.lzy.innovate.dubbo.base.impl.BaseServiceImpl;
import com.lzy.innovate.dubbo.system.ISysUserServiceSoa;
import com.lzy.innovate.entity.SysUser;
import com.lzy.innovate.service.ISysUserService;
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
public class SysUserServiceSoaImpl extends BaseServiceImpl<ISysUserService, SysUser> implements ISysUserServiceSoa {


    /**
     * 检索用户
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
    public SysUser selectByNameAndPwd(String username, String password) {

        return e.selectByNameAndPwd(username , password);
    }

    /**
     * 检索列表
     * @param param
     * @return
     */
    @Override
    public List<SysUser> findUserListByPagin(Map<String, Object> param) {

        return e.findUserListByPagin(param);
    }

    /**
     * 检索总数
     * @param params
     * @return
     */
    @Override
    public int findUserCount(Map<String, Object> params) {
        return e.findUserCount(params);
    }

}
