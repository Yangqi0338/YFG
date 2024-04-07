package com.base.sbc.module.patternlibrary.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 列表筛选条件VO
 *
 * @author XHTE
 * @create 2024-04-03
 */
@Data
@ApiModel(value = "FilterCriteriaVO对象", description = "筛选条件VO")
public class FilterCriteriaVO implements Serializable {

    private static final long serialVersionUID = 1L;

    // /**
    //  * 版型编码列表
    //  */
    // @ApiModelProperty("版型编码列表")
    // private List<> patternLibraryItemParts;

}
