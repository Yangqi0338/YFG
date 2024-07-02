package com.base.sbc.client.flowable.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.base.sbc.client.flowable.vo.FlowRecordVo;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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


    public static final String SAMPLE_DESIGN_PDN = "款式设计审批";
    public static final String BIG_GOODS_REVERSE = "大货资料包反审";
    public static final String DESIGN_BOM_PDN = "设计BOM物料审批";

    public static final String MATERIAL = "素材审批";

    public static final String HANGING_TAG_REVIEW = "吊牌审核";
    public static final String PACK_TECH_PDN = "工艺说明审批";

    public static final String SAMPLE_ARCHIVES = "样衣档案审批";

    public static final String PURCHASE_ORDER = "采购单";

    public static final String WAREHOUSING_ORDER = "入库单";

    public static final String OUTBOUND_ORDER = "出库单";

    public static final String FABRIC_PLANNING = "面料企划审批";
    public static final String FABRIC_POOL = "设计面料池审批";
    public static final String BASICSDATUM_MATERIAL = "物料审批";

    public static final String PURCHASE_REQUEST = "采购申请单";

    public static final String DESIGN_MARKING = "款式打标-设计阶段审批";

    public static final String ORDER_MARKING = "款式打标-下单阶段审批";

    public static final String PATTERN_LIBRARY_APPROVAL = "版型库审批";

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

    /**
     * 获取流程当前节点，是否完成
     * @param businessKeys
     * @return
     */
    public Map<String, FlowRecordVo> getFlowRecordMapBybusinessKey(List<String> businessKeys) {
        if (CollectionUtils.isEmpty(businessKeys)) {
            throw new OtherException("业务编码为空");
        }
        String s = flowableFeignService.getFlowRecordMapBybusinessKey(StringUtils.convertListToString(businessKeys));
        Map<String, FlowRecordVo> map = new HashMap<>();
        if (StringUtils.isNotBlank(s)) {
            map = JSON.parseObject(s, new TypeReference<HashMap<String, FlowRecordVo>>() {
            });
        }
        return map;
    }


}
