package com.base.sbc.module.pack.dto;

import com.base.sbc.module.style.dto.StyleInfoColorDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 资料包DTO
 * @author lizan
 * @date 2023-09-05 18:40
 */
@Data
@ApiModel("资料包DTO StyleInfoColorDto")
public class PackInfoDto {

    /**
     *  id
     */
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 主数据id(样衣设计id)
     */
    @ApiModelProperty(value = "主数据id(样衣设计id)")
    private String foreignId;

    @ApiModelProperty(value = "款式设计详情-颜色")
    private List<StyleInfoColorDto> styleInfoColorDtoList;

    @ApiModelProperty(value = "资料包类型:packDesign:设计资料包"  )
    private String packType;

    @ApiModelProperty(value = "颜色code集合")
    private String colorCodes;

    @ApiModelProperty(value = "颜色名称集合")
    private String productColors;

    @ApiModelProperty(value = "尺码集合")
    private String productSizes;

    @ApiModelProperty(value = "设计款号")
    private String designNo;

    @ApiModelProperty(value = "尺码code集合")
    private String sizeCodes;

    @ApiModelProperty(value = "新资料包id")
    private String  targetForeignId;

    @ApiModelProperty(value = "新资料包类型")
    private String  targetPackType;

    @ApiModelProperty(value = "源资料包类型")
    private String  sourcePackType;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 样板号
     */
    @ApiModelProperty(value = "样板号")
    private String patternNo;



}
