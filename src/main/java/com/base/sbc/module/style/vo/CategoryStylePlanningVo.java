package com.base.sbc.module.style.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类描述：品类款式规划vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.vo.CategoryStylePlanningVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-29 11:55
 */
@Data
@ApiModel("品类款式规划Vo CategoryStylePlanningVo")
public class CategoryStylePlanningVo {
    @ApiModelProperty(value = "波段")
    private String bandCode;

    @ApiModelProperty(value = "波段名称")
    private String bandName;

    @ApiModelProperty(value = "品类")
    private String prodCategory;

    @ApiModelProperty(value = "企划需求数")
    private Long planRequirementSkc;

    @ApiModelProperty(value = "设计需求skc")
    private Long designRequirementSkc;

    @ApiModelProperty(value = "上市时间-南")
    private Date timeToMarketS;

    @ApiModelProperty(value = "上市时间-北中")
    private Date timeToMarketNC;

    @ApiModelProperty(value = "节假日")
    private String holiday;

    @ApiModelProperty(value = "陈列杆数")
    private Long displayPolesNumber;

    @ApiModelProperty(value = "陈列占比")
    private BigDecimal displayPolesRatio;

    // 新增字段
    /**
     * 已下稿
     */
    @ApiModelProperty(value = "已下稿")
    private Long scriptedNum;
    /**
     * 打版完成
     */
    @ApiModelProperty(value = "打版完成")
    private Long patternMakingIsCompleteNum;
    /**
     * 样衣制作
     */
    @ApiModelProperty(value = "样衣制作")
    private Long sampleMakingNum;
    /**
     * 订货本制作
     */
    @ApiModelProperty(value = "订货本制作")
    private Long orderBookProductionNum;
}
