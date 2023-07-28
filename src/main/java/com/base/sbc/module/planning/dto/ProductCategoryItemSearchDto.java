package com.base.sbc.module.planning.dto;


import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：产品季总览-波段企划查询
 * @address com.base.sbc.module.planning.dto.ProductSeasonBandSearchDto
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-20 14:52
 * @version 1.0
 */
@Data
@ApiModel("产品季总览-坑位信息查询 ProductCategoryItemSearchDto")
public class ProductCategoryItemSearchDto extends Page {

    @ApiModelProperty(value = "产品季id", example = "122222")
    private String planningSeasonId;

    @ApiModelProperty(value = "月份", example = "01")
    private String month;
    @ApiModelProperty(value = "波段", example = "1A")
    private String bandCode;

    @ApiModelProperty(value = "波段企划id", required = false, example = "122222")
    private String planningBandId;

    @ApiModelProperty(value = "渠道id")
    private String planningChannelId;


    @ApiModelProperty(value = "设计师ids", required = false, example = "['1233']")
    private List<String> designerIds;

    @ApiModelProperty(value = "任务等级", required = false, example = "['1']")
    private List<String> taskLevels;

    @ApiModelProperty(value = "状态", required = false, example = "['1']")
    private List<String> statusList;

    @ApiModelProperty(value = "大类code")
    private String prodCategory1st;
    /**
     * 品类id
     */
    @ApiModelProperty(value = "品类code")
    private String prodCategory;
    @ApiModelProperty(value = "款式状态")
    private String sdStatus;
    @ApiModelProperty(value = "特别需求:(1是,0否)")
    private String specialNeedsFlag;

}
