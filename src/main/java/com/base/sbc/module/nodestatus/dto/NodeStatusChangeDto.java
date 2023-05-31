package com.base.sbc.module.nodestatus.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：节点状态改变dto
 * @address com.base.sbc.module.nodestatus.dto.NodeStatusChangeDto
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-30 11:10
 * @version 1.0
 */
@Data
@ApiModel("节点状态改变dto PatternMakingDto ")
public class NodeStatusChangeDto {

    /**
     * 数据Id
     */
    @ApiModelProperty(value = "数据Id")
    @NotBlank(message = "数据id不能为空")
    private String dataId;
    /**
     * 节点
     */
    @ApiModelProperty(value = "节点")
    @NotBlank(message = "节点不能为空")
    private String node;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    @NotBlank(message = "状态不能为空")
    private String status;
}
