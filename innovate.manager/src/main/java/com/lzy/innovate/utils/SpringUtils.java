package com.lzy.innovate.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by lzy on 2017/3/12.
 */
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        ctx = applicationContext;
    }

    public static Object getBean(String name ){
        return ctx.getBean(name);
    }
}
