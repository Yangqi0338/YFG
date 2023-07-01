package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 类描述： 样衣分页查询
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.dto.SamplePageDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-09 18:03
 */
@Data
@ApiModel("样衣设计分页查询 SampleDto")
public class SampleDesignPageDto extends Page {

    /**
     * 1我下发的
     */
    public static final String userType1 = "1";
    /**
     * 2我创建的
     */
    public static final String userType2 = "2";
    /**
     * 3我负责的
     */
    public static final String userType3 = "3";
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

    @ApiModelProperty(value = "品类id路径:(大类/品类/中类/小类)"  )
    private String categoryIds;
    @ApiModelProperty(value = "设计师id",example = "680014765321355265" )
    private String designerId;
    /*小类*/
    private String prodCategory3rd;
//    产品季节id
    private String planningSeasonId;
    /*设计款号*/
    private String designNo;
    /*生产模式*/
    private String devtType;
}
