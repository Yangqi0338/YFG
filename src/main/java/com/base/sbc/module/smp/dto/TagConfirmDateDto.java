package com.base.sbc.module.smp.dto;

import lombok.Data;

import java.util.Date;

/**
 * 吊牌确认日期
 */
@Data
public class TagConfirmDateDto {
    /**
     * 大货款号
     */
    private String styleNo;
    /**
     * 工艺员确认 0:未确认，1:已确认
     */
    private Integer technologistConfirm;
    /**
     * 技术确认 0:未确认，1:已确认
     */
    private Integer technicalConfirm;
    /**
     * 品控确认 0:未确认，1:已确认
     */
    private Integer qualityControlConfirm;
    /**
     * 商品吊牌价是否确认 0:未确认，1:已确认
     */
    private Integer productTagPriceConfirm;
    /**
     * 计控吊牌价是否确认 0:未确认，1:已确认
     */
    private Integer planTagPriceConfirm;
    /**
     * 是否计控成本确认 0:未确认，1:已确认
     */
    private Integer planCostConfirm;

    /**
     * 工艺员确认时间
     */
    private Date technologistConfirmDate;
    /**
     * 技术确认时间
     */
    private Date technicalConfirmDate;
    /**
     * 品控确认时间
     */
    private Date qualityControlConfirmDate;
    /**
     * 商品吊牌价确认时间
     */
    private Date productTagPriceConfirmDate;
    /**
     * 计控吊牌价确认时间
     */
    private Date planTagPriceConfirmDate;
    /**
     * 计控成本确认时间
     */
    private Date planCostConfirmDate;
}
