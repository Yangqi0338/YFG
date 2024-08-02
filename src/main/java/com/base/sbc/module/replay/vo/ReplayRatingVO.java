/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.vo;

import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.module.replay.entity.ReplayRating;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：基础资料-复盘评分Vo 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.vo.ReplayRatingVo
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("基础资料-复盘评分 ReplayRatingVo")
public class ReplayRatingVO extends ReplayRating {
    /** 品牌 */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /** 品牌名 */
    @ApiModelProperty(value = "品牌名")
    private String brandName;

    /** 配色Id */
    @ApiModelProperty(value = "配色Id")
    private String styleColorId;

    /** 款号 */
    @ApiModelProperty(value = "款号")
    private String designNo;

    /** 大货款号 */
    @ApiModelProperty(value = "大货款号")
    private String bulkStyleNo;

    /** 大货款图 */
    @ApiModelProperty(value = "大货款图")
    private String styleColorPic;
    /** 款图 */
    @ApiModelProperty(value = "款图")
    private String stylePic;
    /**
     * 是否可编辑,0：可编辑,1：不可编辑
     */
    private Integer isEdit = 0;

    /** 产品季id */
    @ApiModelProperty(value = "产品季id")
    private String planningSeasonId;

    /** 产品季名称 */
    @ApiModelProperty(value = "产品季名称")
    @JsonIgnore
    private String planningSeasonName;

    /** 波段code */
    @ApiModelProperty(value = "波段code")
    private String bandCode;

    /** 波段名称 */
    @ApiModelProperty(value = "波段名称")
    @JsonIgnore
    private String bandName;

    /** 款式id */
    @ApiModelProperty(value = "款式id")
    private String styleId;

    /** 大类code */
    @ApiModelProperty(value = "大类code")
    private String prodCategory1st;

    /** 大类name */
    @ApiModelProperty(value = "大类name")
    @JsonIgnore
    private String prodCategory1stName;

    /** 品类code */
    @ApiModelProperty(value = "品类code")
    private String prodCategory;

    /** 品类name */
    @ApiModelProperty(value = "品类name")
    @JsonIgnore
    private String prodCategoryName;

    /** 中类code */
    @ApiModelProperty(value = "中类code")
    private String prodCategory2nd;

    /** 中类name */
    @ApiModelProperty(value = "中类name")
    @JsonIgnore
    private String prodCategory2ndName;

    /** 小类code */
    @ApiModelProperty(value = "小类code")
    private String prodCategory3rd;

    /** 小类code */
    @ApiModelProperty(value = "小类code")
    @JsonIgnore
    private String prodCategory3rdName;
    /**
     * 列头筛选数量
     */
    private Integer groupCount;

    public String getStyleColorPic() {
        return StrUtil.isBlank(styleColorPic) ? stylePic : styleColorPic;
    }

    public String getTypeName() {
        return getType().getText();
    }

    public String getCategoryName() {
        return categoryNameJoiner().toString();
    }

    protected StrJoiner categoryNameJoiner() {
        return StrJoiner.of("\n").setNullMode(StrJoiner.NullMode.TO_EMPTY).append(planningSeasonName).append(bandName).append(
                StrJoiner.of("/").setNullMode(StrJoiner.NullMode.IGNORE).append(prodCategory1stName).append(prodCategoryName).append(prodCategory2ndName).append(prodCategory3rdName)
        );
    }

}
