package com.base.sbc.module.hangtag.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author xhj
 * @Date 2023/6/26 17:53
 */
@Data
@ApiModel(value = "吊牌列表查询")
public class HangTagSearchDTO extends Page {

    /**
     * 款式
     */
    @ApiModelProperty(value = "款式")
    private String style;

    /**
     * 大货款号
     */
    @ApiModelProperty(value = "大货款号")
    private String bulkStyleNo;

    /**
     * 成分
     */
    @ApiModelProperty(value = "成分")
    private String ingredient;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String status;
    /**
     * 确认时间
     */
    @ApiModelProperty(value = "确认时间")
    private String confirmDate;
    /**
     * 款号
     */
    @ApiModelProperty(value = "款号")
    private String styleNo;
    /**
     * 款号是否模糊查询 1是、0否
     */
    @ApiModelProperty(value = "款号是否模糊查询")
    private String likeQueryFlag;
    @ApiModelProperty(value = "设计款号")
    private String designNo;
    private String companyCode;
    @ApiModelProperty(value = "查询类型：packBigGoods.标准资料包")
    private String selectType;

    private String checkType;

}
