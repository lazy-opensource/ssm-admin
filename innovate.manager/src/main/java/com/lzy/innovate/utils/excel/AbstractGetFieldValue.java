package com.lzy.innovate.utils.excel;

/**
 * 获得属性值抽象模块
 * Created by Dell on 2017/6/5.
 */
public abstract class AbstractGetFieldValue {

    /**
     * 抽象获得属性值方法
     * @param obj 反射需要的对象
     * @param fieldName 属性字符串
     * @return
     */
    public abstract Object getFiledValue(Object obj, String fieldName);
}
