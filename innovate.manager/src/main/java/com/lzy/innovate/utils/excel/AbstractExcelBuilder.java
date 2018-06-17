package com.lzy.innovate.utils.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 建造者模式-抽象建造者
 * 为创建一个 Workbook 对象的各个部件指定抽象接口
 *
 *
 * Created by laizhiyuan on 2017/6/2.
 */
public abstract class AbstractExcelBuilder<T> {

    //记录日志
    protected static Logger log = LoggerFactory.getLogger("manager.log");

    /**
     * 构建对象
     */
    protected Workbook workbook;

    /**
     * 表格
     */
    protected Sheet sheet;

    /**
     * 样式库
     */
    protected Map<String, CellStyle> styleMap = new HashMap<String, CellStyle>();

    /**
     * 属性装换为标题对照表
     */
    protected Map<String, String> fieldTitleTable = new HashMap<String, String>();

    /**
     * 设置值到单元格模块
     */
    protected AbstractSetValueToCell abstractSetValueToCell;

    /**
     * 获得属性值模块
     */
    protected AbstractGetFieldValue abstractGetFieldValue;

    /**
     * 设置样式模块
     */
    protected AbstractSetStyle abstractSetStyle;

    /**
     * 设置标题模块
     */
    protected AbstractSetTitle abstractSetTitle;

    /**
     * 设置内容模块
     */
    protected AbstractSetContent<T> abstractSetContent;

    protected void addStyle(String key, CellStyle style){
        styleMap.put(key, style);
    }

    protected CellStyle getStyle(String key){
        return styleMap.get(key);
    }

    /**
     * 添加一个属性标题对照表
     *
     * @param fieldName 属性名称
     * @param titleName 标题名称
     */
    protected void addFieldTitleTable(String fieldName, String titleName){
        fieldTitleTable.put(fieldName, titleName);
    }


    /**
     * 建造工作簿
     */
    public abstract AbstractExcelBuilder buildWorkbook(Workbook wb);

    /**
     * 建造表格
     */
    public abstract AbstractExcelBuilder buildSheet(String sheetName);

    public AbstractExcelBuilder buildColumn(int columnSize, boolean isAutoColumnWith){

        if (isAutoColumnWith){
            for (int i = 0; i < columnSize; i++){
                sheet.autoSizeColumn(i);
            }
            return this;
        }else{
            for (int i = 0; i < columnSize; i++){
                sheet.setDefaultColumnWidth(i);
            }
        }

        return this;
    }

    /**
     * 建造内容
     * @param dataSources 数据
     * @param columns title
     */
    public abstract AbstractExcelBuilder buildContent(Collection<T> dataSources, String[] columns, AbstractSetContent<T> abstractSetContent);

    /**
     * 建造标题
     * @param titles
     * @return
     */
    public abstract AbstractExcelBuilder buildTitle(String[] titles);


    /**
     * 建造样式
     * @return
     */
    public abstract AbstractExcelBuilder buildStyle();


    /**
     * 生成最终构建好的对象
     * @return
     */
    public abstract void flush(HttpServletResponse response);

    /**
     *  获得属性值的方式，客户端可自定义
     * @param getFieldValue
     */
    public void getFieldValueImpl(AbstractGetFieldValue getFieldValue){
        if (getFieldValue == null){
            this.abstractGetFieldValue = new DefaultGetValue();
        }else{
            this.abstractGetFieldValue = getFieldValue;
        }
    }

    /**
     * 设置单元格值的方式，客户端自定义
     * @param setValue
     */
    public void setValueToCellImpl(AbstractSetValueToCell setValue){

        if (setValue == null){
            this.abstractSetValueToCell = new DefaultSetValueToCell();
        }else{
            this.abstractSetValueToCell = setValue;
        }

    }

    /**
     * 设置样式实现者，客户端自定义
     * @param style
     */
    public void setStyleImpl(AbstractSetStyle style){
        if (style == null){
            this.abstractSetStyle = new DefaultSetStyle();
        }else{
            this.abstractSetStyle = style;
        }
    }

    /**
     * 设置标题实现者，客户端自定义
     * @param setTitle
     */
    public void setTitleImpl(AbstractSetTitle setTitle){
        if (setTitle == null){
            this.abstractSetTitle = new DefaultSetTitle();
        }else{
            this.abstractSetTitle = setTitle;
        }
    }

    public void setContentImpl(AbstractSetContent<T> abstractSetContent){
        if(abstractSetContent == null){
            this.abstractSetContent = new DefaultSetContent<T>(styleMap, sheet, abstractGetFieldValue, abstractSetValueToCell);
        }else{
            this.abstractSetContent = abstractSetContent;
        }
    }
}
