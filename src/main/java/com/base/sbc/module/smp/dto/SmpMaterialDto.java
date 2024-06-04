package com.base.sbc.module.smp.dto;

import com.base.sbc.module.smp.base.SmpBaseDto;
import com.base.sbc.module.smp.entity.SmpColor;
import com.base.sbc.module.smp.entity.SmpModuleSize;
import com.base.sbc.module.smp.entity.SmpQuot;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/5/9 10:37:13
 * @mail 247967116@qq.com
 */
@Data
public class  SmpMaterialDto extends SmpBaseDto {
    /** 材料编号 */
    private String materialCode;
    /** 材料名称 */
    private String materialName;
    /** 材料单位（采购单位） */
    private String materialUnit;
    /** 库存单位（新增） */
    private String stockUnit;
    /** 三级分类 */
    private String thirdLevelCategory;
    /** 物料来源 */
    private String materialSource;
    /** 二级分类 */
    private String secondLevelCategory;
    /** 开发年份 */
    private String seasonYear;
    /** 开发季节 */
    private String seasonQuarter;
    /** 开发季节ID */
    private String seasonQuarterId;
    /** 开发品牌 */
    private String seasonBrand;
    /** 开发品牌ID */
    private String seasonBrandId;
    /** 公斤米数 */
    private BigDecimal kilogramsAndMeters;
    /** 开发员 */
    private String developer;
    /** 采购员 */
    private String buyer;
    /** 采购组 */
    private String buyerTeam;
    /** 经缩(%) */
    private BigDecimal longitudinalShrinkage;
    /** 纬缩(%) */
    private BigDecimal weftShrinkage;
    /** 克重(g/m2) */
    private String weight;
    /** 成分 */
    private String composition;
    /** JIT标识 */
    private Boolean JIT;
    /** 物料类型id */
    private String productTypeId;
    /** 物料类型 */
    private String productType;
    /** 采购模式 */
    private String procurementMode;
    /** 默认超收比例 */
    private BigDecimal tolerance;
    /** 厂家成分 */
    private String supplierComposition;
    /** 领料方式 */
    private String pickingMethod;
    /** 颜色集合 */
    private List<SmpColor> colorList;
    /** 材料尺码集合 */
    private List<SmpModuleSize> moduleSizeList;
    /** 报价集合 */
    private List<SmpQuot> quotList;
    /**图片地址集合*/
    private List<String> imgList;

    //2023-11-23 新加字段
    /** 供应商色号 */
    @ApiModelProperty(value = "供应商色号")
    private String supplierColorNo;
    /** 供应商颜色描述 */
    @ApiModelProperty(value = "供应商颜色描述")
    private String supplierColorSay;
    /** 面料成分说明 */
    @ApiModelProperty(value = "面料成分说明")
    private String ingredientSay;
    /** 面料卖点 */
    @ApiModelProperty(value = "面料卖点")
    private String fabricSalePoint;
    /** 有胚周期(天) */
    @ApiModelProperty(value = "有胚周期(天)")
    private BigDecimal embryonicCycle;
    /** 无胚周期(天) */
    @ApiModelProperty(value = "无胚周期(天)")
    private BigDecimal embryonicFreeCycle;
    /** 补单生产周期 */
    @ApiModelProperty(value = "补单生产周期")
    private BigDecimal replenishmentProductionCycle;
    /** 纱支规格 */
    @ApiModelProperty(value = "纱支规格")
    private String specification;
    /** 密度 */
    @ApiModelProperty(value = "密度")
    private String density;
    /**持续环保（1是 0否 空白） */
    @ApiModelProperty(value = "持续环保（1是 0否 空白）")
    private String isProtection;
    /** 门幅 */
    @ApiModelProperty(value = "门幅")
    private String translate;
    /** 织造类型 */
    @ApiModelProperty(value = "织造类型")
    private String weaveType;
    /** 织造类型名称 */
    @ApiModelProperty(value = "织造类型名称")
    private String weaveTypeName;
    /** 胚布类型 */
    @ApiModelProperty(value = "胚布类型")
    private String embryoType;
    /** 胚布类型名称 */
    @ApiModelProperty(value = "胚布类型名称")
    private String embryoTypeName;
    /** 面料属性分类 */
    @ApiModelProperty(value = "面料属性分类")
    private String fabricPropertyType;
    /** 面料属性分类名称 */
    @ApiModelProperty(value = "面料属性分类名称")
    private String fabricPropertyTypeName;
    /** 辅料材质 */
    @ApiModelProperty(value = "辅料材质")
    private String auxiliaryMaterial;
    @ApiModelProperty(value = "送检单号")
    private String checkBillCode;
    /** 送检单位 */
    @ApiModelProperty(value = "送检单位")
    private String checkCompany;
    /** 送检单位名称 */
    @ApiModelProperty(value = "送检单位名称")
    private String checkCompanyName;
    /** 质检结果 */
    @ApiModelProperty(value = "质检结果")
    private String checkResult;
    /** 质检日期 */
    @ApiModelProperty(value = "质检日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date checkDate;
    /** 有效期 */
    @ApiModelProperty(value = "有效期")
    private Integer checkValidDate;
    /** 质检项目 */
    @ApiModelProperty(value = "质检项目")
    private String checkItems;
    /** 质检制单人ID */
    @ApiModelProperty(value = "质检制单人ID")
    private String checkOrderUserId;
    /** 质检制单人 */
    @ApiModelProperty(value = "质检制单人")
    private String checkOrderUserName;
    /** 质检文件路径 */
    @ApiModelProperty(value = "质检文件路径")
    private String checkFileUrl;
    /** 面料难度评分 */
    @ApiModelProperty(value = "面料难度评分")
    private String fabricDifficultyScore;
    /** 面料难度评分名称 */
    @ApiModelProperty(value = "面料难度评分名称")
    private String fabricDifficultyScoreName;
    /** 面料评价 */
    @ApiModelProperty(value = "面料评价")
    private String fabricEvaluation;
    /** 风险评估 */
    @ApiModelProperty(value = "风险评估")
    private String riskDescription;
}
