package com.base.sbc.module.patternmaking.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * 类描述：节点状态改变dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.dto.NodeStatusChangeDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-05 11:22
 */
@Data
@ApiModel("节点状态改变dto NodeStatusChangeDto ")
public class NodeStatusChangeDto {

    @ApiModelProperty(value = "数据id")
    private String dataId;
    @ApiModelProperty(value = "节点")
    private String node;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String status;
    /**
     * 节点开始标志(0否，1是)
     */
    @ApiModelProperty(value = "节点开始标志(0否，1是)")
    private String startFlg;
    /**
     * 节点结束标志(0否，1是)
     */
    @ApiModelProperty(value = "节点结束标志(0否，1是)")
    private String endFlg;

    @ApiModelProperty(value = "需要修改的属性")
    private Map<String, Object> updates;
}
