package com.lzy.innovate.permission;

import com.baomidou.kisso.annotation.Action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lzy on 2017/3/11.
 * 菜单权限注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MenuPermission {

    Action action() default Action.Normal;

    String code() default "";
}
