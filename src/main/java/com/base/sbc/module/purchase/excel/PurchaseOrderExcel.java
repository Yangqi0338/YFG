package com.base.sbc.module.purchase.excel;

import cn.hutool.core.date.DateUtil;
import com.base.sbc.config.enums.OrderStatusEnum;
import com.base.sbc.config.enums.WarehouseStatusEnum;
import com.base.sbc.config.utils.PoiUtilsXlsx;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.purchase.entity.PurchaseOrder;
import com.base.sbc.module.purchase.entity.PurchaseOrderDetail;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

public class PurchaseOrderExcel {
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
        ClassPathResource resource = new ClassPathResource("excelTemp/purchaseOrderTemp.xlsx");
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

    public XSSFWorkbook createWorkBook(List<PurchaseOrder> purchaseOrderList) throws IOException {
        init();
        XSSFSheet sheet = this.objWb.getSheetAt(0);
        XSSFSheet detailSheet = this.objWb.getSheetAt(1);
        initTemplateStyle(sheet);

        int i = 1;
        int k = 1;
        for(PurchaseOrder purchaseOrder : purchaseOrderList){
            XSSFRow row = sheet.createRow(i);

            //序号
            PoiUtilsXlsx.createStringCell(row, 0, String.valueOf(i), this.templateCellStyleCenter);
            //采购单号
            PoiUtilsXlsx.createStringCell(row, 1, purchaseOrder.getCode(), this.templateCellStyleCenter);
            //单据状态
            PoiUtilsXlsx.createStringCell(row, 2, StringUtils.equals(purchaseOrder.getOrderStatus(), "0") ? "正常" : "作废", this.templateCellStyleCenter);
            //状态
            PoiUtilsXlsx.createStringCell(row, 3, OrderStatusEnum.keyGetValue(purchaseOrder.getStatus()), this.templateCellStyleCenter);
            //下发状态
            PoiUtilsXlsx.createStringCell(row, 4, StringUtils.equals(purchaseOrder.getDistributeStatus(), "0") ? "未下发" : "已下发", this.templateCellStyleCenter);
            //供应商
            PoiUtilsXlsx.createStringCell(row, 5, purchaseOrder.getSupplierName(), this.templateCellStyleCenter);
            //入库状态
            PoiUtilsXlsx.createStringCell(row, 6, WarehouseStatusEnum.keyGetValue(purchaseOrder.getWarehouseStatus()), this.templateCellStyleCenter);
            //采购员
            PoiUtilsXlsx.createStringCell(row, 7, purchaseOrder.getPurchaserName(), this.templateCellStyleCenter);
            //入库仓库
            PoiUtilsXlsx.createStringCell(row, 8, purchaseOrder.getWarehouseName(), this.templateCellStyleCenter);
            //采购日期
            PoiUtilsXlsx.createStringCell(row, 9, DateUtil.format(purchaseOrder.getPurchaseDate(), "yyyy-MM-dd"), this.templateCellStyleCenter);
            //总数量
            PoiUtilsXlsx.createStringCell(row, 10, purchaseOrder.getTotal().toString(), this.templateCellStyleCenter);
            //总金额
            PoiUtilsXlsx.createStringCell(row, 11, purchaseOrder.getTotalAmount().toString(), this.templateCellStyleCenter);
            //备注
            PoiUtilsXlsx.createStringCell(row, 12, purchaseOrder.getRemarks(), this.templateCellStyleCenter);

            //物料明细表 数据渲染
            List<PurchaseOrderDetail> detailList = purchaseOrder.getPurchaseOrderDetailList();
            for(PurchaseOrderDetail detail : detailList){
                XSSFRow detailRow = detailSheet.createRow(k);

                //序号
                PoiUtilsXlsx.createStringCell(detailRow, 0, String.valueOf(k), this.templateCellStyleCenter);
                //采购单号
                PoiUtilsXlsx.createStringCell(detailRow, 1, purchaseOrder.getCode(), this.templateCellStyleCenter);
                //物料编码
                PoiUtilsXlsx.createStringCell(detailRow, 2, detail.getMaterialCode(), this.templateCellStyleCenter);
                //物料名称
                PoiUtilsXlsx.createStringCell(detailRow, 3, detail.getMaterialName(), this.templateCellStyleCenter);
                //设计款号
                PoiUtilsXlsx.createStringCell(detailRow, 4, detail.getDesignStyleCode(), this.templateCellStyleCenter);
                //制版号
                PoiUtilsXlsx.createStringCell(detailRow, 5, detail.getPlateBillCode(), this.templateCellStyleCenter);
                //物料规格
                PoiUtilsXlsx.createStringCell(detailRow, 6, detail.getMaterialSpecifications(), this.templateCellStyleCenter);
                //经缩率
                PoiUtilsXlsx.createStringCell(detailRow, 7, detail.getWarpShrinkage(), this.templateCellStyleCenter);
                //纬缩率
                PoiUtilsXlsx.createStringCell(detailRow, 8, detail.getLeftShrinkage(), this.templateCellStyleCenter);
                //纸筒
                PoiUtilsXlsx.createStringCell(detailRow, 9, detail.getPaperTube(), this.templateCellStyleCenter);
                //空差
                PoiUtilsXlsx.createStringCell(detailRow, 10, detail.getSpaceDifference(), this.templateCellStyleCenter);
                //供应商色号
                PoiUtilsXlsx.createStringCell(detailRow, 11, detail.getSupplierColor(), this.templateCellStyleCenter);
                //采购单位
                PoiUtilsXlsx.createStringCell(detailRow, 12, detail.getPurchaseUnitName(), this.templateCellStyleCenter);
                //采购转计量
                PoiUtilsXlsx.createStringCell(detailRow, 13, detail.getConvertUnitRatio() == null ? "1" : detail.getConvertUnitRatio().toString(), this.templateCellStyleCenter);
                //采购数量
                PoiUtilsXlsx.createStringCell(detailRow, 14, detail.getPurchaseNum() == null ? "0" : detail.getPurchaseNum().toString(), this.templateCellStyleCenter);
                //物料颜色
                PoiUtilsXlsx.createStringCell(detailRow, 15, detail.getMaterialColor(), this.templateCellStyleCenter);
                //损耗
                PoiUtilsXlsx.createStringCell(detailRow, 16, detail.getLoss() == null ? "0" : detail.getLoss().toString(), this.templateCellStyleCenter);
                //单价
                PoiUtilsXlsx.createStringCell(detailRow, 17, detail.getPrice() == null ? "" : detail.getPrice().toString(), this.templateCellStyleCenter);
                //金额
                PoiUtilsXlsx.createStringCell(detailRow, 18, detail.getTotalAmount() == null ? "" : detail.getTotalAmount().toString(), this.templateCellStyleCenter);
                //物料交期
                PoiUtilsXlsx.createStringCell(detailRow, 19, detail.getDeliveryDate() == null ? "" : DateUtil.format(detail.getDeliveryDate(), "yyyy-MM-dd"), this.templateCellStyleCenter);
                //备注
                PoiUtilsXlsx.createStringCell(detailRow, 20, detail.getRemarks(), this.templateCellStyleCenter);

                k++;
            }
            i++;
        }
        return this.objWb;
    }
}
