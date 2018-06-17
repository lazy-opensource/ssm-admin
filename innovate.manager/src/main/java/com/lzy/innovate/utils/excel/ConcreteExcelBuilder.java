package com.lzy.innovate.utils.excel;

import com.lzy.innovate.utils.date.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collection;

/**
 * 建造者模式-具体建造者
 *
 * 实现Builder的接口以构造和装配该产品的各个部件
 *
 * 这里是建造一个2007Excel
 *
 * Created by laizhiyuan on 2017/6/5.
 */
public class ConcreteExcelBuilder<T> extends AbstractExcelBuilder<T>{


    /**
     * 建造工作簿
     */
    @Override
    public AbstractExcelBuilder buildWorkbook(Workbook wb) {

        if (workbook == null){
            workbook = new XSSFWorkbook();
        }else{
            workbook = wb;
        }

        return this;
    }

    /**
     * 建造表格
     */
    @Override
    public AbstractExcelBuilder buildSheet(String sheetName) {
        sheet = workbook.createSheet(sheetName);

        return this;
    }

    /**
     * 建造内容
     * @param dataSources 数据
     * @param columns title
     */
    @Override
    public AbstractExcelBuilder buildContent(Collection<T> dataSources, String[] columns, AbstractSetContent<T> abstractSetContent) {

        setContentImpl(abstractSetContent);
        this.abstractSetContent.setContent(dataSources, columns);
        return this;
    }


    /**
     * 建造标题
     * @param titles
     * @return
     */
    @Override
    public AbstractExcelBuilder buildTitle(String[] titles) {

        abstractSetTitle.setTitle(sheet, titles, styleMap, fieldTitleTable);

        return this;
    }

    /**
     * 建造样式
     * @return
     */
    @Override
    public AbstractExcelBuilder buildStyle() {

        abstractSetStyle.setStyle(workbook, styleMap);

        return this;
    }

    /**
     * 生成最终构建好的对象
     * @return
     */
    @Override
    public void flush(HttpServletResponse response) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            /**
             * 将工作簿对象转换为字节输出流对象
             */
            workbook.write(os);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("com.aebiz.b2b2c.basebusiness.utils.excel.Concrete2007ExcelBuilder\n write", e);
        }

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {

            /**
             * 将工作簿字节流输出流转换为字节数组
             */
            byte[] content = os.toByteArray();

            /**
             * 将字节数组转化为字节数组输入流对象
             */
            InputStream is = new ByteArrayInputStream(content);
            /**
             * 设置response参数，可以打开下载页面
             */
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader(
                    "Content-Disposition", "attachment;filename="
                            + new String((sheet.getSheetName() + DateUtil.getCurrentTime("yyyy-MM-dd") + ".xlsx").getBytes(), "iso-8859-1"));

            ServletOutputStream out = response.getOutputStream();

            /**
             * 封装为缓冲，减少读写次数，提高效率
             */
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);

            /**
             * 设定每次从内存读多少
             */
            byte[] buff = new byte[2048];
            int bytesRead;

            /**
             * 循环从内存读取，并写到Resonse响应流中
             */
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }

            /**
             * 异常部分
             */
        } catch (final IOException e) {
            e.printStackTrace();
            log.error("com.aebiz.b2b2c.basebusiness.utils.excel.Concrete2007ExcelBuilder\n write", e);
        } finally {

            /**
             * 关闭资源
             */
            if (bis != null)
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("com.aebiz.b2b2c.basebusiness.utils.excel.Concrete2007ExcelBuilder\n write", e);
                }
            if (bos != null)
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("com.aebiz.b2b2c.basebusiness.utils.excel.Concrete2007ExcelBuilder\n write", e);
                }
        }
    }

}
