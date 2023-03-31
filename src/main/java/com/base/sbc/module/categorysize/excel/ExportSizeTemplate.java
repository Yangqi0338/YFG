package com.base.sbc.module.categorysize.excel;


import com.base.sbc.config.utils.PoiUtilsXlsx;
import com.base.sbc.config.utils.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * 类描述：导出尺寸表模板
 *
 * @author lile
 * @version 1.0
 * @address com.base.sbc.api.saas.excel;
 * @email lilemyemail@163.com
 * @date 创建时间：2022-3-10 17:00:00
 */
public class ExportSizeTemplate {
    /**
     * 工作薄对象
     */
    private XSSFWorkbook objWb = null;

    /**
     * 居中对齐样式
     */
    private XSSFCellStyle objCellStyleCenter = null;
    private XSSFCellStyle objCellStyleCenter2 = null;
    /**
     * 居左对齐样式
     */
    private XSSFCellStyle objCellStyleLeft = null;
    /**
     * 居右对齐样式
     */
    private XSSFCellStyle objCellStyleRight = null;
    private XSSFCellStyle objCellStyleRight2 = null;

    /**
     * 单元格高度基础倍率，单元格140，在此设置为140*basicHeightMul
     */
    private static int basicHeightMul = 20;

    /**
     * 初始化字体和样式
     *
     * @throws IOException
     * @throws FileNotFoundException
     */
    private void init() throws FileNotFoundException, IOException {

        ClassPathResource resource = new ClassPathResource("excelTemp/partSizeTemp.xlsx");
        objWb = new XSSFWorkbook(resource.getInputStream());
        // 定义3种字体
        XSSFFont normalFont = PoiUtilsXlsx.createFont(this.objWb, "宋体", 200, (short) (-1), (short) (-1));
        XSSFDataFormat fmt = objWb.createDataFormat();
        // 定义基本样式
        XSSFCellStyle objCellStyle = PoiUtilsXlsx.createCellStyle(this.objWb, normalFont);

        // 定义样式——有边框居左
        this.objCellStyleLeft = PoiUtilsXlsx.createCellStyle(this.objWb, objCellStyle, normalFont,
                HorizontalAlignment.LEFT);
        // 自动换行
        this.objCellStyleLeft.setWrapText(false);
        // 上下居中
        this.objCellStyleLeft.setVerticalAlignment(VerticalAlignment.CENTER);
        //下边框
        this.objCellStyleLeft.setBorderBottom(BorderStyle.THIN);
        //左边框
        this.objCellStyleLeft.setBorderLeft(BorderStyle.THIN);
        //上边框
        this.objCellStyleLeft.setBorderTop(BorderStyle.THIN);
        //右边框
        this.objCellStyleLeft.setBorderRight(BorderStyle.THIN);

        // 定义样式——有边框居中
        this.objCellStyleCenter = PoiUtilsXlsx.createCellStyle(this.objWb, objCellStyle, normalFont,
                HorizontalAlignment.CENTER);
        // 自动换行
        this.objCellStyleCenter.setWrapText(false);
        // 上下居中
        this.objCellStyleCenter.setVerticalAlignment(VerticalAlignment.CENTER);
        //下边框
        this.objCellStyleCenter.setBorderBottom(BorderStyle.THIN);
        //左边框
        this.objCellStyleCenter.setBorderLeft(BorderStyle.THIN);
        //上边框
        this.objCellStyleCenter.setBorderTop(BorderStyle.THIN);
        //右边框
        this.objCellStyleCenter.setBorderRight(BorderStyle.THIN);

        // 定义样式——有边框居中
        this.objCellStyleCenter2 = PoiUtilsXlsx.createCellStyle(this.objWb, objCellStyle, normalFont,
                HorizontalAlignment.CENTER);
        // 自动换行
        this.objCellStyleCenter2.setWrapText(false);
        // 上下居中
        this.objCellStyleCenter2.setVerticalAlignment(VerticalAlignment.CENTER);

        // 定义样式——有边框居右
        this.objCellStyleRight = PoiUtilsXlsx.createCellStyle(this.objWb, objCellStyle, normalFont,
                HorizontalAlignment.RIGHT);
        // 自动换行
        this.objCellStyleRight.setWrapText(false);
        // 上下居中
        this.objCellStyleRight.setVerticalAlignment(VerticalAlignment.CENTER);
        //下边框
        this.objCellStyleRight.setBorderBottom(BorderStyle.THIN);
        //左边框
        this.objCellStyleRight.setBorderLeft(BorderStyle.THIN);
        //上边框
        this.objCellStyleRight.setBorderTop(BorderStyle.THIN);
        //右边框
        this.objCellStyleRight.setBorderRight(BorderStyle.THIN);


        // 定义样式——有边框居右
        this.objCellStyleRight2 = PoiUtilsXlsx.createCellStyle(this.objWb, objCellStyle, normalFont,
                HorizontalAlignment.RIGHT);
        // 自动换行
        this.objCellStyleRight2.setWrapText(false);
        // 上下居中
        this.objCellStyleRight2.setVerticalAlignment(VerticalAlignment.CENTER);
        this.objCellStyleRight2.setDataFormat(fmt.getFormat("0.00"));
    }

    /**
     * 创建excel
     *
     * @return 返回创建好的excel
     * @throws IOException IO异常
     */
    public XSSFWorkbook createWorkBook(String sizes) throws IOException {
        // 初始化字体和格式
        init();
        // 创建Sheet页  获取第一页
        XSSFSheet objSheet = this.objWb.getSheetAt(0);
        setSheetRow(objSheet, sizes);
        return this.objWb;
    }

    /**
     * 用于给创建的excel 赋值
     */
    private void setSheetRow(XSSFSheet objSheet, String sizes) {
        List<String> strings = StringUtils.convertList(sizes);
        int beginCol = 5;
        for (String string : strings) {
            PoiUtilsXlsx.createStringCell(objSheet.getRow(4), beginCol, string, objCellStyleCenter);
            beginCol++;
        }

    }
}
