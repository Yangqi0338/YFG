package com.base.sbc.module.planning.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：工艺信息查询 dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.dto.TechnologySearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-09 15:43
 */
@Data
@ApiModel("款式设计维度信息查询条件dto   DimensionLabelsSearchDto")
public class DimensionLabelsSearchDto {
    private String names;

    public DimensionLabelsSearchDto() {
    }

    public DimensionLabelsSearchDto(String foreignId) {
        this.id = this.foreignId;
        this.foreignId = foreignId;
    }

    @ApiModelProperty(value = "款式设计id", example = "0")
    private String id;

    @ApiModelProperty(value = "款式设计id", example = "0")
    private String foreignId;

    @ApiModelProperty(value = "坑位信息id", example = "0")
    private String planningCategoryItemId;
    @ApiModelProperty(value = "产品季id", example = "0")
    @NotBlank(message = "产品季id不能为空")
    private String planningSeasonId;

    @ApiModelProperty(value = "渠道", example = "0")
    private String planningChannelId;

    @ApiModelProperty(value = "渠道", example = "0")
    private String channel;
    /**
     * 大类code
     */
    @ApiModelProperty(value = "大类code")
    private String prodCategory1st;
    /**
     * 品类code
     */
    @ApiModelProperty(value = "品类code")
    private String prodCategory;
    /**
     * 中类code
     */
    @ApiModelProperty(value = "中类code")
    private String prodCategory2nd;
    /**
     * 小类code
     */
    @ApiModelProperty(value = "小类code")
    private String prodCategory3rd;


    @ApiModelProperty(value = "波段")
    private String  brand;


    @ApiModelProperty(value = "季节")
    private String season;

    @ApiModelProperty(value = "季节")
    private String dataGroup;


    @ApiModelProperty(value = "品类标识 0品类 1中类")
    private String categoryFlag;

    /**
     * 配置页面值
     */
    @ApiModelProperty(value = "配置页面")
    private String configPageFlag;

    /**
     * 引用的产品季id
     */
    private String refPlanningSeasonId;

    /**
     * 围度系数标识
     */
    @ApiModelProperty(value = "围度系数标识")
    private String coefficientFlag;

    @ApiModelProperty(value = "显示标识")
    private String showConfig;


    @ApiModelProperty(value = "系数模板id")
    private String  coefficientTemplateId;

    @ApiModelProperty(value = "款式配色Id")
    private String styleColorId;

}
