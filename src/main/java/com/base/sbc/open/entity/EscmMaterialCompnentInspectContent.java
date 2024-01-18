package com.base.sbc.open.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 物料送检成分明细对象 escm_material_compnent_inspect_content
 *
 * @author regent
 * @date 2023-05-26
 */
@Data
@ApiModel(description="物料送检成分明细")
@EqualsAndHashCode(callSuper=false)
@TableName(value = "escm_material_compnent_inspect_content")
public class EscmMaterialCompnentInspectContent{

    @ApiModelProperty(notes = "主键")
    private Long id;


    @ApiModelProperty(notes = "单据id")
    private Long billId;


    @ApiModelProperty(notes = "成分内容")
    private String inspectContent;

    @ApiModelProperty(value = "物料成分编号")
    private String kindCode;

    @ApiModelProperty(value = "物料成分名称")
    private String kindName;

    @ApiModelProperty(notes = "成分占比")
    private String contentProportion;

    @ApiModelProperty(notes = "备注")
    private String remark;

    @ApiModelProperty(notes = "操作标示, 0: 新增, 1:修改, 2删除")
    @TableField(exist = false)
    private Integer operateStatus;
}
