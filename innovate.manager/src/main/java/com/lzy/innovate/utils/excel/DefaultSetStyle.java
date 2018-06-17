package com.lzy.innovate.utils.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

import java.awt.*;
import java.util.Map;

/**
 * Created by laizhiyuan on 2017/6/5.
 */
public class DefaultSetStyle extends AbstractSetStyle {


    @Override
    public void setStyle(Workbook workbook, Map<String, CellStyle> styleMap) {
        // 生成一个样式
        XSSFCellStyle style1 = (XSSFCellStyle) workbook.createCellStyle();
        /**
         * 标题样式
         */
        // 前景色(蓝色)
        XSSFColor myColor = new XSSFColor(Color.LIGHT_GRAY);
        style1.setFillForegroundColor(myColor);
        // 设置单元格填充样式
        style1.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        // 设置单元格边框
        style1.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style1.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style1.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style1.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style1.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style1.setFont(font);
        styleMap.put(TITLE_STYLE_KEY, style1);

        /**
         * 内容样式
         */
        XSSFCellStyle style2 = (XSSFCellStyle) workbook.createCellStyle();
        style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        XSSFFont font2 = (XSSFFont) workbook.createFont();
        font2.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);
        styleMap.put(CONTENT_STYLE_KEY, style2);
    }
}
