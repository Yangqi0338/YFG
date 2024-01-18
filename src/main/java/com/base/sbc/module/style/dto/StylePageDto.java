package com.base.sbc.module.style.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 类描述： 款式设计分页查询
 *
 * @author lixianglin
 * @version 1.0
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-09 18:03
 */
@Data
@ApiModel("款式设计分页查询 StylePageDto")
public class StylePageDto extends Page {

    /**
     * 所有
     */
    public static final String USER_TYPE_0 = "0";
    /**
     * 1我下发的
     */
    public static final String USER_TYPE_1 = "1";
    /**
     * 2我创建的
     */
    public static final String USER_TYPE_2 = "2";
    /**
     * 3我负责的
     */
    public static final String USER_TYPE_3 = "3";
    /**
     * 年份
     */
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
     * 是否是款式 0否,1:是
     */
    @ApiModelProperty(value = "是否是款式", example = "0")
    private String isTrim;

    @ApiModelProperty(value = "状态:0未开款，1已开款，2已下发打板(完成,引用历史款)", example = "0")
    private String status;

    @ApiModelProperty(value = "是否齐套:0未齐套，1已齐套", example = "0")
    private String kitting;

    @ApiModelProperty(value = "类型:1我下发的,2我创建的,3我负责的", example = "1")
    private String userType;


    @ApiModelProperty(value = "设计师id", example = "680014765321355265")
    private String designerId;
    @ApiModelProperty(value = "小类")
    private String prodCategory3rd;
    @ApiModelProperty(value = "产品季节id")
    private String planningSeasonId;
    @ApiModelProperty(value = "设计款号")
    private String designNo;

    @ApiModelProperty(value = "生产模式")
    private String devtType;

    @ApiModelProperty(value = "大类code")
    private String prodCategory1st;
    @ApiModelProperty(value = "品类code")
    private String prodCategory;

    @ApiModelProperty(value = "波段code")
    private String bandCode;
    @ApiModelProperty(value = "业务对象编码(用于数据权限)")
    private String businessType;
    @ApiModelProperty(value = "设计师编码")
    private String  designerIds;
    @ApiModelProperty(value = "是否下单")
    private String orderStatus;

    @ApiModelProperty(value = "1停用0启用2所有")
    private String enableStatus;
}
