package com.lzy.innovate.utils.excel;

import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

/**
 * Created by laihziyuan on 2017/6/2.
 *
 * 反射工具类
 */
public abstract class ReflectingHelper {

    private static final Logger logger = LoggerFactory.getLogger("manager.log");

    /**
     * 通过属性反射获得值
     * @param t
     * @param fieldName
     * @return
     * @throws Exception
     */
    public static Object getValueByField(Object t, String fieldName){

        if (t == null || StringUtils.isEmpty(fieldName)){
            return null;
        }

        Field field = getFieldByFieldName(t, fieldName);
        Object value = null;

        try {
            if (!field.isAccessible()){
                /**
                 * 设置属性为可访问
                 */
                field.setAccessible(true);

                    value = field.get(t);
                field.setAccessible(false);
            }else{
                value = field.get(t);
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error("com.aebiz.b2b2c.basebusiness.utils.excel.ReflectingHelper \n getValueByField", e);
        }

        return value;
    }

    /**
     * 通过字符串获得对应的属性
     * @param t
     * @param fieldName
     * @return
     */
    public static Field getFieldByFieldName(Object t, String fieldName) {

        for (Class<?> superClass = t.getClass(); superClass != T.class; superClass = superClass
                .getSuperclass()) {

            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                logger.error("com.aebiz.b2b2c.basebusiness.utils.excel.ReflectingHelper \n getFieldByFieldName", e);
            }

        }
        return null;
    }
}
