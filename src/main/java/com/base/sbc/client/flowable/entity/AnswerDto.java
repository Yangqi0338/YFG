package com.base.sbc.client.flowable.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * 类描述：回调实体
 * @address com.base.sbc.client.flowable.entity.AnswerDto
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-11 10:35
 * @version 1.0
 */
@Data
@ApiModel("审批回调实体 AnswerDto")
public class AnswerDto {
    @ApiModelProperty(value = "业务id", example = "122222")
    private String businessKey;
    @ApiModelProperty(value = "审批意见", example = "通过")
    private String confirmSay;
    @ApiModelProperty(value = "审批类型,pass:通过,reject:驳回,cancel:取消申请", example = "通过")
    private String approvalType;
    @ApiModelProperty(value = "撤回标记", example = "1")
    private String recallFlag;
    @ApiModelProperty(value = "流程参数")
    private Map<String, Object> variables;
}
