package com.base.sbc.module.patternlibrary.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import com.base.sbc.module.patternlibrary.config.UrlImageConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

/**
 * 常青原版 VO
 *
 * @author XHTE
 * @create 2024-06-13
 */
@Data
@ApiModel(value = "EverGreenVO对象")
public class EverGreenVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty("ID")
    private String id;

    /**
     * 版型编码
     */
    @ApiModelProperty("版型编码")
    private String code;

    /**
     * 常青编号
     */
    @ApiModelProperty("常青编号")
    private String everGreenCode;

    /**
     * 父级 ID
     */
    @ApiModelProperty("父级 ID")
    private String parentId;

    /**
     * 所有的上层父级 ID
     */
    @ApiModelProperty("所有的上层父级 ID")
    private String parentIds;

    /**
     * 子集
     */
    @ApiModelProperty("子集")
    private List<EverGreenVO> everGreenVOList;

}
