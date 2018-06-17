package com.lzy.innovate.permission;

import com.baomidou.kisso.Token;

/**
 * Created by lzy on 2017/3/11.
 * 检查菜单或者按钮权限接口
 */
public interface Permission {

    /**
     * 检查菜单权限
     * @param token
     * @param code
     * @return
     */
    boolean checkMenuPermission(Token token, String code);

    /**
     * 检查按钮权限
     * @param token
     * @param code
     * @return
     */
    boolean checkOperPermission(Token token, String code);

    /**
     * 检查权限
     * @param token
     * @param code
     * @return
     */
    boolean checkPermission(Token token, String code);
}
