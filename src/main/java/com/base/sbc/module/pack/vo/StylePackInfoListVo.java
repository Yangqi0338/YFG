package com.base.sbc.module.pack.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 设计BOM管理页面列表Vo
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.vo.StylePackInfoListVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-06 17:30
 */
@Data
@ApiModel("设计BOM管理页面列表Vo StylePackInfoListVo")
public class StylePackInfoListVo {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "款式id")
    private String styleId;
    /**
     * 设计款号
     */
    @ApiModelProperty(value = "设计款号")
    private String designNo;

    /**
     * 款式名称
     */
    @ApiModelProperty(value = "款式名称")
    private String styleName;


    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;

    @ApiModelProperty(value = "生产模式")
    private String devtType;
    @ApiModelProperty(value = "生产模式名称")
    private String devtTypeName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "创建人")
    private String createName;


    @ApiModelProperty(value = "款式图")
    private String stylePic;


    /**
     * 是否是迁移历史数据
     */
    @ApiModelProperty(value = "是否是迁移历史数据")
    private String historicalData;
    @ApiModelProperty(value = "BOM信息")
    private List<PackInfoListVo> packInfoList;


    @ApiModelProperty(value = "款式")
    public String getStyle() {
        return Optional.ofNullable(designNo).orElse("") + Optional.ofNullable(styleName).orElse("");
    }

}
