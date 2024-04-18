package com.base.sbc.module.patternlibrary.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.module.common.vo.AttachmentVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 版型库-主表
 *
 * @author xhte
 * @create 2024-03-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pattern_library")
@ApiModel(value = "PatternLibrary对象", description = "版型库-主表")
public class PatternLibrary extends BaseDataEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @ApiModelProperty("序号")
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
     * 所属品类 大类/品类/中类/小类
     */
    @ApiModelProperty("所属品类 大类/品类/中类/小类")
    @TableField(exist = false)
    private String allProdCategoryNames;

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
     * 模板名称（t_pattern_library name）
     */
    @ApiModelProperty("模板名称")
    private String templateName;

    /**
     * 文件 ID
     */
    @ApiModelProperty("文件 ID")
    private String fileId;

    /**
     * 文件地址链接
     */
    @ApiModelProperty("文件地址链接")
    private String fileAddress;

    /**
     * 文件附件信息
     */
    @ApiModelProperty("文件附件信息")
    @TableField(exist = false)
    private AttachmentVo fileAttachmentVo;

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
     * 图片 ID
     */
    @ApiModelProperty("图片 ID")
    private String picId;

    /**
     * 图片 URL
     */
    @ApiModelProperty("图片 URL")
    private String picUrl;

    /**
     * 图片附件信息
     */
    @ApiModelProperty("图片附件信息")
    @TableField(exist = false)
    private AttachmentVo picAttachmentVo;

    /**
     * 款式图片 ID
     */
    @ApiModelProperty("图片 ID")
    @TableField(exist = false)
    private String stylePicId;

    /**
     * 款式图片 URL
     */
    @ApiModelProperty("图片 URL")
    @TableField(exist = false)
    private String stylePicUrl;

    /**
     * 大货图片文件 ID-URL 集合
     */
    @ApiModelProperty("大货图片文件 ID-URL 集合")
    @TableField(exist = false)
    private List<Map<String, String>> picIdList;

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

    /**
     * 品牌（多个/分隔）
     */
    @ApiModelProperty("品牌（多个/分隔）")
    @TableField(exist = false)
    private String brandNames;

    /**
     * 品牌集合
     */
    @ApiModelProperty("品牌集合")
    @TableField(exist = false)
    private List<PatternLibraryBrand> patternLibraryBrandList;

    /**
     * 部件库-所属版型库
     */
    @ApiModelProperty("部件库-所属版型库")
    @TableField(exist = false)
    private PatternLibraryTemplate patternLibraryTemplate;

    /**
     * 模板子表信息格式化后
     */
    @ApiModelProperty("模板子表信息格式化后")
    @TableField(exist = false)
    private String patternLibraryTemplateItem;

    /**
     * 部件库-子表数据集合
     */
    @ApiModelProperty("部件库-子表数据集合")
    @TableField(exist = false)
    private List<PatternLibraryItem> patternLibraryItemList;

    /**
     * 部件库-子表围度数据
     */
    @ApiModelProperty("部件库-子表围度数据")
    @TableField(exist = false)
    private String patternLibraryItemPattern;

    /**
     * 部件库-子表长度数据
     */
    @ApiModelProperty("部件库-子表长度数据")
    @TableField(exist = false)
    private String patternLibraryItemLength;

    /**
     * 部件库-子表部位数据
     */
    @ApiModelProperty("部件库-子表部位数据")
    @TableField(exist = false)
    private String patternLibraryItemPosition;

    /**
     * 部件库-子表部件数据
     */
    @ApiModelProperty("部件库-子表部件数据")
    @TableField(exist = false)
    private String patternLibraryItemParts;

}
