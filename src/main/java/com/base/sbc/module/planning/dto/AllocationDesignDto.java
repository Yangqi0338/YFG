package com.base.sbc.module.planning.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 类描述：分配设计师
 * @address com.base.sbc.module.planning.dto.AllocationDesignDto
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-21 11:10
 * @version 1.0
 */
@Data
@ApiModel("产品季总览-分配设计师 AllocationDesignDto")
public class AllocationDesignDto {
    @ApiModelProperty(value = "坑位编号" ,example = "689467740238381056",required = true)
    @NotNull(message = "坑位编号不能为空")
    private String id;

    @ApiModelProperty(value = "设计师名称",example = "张三" ,required = true )
    @NotNull(message = "设计师名称代码不能为空,格式:名称,代码")
    private String designer;
    @ApiModelProperty(value = "设计师id" ,example = "689467740238381056",required = true )
    @NotNull(message = "设计师id不能为空")
    private String designerId;
}
