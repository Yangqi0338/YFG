package com.base.sbc.module.sample.dto;

import com.base.sbc.config.dto.QueryFieldDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.dto.PreProductionSampleTaskSearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-18 20:07
 */
@Data
@ApiModel("产前样-任务筛选 PreProductionSampleTaskSearchDto")
public class PreProductionSampleTaskSearchDto extends QueryFieldDto {

    @ApiModelProperty(value = "关键字筛选", example = "1")
    private String search;
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
    @ApiModelProperty(value = "节点")
    private String node;

    @ApiModelProperty(value = "流程完成状态:(0未完成,1已完成)")
    private String finishFlag;

    private String prodCategory;

    /*是否导出图片*/
    private String imgFlag;

    /*导出标记*/
    private String excelFlag;

    /**
     * 车缝工名称
     */
    @ApiModelProperty(value = "车缝工名称")
    private String stitcher;

    private String cfkssj;//	车缝开始时间
    private String cfwcsj;//	车缝完成时间

    @ApiModelProperty(value = "生产模式")
    private String devtType;

    private String barCodeStatus;

    private String patternRoom;
}
