package com.base.sbc.client.flowable.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.exception.OtherException;
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


    public static final String sample_design_pdn = "款式设计审批";
    public static final String big_goods_reverse = "大货资料包反审";
    public static final String design_bom_pdn = "设计BOM物料审批";

    public static final String MATERIAL = "素材审批";
    public static final String pack_tech_pdn = "工艺说明审批";

    public static final String SAMPLE_ARCHIVES = "样衣档案审批";

    public static final String PURCHASE_ORDER = "采购单";

    public static final String WAREHOUSING_ORDER = "入库单";

    public static final String OUTBOUND_ORDER = "出库单";

    @Autowired
    FlowableFeignService flowableFeignService;

    /**
     * 开始流程
     *
     * @param title                 单据标题
     * @param processDefinitionName 流程名称
     * @param businessKey           业务id
     * @param answerAddress         通过回调地址
     * @param rejectAddress         驳回回调地址
     * @param cancelAddress         取消回调地址
     * @param checkAddress          查看明细地址
     * @param variables             流程数据
     * @return
     */
    public boolean start(String title, String processDefinitionName, String businessKey, String answerAddress, String rejectAddress, String cancelAddress, String checkAddress, Map<String, Object> variables) {
        if (variables == null) {
            variables = new LinkedHashMap<>(16);
        }
        variables.put("title", title);
        variables.put("answerAddress", answerAddress);
        variables.put("rejectAddress", rejectAddress);
        variables.put("checkAddress", checkAddress);
        variables.put("cancelAddress", cancelAddress);
        String result = flowableFeignService.start(title, null, processDefinitionName, businessKey, null, variables);
        if (StrUtil.isNotBlank(result)) {
            ApiResult apiResult = JSONObject.parseObject(result, ApiResult.class);
            if (apiResult.getSuccess()) {
                return true;
            } else {
                throw new OtherException(apiResult.getMessage());
            }
        }
        return false;
    }
}
