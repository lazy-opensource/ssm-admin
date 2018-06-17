package com.lzy.innovate.utils.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.util.Map;

/**
 * Created by Dell on 2017/6/5.
 */
public class DefaultSetTitle extends AbstractSetTitle {


    /**
     * 设置标题抽象类
     * @param sheet 表格
     * @param columns 属性数组
     * @param styleMap 样式库
     * @param fieldTitleTable 属性标题对照表
     */
    @Override
    public void setTitle(Sheet sheet, String[] columns, Map<String, CellStyle> styleMap, Map<String, String> fieldTitleTable){
        //产生表格标题行
        XSSFRow row = (XSSFRow) sheet.createRow(0);

        if(columns !=null  && columns.length != 0){

            for (int i = 0; i < columns.length; i++) {
                XSSFCell cell = row.createCell(i);
                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(styleMap.get(AbstractSetStyle.TITLE_STYLE_KEY));

                XSSFRichTextString text = new XSSFRichTextString(fieldTitleTable.get(columns[i]));
                cell.setCellValue(text);
            }
        }

    }

}
