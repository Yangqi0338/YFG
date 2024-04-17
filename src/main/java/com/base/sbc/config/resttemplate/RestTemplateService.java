package com.base.sbc.config.resttemplate;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.BooleanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.config.JsonStringUtils;
import com.base.sbc.module.smp.dto.HttpResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static com.base.sbc.client.ccm.enums.CcmBaseSettingEnum.DESIGN_BOM_TO_BIG_GOODS_CHECK_SWITCH;
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

    private final RestTemplate restTemplate;
    private final CcmFeignService ccmFeignService;


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
            ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, fromEntity, String.class);
            String body = stringResponseEntity.getBody();

            httpResp = buildHttpResp(body);
            httpResp.setUrl(url);
            httpResp.setStatusCode(String.valueOf(stringResponseEntity.getStatusCodeValue()));
            return httpResp;
        } catch (Exception e) {
            e.printStackTrace();
            httpResp.setSuccess(false);
            httpResp.setMessage(e.getMessage());
        }
        return httpResp;
    }

    public static HttpResp buildHttpResp(String responseStrData) {
        HttpResp httpResp = new HttpResp();
        JSONObject jsonObject = JSONObject.parseObject(responseStrData);
        if (jsonObject != null) {
            httpResp.setMessage(jsonObject.getString("message"));
            httpResp.setCode(jsonObject.getString("code"));
            httpResp.setSuccess(BooleanUtil.toBoolean(jsonObject.getOrDefault("success", "true").toString()));
            httpResp.setDataMap(JSON.parseObject(jsonObject.getString("data"), new TypeReference<List<Map<String,Object>>>() {}.getType()));
            if ("0000000".equals(httpResp.getCode())) {
                httpResp.setSuccess(true);
            }
        }
        return httpResp;
    }

}
