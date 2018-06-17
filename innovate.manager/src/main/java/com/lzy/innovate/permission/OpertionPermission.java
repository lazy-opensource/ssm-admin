package com.lzy.innovate.permission;

import com.baomidou.kisso.annotation.Action;

import java.lang.annotation.*;

/**
 * Created by lzy on 2017/3/11.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpertionPermission {


    /**
     * 按钮级别注解
     * @return
     */
    Action action() default Action.Normal;
    String code() default "";
}
