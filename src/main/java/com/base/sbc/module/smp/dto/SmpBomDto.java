package com.base.sbc.module.smp.dto;

import com.base.sbc.module.smp.base.SmpBaseDto;
import com.base.sbc.module.smp.entity.SmpSizeQty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/5/10 11:08:38
 * @mail 247967116@qq.com
 */
@Data
public class SmpBomDto extends SmpBaseDto {
    /**BOM编号*/
    private String bomCode;
    /**大货款号颜色名称*/
    private String pColorName;
    /**大货款号色号*/
    private String pColorCode;
    /**大货款号*/
    private String bulkNumber;
    /**材料颜色名称*/
    private String colorName;
    /**材料色号*/
    private String colorCode;
    /**bom阶段*/
    private String bomStage;
    /**材料编号*/
    private String materialCode;
    /** 材料名称 */
    private String materialName;
    /** 材料单位 */
    private String materialUnit;
    /** 使用部位 */
    private String placeOfUse;
    /** 损耗率 */
    private BigDecimal lossRate;
    /**供应商物料编号*/
    private String supplierMaterialCode;
    /**报价供应商编号*/
    private String quotationSupplierCode;
    /**搭配*/
    private String collocation;
    /**bom行项目id*/
    private String bomLineItemId;
    /**是否主材料*/
    private Boolean mainMaterial;
    /**单件用量*/
    private List<SmpSizeQty> sizeQtyList;
}
