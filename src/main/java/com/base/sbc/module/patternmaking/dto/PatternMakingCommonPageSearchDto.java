package com.base.sbc.module.patternmaking.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：打版-样衣进度列表搜索条件Dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.dto.PatternMakingCommonPageSearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-12 11:26
 */
@Data
@ApiModel("打版管理通用分页筛选类Dto PatternMakingCommonPageSearchDto ")
public class PatternMakingCommonPageSearchDto extends Page {
    @ApiModelProperty(value = "关键字筛选", example = "1")
    private String search;
    @ApiModelProperty(value = "年份", example = "2022")
    private String year;

    @ApiModelProperty(value = "季节", example = "S")
    private String season;

    @ApiModelProperty(value = "月份", example = "1")
    private String month;
    @ApiModelProperty(value = "波段", example = "1")
    private String bandCode;

    @ApiModelProperty(value = "设计师id", example = "1223333122223333333")
    private String designerId;
    @ApiModelProperty(value = "设计师ids", example = "1223333122223333333")
    private String designerIds;

    @ApiModelProperty(value = "版师id", example = "1223333122223333333")
    private String patternDesignId;

    @ApiModelProperty(value = "状态", example = "打版中")
    private String status;
    @ApiModelProperty(value = "产品季id", example = "1223333122223333333")
    private String planningSeasonId;

    @ApiModelProperty(value = "导出标记")
    private String deriveflag;


    private String patternNo;

    private String sampleType;

    private String technicianKittingDate;

    private String bfzgxfsj;

    private String bsjssj;

    private String zysj;

    private String cjsj;

    private String cfsj;

    private String yywcsj;

    private String sampleBarCode;
    //模板部件
    private String patternParts;

    @ApiModelProperty(value = "创建指令时间")
    private String pmCreateDate;

    @ApiModelProperty(value = "打样工艺员")
    private String patternTechnicianName;

    /*
    * 0 启用
    * 样衣没中断
    * 打板没中断
    * 1样衣中断
    * 2打板中断
    * */
    @ApiModelProperty(value = "状态")
    private String  pmStatus;
}
