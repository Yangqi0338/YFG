package com.base.sbc.module.patternlibrary.vo;

import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryBrand;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryItem;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryTemplate;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private List<String> brandPuts;

    /**
     * 大类 下装集合
     */
    @ApiModelProperty("大类 下装集合")
    private List<String> brandBottoms;

}
