package com.base.sbc.module.patternlibrary.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 版型库-品类关联表
 *
 * @author xhte
 * @create 2024-03-22
 */
@Data
@TableName("t_pattern_library_brand")
@ApiModel(value = "TPatternLibraryBrand对象", description = "版型库-品类关联表")
public class PatternLibraryBrand extends BaseDataEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部件库 ID（t_pattern_library id）
     */
    @ApiModelProperty("部件库 ID")
    private String patternLibraryId;

    /**
     * 品牌 code
     */
    @ApiModelProperty("品牌 code")
    private String brand;

    /**
     * 品牌名称
     */
    @ApiModelProperty("品牌名称")
    private String brandName;
}
