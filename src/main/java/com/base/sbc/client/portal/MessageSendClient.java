package com.base.sbc.client.portal;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.portal.bo.AuthLoginSSOResultBo;
import com.base.sbc.client.portal.bo.ResponseResultBo;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;

@Component
public class MessageSendClient {

    @Autowired
    private RedisUtils redisUtils;

    private static final Logger log = LoggerFactory.getLogger(MessageSendClient.class);

    /**
     * yfg portal 地址
     */
    private final String portalUrl = "http://portal-test.eifini.com";


    /**
     * portal 单点登录
     */
    private String authLoginSSO(){
        String authAccessTokenKey = "SSO_ACCESS_TOKEN";
        Object o = redisUtils.get(authAccessTokenKey);
        if (null != o){
            return (String) o;
        }
        Map<String, String> map = Maps.newHashMap();
        //默认账号
        map.put("tokenName","pdm");
        map.put("username","pdmUser");
        String post = HttpUtil.post(portalUrl + "/auth/loginSSO", JSON.toJSONString(map));
        ResponseResultBo result = JSON.parseObject(post, ResponseResultBo.class);

        if (200 != result.getCode()){
            log.error("authLoginSSO error body = {}",post);
            throw new OtherException("portal 登录异常："+result.getMsg());
        }
        AuthLoginSSOResultBo authLoginSSOResultBo = JSON.parseObject(JSON.toJSONString( result.getData()),AuthLoginSSOResultBo.class);

        String accessToken =  authLoginSSOResultBo.getToken().getAccess_token();
        redisUtils.set(authAccessTokenKey,accessToken,190);
        return accessToken;

    }

    /**
     * 钉钉，ding消息
     * @param businesstype 业务类型
     * @param content 发送内容
     * @param userNames 工号
     */
    public void dingMsg(String businesstype, String content, List<String> userNames){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("businesstype",businesstype);
        jsonObject.put("requestsysid","pdm");
        jsonObject.put("token","0ae69101d3b4423b9aa632151d45b1f1");

        JSONObject xxtzDingMsgDto = new JSONObject();
        xxtzDingMsgDto.put("content",content);
        jsonObject.put("xxtzDingMsgDto",xxtzDingMsgDto);

        List<JSONObject> userNameList = Lists.newArrayList();
        userNames.forEach(item ->{
            JSONObject userName = new JSONObject();
            userName.put("userName",item);
            userNameList.add(userName);
        });
        jsonObject.put("xxtzTaskUsers",userNameList);

        HttpRequest request = HttpUtil.createPost(portalUrl+"/message/task/call").header("Authorization",authLoginSSO());

        request.body(JSON.toJSONString(jsonObject));
        String body = request.execute().body();

        ResponseResultBo result = JSON.parseObject(body, ResponseResultBo.class);
        if (200 != result.getCode()){
            log.error("dingMsg error body = {}",body);
            throw new OtherException("portal 登录异常："+result.getMsg());
        }
    }


    public static void main(String[] args) {
        MessageSendClient messageSendClient = new MessageSendClient();
        messageSendClient.dingMsg("测试","测试11",Lists.newArrayList("1104285"));
    }




}
