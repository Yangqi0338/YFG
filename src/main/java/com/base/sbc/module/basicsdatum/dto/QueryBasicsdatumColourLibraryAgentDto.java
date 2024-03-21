package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryBasicsdatumColourLibraryAgentDto extends Page {

    private String id;


    /** 颜色组id */
    @ApiModelProperty(value = "颜色组id"  )
    private String colorType;
    /** 颜色规格 */
    @ApiModelProperty(value = "颜色规格"  )
    private String colourSpecification;
    /** 代码 */
    @ApiModelProperty(value = "代码"  )
    private String colourCode;
    /** 颜色名称 */
    @ApiModelProperty(value = "颜色名称"  )
    private String colourName;
    /** 库 */
    @ApiModelProperty(value = "库"  )
    private String library;
    /** 可用于款式 */
    @ApiModelProperty(value = "可用于款式"  )
    private String isStyle;
    /** 可用于材料 */
    @ApiModelProperty(value = "可用于材料"  )
    private String isMaterials;
    /** 潘通 */
    @ApiModelProperty(value = "潘通"  )
    private String pantone;
    /** RGB三角 */
    @ApiModelProperty(value = "RGB三角"  )
    private String colorRgb;
    /** 色度 */
    @ApiModelProperty(value = "色度"  )
    private String chroma;
    /** 英文名称 */
    @ApiModelProperty(value = "英文名称"  )
    private String englishName;
    /** 法文名称 */
    @ApiModelProperty(value = "法文名称"  )
    private String frenchName;
    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    private String picture;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;

    private String[] createDate;

    /**  创建者名称 */
    private String createName;

    @ApiModelProperty(value = "下发状态"  )
    private String  scmSendFlag;

    @ApiModelProperty(value = "品牌"  )
    private String brand;
}
