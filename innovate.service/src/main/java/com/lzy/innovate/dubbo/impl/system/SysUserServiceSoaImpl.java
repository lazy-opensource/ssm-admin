package com.lzy.innovate.dubbo.impl.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.lzy.innovate.dubbo.base.impl.BaseServiceImpl;
import com.lzy.innovate.dubbo.system.ISysUserServiceSoa;
import com.lzy.innovate.entity.SysUser;
import com.lzy.innovate.service.ISysUserService;
import org.springframework.stereotype.Service;

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


    @Override
    public SysUser selectByNameAndPwd(String username, String password) {

        SysUser sysUser = new SysUser();
        if (StringUtils.isEmpty(username) && StringUtils.isEmpty(password)){
            return sysUser;
        }

        sysUser.setLoginName(username);
        sysUser.setPassword(password);

        return super.selectOne(new EntityWrapper<SysUser>(sysUser));
    }
}
