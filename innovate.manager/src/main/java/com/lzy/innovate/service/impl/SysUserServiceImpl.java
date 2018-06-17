package com.lzy.innovate.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.lzy.innovate.entity.SysUser;
import com.lzy.innovate.mapper.SysUserMapper;
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
 * @since 2017-02-27
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

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


    @Override
    public List<SysUser> findUserListByPagin(Map<String, Object> param) {
        return baseMapper.findUserListByPagin(param);
    }

    @Override
    public int findUserCount(Map<String, Object> param) {
        return baseMapper.findUserCount(param);
    }
}
