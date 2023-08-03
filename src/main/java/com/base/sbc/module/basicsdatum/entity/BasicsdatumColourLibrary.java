/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.smp.dto.SmpColorDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：基础资料-颜色库 实体类
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-20 20:23:02
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_colour_library")
@ApiModel("基础资料-颜色库 BasicsdatumColourLibrary")
public class BasicsdatumColourLibrary extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/

    public SmpColorDto toSmpColorDto(){
        IdGen idGen =new IdGen();
        SmpColorDto smpColorDto =new SmpColorDto();
        smpColorDto.setId(id);
        smpColorDto.setName(colourName);
        smpColorDto.setSyncId(String.valueOf(idGen.nextId()));
        smpColorDto.setActive("0".equals(status));
        smpColorDto.setCreator(getCreateName());
        smpColorDto.setCreateTime(getCreateDate());
        smpColorDto.setModifiedPerson(getUpdateName());
        smpColorDto.setModifiedTime(getUpdateDate());

        if (StringUtils.isEmpty(colourCode)){
            throw new OtherException("颜色编码不能为空");
        }
        if (StringUtils.isEmpty(colourName)){
            throw new OtherException("颜色名称不能为空");
        }
        if (StringUtils.isEmpty(colorType)){
            throw new OtherException("色系编码不能为空");
        }
        if (StringUtils.isEmpty(colorTypeName)){
            throw new OtherException("色系名称不能为空");
        }
        smpColorDto.setColorName(colourName);
        smpColorDto.setColorCode(colourCode);
        smpColorDto.setColorType(colorType);
        smpColorDto.setColorTypeName(colorTypeName);
        smpColorDto.setColorChromaId(chroma);
        smpColorDto.setColorChroma(chromaName);
        smpColorDto.setRange("款式/");
        return smpColorDto;
    }

	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 色系 */
    @ApiModelProperty(value = "色系"  )
    private String colorType;
    /** 色系名称 */
    @ApiModelProperty(value = "色系名称"  )
    private String colorTypeName;
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
//    /** 可用于款式 */
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
    /** 16进制颜色 */
    @ApiModelProperty(value = "16进制颜色"  )
    private String color16;
    /** 色度 */
    @ApiModelProperty(value = "色度"  )
    private String chroma;
    /** 色度名称 */
    @ApiModelProperty(value = "色度名称"  )
    private String chromaName;
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

    /** SCM下发状态:0未下发,1已下发 */
    @ApiModelProperty(value = "SCM下发状态:0未下发,1已下发"  )
    private String scmSendFlag;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
