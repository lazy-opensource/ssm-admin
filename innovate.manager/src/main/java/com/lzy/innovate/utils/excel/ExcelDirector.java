package com.lzy.innovate.utils.excel;

import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

/**
 * 建造者模式- 负责规范流程
 * Created by laizhiyuan on 2017/6/2.
 */
public class ExcelDirector<T> {

    private AbstractExcelBuilder<T> abstractExcelBuilder;

    private String sheetName;

    private String[] columns;

    private Collection<T> dataSources;

    private Workbook workbook = null;

    private AbstractSetContent<T> abstractSetContent;

    private Map<Integer, Integer> columnWithMap;

    private boolean isAutoColumnWith = false;

    private Integer columnSize = null;

    public ExcelDirector(AbstractExcelBuilder<T> abstractExcelBuilder) {
        this.abstractExcelBuilder = abstractExcelBuilder;
        /**
         * 初始化时使用默认方式
         */
        overrideDefaultSetValueToCell(null)
                .overrideDefaultGetFieldValue(null)
                .overrideDefaultSetTitle(null)
                .overrideDefaultSetStyle(null)
                .overrideDefaultSetContent(null);
    }


    /**
     * 核心方法，这里决定构建一个复杂对象的流程
     * @return
     */
    public ExcelDirector construct(){

        abstractExcelBuilder.
                /**
                 * 先构建一个工作簿
                 */
                buildWorkbook(workbook).
                /**
                 * 再构建一个工作表
                 */
                buildSheet(sheetName).
                /**
                 * 再构建列宽
                 */
                buildColumn(columnSize,isAutoColumnWith).
                /**
                 * 再构建一个样式
                 */
                buildStyle().
                /**
                 * 再构建表的标题
                 */
                buildTitle(columns).
                /**
                 * 最后构建内容
                 */
                buildContent(dataSources, columns, abstractSetContent);


        /**
         * 至此构建一个Excel完毕
         */
        return this;
    }

    public ExcelDirector setColumnSize(int size){
        this.columnSize = size;
        return this;
    }

    /**
     * 添加一个属性标题对照表
     *
     * @param fieldName 属性名称
     * @param titleName 标题名称
     */
    public ExcelDirector addFieldTitleTable(String fieldName, String titleName){
        abstractExcelBuilder.addFieldTitleTable(fieldName, titleName);
        return this;
    }

    /**
     * 覆盖设置值到表格内容
     * @param setValue
     * @return
     */
    public ExcelDirector overrideDefaultSetValueToCell(AbstractSetValueToCell setValue){

        abstractExcelBuilder.setValueToCellImpl(setValue);
        return this;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    /**
     * 覆盖获得属性值
     * @param getFieldValue
     * @return
     */
    public ExcelDirector overrideDefaultGetFieldValue(AbstractGetFieldValue getFieldValue){

        abstractExcelBuilder.getFieldValueImpl(getFieldValue);
        return this;
    }

    public ExcelDirector isAutoColumnWith(boolean isAutoColumnWith){

        this.isAutoColumnWith = isAutoColumnWith;
        return this;
    }

    /**
     * 覆盖默认设置的样式
     * @param setStyle
     * @return
     */
    public ExcelDirector overrideDefaultSetStyle(AbstractSetStyle setStyle){

        abstractExcelBuilder.setStyleImpl(setStyle);
        return this;
    }

    /**
     * 覆盖默认设置的标题
     * @param setTitle
     * @return
     */
    public ExcelDirector overrideDefaultSetTitle(AbstractSetTitle setTitle){

        abstractExcelBuilder.setTitleImpl(setTitle);
        return this;
    }

    /**
     * 覆盖默认工作簿实现
     * @param wb
     * @return
     */
    public ExcelDirector overrideDefaultWorkbook(Workbook wb){
        this.workbook = wb;
        return this;
    }

    public ExcelDirector overrideDefaultSetContent(AbstractSetContent<T> abstractSetContent){

        this.abstractSetContent = abstractSetContent;
        return this;
    }


    /**
     * 写到响应流
     * @param response
     */
    public void flush(HttpServletResponse response){

        abstractExcelBuilder.flush(response);
    }

    public String getSheetName() {
        return sheetName;
    }

    public ExcelDirector setSheetName(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }

    public String[] getColumns() {
        return columns;
    }

    public ExcelDirector setColumns(String[] columns) {
        if (columnSize == null){
            columnSize = columns.length;
        }
        this.columns = columns;
        return this;
    }

    public Collection<T> getDataSources() {
        return dataSources;
    }

    public ExcelDirector setDataSources(Collection<T> dataSources) {
        this.dataSources = dataSources;
        return this;
    }

    public ExcelDirector setColumnWith(Map<Integer, Integer> columnWithMap){
        this.columnWithMap = columnWithMap;
        return this;
    }
}
