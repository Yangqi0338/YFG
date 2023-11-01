package com.base.sbc.module.purchase.excel;

import cn.hutool.core.date.DateUtil;
import com.base.sbc.config.enums.OrderStatusEnum;
import com.base.sbc.config.utils.PoiUtilsXlsx;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.purchase.entity.OutboundOrder;
import com.base.sbc.module.purchase.entity.OutboundOrderDetail;
import com.base.sbc.module.purchase.entity.WarehousingOrder;
import com.base.sbc.module.purchase.entity.WarehousingOrderDetail;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

public class WarehousingOrderExcel {
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
        ClassPathResource resource = new ClassPathResource("excelTemp/warehousingOrderTemp.xlsx");
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

    public XSSFWorkbook createWorkBook(List<WarehousingOrder> warehousingOrderList) throws IOException {
        init();
        XSSFSheet sheet = this.objWb.getSheetAt(0);
        XSSFSheet detailSheet = this.objWb.getSheetAt(1);
        initTemplateStyle(sheet);

        int i = 1;
        int k = 1;
        for(WarehousingOrder warehousingOrder : warehousingOrderList){
            XSSFRow row = sheet.createRow(i);

            //序号
            PoiUtilsXlsx.createStringCell(row, 0, String.valueOf(i), this.templateCellStyleCenter);
            //入库单号
            PoiUtilsXlsx.createStringCell(row, 1, warehousingOrder.getCode(), this.templateCellStyleCenter);
            //单据状态
            PoiUtilsXlsx.createStringCell(row, 2, StringUtils.equals(warehousingOrder.getOrderStatus(), "0") ? "正常" : "作废", this.templateCellStyleCenter);
            //状态
            PoiUtilsXlsx.createStringCell(row, 3, OrderStatusEnum.keyGetValue(warehousingOrder.getStatus()), this.templateCellStyleCenter);
            //单据类型
            PoiUtilsXlsx.createStringCell(row, 4, StringUtils.equals(warehousingOrder.getOrderType(), "0") ? "采购单入库" : "手工", this.templateCellStyleCenter);
            //供应商/工厂
            PoiUtilsXlsx.createStringCell(row, 5, warehousingOrder.getSupplierName(), this.templateCellStyleCenter);
            //所入仓库
            PoiUtilsXlsx.createStringCell(row, 6, warehousingOrder.getWarehouseName(), this.templateCellStyleCenter);
            //送货数量
            PoiUtilsXlsx.createStringCell(row, 7, warehousingOrder.getDeliveryQuantity().stripTrailingZeros().toPlainString(), this.templateCellStyleCenter);
            //经办人
            PoiUtilsXlsx.createStringCell(row, 8, warehousingOrder.getAgentName(), this.templateCellStyleCenter);
            //入库时间
            PoiUtilsXlsx.createStringCell(row, 9, DateUtil.format(warehousingOrder.getWarehousingDate(), "yyyy-MM-dd HH:mm:ss"), this.templateCellStyleCenter);
            //驳回理由
            PoiUtilsXlsx.createStringCell(row, 10, warehousingOrder.getRejectReason(), this.templateCellStyleCenter);
            //审核人
            PoiUtilsXlsx.createStringCell(row, 11, warehousingOrder.getReviewerName(), this.templateCellStyleCenter);
            //审核人
            PoiUtilsXlsx.createStringCell(row, 12, DateUtil.format(warehousingOrder.getReviewDate(), "yyyy-MM-dd HH:mm:ss"), this.templateCellStyleCenter);
            //备注
            PoiUtilsXlsx.createStringCell(row, 13, warehousingOrder.getRemarks(), this.templateCellStyleCenter);

            //物料明细表 数据渲染
            List<WarehousingOrderDetail> detailList = warehousingOrder.getOrderDetailList();
            for(WarehousingOrderDetail detail : detailList){
                XSSFRow detailRow = detailSheet.createRow(k);

                //序号
                PoiUtilsXlsx.createStringCell(detailRow, 0, String.valueOf(k), this.templateCellStyleCenter);
                //入库单号
                PoiUtilsXlsx.createStringCell(detailRow, 1, warehousingOrder.getCode(), this.templateCellStyleCenter);
                //物料编码
                PoiUtilsXlsx.createStringCell(detailRow, 2, detail.getMaterialCode(), this.templateCellStyleCenter);
                //物料名称
                PoiUtilsXlsx.createStringCell(detailRow, 3, detail.getMaterialName(), this.templateCellStyleCenter);
                //款号
                PoiUtilsXlsx.createStringCell(detailRow, 4, detail.getDesignStyleCode(), this.templateCellStyleCenter);
                //制版号
                PoiUtilsXlsx.createStringCell(detailRow, 5, detail.getPlateBillCode(), this.templateCellStyleCenter);
                //物料规格
                PoiUtilsXlsx.createStringCell(detailRow, 6, detail.getMaterialSpecifications(), this.templateCellStyleCenter);
                //物料颜色
                PoiUtilsXlsx.createStringCell(detailRow, 7, detail.getMaterialColor(), this.templateCellStyleCenter);
                //采购预计数量
                PoiUtilsXlsx.createStringCell(detailRow, 8, detail.getExpectedPurchaseNum() == null ? "0" : detail.getExpectedPurchaseNum().stripTrailingZeros().toPlainString(), this.templateCellStyleCenter);
                //采购单价
                PoiUtilsXlsx.createStringCell(detailRow, 9, detail.getPrice() == null ? "0" : detail.getPrice().stripTrailingZeros().toPlainString() , this.templateCellStyleCenter);
                //采购送货数量
                PoiUtilsXlsx.createStringCell(detailRow, 10, detail.getDeliveryQuantity() == null ? "0" : detail.getDeliveryQuantity().stripTrailingZeros().toPlainString(), this.templateCellStyleCenter);
                //采购收货数量
                PoiUtilsXlsx.createStringCell(detailRow, 11, detail.getReceivedQuantity() == null ? "0" : detail.getReceivedQuantity().stripTrailingZeros().toPlainString(), this.templateCellStyleCenter);
                //库存单位
                PoiUtilsXlsx.createStringCell(detailRow, 12, detail.getStockUnitName(), this.templateCellStyleCenter);
                //入库数量
                PoiUtilsXlsx.createStringCell(detailRow, 13, detail.getWarehouseNum() == null ? "0" : detail.getWarehouseNum().stripTrailingZeros().toPlainString(), this.templateCellStyleCenter);
                //金额
                PoiUtilsXlsx.createStringCell(detailRow, 14, detail.getActualAmount() == null ? "0" : detail.getActualAmount().stripTrailingZeros().toPlainString(), this.templateCellStyleCenter);
                //供应商名称
                PoiUtilsXlsx.createStringCell(detailRow, 15, detail.getSupplierName(), this.templateCellStyleCenter);
                //供应商色号
                PoiUtilsXlsx.createStringCell(detailRow, 16, detail.getSupplierColor(), this.templateCellStyleCenter);
                //采购单位
                PoiUtilsXlsx.createStringCell(detailRow, 17, detail.getPurchaseUnitName(), this.templateCellStyleCenter);
                //库位
                PoiUtilsXlsx.createStringCell(detailRow, 18, detail.getPosition(), this.templateCellStyleCenter);
                k++;
            }
            i++;
        }
        return this.objWb;
    }
}
