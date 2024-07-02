package com.base.sbc.module.planning.vo;

import com.base.sbc.module.formtype.entity.Option;
import com.base.sbc.module.planning.entity.PlanningDimensionality;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PlanningDimensionalityVo extends PlanningDimensionality {



    @ApiModelProperty(value = "分组名称")
    private String groupName;


    @ApiModelProperty(value = "字段名称")
    private String  fieldName;


    @ApiModelProperty(value = "字段说明")
    private String    fieldExplain;


    private List<PlanningDimensionalityVo> list;

    @ApiModelProperty(value = "编辑")
    private boolean readonly;

    public boolean getReadonly() {
        return true;
    }

    /**
     * 表单类型主键
     */
    @ApiModelProperty(value = "表单类型主键")
    private String formTypeId;
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



}
