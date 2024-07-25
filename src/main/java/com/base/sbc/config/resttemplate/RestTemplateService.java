package com.base.sbc.config.resttemplate;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.constant.SmpProperties;
import com.base.sbc.config.constant.UrlConfig;
import com.base.sbc.module.pushrecords.service.PushRecordsService;
import com.base.sbc.module.smp.dto.HttpResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.base.sbc.client.ccm.enums.CcmBaseSettingEnum.SEND_FLAG;

/**
 * @author 卞康
 * @date 2023/5/8 15:31:18
 * @mail 247967116@qq.com
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RestTemplateService {

    @Autowired
    private  CcmFeignService ccmFeignService;

    private final RestTemplate restTemplate;

    @SafeVarargs
    public final HttpResp spmPost(String url, String jsonStr, Pair<String, String>... headers) {
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpResp httpResp = new HttpResp();
        try {
            Boolean b = ccmFeignService.getSwitchByCode(SEND_FLAG.getKeyCode());
            if (!b){
                httpResp.setSuccess(true);
                return httpResp;
            }
            requestHeaders.add("Content-Type", "application/json");
            for (Pair<String, String> header : headers) {
                requestHeaders.add(header.getKey(), header.getValue());
            }

            HttpEntity<String> fromEntity = new HttpEntity<>(jsonStr, requestHeaders);
            restTemplate.postForEntity(url, fromEntity, String.class);
            return RequestLoggingInterceptor.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            httpResp.setSuccess(false);
            httpResp.setMessage(e.getMessage());
        }
        return httpResp;
    }

    @SafeVarargs
    public final HttpResp spmPost(UrlConfig url, String jsonStr, Pair<String, String>... headers) {
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpResp httpResp = new HttpResp();
        try {
            Boolean b = ccmFeignService.getSwitchByCode(SEND_FLAG.getKeyCode());
            if (!b){
                httpResp.setSuccess(true);
                return httpResp;
            }
            requestHeaders.add("Content-Type", "application/json");
            for (Pair<String, String> header : headers) {
                requestHeaders.add(header.getKey(), header.getValue());
            }

            HttpEntity<String> fromEntity = new HttpEntity<>(jsonStr, requestHeaders);
            restTemplate.postForEntity(url.toString(), fromEntity, String.class);
            return RequestLoggingInterceptor.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            httpResp.setSuccess(false);
            httpResp.setMessage(e.getMessage());
        }
        return httpResp;
    }

    public static HttpResp buildHttpResp(String responseStrData) {
        HttpResp httpResp = JSONUtil.toBean(responseStrData, HttpResp.class);
        JSONObject jsonObject = JSONObject.parseObject(responseStrData);
        if (!jsonObject.containsKey("success") && (!StrUtil.startWithAny(httpResp.getCode(),"4","5"))) {
            httpResp.setSuccess(true);
        }
        if ("0000000".equals(httpResp.getCode())) {
            httpResp.setSuccess(true);
        }
        return httpResp;
    }

}
