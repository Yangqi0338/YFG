package com.base.sbc.module.patternlibrary.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.base.sbc.module.common.vo.AttachmentVo;
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
public class PatternLibraryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    /**
     * 序号
     */
    @ApiModelProperty("序号")
    private Integer serialNumber;

    /**
     * 版型编码
     */
    @ExcelProperty(value = "版型编码", index = 0)
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
    @ExcelProperty(value = "所属品类", index = 6)
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
    @ExcelProperty(value = "廓形", index = 7)
    private String silhouetteName;

    /**
     * 模板 code（t_pattern_library code）
     */
    @ApiModelProperty("模板 code")
    private String templateCode;

    /**
     * 模板名称（t_pattern_library name）
     */
    @ExcelProperty(value = "所属版型库", index = 8)
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
    @ExcelProperty(value = "面料", index = 5)
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
    private AttachmentVo picAttachmentVo;

    /**
     * 大货图片文件 ID-URL 集合
     */
    @ApiModelProperty("大货图片文件 ID-URL 集合")
    private List<Map<String, String>> picIdList;

    /**
     * 状态（1-待补齐 2-待提交 3-待审核 4-已审核 5-已驳回）
     */
    @ExcelProperty(value = "审核状态", index = 11)
    @ApiModelProperty("状态（1-待补齐 2-待提交 3-待审核 4-已审核 5-已驳回）")
    private Integer status;

    /**
     * 启用状态（0-停用，1-启用)
     */
    @ExcelProperty(value = "启用状态", index = 12)
    @ApiModelProperty("启用状态（0-停用，1-启用)")
    private Integer enableFlag;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @ExcelProperty(value = "创建人", index = 13)
    private String createName;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelProperty(value = "创建时间", index = 14)
    private Date createDate;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    @ExcelProperty(value = "修改人", index = 15)
    private String updateName;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelProperty(value = "修改时间", index = 16)
    private Date updateDate;

    /**
     * 品牌（多个/分隔）
     */
    @ExcelProperty(value = "所属品牌", index = 1)
    @ApiModelProperty("品牌（多个/分隔）")
    private String brandNames;

    /**
     * 品牌集合
     */
    @ApiModelProperty("品牌集合")
    private List<PatternLibraryBrand> patternLibraryBrandList;

    /**
     * 部件库-所属版型库
     */
    @ApiModelProperty("部件库-所属版型库")
    private PatternLibraryTemplate patternLibraryTemplate;

    /**
     * 模板子表信息格式化后
     */
    @ApiModelProperty("模板子表信息格式化后")
    @ExcelProperty(value = "可否改版", index = 9)
    private String patternLibraryTemplateItem;

    /**
     * 部件库-子表数据集合
     */
    @ApiModelProperty("部件库-子表数据集合")
    private List<PatternLibraryItem> patternLibraryItemList;

    /**
     * 部件库-子表围度数据
     */
    @ApiModelProperty("部件库-子表围度数据")
    @ExcelProperty(value = "围度信息", index = 2)
    private String patternLibraryItemPattern;

    /**
     * 部件库-子表长度数据
     */
    @ApiModelProperty("部件库-子表长度数据")
    @ExcelProperty(value = "长度信息", index = 3)
    private String patternLibraryItemLength;

    /**
     * 部件库-子表部位数据
     */
    @ApiModelProperty("部件库-子表部位数据")
    @ExcelProperty(value = "细节尺寸描述", index = 4)
    private String patternLibraryItemPosition;

    /**
     * 部件库-子表部件数据
     */
    @ApiModelProperty("部件库-子表部件数据")
    @ExcelProperty(value = "涉及部件", index = 10)
    private String patternLibraryItemParts;

}
