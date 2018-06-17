package com.lzy.innovate.utils.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Collection;
import java.util.Map;

/**
 * 设置内容抽象模块
 * Created by laizhiyuan on 2017/6/6.
 */
public abstract class AbstractSetContent<T> {

    public AbstractSetContent(Map<String, CellStyle> styleMap, Sheet sheet, AbstractGetFieldValue abstractGetFieldValue, AbstractSetValueToCell abstractSetValueToCell) {
        this.styleMap = styleMap;
        this.sheet = sheet;
        this.abstractGetFieldValue = abstractGetFieldValue;
        this.abstractSetValueToCell = abstractSetValueToCell;
    }

    /**
     * 样式库
     */
    protected Map<String, CellStyle> styleMap;

    /**
     * 组合表
     */
    protected Sheet sheet;

    /**
     * 组合获得属性
     */
    protected AbstractGetFieldValue abstractGetFieldValue;

    /**
     * 组合设置值到单元格
     */
    protected AbstractSetValueToCell abstractSetValueToCell;


    /**
     * 设置内容抽象方法
     * @param dataSources
     * @param columns
     */
    public abstract void setContent(Collection<T> dataSources, String[] columns);
}
