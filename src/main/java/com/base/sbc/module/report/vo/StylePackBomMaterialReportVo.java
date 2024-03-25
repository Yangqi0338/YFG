package com.base.sbc.module.report.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StylePackBomMaterialReportVo {
    /**
     * 图片
     */

    private String styleColorPic;
    /**
     * 大货款号
     */

    private String styleNo;
    /**
     * 配色
     */

    private String colorName;
    /**
     * 大类
     */

    private String prodCategory1stName;
    /**
     * 品类
     */

    private String prodCategoryName;
    /**
     * 生产类型
     */

    private String devtTypeName;
    /**
     * 款式
     */

    private String styleName;
    /**
     * 款式类型
     */

    private String styleTypeName;
    /**
     * BOM阶段
     */

    private String bomPhase;
    /**
     * 材料图片
     */

    private String imageUrl;
    /**
     * 编码
     */

    private String materialCode;
    /**
     * 供应商报价供应商物料号
     */

    private String supplierPrice;
    /**
     * 供应商报价供应商物料号
     */

    private String materialName;
    /**
     * 供应商报价供应商物料号
     */

    private String supplierMaterialCode;
    /**克重(g/m2)*/
    ;
    private String gramWeight;
    /**
     * 材料成份
     */

    private String materialIngredient;
    /**
     * 材料成份备注
     */

    private String remarks;
    /**
     * 材料季节
     */

    private String yearName;
    /**
     * 材料年份
     */
    private String seasonName;
    /**
     * 材料品牌
     */
    private String brandName;
    /**
     * 类型
     */
    private String category1Name;
    /**
     * 材料二级分类
     */
    private String category2Name;
    /**
     * 材料三级分类
     */
    private String category3Name;
    /**
     * 供应商报价供应商
     */
    private String supplierName;
    /**
     * 厂家有效门幅/规格(通用)
     */
    private String translate;
    /**
     * 单价
     */
    private String bulkPrice;
    /**
     * 单件用量
     */
    private String bulkUnitUse;
    /**
     * 单位
     */
    private String stockUnitName;
    /**
     * 损耗%
     */
    private String planningLoossRate;
    /**
     * 成本
     */
    private String cost;
    /**
     * 搭配
     */
    private String collocationName;
    /**
     * 材料启用
     */
    private String unusableFlag;
    /**
     * 材料
     */
    private String materialCodeName;
    /**
     * 可用
     */
    private String status;
    /**
     * 设计师
     */
    private String designer;
    /**
     * 工艺员
     */
    private String technicianName;
    /**
     * 版师
     */
    private String patternDesignName;
    /**
     * 成份信息
     */
    private String tagIngredient;
    /**
     * 安全标题
     */
    private String saftyTitle;
    /**
     * 安全类别
     */
    private String saftyType;
    /**
     * 后技术工艺师
     */
    private String technologistName;
    /**
     * 品名
     */
    private String productName;
    /**
     * 温馨提示
     */
    private String warmTips;
    /**
     * 洗唛材质备注
     */
    private String washingMaterialRemarksName;
    /**
     * 执行标准
     */
    private String executeStandard;
    /**
     * 质量等级
     */
    private String qualityGrade;
    /**
     * 贮藏要求
     */
    private String storageDemandName;
    /**
     * 计控成本价确认
     */
    private String controlConfirm;
    /**
     * 商品吊牌价确认
     */
    private String productHangtagConfirm;
    /**
     * 计控吊牌价确认
     */
    private String controlHangtagConfirm;
    /**
     * 大货工艺员确认
     */
    private String designCheckConfirm;
    /**
     * 后技术确认
     */
    private String techCheckConfirm;
    /**
     * 品控部确认
     */
    private String qcCheckConfirm;
    /**
     * 列头筛选数量
     */
    private Integer groupCount;

}
