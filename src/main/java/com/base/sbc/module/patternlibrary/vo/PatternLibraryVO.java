package com.base.sbc.module.patternlibrary.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 版型库-主表VO
 *
 * @author xhte
 * @create 2024-03-22
 */
@Data
@ApiModel(value = "PatternLibraryVO对象", description = "版型库-主表VO")
public class PatternLibraryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @ApiModelProperty("序号")
    @TableId(value = "serial_number", type = IdType.AUTO)
    private Integer serialNumber;

    /**
     * 版型编码
     */
    @ApiModelProperty("版型编码")
    private String code;

    /**
     * 款式编码
     */
    @ApiModelProperty("款式编码")
    private String designNo;

    /**
     * 款式 ID（t_style：id）
     */
    @ApiModelProperty("款式 ID")
    private String styleId;

    /**
     * 品牌
     */
    @ApiModelProperty("品牌")
    private String brand;

    /**
     * 品牌名称
     */
    @ApiModelProperty("品牌名称")
    private String brandName;

    /**
     * 大类 code
     */
    @ApiModelProperty("大类 code")
    private String prodCategory1st;

    /**
     * 大类名称
     */
    @ApiModelProperty("大类名称")
    private String prodCategory1stName;

    /**
     * 品类 code
     */
    @ApiModelProperty("品类 code")
    private String prodCategory;

    /**
     * 品类名称
     */
    @ApiModelProperty("品类名称")
    private String prodCategoryName;

    /**
     * 中类 code
     */
    @ApiModelProperty("中类 code")
    private String prodCategory2nd;

    /**
     * 中类名称
     */
    @ApiModelProperty("中类名称")
    private String prodCategory2ndName;

    /**
     * 小类 code
     */
    @ApiModelProperty("小类 code")
    private String prodCategory3rd;

    /**
     * 小类名称
     */
    @ApiModelProperty("小类名称")
    private String prodCategory3rdName;

    /**
     * 廓形 code
     */
    @ApiModelProperty("廓形 code")
    private String silhouetteCode;

    /**
     * 廓形名称
     */
    @ApiModelProperty("廓形名称")
    private String silhouetteName;

    /**
     * 模板 code（t_pattern_library code）
     */
    @ApiModelProperty("模板 code")
    private String templateCode;

    /**
     * 文件 ID
     */
    @ApiModelProperty("文件 ID")
    private String fileId;

    /**
     * 面料 code，暂无
     */
    @ApiModelProperty("面料 code，暂无")
    private String materialCode;

    /**
     * 面料名称
     */
    @ApiModelProperty("面料名称")
    private String materialName;

    /**
     * 图片文件 ID
     */
    @ApiModelProperty("图片文件 ID")
    private String picFileId;

    /**
     * 状态（1-待补齐 2-待提交 3-待审核 4-已审核 5-已驳回）
     */
    @ApiModelProperty("状态（1-待补齐 2-待提交 3-待审核 4-已审核 5-已驳回）")
    private Integer status;

    /**
     * 启用状态（0-停用，1-启用)
     */
    @ApiModelProperty("启用状态（0-停用，1-启用)")
    private Integer enableFlag;
}
