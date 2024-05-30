package com.base.sbc.module.pricing.dto;

import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.dto.QueryFieldDto;
import com.base.sbc.module.pricing.enums.PricingQueryDimensionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 核价列表查询
 *
 * @Author xhj
 * @Date 2023/6/16 15:16
 */
@Data
@ApiModel("核价列表查询")
public class PricingSearchDTO extends QueryFieldDto {

    /**
     * 查询维度
     *
     * @see PricingQueryDimensionEnum
     */
    @NotBlank(message = "查询维度不可为空")
    private String dimension;
    /**
     * 设计款号
     */
    @ApiModelProperty(value = "设计款号", example = "123")
    private String designStyleCode;
    /**
     * 报价单号
     */
    @ApiModelProperty(value = "报价单号", example = "123")
    private String code;


    /**
     * 版本号(核价次数)
     */
    @ApiModelProperty(value = "版本号(核价次数)")
    private String version;

    /**
     * 品类
     */
    @ApiModelProperty(value = "品类")
    private String category;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")
    private String year;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String season;

    private String companyCode;
}
