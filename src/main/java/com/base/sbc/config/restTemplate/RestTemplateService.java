package com.base.sbc.config.restTemplate;

import com.alibaba.fastjson.JSONObject;
import com.base.sbc.config.JSONStringUtils;
import com.base.sbc.module.smp.dto.HttpResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

    /**
     * smp系统对接post请求
     *
     * @param url 请求地址
     * @param o   请求对象
     * @return 返回的结果
     */
    public HttpResp spmPost(String url, Object o) {
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpResp httpResp = new HttpResp();
        try {
            requestHeaders.add("Content-Type", "application/json");
            // 2.请求头 & 请求体
            String jsonStr = JSONStringUtils.toJSONString(o);
            HttpEntity<String> fromEntity = new HttpEntity<>(jsonStr, requestHeaders);
            ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, fromEntity, String.class);

            httpResp.setUrl(url);
            httpResp.setStatusCode(String.valueOf(stringResponseEntity.getStatusCodeValue()));

            String body = stringResponseEntity.getBody();

            JSONObject jsonObject = JSONObject.parseObject(body);


            if (jsonObject != null) {
                httpResp.setMessage(jsonObject.getString("message"));
                httpResp.setCode(jsonObject.getString("code"));
                if (jsonObject.getBoolean("success") || "0000000".equals(jsonObject.getString("code"))){
                    httpResp.setSuccess(true);
                }else {
                    httpResp.setSuccess(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            httpResp.setSuccess(false);
        }
        return httpResp;
    }



}
