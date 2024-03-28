package com.base.sbc.client.flowable.service;

import com.base.sbc.client.flowable.vo.FlowQueryVo;
import com.base.sbc.config.common.ApiResult;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 类描述： 工作流相关接口
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.client.flowable.service.FlowableFeignService
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-11 09:43
 */
@FeignClient(name = "flowable", url = "http://" + "${baseGateAwayIpaddress}" + ":9151/", decode404 = true)
//@FeignClient(name = "flowable", url = "http://" + "127.0.0.1" + ":9151/", decode404 = true)
public interface FlowableFeignService {


    /**
     *
     * @param processDefinitionKey 流程定义id
     * @param processDefinitionName 流程名称
     * @param businessKey 业务id
     * @param version 版本
     * @param variables 变量集合,json对象
     * @return
     */
    @PostMapping("/flowable/api/saas/process/start")
    String start(
            @ApiParam(value = "标题") @RequestParam(value = "title") String title,
            @ApiParam(value = "流程定义id") @RequestParam(value = "processDefinitionKey",required = false) String processDefinitionKey,
            @ApiParam(value = "流程名称") @RequestParam(value = "processDefinitionName",required = false) String processDefinitionName,
            @ApiParam(value = "业务id") @RequestParam(value = "businessKey") String businessKey,
            @ApiParam(value = "版本") @RequestParam(value = "version", required = false) Integer version,
            @ApiParam(value = "变量集合,json对象") @RequestBody Map<String, Object> variables);


    /**
     * 获取流程当前节点，是否完成
     * @param businessKeys
     * @return
     */
    @GetMapping(value = "/flowable/api/saas/task/getFlowRecordMapBybusinessKey")
    String getFlowRecordMapBybusinessKey(@RequestParam("businessKeys") String businessKeys);


    @GetMapping(value = "/flowable/api/saas/task/todoList")
    public ApiResult todoList(@RequestParam Map<String,Object> queryVo);

    @GetMapping(value = "/flowable/api/saas/task/finishedList")
    public ApiResult finishedList(@RequestParam Map<String, Object> queryVo);


}
