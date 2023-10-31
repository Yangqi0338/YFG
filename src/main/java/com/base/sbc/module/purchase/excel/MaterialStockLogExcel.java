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

public class MaterialStockLogExcel {
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
        ClassPathResource resource = new ClassPathResource("excelTemp/materialStockLogTemp.xlsx");
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

    public XSSFWorkbook createWorkBook(List<MaterialStockLog> materialStockLogList) throws IOException {
        init();
        XSSFSheet sheet = this.objWb.getSheetAt(0);
        initTemplateStyle(sheet);

        int i = 1;
        for(MaterialStockLog log : materialStockLogList){
            XSSFRow row = sheet.createRow(i);

            // 序号
            PoiUtilsXlsx.createStringCell(row, 0, String.valueOf(i), this.templateCellStyleCenter);
            // 操作时间
            PoiUtilsXlsx.createStringCell(row, 1, DateUtil.format(log.getCreateDate(), "yyyy-MM-dd HH:mm:ss"), this.templateCellStyleCenter);
            // 物料编码
            PoiUtilsXlsx.createStringCell(row, 2, log.getMaterialCode(), this.templateCellStyleCenter);
            // 物料名称
            PoiUtilsXlsx.createStringCell(row, 3, log.getMaterialName(), this.templateCellStyleCenter);
            // 颜色
            PoiUtilsXlsx.createStringCell(row, 4, log.getMaterialColor(), this.templateCellStyleCenter);
            // 规格
            PoiUtilsXlsx.createStringCell(row, 5, log.getMaterialSpecifications(), this.templateCellStyleCenter);
            // 单号
            PoiUtilsXlsx.createStringCell(row, 6, log.getRelationCode(), this.templateCellStyleCenter);
            // 仓库
            PoiUtilsXlsx.createStringCell(row, 7, log.getWarehouseName(), this.templateCellStyleCenter);
            // 出入库类型
            PoiUtilsXlsx.createStringCell(row, 8, StringUtils.equals("0", log.getType()) ? "入库" : "出库" , this.templateCellStyleCenter);
            // 变动前数量
            PoiUtilsXlsx.createStringCell(row, 9, log.getBeforeQuality().stripTrailingZeros().toPlainString() , this.templateCellStyleCenter);
            // 变动数量
            PoiUtilsXlsx.createStringCell(row, 10, log.getQuality().stripTrailingZeros().toPlainString() , this.templateCellStyleCenter);
            // 库存数量
            PoiUtilsXlsx.createStringCell(row, 11, log.getStockQuality().stripTrailingZeros().toPlainString() , this.templateCellStyleCenter);

            i++;
        }

        return this.objWb;
    }
}
