package com.base.sbc.module.fieldManagement.dto;

import com.base.sbc.module.fieldManagement.entity.Option;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

@Data
@ApiModel("保存修改字段管理表 SaveUpdateFieldManagementDto")
public class SaveUpdateFieldManagementDto {

    private String id;

    /** 表单类型主键 */
    @ApiModelProperty(value = "表单类型主键"  )
    @NotBlank(message = "表单类型主键必填")
    private String formTypeId;

    /** 分组名称 */
    @ApiModelProperty(value = "分组名称"  )
    private String groupName;

    /** 字段名称 */
    @ApiModelProperty(value = "字段名称"  )
    @NotBlank(message = "字段名称")
    private String fieldName;

    /** 字段类型 */
    @ApiModelProperty(value = "字段类型"  )
    @NotBlank(message = "字段名称")
    private String fieldType;
    /** 字段类型id */
    @ApiModelProperty(value = "字段类型id"  )
    @NotBlank(message = "字段类型id")
    private String fieldTypeId;
    /** 字段类型名 */
    @ApiModelProperty(value = "字段类型名"  )
    @NotBlank(message = "字段名称")
    private String fieldTypeName;
    @ApiModelProperty(value = "字段类型编码"  )
    private String  fieldTypeCoding;
    /** 默认提示 */
    @ApiModelProperty(value = "默认提示"  )
    @NotBlank(message = "默认提示")
    private String defaultHint;

    /** 选项（0手动，1外部' */
    @ApiModelProperty(value = "选项"  )
    private String isOption;

    /** 是否必填(0是，1否) */
    @ApiModelProperty(value = "是否必填(0是，1否)"  )
    @NotBlank(message = "是否必填")
    private String isMustFill;

    /** 品类使用范围对应结构树id */
    @ApiModelProperty(value = "品类使用范围对应结构树id"  )
    @NotBlank(message = "品类使用范围对应结构树id")
    private String categoryId;

    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    @NotBlank(message = "季节")
    private String season;

    /** 是否检查(0是，1否) */
    @ApiModelProperty(value = "是否检查(0是，1否)"  )
    @NotBlank(message = "是否检查")
    private String isExamine;

    /** 对象类型 对应表单类型表 */
    @ApiModelProperty(value = "对象类型 对应表单类型表"  )
    private String  formObjectId;

    /** 是否启用(0是，1否) */
    @ApiModelProperty(value = "是否启用(0是，1否)"  )
    @NotBlank(message = "是否启用")
    private String status;

    private String  fieldExplain;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remark;

    @ApiModelProperty(value = "顺序"  )
    private Integer sequence;

    @ApiModelProperty(value = "选项字典"  )
    private String optionDictKey;

    private List<Option> optionList;



}
