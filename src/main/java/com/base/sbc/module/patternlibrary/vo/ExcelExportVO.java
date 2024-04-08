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

/**
 * Excel 导出VO
 *
 * @author XHTE
 * @create 2024-04-08
 */
@Data
@ApiModel(value = "ExcelExportVO对象", description = "Excel 导出VO")
@ColumnWidth(25)
@ContentRowHeight(60)
@ContentStyle(
        wrapped = BooleanEnum.TRUE,
        horizontalAlignment =  HorizontalAlignmentEnum.CENTER,
        verticalAlignment = VerticalAlignmentEnum.CENTER,
        shrinkToFit = BooleanEnum.TRUE
)

public class ExcelExportVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 版型编码
     */
    @ExcelProperty(value = "版型编码")
    @ApiModelProperty("版型编码")
    private String code;

    /**
     * 图片
     */
    @ExcelProperty(value = "图片", converter = UrlImageConverter.class)
    @ApiModelProperty("图片")
    private URL picUrl;

    /**
     * 品牌（多个/分隔）
     */
    @ColumnWidth(10)
    @ExcelProperty(value = "所属品牌")
    @ApiModelProperty("品牌（多个/分隔）")
    private String brandNames;

    /**
     * 部件库-子表围度数据
     */
    @ExcelProperty(value = "围度信息")
    @ApiModelProperty("部件库-子表围度数据")
    @ContentStyle(
            wrapped = BooleanEnum.TRUE,
            verticalAlignment = VerticalAlignmentEnum.CENTER
    )
    private String patternLibraryItemPattern;

    /**
     * 部件库-子表长度数据
     */
    @ExcelProperty(value = "长度信息")
    @ApiModelProperty("部件库-子表长度数据")
    @ContentStyle(
            wrapped = BooleanEnum.TRUE,
            verticalAlignment = VerticalAlignmentEnum.CENTER
    )
    private String patternLibraryItemLength;

    /**
     * 部件库-子表部位数据
     */
    @ColumnWidth(35)
    @ApiModelProperty("部件库-子表部位数据")
    @ExcelProperty(value = "细节尺寸描述")
    @ContentStyle(
            wrapped = BooleanEnum.TRUE,
            verticalAlignment = VerticalAlignmentEnum.CENTER
    )
    private String patternLibraryItemPosition;

    /**
     * 面料名称
     */
    @ApiModelProperty("面料名称")
    @ExcelProperty(value = "面料")
    private String materialName;

    /**
     * 所属品类 大类/品类/中类/小类
     */
    @ColumnWidth(30)
    @ApiModelProperty("所属品类 大类/品类/中类/小类")
    @ExcelProperty(value = "所属品类")
    private String allProdCategoryNames;

    /**
     * 廓形名称
     */
    @ApiModelProperty("廓形名称")
    @ExcelProperty(value = "廓形")
    private String silhouetteName;

    /**
     * 模板名称（t_pattern_library name）
     */
    @ExcelProperty(value = "所属版型库")
    @ApiModelProperty("模板名称")
    private String templateName;

    /**
     * 模板子表信息格式化后
     */
    @ColumnWidth(40)
    @ApiModelProperty("模板子表信息格式化后")
    @ExcelProperty(value = "可否改版")
    private String patternLibraryTemplateItem;

    /**
     * 部件库-子表部件数据
     */
    @ApiModelProperty("部件库-子表部件数据")
    @ExcelProperty(value = "涉及部件")
    private String patternLibraryItemParts;

    /**
     * 状态（1-待补齐 2-待提交 3-待审核 4-已审核 5-已驳回）
     */
    @ExcelProperty(value = "审核状态")
    @ApiModelProperty("状态（1-待补齐 2-待提交 3-待审核 4-已审核 5-已驳回）")
    private String status;

    /**
     * 启用状态（0-停用，1-启用)
     */
    @ExcelProperty(value = "启用状态")
    @ApiModelProperty("启用状态（0-停用，1-启用)")
    private String enableFlag;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @ExcelProperty(value = "创建人")
    private String createName;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @ExcelProperty(value = "创建时间")
    private String createDate;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    @ExcelProperty(value = "修改人")
    private String updateName;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @ExcelProperty(value = "修改时间")
    private String updateDate;


}
