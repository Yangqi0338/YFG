package com.base.sbc.client.flowable.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.config.common.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 类描述：工作流接口
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.client.flowable.service.FlowableService
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-11 09:58
 */
@Service
public class FlowableService {


    public static final  String sample_pdn="样衣设计审批";

    @Autowired
    FlowableFeignService flowableFeignService;

    /**
     * 开始流程
     * @param processDefinitionName 流程名称
     * @param businessKey           业务id
     * @param answerAddress         通过回调地址
     * @param rejectAddress         驳回回调地址
     * @param checkAddress          查看明细地址
     * @param variables             流程数据
     * @return
     */
    public boolean start(String processDefinitionName, String businessKey, String answerAddress, String rejectAddress, String checkAddress, Map<String, Object> variables) {
        if (variables == null) {
            variables = new LinkedHashMap<>(16);
        }
        variables.put("answerAddress", answerAddress);
        variables.put("rejectAddress", rejectAddress);
        variables.put("checkAddress", checkAddress);
        String result=flowableFeignService.start(null, processDefinitionName, businessKey, null, variables);
        if(StrUtil.isNotBlank(result)){
            ApiResult apiResult = JSONObject.parseObject(result, ApiResult.class);
            return apiResult.getSuccess();
        }
        return false;
    }
}
