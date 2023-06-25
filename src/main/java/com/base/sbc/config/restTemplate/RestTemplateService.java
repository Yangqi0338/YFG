package com.base.sbc.config.restTemplate;


import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
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
public class RestTemplateService {

    private final RestTemplate restTemplate;
    /**
     * smp系统对接post请求
     * @param url 请求地址
     * @param o 请求对象
     * @return 返回的结果
     */
    public Boolean spmPost(String url, Object o) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", "application/json");
        // 2.请求头 & 请求体
        HttpEntity<String> fromEntity = new HttpEntity<>(JSONUtil.toJsonStr(o), requestHeaders);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, fromEntity, String.class);
        if (stringResponseEntity.getStatusCodeValue() == 200) {
            String body = stringResponseEntity.getBody();
            JSONObject jsonObject = JSONObject.parseObject(body);
            return jsonObject.getInteger("code") == 10001 && jsonObject.getBoolean("success");
        }
        return false;
    }
}
