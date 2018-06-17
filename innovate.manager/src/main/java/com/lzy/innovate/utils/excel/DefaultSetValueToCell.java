package com.lzy.innovate.utils.excel;

import org.apache.poi.xssf.usermodel.XSSFRichTextString;

/**
 * 默认设置值实现
 * Created by Dell on 2017/6/5.
 */
public class DefaultSetValueToCell extends AbstractSetValueToCell {


    /**
     * 默认设置值得方式是直接装为字符串
     * @param fieldValue
     * @return
     */
    @Override
    public XSSFRichTextString setValue(Object fieldValue) {

        return new XSSFRichTextString(String.valueOf(fieldValue));
    }
}
