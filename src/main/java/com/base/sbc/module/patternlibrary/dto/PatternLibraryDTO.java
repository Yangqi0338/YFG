package com.base.sbc.module.patternlibrary.dto;

import com.base.sbc.module.patternlibrary.entity.Page;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryBrand;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryItem;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 版型库-主表DTO
 *
 * @author xhte
 * @create 2024-03-22
 */
@Data
@ApiModel(value = "PatternLibraryDTO对象", description = "版型库-主表DTO")
public class PatternLibraryDTO extends Page implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

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
     * 产品季
     */
    @ApiModelProperty("产品季")
    private String planningSeasonId;

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
     * 热销大货款（多选逗号分隔，从当前款下获取）
     */
    @ApiModelProperty("热销大货款（多选逗号分隔，从当前款下获取）")
    private String styleNos;

    /**
     * 下单大货款（多选逗号分隔，默认带出设计款下面的所有已投产的大货款）
     */
    @ApiModelProperty("下单大货款（多选逗号分隔，默认带出设计款下面的所有已投产的大货款）")
    private String placeOrderStyleNos;

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
     * 模板名称（t_pattern_library name）
     */
    @ApiModelProperty("模板名称")
    private String templateName;

    /**
     * 常青编号
     */
    @ApiModelProperty("常青编号")
    private String everGreenCode;

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
     * 图片来源（1-文件上传 2-已有图片选择），因为已有图片选择的需要重新下载并且上传所以需要区分
     */
    @ApiModelProperty("图片来源（1-文件上传 2-已有图片选择）")
    private Integer picSource;

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
     * 品牌集合
     */
    @ApiModelProperty("品牌集合")
    private List<PatternLibraryBrand> patternLibraryBrandList;

    /**
     * 部件库-所属版型库
     */
    @ApiModelProperty("部件库-所属版型库)")
    private PatternLibraryTemplate patternLibraryTemplate;

    /**
     * 部件库-子表数据集合
     */
    @ApiModelProperty("部件库-子表数据集合)")
    private List<PatternLibraryItem> patternLibraryItemList;

    /**
     * 部件库-子表数据类型 用作列表修改判断（1-围度信息 2-长度信息 3-部位尺寸 4-涉及部件）
     */
    @ApiModelProperty("部件库-子表数据类型（1-围度信息 2-长度信息 3-部位尺寸 4-涉及部件）")
    private Integer patternLibraryItemType;

    /**
     * 大货编号
     */
    @ApiModelProperty("大货编号")
    private String styleNo;
}
