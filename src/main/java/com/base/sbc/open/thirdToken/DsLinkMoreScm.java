package com.base.sbc.open.thirdToken;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.config.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 笛莎领猫scm对接实体
 *
 * @author Liu YuBin
 * @version 1.0
 * @date 2023/8/6 17:13
 */
@Component
public class DsLinkMoreScm {


    @Autowired
    public RedisUtils redisUtils;

    /**测试环境：appId*/
    public static final String TEST_APP_ID = "89AE4542-F838-EB11-8DBB-B808A01B368F";
    /**测试环境：appSecret*/
    public static final String TEST_APP_SECRET = "test";
    /**测试环境：token获取接口*/
    public static final String TEST_TOKEN_URL = "http://120.55.193.39:9777/auth/token";
    /**测试环境*/
    public static final String TEST_URL = "http://120.55.193.39:9777/";


    /**正式环境：appId*/
    public static final String APP_ID = "89AE4542-F838-EB11-8DBB-B808A01B368F";
    /**正式环境：appSecret*/
    public static final String APP_SECRET = "test";
    /**正式环境：token获取*/
    public static final String TOKEN_URL = "http://open.scm321.com/auth/token";
    /**正式环境：https://gw.scm321.com （建议使用，以后会逐步向这个方向迁移）除授权接口外，其它接口需拼接'/openapi'路径*/
    public static final String URL = "http://open.scm321.com";


    /**
     * 正式环境获取token
     * @return
     */
    public String getAuthTokenOrSign() {
        //1.从redis中获取
        String token = (String) redisUtils.get("DS_TOKEN");
        if (StringUtils.isNotBlank(token)) {
            return token;
        }

        //2.远程获取
        HttpResponse response = HttpRequest.post(TEST_TOKEN_URL).body(getTokenParam(TEST_APP_ID,TEST_APP_SECRET))
                .header("Content-Type", "application/json")
                .timeout(100000).execute();
        token = DsLinkMoreScm.changeJson(response.body());

        //3.放入token
        redisUtils.set("DS_TOKEN", token, 60*60);
        return token;
    }

    /**
     * 请求领猫接口
     * @param url
     * @param reqArgs
     * @return
     */
    public HttpResponse sendToLinkMore(String url, String reqArgs){
        String token = (String) redisUtils.get("DS_TOKEN");
        if (StringUtils.isBlank(token)) {
            token = getAuthTokenOrSign();
        }

        HttpResponse result = HttpRequest.post(TEST_URL+url).body(reqArgs)
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .timeout(100000).execute();
        return result;
    }

    /**
     * 组装请求参数
     *
     * @param appId
     * @param appSecret
     * @return
     */
    public static String getTokenParam(String appId, String appSecret) {
        return "{\"appId\":\"" + appId + "\",\"appSecret\":\"" + appSecret + "\"}";
    }

    /**
     * 特定JSON转换
     *
     * @param json
     * @return
     */
    public static String changeJson(String json) {
        String result = "";
        JSONObject jsonObject = JSON.parseObject(json);
        if (jsonObject != null && jsonObject.getBoolean("isSuccess")) {
            //放入redis中
            result = "Bearer " + jsonObject.getJSONObject("data").get("token").toString();
        }
        return result;
    }


    /**
     * 校验失败的数据
     * @param errorList
     * @param body
     * @param code
     * @return
     */
    public List<String> checkAndReturn(List<String> errorList, String body, String code){
        if (StringUtils.isNotBlank(body)){
            JSONObject jsonObject = JSONObject.parseObject(body);
            try {
                int status = jsonObject.getInteger("code");
                if (status != 200){
                    errorList.add(code);
                }
            }catch (Exception e){
                errorList.add(code);
            }
        }else{
            errorList.add(code);
        }
        return errorList;
    }
}
