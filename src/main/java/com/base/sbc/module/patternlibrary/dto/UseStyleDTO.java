package com.base.sbc.module.patternlibrary.dto;

import com.base.sbc.module.patternlibrary.entity.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 使用款记录 DTO
 *
 * @author XHTE
 * @create 2024-03-22
 */
@Data
@ApiModel(value = "UseStyleDTO对象", description = "使用款 DTO")
public class UseStyleDTO extends Page implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 版型库 id
     */
    @ApiModelProperty("版型库 id")
    private String patternLibraryId;

}
