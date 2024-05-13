package com.base.sbc.module.patternmaking.dto;

import com.base.sbc.config.dto.QueryFieldDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：技术中心看板-任务列表搜索dto
 * @address com.base.sbc.module.patternmaking.dto.TechnologyCenterTaskSearchDto
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-31 13:57
 * @version 1.0
 */
@Data
@ApiModel("技术中心看板-任务列表搜索dto TechnologyCenterTaskSearchDto ")
public class TechnologyCenterTaskSearchDto extends QueryFieldDto {


    @ApiModelProperty(value = "年份", example = "2022")
    private String year;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节", example = "S")
    private String season;
    /**
     * 月份
     */
    @ApiModelProperty(value = "月份", example = "1")
    private String month;

    /**
     * 版师id
     */
    @ApiModelProperty(value = "版师id", example = "1223333122223333333")
    private String patternDesignId;
    @ApiModelProperty(value = "产品季id", example = "1223333122223333333")
    private String planningSeasonId;

    private String designerIds;

    private String prodCategory;

    private String designSendDate;

    private String prmSendDate;

    private String sampleType;

    private String urgencyName;

    @ApiModelProperty(value = "是否滞留款查询")
    private String isRetentionQuery;

    @ApiModelProperty(value = "版房")
    private String patternRoom;

    @ApiModelProperty(value = "打样设计师")
    private String patternDesignerName;

    @ApiModelProperty(value = "确认收到样衣时间")
    private String receiveSampleDate;

    @ApiModelProperty(value = "波段名称")
    private String bandName;

    @ApiModelProperty(value = "图片标记"  )
    private String imgFlag;

}
