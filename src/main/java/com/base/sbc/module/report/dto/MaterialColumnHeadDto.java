package com.base.sbc.module.report.dto;

import com.base.sbc.config.dto.QueryFieldDto;
import lombok.Data;

@Data
public class MaterialColumnHeadDto extends QueryFieldDto {

    /**
     * 下发状态
     */

    private String distribute;
    /**
     * 纱支规格
     */

    private String specification;
    /**
     * 克重
     */

    private String gramWeight;
    /**
     * 持续环保
     */

    private String isProtection;
    /**
     * 门幅
     */

    private String widthName;
    /**
     * 颜色
     */

    private String colorName;
    /**
     * 颜色编码
     */

    private String colorCode;
    /**
     * 供应商色号
     */

    private String supplierColorCode;
    /**
     * 供应商颜色描述
     */

    private String supplierColorSay;
    /**
     * 图片
     */

    private String imageUrl;
    /**
     * 物料编码
     */

    private String materialCode;
    /**
     * 物料类别
     */

    private String categoryName;
    /**
     * 供应物料号
     */

    private String supplierMaterialCode;
    /**
     * 状态
     */

    private String status;
    /**
     * 审核状态
     */

    private String confirmStatus;
    /**
     * 物料名称
     */

    private String materialName;
    /**
     * 品牌
     */

    private String brandName;
    /**
     * 年份
     */

    private String year;
    /**
     * 季节
     */

    private String seasonName;
    /**
     * 材料类型
     */

    private String materialCategoryName;
    /**
     * 面料成分
     */

    private String ingredient;
    /**
     * 采购模式
     */

    private String procModeName;
    /**
     * 材料
     */

    private String materialCodeName;
    /**
     * 领料方式
     */

    private String pickingMethodName;
    /**
     * 物料来源
     */

    private String materialSourceName;
    /**
     * 开发员
     */

    private String devName;
    /**
     * 采购组
     */

    private String purchaseDeptName;
    /**
     * 采购员
     */

    private String purchaseName;
    /**
     * 总库存
     */

    private String totalInventory;
    /**
     * 自由库存
     */

    private String inventoryAvailability;
    /**
     * 面料难度
     */

    private String fabricDifficultyName;
    /**
     * 备注
     */

    private String remarks;
    /**
     * 采购单位
     */

    private String purchaseUnitName;
    /**
     * 供应商名称
     */

    private String supplierName;
    /**
     * 采购转库存
     */

    private String purchaseConvertStock;
    /**
     * 面料成分说明
     */

    private String ingredientSay;
    /**
     * 面料卖点
     */

    private String fabricSalePoint;
    /**
     * 有胚周期(天)
     */

    private String embryonicCycle;
    /**
     * 无胚周期(天)
     */

    private String embryonicFreeCycle;
    /**
     * 补单生产周期(天)
     */

    private String replenishmentProductionCycle;
    /**
     * 密度
     */

    private String density;
    /**
     * 织造类型
     */

    private String weaveTypeName;
    /**
     * 胚布类型
     */

    private String embryoTypeName;
    /**
     * 面料属性分类
     */

    private String fabricPropertyTypeName;
    /**
     * 供应商物料编号
     */

    private String supplierFabricCode;
    /**
     * 辅料材质
     */

    private String auxiliaryMaterial;
    /**
     * 送检单号
     */

    private String checkBillCode;
    /**
     * 送检单位
     */

    private String checkCompanyName;
    /**
     * 质检结果
     */

    private String checkResult;
    /**
     * 质检日期
     */

    private String checkDate;
    /**
     * 有效期
     */

    private String checkValidDate;
    /**
     * 面料难度评分
     */

    private String fabricDifficultyScoreName;
    /**
     * 面料评价
     */

    private String fabricEvaluation;
    /**
     * 风险评估
     */

    private String riskDescription;
    /**
     * 创建人
     */

    private String createName;
    /**
     * 厂家成分
     */

    private String factoryComposition;
    /**
     * 创建时间
     */

    private String createDate;
    /**
     * 修改人
     */

    private String updateName;
    /**
     * 修改时间
     */

    private String updateDate;
    /**
     * 质量等级
     */

    private String qualityLevelName;
    /**
     * 操作
     */

    private String operation;
    /**
     * 采购报价
     */

    private String quotationPrice;
    /**
     * 库存单位
     */

    private String stockUnitName;
    /**
     * 订货周期
     */

    private String orderDay;


    /** 类别（物料分类id） */
    private String categoryId;

    /**
     * 物料编码多个查询
     */
    private String materialCodesHead;

    private String detail;

}
