package com.base.sbc.module.purchase.excel;

import cn.hutool.core.date.DateUtil;
import com.base.sbc.config.utils.PoiUtilsXlsx;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.purchase.entity.MaterialStock;
import com.base.sbc.module.purchase.entity.MaterialStockLog;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

public class MaterialStockExcel {
    /** 工作薄对象 */
    private XSSFWorkbook objWb = new XSSFWorkbook();

    private XSSFCellStyle templateCellStyleLeft = null;
    private XSSFCellStyle templateCellStyleCenter = null;

    /**
     * 初始化字体和样式
     *
     * @throws IOException
     */
    private void init() throws IOException {
        ClassPathResource resource = new ClassPathResource("excelTemp/materialStockTemp.xlsx");
        objWb = new XSSFWorkbook(resource.getInputStream());
    }

    /**
     * 初始化模板样式
     *
     * @param objSheet
     */
    public void initTemplateStyle(XSSFSheet objSheet) {
        XSSFRow row = objSheet.getRow(0);
        XSSFCell cell = row.getCell(0);

        XSSFFont normalFont = PoiUtilsXlsx.createFont(this.objWb, "微软雅黑", 200, (short) (-1), (short) (-1));

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

    public XSSFWorkbook createWorkBook(List<MaterialStock> materialStockList) throws IOException {
        init();
        XSSFSheet sheet = this.objWb.getSheetAt(0);
        initTemplateStyle(sheet);

        int i = 1;
        for(MaterialStock stock : materialStockList){
            XSSFRow row = sheet.createRow(i);

            // 序号
            PoiUtilsXlsx.createStringCell(row, 0, String.valueOf(i), this.templateCellStyleCenter);
            // 仓库
            PoiUtilsXlsx.createStringCell(row, 1, stock.getWarehouseName(), this.templateCellStyleCenter);
            // 物料编码
            PoiUtilsXlsx.createStringCell(row, 2, stock.getMaterialCode(), this.templateCellStyleCenter);
            // 物料名称
            PoiUtilsXlsx.createStringCell(row, 3, stock.getMaterialName(), this.templateCellStyleCenter);
            // 物料SKU
            PoiUtilsXlsx.createStringCell(row, 4, stock.getMaterialSku(), this.templateCellStyleCenter);
            // 单位
            PoiUtilsXlsx.createStringCell(row, 5, stock.getUnit(), this.templateCellStyleCenter);
            // 规格
            PoiUtilsXlsx.createStringCell(row, 6, stock.getMaterialSpecifications(), this.templateCellStyleCenter);
            // 规格编码
            PoiUtilsXlsx.createStringCell(row, 7, stock.getMaterialSpecificationsCode(), this.templateCellStyleCenter);
            // 颜色
            PoiUtilsXlsx.createStringCell(row, 8, stock.getMaterialColor(), this.templateCellStyleCenter);
            // 颜色编码
            PoiUtilsXlsx.createStringCell(row, 9, stock.getMaterialColorCode(), this.templateCellStyleCenter);
            // 库存数量
            PoiUtilsXlsx.createStringCell(row, 10, stock.getStockQuantity().stripTrailingZeros().toPlainString() , this.templateCellStyleCenter);
            // 默认供应商
            PoiUtilsXlsx.createStringCell(row, 11, stock.getDefaultSupplier(), this.templateCellStyleCenter);
            // 库位
            PoiUtilsXlsx.createStringCell(row, 12, stock.getPosition() , this.templateCellStyleCenter);

            i++;
        }

        return this.objWb;
    }
}
