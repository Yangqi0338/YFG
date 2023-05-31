package com.base.sbc.module.nodestatus.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.nodestatus.dto.NodeStatusChangeDto;
import com.base.sbc.module.nodestatus.service.NodeStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 类描述：节点状态管理
 * @address com.base.sbc.module.nodestatus.controller.NodeStatusController
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-30 11:06
 * @version 1.0
 */

@RestController
@Api(tags = "节点状态管理")
@RequestMapping(value = BaseController.SAAS_URL + "/nodeStatus", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class NodeStatusController {
    @Autowired
    private NodeStatusService nodeStatusService;

    @PostMapping("/change")
    @ApiOperation(value = "节点状态改变",notes = "1、设置上一节点状态结束时间,2、保存当前节点状态开始时间")
    public boolean nodeStatusChange(@Valid @RequestBody NodeStatusChangeDto dto){
        nodeStatusService.nodeStatusChange(dto.getDataId(),dto.getNode(),dto.getStatus());
        return true;
    }

    @PostMapping("/finishNodeStatus")
    @ApiOperation(value = "完成指定节点状态",notes = "1、设置节点的结束时间")
    public boolean finishNodeStatus(@Valid @RequestBody NodeStatusChangeDto dto){
        nodeStatusService.finishNodeStatus(dto.getDataId(),dto.getNode(),dto.getStatus());
        return true;
    }

    @PostMapping("/finishCurrentNodeStatus")
    @ApiOperation(value = "完成当前节点状态",notes = "设置当前节点状态完成时间")
    public boolean finishCurrentNodeStatus(@Valid @NotBlank(message = "数据id不能为空") String dataId){
        nodeStatusService.finishCurrentNodeStatus(dataId);
        return true;
    }



}
