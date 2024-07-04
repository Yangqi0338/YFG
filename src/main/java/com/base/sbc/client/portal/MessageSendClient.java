package com.base.sbc.client.portal;


import com.alibaba.fastjson.JSON;
import com.base.sbc.client.portal.bo.AuthLoginSSOResultBo;
import com.base.sbc.client.portal.bo.ResponseResultBo;
import com.base.sbc.config.exception.OtherException;
import com.google.common.collect.Maps;

import java.util.Map;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;

public class MessageSendClient {

    /**
     * yfg portal 地址
     */
    private final String portalUrl = "http://portal-test.eifini.com";


    /**
     * portal 单点登录
     */
    private void authLoginSSO(){
        Map<String, String> map = Maps.newHashMap();
        //默认账号
        map.put("tokenName","pdm");
        map.put("username","pdmUser");
        String post = HttpUtil.post(portalUrl + "/auth/loginSSO", JSON.toJSONString(map));
        ResponseResultBo<AuthLoginSSOResultBo> result = JSON.parseObject(post, ResponseResultBo.class);

        if (200 != result.getCode()){

            throw new OtherException("portal 登录异常："+result.getMsg());
        }


    }

    public static void main(String[] args) {

//        Map<String, String> map = Maps.newHashMap();
//        //默认账号
//        map.put("tokenName","pdm");
//        map.put("username","pdmUser");
//        String post = HttpUtil.post("http://portal-test.eifini.com" + "/auth/loginSSO", JSON.toJSONString(map));
//
//        JSONObject jsonObject = JSON.parseObject(post);
//        JSONObject data = (JSONObject) jsonObject.get("data");
//        JSONObject token = (JSONObject) data.get("token");
//        String access_token = (String) token.get("access_token");
//        System.out.println(access_token);


        HttpRequest request = HttpUtil.createPost("http://portal-test.eifini.com/message/task/call");
        request.header("Authorization","fc1070ff-19b0-4ef3-9c95-fbf62d9f7280");
        request.body(getBody());
        HttpResponse execute = request.execute();
        String body = execute.body();
        System.out.println(body);


    }


    public static String  getBody (){
        return "{\n" +
                "  \"businesstype\": \"测试\",\n" +
                "  \"requestsysid\": \"pdm\",\n" +
                "  \"token\": \"0ae69101d3b4423b9aa632151d45b1f1\",\n" +
                "  \"xxtzDingMsgDto\": {\n" +
                "    \"content\": \"测试测试测试测试\"\n" +
                "  },\n" +
                "  \"xxtzTaskUsers\": [\n" +
                "    {\n" +
                "      \"userName\": \"1104285\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

}
