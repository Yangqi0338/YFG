package com.base.sbc.module.fieldManagement.vo;

import com.base.sbc.module.fieldManagement.entity.Option;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(value = {"handler"})
public class FieldManagementVo {

    private String id;
    /** 表单类型主键 */
    @ApiModelProperty(value = "表单类型主键"  )
    private String formTypeId;
    /** 分组名称 */
    @ApiModelProperty(value = "分组名称"  )
    private String groupName;
    /** 字段名称 */
    @ApiModelProperty(value = "字段名称"  )
    private String fieldName;
    /** 字段类型id */
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
    /** 是否必填(0是，1否) */
    @ApiModelProperty(value = "是否必填(0是，1否)"  )
    private String isMustFill;
    /** 品类使用范围对应结构树id */
    @ApiModelProperty(value = "品类使用范围对应结构树id"  )
    private String categoryId;
    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    private String season;
    /** 是否检查(0是，1否) */
    @ApiModelProperty(value = "是否检查(0是，1否)"  )
    private String isExamine;
    /** 是否启用(0是，1否) */
    @ApiModelProperty(value = "是否启用(0是，1否)"  )
    private String status;

    private String fieldExplain;

    private String  isCompile;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remark;
    private String  sequence;
    private List<Option> optionList;
}
