package com.base.sbc.module.formtype.vo;

import com.base.sbc.module.formtype.entity.Option;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(value = {"handler"})
public class FieldManagementVo {

    private String id;

    @ApiModelProperty(value = "字段id")
    private String fieldId;
    /**
     * 表单类型主键
     */
    @ApiModelProperty(value = "表单类型主键")
    private String formTypeId;
    /**
     * 分组名称
     */
    @ApiModelProperty(value = "分组名称")
    private String groupName;
    /**
     * 字段名称
     */
    @ApiModelProperty(value = "字段名称")
    private String fieldName;
    /**
     * 字段类型id
     */
    @ApiModelProperty(value = "字段类型id"  )
    private String fieldTypeId;
    @ApiModelProperty(value = "字段类型"  )
    private String fieldType;
    /** 字段类型id */
    @ApiModelProperty(value = "字段类型名"  )
    private String fieldTypeName;
    @ApiModelProperty(value = "字段类型编码"  )
    private String  fieldTypeCoding;
    /** 默认提示 */
    @ApiModelProperty(value = "默认提示"  )
    private String defaultHint;
    /** 选项（0手动，1外部' */
    @ApiModelProperty(value = "选项"  )
    private String isOption;
    /** 结构管理层级 */
    @ApiModelProperty(value = "结构管理层级"  )
    private String structureTier;
    /** 是否必填(0是，1否) */
    @ApiModelProperty(value = "是否必填(0是，1否)"  )
    private String isMustFill;
    /** 品类使用范围对应结构树id */
    @ApiModelProperty(value = "品类使用范围对应结构树id"  )
    private String categoryId;
    private String categoryName;
    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    private String season;
    private String seasonName;
    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;
    /** 品牌名称 */
    @ApiModelProperty(value = "品牌名称"  )
    private String brandName;
    /** 是否检查(0是，1否) */
    @ApiModelProperty(value = "是否检查(0是，1否)"  )
    private String isExamine;

    /** 对象类型 对应表单类型表 */
    @ApiModelProperty(value = "对象类型 对应表单类型表"  )
    private String  formObjectId;

    /** 是否启用(0是，1否) */
    @ApiModelProperty(value = "是否启用(0是，1否)"  )
    private String status;

    private String fieldExplain;

    private String isCompile;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    private String sequence;
    @ApiModelProperty(value = "字典key")
    private String optionDictKey;
    private List<Option> optionList;
    @ApiModelProperty(value = "字段值")
    private String val;
    @ApiModelProperty(value = "字段值名称")
    private String valName;
    @ApiModelProperty(value = "选中")
    private boolean selected = true;

    /** 设计显示标记 */
    @ApiModelProperty(value = "设计显示标记"  )
    private String designShowFlag;
    /** 设计检查标记 */
    @ApiModelProperty(value = "设计检查标记"  )
    private String designExamineFlag;
    /** 研发显示标记 */
    @ApiModelProperty(value = "研发显示标记"  )
    private String researchShowFlag;
    /** 研发检查标记 */
    @ApiModelProperty(value = "研发检查标记"  )
    private String researchExamineFlag;
    /** 复盘显示标记 */
    @ApiModelProperty(value = "复盘显示标记"  )
    private String replayShowFlag;
    /** 复盘检查标记 */
    @ApiModelProperty(value = "复盘检查标记"  )
    private String replayExamineFlag;
    /** 组内排序 */
    @ApiModelProperty(value = "组内排序"  )
    private Integer sort;
    /** 维度分组排序 */
    @ApiModelProperty(value = "维度分组排序"  )
    private Integer groupSort;
    /** 维度数据创建时间 */
    @ApiModelProperty(value = "维度数据创建时间"  )
    private Date createDate;

    List<FieldManagementVo> list;

    List<FieldOptionConfigVo> configVoList;


}
