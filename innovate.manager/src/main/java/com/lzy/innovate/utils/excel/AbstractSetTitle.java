package com.lzy.innovate.utils.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Map;

/**
 * 设置标题抽象模块
 * Created by Dell on 2017/6/5.
 */
public abstract class AbstractSetTitle {


    /**
     * 设置标题抽象类
     * @param sheet 表格
     * @param columns 属性数组
     * @param styleMap 样式库
     * @param fieldTitleTable 属性标题对照表
     */
    public abstract void setTitle(Sheet sheet, String[] columns, Map<String, CellStyle> styleMap, Map<String, String> fieldTitleTable);
}
