package com.base.sbc.module.patternlibrary.vo;

import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 版型库-主表VO
 *
 * @author XHTE
 * @create 2024-03-22
 */
@Data
@ApiModel(value = "PatternLibraryVO对象", description = "版型库-主表VO")
public class CategoriesTypeVO extends BaseDataEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 大类 上装集合
     */
    @ApiModelProperty("大类 上装集合")
    private String brandPuts;

    /**
     * 大类 下装集合
     */
    @ApiModelProperty("大类 下装集合")
    private String brandBottoms;

}
