package com.lzy.innovate.utils.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

/**
 * 设置样式库抽象模块
 * Created by Dell on 2017/6/5.
 */
public abstract class AbstractSetStyle {

    /**
     * 任何实现类，如果定义的是内容样式，存入map的key必须是这个，否则不生效
     */
    public static final String CONTENT_STYLE_KEY = "contentStyleKey";

    /**
     * 任何实现类，如果定义的是标题样式且使用默认的设置Title方式，
     * 则存入map的key必须是这个，否则不生效，如果同时自定义了设置Title的实现，则不受这个限制
     */
    public static final String TITLE_STYLE_KEY = "titleStyleKey";

    /**
     * 封装样式抽象方法
     *
     * @param workbook 表格
     * @param styleMap  样式库
     */
    public abstract void setStyle(Workbook workbook, Map<String, CellStyle> styleMap);
}
