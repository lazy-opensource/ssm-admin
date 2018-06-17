package com.lzy.innovate.permission;

import com.baomidou.kisso.Token;
import com.lzy.innovate.controller.common.Pagin;
import com.lzy.innovate.dubbo.system.ISysMenuServiceSoa;
import com.lzy.innovate.dubbo.system.ISysOperServiceSoa;
import com.lzy.innovate.entity.SysMenu;
import com.lzy.innovate.entity.SysOper;
import com.lzy.innovate.utils.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by lzy on 2017/3/11.
 * 检查菜单或者按钮权限实现类
 */
public class PermissionImpl implements Permission {

    @Autowired
    private ISysOperServiceSoa iSysOperServiceSoa;

    @Autowired
    private ISysMenuServiceSoa iSysMenuServiceSoa;

    /**
     * 检查菜单权限
     * @param token 用户标识符
     * @param code  权限码
     * @return
     */
    @Override
    public boolean checkMenuPermission(Token token, String code) {

        if (token == null || StringUtils.isEmpty(code)){
            return true;
        }

        List<SysMenu> sysMenus = iSysMenuServiceSoa.findMenusByUserId(token.getUid());
        if (sysMenus == null){
            return false;
        }
        for (SysMenu sysMenu : sysMenus){
            if (code.equals(sysMenu.getCode())){
                return true;
            }
        }

        return false;
    }

    /**
     * 检查按钮权限
     * @param token 用户标识符
     * @param code  权限码
     * @return
     */
    @Override
    public boolean checkOperPermission(Token token, String code) {

        if (token == null || StringUtils.isEmpty(code)){
            return true;
        }
        Pagin<SysOper> sysOperPagin = new Pagin<>();
        sysOperPagin.setAll(true);
        sysOperPagin.setSort(false);
        Map<String, Object> map = Sets.map();
        map.put("page" , sysOperPagin);
        map.put("userId" , token.getUid());
        List<SysOper> sysOpers = iSysOperServiceSoa.findOpersByPagin(map);
        if (sysOpers != null){
            for (SysOper sysOper : sysOpers){
                if (code.equals(sysOper.getCode())){
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean checkPermission(Token token, String code) {
        return true;
    }
}
