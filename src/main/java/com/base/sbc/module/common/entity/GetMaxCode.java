package com.base.sbc.module.common.entity;

import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.config.common.ApiResult;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 获取最大流水号需要传递的参数
 *
 * @author ykh
 * @date 2022-07-08 18:29
 */
public class GetMaxCode {
    /**
     * 流水号起始位置
     */

    private Integer digit;
    /**
     * 流水号占位数
     */

    private Integer num;
    /**
     * 对应字段和值，例如: brand->sjkj
     */

    private List<Map<String, String>> columns;
    /**
     * 编码总位数，用此过滤掉不符合位数的数据
     */

    private Integer codeLength;
    /**
     * ccm 生成的正则
     */
    private String regexp;
    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    public Integer getDigit() {
        return digit;
    }

    public void setDigit(Integer digit) {
        this.digit = digit;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public List<Map<String, String>> getColumns() {
        return columns;
    }

    public void setColumns(List<Map<String, String>> columns) {
        this.columns = columns;
    }

    public Integer getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(Integer codeLength) {
        this.codeLength = codeLength;
    }

    /**
     * 远程请求的服务
     */
    private CcmService ccmService;

    public GetMaxCode() {
    }

    public GetMaxCode(CcmService ccmService) {
        this.ccmService = ccmService;
    }

    /**
     * 根据编码规则中配置的编码，获取编号
     *
     * @param billCode
     * @param object
     * @return
     */
    public String genCode(String billCode, Object object) {
        //远程请求获取结果
        try {
            ApiResult apiResult = this.ccmService.genCodeByCodeRule(billCode, object);
            if (apiResult.getSuccess()) {
                return apiResult.getData().toString();
            }
        } catch (Exception e) {
            //未生成，或生成有异常
            return null;
        }
        //未生成，或生成有异常
        return null;
    }
}
