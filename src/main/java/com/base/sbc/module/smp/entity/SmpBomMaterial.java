package com.base.sbc.module.smp.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 卞康
 * @date 2023/5/10 11:52:25
 * @mail 247967116@qq.com
 * bom材料清单
 */
@Data
public class SmpBomMaterial {
    /**材料编号*/
    private String materialCode;
    /**材料色号*/
    private String materialColorNumber;
    /**材料单位*/
    private String materialUnit;
    /**bom行项目id*/
    private String bomLineItemId;
    /**部位*/
    private String position;
    /**损耗率*/
    private BigDecimal lossRate;
    /**是否启用*/
    private Boolean active;
    /**是否下发（新增）*/
    private Boolean distribute;
    /**搭配名称*/
    private Boolean matchingName;
}
