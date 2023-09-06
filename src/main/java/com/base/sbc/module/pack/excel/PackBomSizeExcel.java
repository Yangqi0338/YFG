package com.base.sbc.module.pack.excel;

import com.base.sbc.client.ccm.entity.BasicUnitConfig;
import com.base.sbc.config.utils.PoiUtilsXlsx;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.band.entity.Band;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMeasurement;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PackBomSizeExcel {
    /**
     * 工作薄对象
     */
    private XSSFWorkbook objWb = null;

    private XSSFCellStyle templateCellStyleLeft = null;
    private XSSFCellStyle templateCellStyleCenter = null;

    /**
     * 初始化字体和样式
     *
     * @throws IOException
     */
    private void init() throws IOException {
        ClassPathResource resource = new ClassPathResource("excelTemp/packBom-size.xlsx");
        objWb = new XSSFWorkbook(resource.getInputStream());

        XSSFFont normalFont = PoiUtilsXlsx.createFont(this.objWb, "宋体", 200, (short) (-1), (short) (-1));
        XSSFDataFormat fmt = objWb.createDataFormat();
        // 定义基本样式
        XSSFCellStyle objCellStyle = PoiUtilsXlsx.createCellStyle(this.objWb, normalFont);
    }

    /**
     * 初始化模板样式
     *
     * @param objSheet
     */
    public void initTemplateStyle(XSSFSheet objSheet) {
        XSSFRow row = objSheet.getRow(0);
        XSSFCell cell = row.getCell(0);

        XSSFFont normalFont = PoiUtilsXlsx.createFont(this.objWb, "宋体", 200, (short) (-1), (short) (-1));

        this.templateCellStyleLeft = cell.getCellStyle();
        // 上下居中
        this.templateCellStyleLeft.setVerticalAlignment(VerticalAlignment.CENTER);
        // 水平居左
        this.templateCellStyleLeft.setAlignment(HorizontalAlignment.LEFT);
        // 设置字体
        this.templateCellStyleLeft.setFont(normalFont);

        this.templateCellStyleCenter = cell.getCellStyle();
        // 上下居中
        this.templateCellStyleCenter.setVerticalAlignment(VerticalAlignment.CENTER);
        // 水平居中
        this.templateCellStyleCenter.setAlignment(HorizontalAlignment.CENTER);
        // 设置字体
        this.templateCellStyleCenter.setFont(normalFont);
        this.templateCellStyleCenter.setWrapText(true);

        this.templateCellStyleCenter.setBorderBottom(BorderStyle.THIN);
        this.templateCellStyleCenter.setBorderLeft(BorderStyle.THIN);
        this.templateCellStyleCenter.setBorderTop(BorderStyle.THIN);
        this.templateCellStyleCenter.setBorderRight(BorderStyle.THIN);
    }

    /**
     *
     * @param sizeArray 尺寸集合
     * @return
     * @throws Exception
     */
    public XSSFWorkbook createWorkBook(String sizeArray, String ifWashing, List<BasicsdatumMeasurement> measurementList, List<BasicUnitConfig> basicUnitConfigList) throws Exception {
        // 初始化字体和格式
        init();
        XSSFSheet sheet = this.objWb.getSheetAt(0);
        initTemplateStyle(sheet);

        List<String> sizeList = StringUtils.convertList(sizeArray);

        int i = 6;
        int addIndex = StringUtils.equals(ifWashing, "1") ? 2 : 1;
        XSSFRow headRow = sheet.getRow(0);
        XSSFRow headTwoRow = sheet.createRow(1);
        for(String size : sizeList){
            PoiUtilsXlsx.createStringCell(headRow, i, size, this.templateCellStyleCenter);
            PoiUtilsXlsx.createStringCell(headRow, i+1, "", this.templateCellStyleCenter);

            PoiUtilsXlsx.createStringCell(headTwoRow, i, "样板尺寸", this.templateCellStyleCenter);
            PoiUtilsXlsx.createStringCell(headTwoRow, i+1, "成衣尺寸", this.templateCellStyleCenter);

            if(StringUtils.equals(ifWashing, "1")){
                //展示 洗后尺寸
                PoiUtilsXlsx.createStringCell(headRow, i+2, "", this.templateCellStyleCenter);
                PoiUtilsXlsx.createStringCell(headTwoRow, i+2, "洗后尺寸", this.templateCellStyleCenter);
            }

            PoiUtilsXlsx.mergeCells(sheet, this.templateCellStyleCenter, 0, i, 0, i + addIndex);
            i = i + addIndex + 1;
        }
        PoiUtilsXlsx.createStringCell(headRow, i, "备注", this.templateCellStyleCenter);

        //前置标题 行合并
        for(int k = 0; k < 6; k ++) {
            PoiUtilsXlsx.mergeCells(sheet, this.templateCellStyleCenter, 0, k, 1, k);
        }
        PoiUtilsXlsx.mergeCells(sheet, this.templateCellStyleCenter, 0, i, 1, i);

        // 部位表 数据填充
        XSSFSheet partSheet = this.objWb.getSheetAt(1);
        int k = 0;
        for (BasicsdatumMeasurement item : measurementList) {
            partSheet.createRow(k).createCell(0).setCellValue(item.getMeasurement());
            k++;
        }

        // 单位表 数据填充
        XSSFSheet unitSheet = this.objWb.getSheetAt(2);
        k = 0;
        for (BasicUnitConfig item : basicUnitConfigList) {
            unitSheet.createRow(k).createCell(0).setCellValue(item.getUnitName());
            k++;
        }
        return this.objWb;
    }
}
