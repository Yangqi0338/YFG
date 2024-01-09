package com.base.sbc.module.purchaseorder.dto;

import com.base.sbc.config.common.base.Page;
import com.base.sbc.module.smp.base.SmpBaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class OrderBasisDto extends Page {
    private String id;

    /** 序号 */
    private String serialNumber;
    /** 订货本名称 */
    private String purchaseOrderName;
    /** 投产日期 */
    private Date commissioningDate;
    /** 研发类型 */
    private String researchType;
    /** 颜色id */
    private String colourLibraryId;
    /** 产品季节id */
    private String seasonId;
    /** 产品季节名称 */
    private String seasonName;
    /** 供应商id */
    private String smpSupplierId;
    /** 款式配色id */
    private String styleColorId;
    /**
     * 大货款号
     */
    private String styleNo;
    @ApiModelProperty(value = "品类")
    private String prodCategory;

    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;
    /** 波段名称 */
    @ApiModelProperty(value = "波段名称"  )
    private String bandName;
    /** 波段编码 */
    @ApiModelProperty(value = "波段编码"  )
    private String bandCode;
    /** 状态 */
    private String status;
    /** 系数 */
    private String coefficient;
    /** 4倍价 */
    private String multiplePrice;
    /** 目标入仓日期 */
    private String targetTime;
    /** 生产紧急程度 */
    private String productionUrgency;
    /** 备注 */
    private String remark;
    /** 后道 */
    private String honest;
    /** 其他 */
    private String other;
    /** 单件面料用量/米 */
    private String unitFabricDosage;
    /** 单件用量/里*/
    private String unitDosage;
    /** 面料状态 */
    private String fabricState;
    /** 是否延续 */
    private String continuationPoint;
    /** 公司面料编号 */
    private String companyFabricCode;
    /** 供应商名称 */
    private String supplierName;
    /** 供应商编号 */
    private String supplierNumber;
    /** 供应商色号 */
    private String supplierColour;
    /** 面料类型 */
    private String fabricType;
    /** 含税单价/米 */
    private String unitPrice;
    /** 货期 */
    private String deliveryTime;
    /** 库存面料米数 */
    private String inventoryFabricMeter;
    /** 库存可做件数 */
    private String inventoryDoableNumber;
    /** 合计 */
    private String total;
    /** 面料备注 */
    private String fabricRemarks;
    /** 面料吊牌 */
    private String fabricDrop;
}
