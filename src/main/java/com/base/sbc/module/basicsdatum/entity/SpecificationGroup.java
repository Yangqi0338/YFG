package com.base.sbc.module.basicsdatum.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 卞康
 * @date 2023/6/27 17:25
 * @mail 247967116@qq.com
 * 规格组
 */
@Data
@TableName("t_basicsdatum_specification_group")
public class SpecificationGroup extends BaseDataEntity<String> {
    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "类型名称")
    private String  typeName;

    @ApiModelProperty(value = "使用范围")
    private String applyRange;

    @ApiModelProperty(value = "规格id集合")
    private String specificationIds;

    @ApiModelProperty(value = "规格名称集合")
    private String specificationNames;

    @ApiModelProperty(value = "基础规格")
    private String  basicsSpecification;

    @ApiModelProperty(value = "基础规格名称")
    private String  basicsSpecificationName;

    @ApiModelProperty(value = "备注")
    private String remarks;
}
