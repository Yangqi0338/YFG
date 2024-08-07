/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.entity;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.utils.CommonUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;

/**
 * 类描述：资料包-尺寸表配置 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.entity.PackSizeConfig
 * @email your email
 * @date 创建时间：2023-9-1 14:07:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pack_size_config")
@ApiModel("资料包-尺寸表配置 PackSizeConfig")
public class PackSizeConfig extends BaseDataEntity<String> {

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
     * 资料包类型:packDesign:设计资料包/packBigGoods:标准资料包(大货资料包)
     */
    @ApiModelProperty(value = "资料包类型:packDesign:设计资料包/packBigGoods:标准资料包(大货资料包)")
    private String packType;
    /**
     * 尺寸表洗后尺寸跳码:(0关闭,1开启)
     */
    @ApiModelProperty(value = "尺寸表洗后尺寸跳码:(0关闭,1开启)")
    private String washSkippingFlag;
    /**
     * 尺码
     */
    @ApiModelProperty(value = "尺码")
    private String productSizes;
    /**
     * 编辑的尺码
     */
    @ApiModelProperty(value = "编辑的尺码")
    private String activeSizes;
    /**
     * Default尺码
     */
    @ApiModelProperty(value = "Default尺码")
    private String defaultSize;
    /**
     * 尺码ids
     */
    @ApiModelProperty(value = "尺码ids")
    private String sizeIds;
    /**
     * 尺码codes
     */
    @ApiModelProperty(value = "尺码codes")
    private String sizeCodes;
    /**
     * 号型编码
     */
    @ApiModelProperty(value = "号型编码")
    private String sizeRange;
    /**
     * 号型名称
     */
    @ApiModelProperty(value = "号型名称")
    private String sizeRangeName;
    /**
     * 档差
     */
    @ApiModelProperty(value = "档差")
    private String  difference;

    /**
     * 档差id
     */
    @ApiModelProperty(value = "档差id")
    private String  differenceId;
    /**
     * 档差编码
     */
    @ApiModelProperty(value = "档差编码")
    private String differenceCode;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/

    public String getActiveSizes() {
        return newSortSizeName(activeSizes);
    }

    public String getProductSizes() {
        return newSortSizeName(productSizes);
    }

    private String newSortSizeName(String sizeName) {
        if (StrUtil.isBlank(sizeName)) return sizeName;
        return Arrays.stream(sizeName.split(COMMA)).sorted(CommonUtils.sizeNameSort()).collect(Collectors.joining(COMMA));
    }
}

