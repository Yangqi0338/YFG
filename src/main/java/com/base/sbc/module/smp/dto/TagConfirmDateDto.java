package com.base.sbc.module.smp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
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
     * 翻译确认 0:未确认，1:已确认
     */
    private Integer translateControlConfirm;
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
     * 工时部工价确认 0:未确认，1:已确认
     */
    private Integer workingHourConfirm;

    /**
     * 工艺员确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date technologistConfirmDate;
    /**
     * 技术确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date technicalConfirmDate;
    /**
     * 品控确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date qualityControlConfirmDate;
    /**
     * 商品吊牌价确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date productTagPriceConfirmDate;
    /**
     * 计控吊牌价确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planTagPriceConfirmDate;
    /**
     * 计控成本确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planCostConfirmDate;

    /**
     * 工艺部接收日期-正确样
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date technicsDate;

    /**
     * 计控接明细单日期-明细单
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planControlDate;
    /**
     * 吊牌翻译确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date translateConfirmDate;
    /**
     * 工时部工价确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date workingHourConfirmDate;

    /**
     * 枚举：正确样：correct_sample
     */
    private String type;
}
