package com.base.sbc.module.pack.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：资料包-基础信息-分页查询dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.PackInfoSearchPageDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-06 17:17
 */
@Data
@ApiModel("资料包-基础信息-分页查询dto PackInfoSearchPageDto")
public class PackInfoSearchPageDto extends Page {


    @ApiModelProperty(value = "款式id")
    private String styleId;

    @ApiModelProperty(value = "生产模式")
    private String devtType;

    @ApiModelProperty(value = "大类id")
    private String prodCategory1st;

    @ApiModelProperty(value = "品类id")
    private String prodCategory;

    @ApiModelProperty(value = "中类id")
    private String prodCategory2nd;

    @ApiModelProperty(value = "小类")
    private String prodCategory3rd;

    @ApiModelProperty(value = "产品季id")
    private String planningSeasonId;

    @ApiModelProperty(value = "bom状态:(0样品,1大货)")
    private String bomStatus;

    @ApiModelProperty(value = "设计款号")
    private String designNo;

    @ApiModelProperty(value = "大货款号")
    private String styleNo;

    @ApiModelProperty(value = "图片导出标记")
    private String imgFlag ;



}
