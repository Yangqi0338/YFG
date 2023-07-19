package com.base.sbc.module.sample.vo;

import com.base.sbc.module.nodestatus.entity.NodeStatus;
import com.base.sbc.module.sample.entity.PreProductionSampleTask;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 类描述：产前样-任务
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.vo.PreProductionSampleTaskVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-18 15:47
 */
@Data
@ApiModel("产前样-任务 PreProductionSampleTask")
public class PreProductionSampleTaskVo extends PreProductionSampleTask {

    @ApiModelProperty(value = "节点信息list")
    private List<NodeStatus> nodeStatusList;
    @ApiModelProperty(value = "节点信息Map")
    private Map<String, NodeStatus> nodeStatus;
}
