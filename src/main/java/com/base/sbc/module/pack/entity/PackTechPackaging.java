/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 类描述：资料包-工艺说明-包装方式和体积重量 实体类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.entity.PackTechPackaging
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-8-16 17:52:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pack_tech_packaging")
@ApiModel("资料包-工艺说明-包装方式和体积重量 PackTechPackaging")
public class PackTechPackaging extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 主数据id
     */
    @ApiModelProperty(value = "主数据id")
    private String foreignId;
    /**
     * 资料包类型:packDesign:设计资料包
     */
    @ApiModelProperty(value = "资料包类型:packDesign:设计资料包")
    private String packType;
    /**
     * 包装形式
     */
    @ApiModelProperty(value = "包装形式")
    private String packagingForm;
    /**
     * 包装形式名称
     */
    @ApiModelProperty(value = "包装形式名称")
    private String packagingFormName;
    /**
     * 包装袋标准
     */
    @ApiModelProperty(value = "包装袋标准")
    private String packagingBagStandard;
    /**
     * 包装袋标准名称
     */
    @ApiModelProperty(value = "包装袋标准名称")
    private String packagingBagStandardName;
    /**
     * 重量
     */
    @ApiModelProperty(value = "重量")
    private String weight;

    /**
     * 长
     */
     @ApiModelProperty(value = "长")
    private BigDecimal volumeLength;

    /**
     * 宽
     */
    @ApiModelProperty(value = "宽")
    private BigDecimal volumeWidth;

    @ApiModelProperty(value = "高")
    private BigDecimal volumeHeight;
    @ApiModelProperty(value = "长")
    private BigDecimal volumeLength1;

    /**
     * 宽
     */
    @ApiModelProperty(value = "宽")
    private BigDecimal volumeWidth1;


    @ApiModelProperty(value = "高")
    private BigDecimal volumeHeight1;

    /**
     * 体积
     */
    @ApiModelProperty(value = "体积")
    private String volume;

    public String getVolume() {
        if (volumeLength == null || volumeWidth == null || volumeHeight == null) {
            return null;
        }
        return volumeLength.multiply(volumeWidth).multiply(volumeHeight).toString();
    }

    /**
     * 外辅工艺是否打印(0不打印，1打印)
     */
    @ApiModelProperty(value = "外辅工艺是否打印(0不打印，1打印)")
    private String printWaifuFlag;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

