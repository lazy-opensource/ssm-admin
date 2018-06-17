package com.lzy.innovate.utils.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * 设置内容默认实现者
 * Created by laizhiyuan on 2017/6/6.
 */
public class DefaultSetContent<T> extends AbstractSetContent<T>{


    public DefaultSetContent(Map<String, CellStyle> styleMap, Sheet sheet, AbstractGetFieldValue abstractGetFieldValue, AbstractSetValueToCell abstractSetValueToCell) {
        super(styleMap, sheet, abstractGetFieldValue, abstractSetValueToCell);
    }

    @Override
    public void setContent(Collection<T> dataSources, String[] columns) {

        if (dataSources == null && dataSources.size() < 1 || columns == null && columns.length == 0){
            return;
        }

        Iterator<T> iterator = dataSources.iterator();
        XSSFRow row = null;
        XSSFCell cell = null;
        int rowIx = 0;
        Object fieldValue = null;
        XSSFRichTextString xssfValue = null;

        while (iterator.hasNext()){

            rowIx++;

            /**
             * 创建行  一条数据一行
             */
            row = (XSSFRow) sheet.createRow(rowIx);
            T t = iterator.next();
            for (int i = 0; i < columns.length; i++){
                /**
                 * 创建一个列， 一个标题一列
                 */
                cell = row.createCell(i);
                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(styleMap.get(AbstractSetStyle.CONTENT_STYLE_KEY));

                fieldValue = abstractGetFieldValue.getFiledValue(t, columns[i]);

                xssfValue = abstractSetValueToCell.setValue(fieldValue);
                cell.setCellValue(xssfValue);
            }
        }
    }


}
