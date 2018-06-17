package com.lzy.innovate.utils.excel;

import org.apache.poi.xssf.usermodel.XSSFRichTextString;

/**
 * 设置值到单元格抽象模块
 * Created by Dell on 2017/6/5.
 */
public abstract class AbstractSetValueToCell {

    /**
     * 将值设置到单元格抽象方法
     * @param fieldValue 属性值
     * @return XSSFRichTextString 设置好的单元格
     */
    public abstract XSSFRichTextString setValue(Object fieldValue);
}
