package com.base.sbc.open.thirdToken;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.config.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    /**正式环境*/
    private final String PROD = "prod";
    /**测试环境*/
    private final String TEST = "test";
    /**当前服务器类型*/
    private String serverType;

    @Value("${requestConfig.serverType:prod}")
    public void initServerType(String serverType) {
        this.serverType = serverType;
    }

    /************************************************************正式环境start*******************************************************/

    /**正式环境：appId*/
    public static final String APP_ID = "A3A39524-A401-EC11-A0AA-F741DD6C0EE8";
    /**正式环境：appSecret*/
    public static final String APP_SECRET = "DAB3557E5BFB408CA083BAA7569DB23B";
    /**正式环境：token获取*/
//    public static final String TOKEN_URL = "http://open.scm321.com/auth/token";
    public static final String TOKEN_URL = "http://116.62.117.201:9477/auth/token";
    /**正式环境：https://gw.scm321.com （建议使用，以后会逐步向这个方向迁移）除授权接口外，其它接口需拼接'/openapi'路径*/
    public static final String URL = "http://116.62.117.201:9477/";

    /**
     * 正式环境获取token
     * @return
     */
    public String getAuthTokenOrSignProd() {
        //1.远程获取
        String token;
        HttpResponse response = HttpRequest.post(TOKEN_URL).body(getTokenParam(APP_ID,APP_SECRET))
                .header("Content-Type", "application/json")
                .timeout(100000).execute();
        token = DsLinkMoreScm.changeJson(response.body());

        //2.放入token
        redisUtils.set("DS_TOKEN", token, 60*60);
        return token;
    }

    /**
     * 请求领猫接口
     * @param url
     * @param reqArgs
     * @return
     */
    public HttpResponse sendToLinkMoreProd(String url, String reqArgs){
        String token = (String) redisUtils.get("DS_TOKEN");
        if (StringUtils.isBlank(token)) {
            token = getAuthTokenOrSign();
        }

        HttpResponse result = HttpRequest.post(URL+url).body(reqArgs)
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .timeout(100000).execute();
        return result;
    }

    /************************************************************正式环境end*******************************************************/
    /************************************************************测试环境start*******************************************************/

    /**测试环境：appId*/
    public static final String TEST_APP_ID = "89AE4542-F838-EB11-8DBB-B808A01B368F";
    /**测试环境：appSecret*/
    public static final String TEST_APP_SECRET = "test";
    /**测试环境：token获取接口*/
    public static final String TEST_TOKEN_URL = "http://120.55.193.39:9777/auth/token";
    /**测试环境*/
    public static final String TEST_URL = "http://120.55.193.39:9777/";

    /**
     * 测试环境获取token
     * @return
     */
    public String getAuthTokenOrSignTest() {
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
     * 请求领猫接口-test
     * @param url
     * @param reqArgs
     * @return
     */
    public HttpResponse sendToLinkMoreTest(String url, String reqArgs){
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
    /************************************************************测试环境end*******************************************************/
    /************************************************************通用start*******************************************************/

    /**
     * 获取token
     * @return
     */
    public String getAuthTokenOrSign() {
        if (this.PROD.equals(this.serverType)) {
            return getAuthTokenOrSignProd();
        } else if (this.TEST.equals(this.serverType)) {
            return getAuthTokenOrSignTest();
        } else {
            return getAuthTokenOrSignTest();
        }
    }

    /**
     * 请求领猫接口
     * @param url
     * @param reqArgs
     * @return
     */
    public HttpResponse sendToLinkMore(String url, String reqArgs) {
        if (this.PROD.equals(this.serverType)) {
            return sendToLinkMoreProd(url, reqArgs);
        } else if (this.TEST.equals(this.serverType)) {
            return sendToLinkMoreTest(url, reqArgs);
        } else {
            return sendToLinkMoreTest(url, reqArgs);
        }
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

    /**
     * 通用qw
     * @param qw
     * @param foreignId
     * @param idList
     * @param packType
     */
    public void openCommonQw(AbstractWrapper qw, String foreignId, List<String> idList, String packType) {
        qw.eq("del_flag", BaseGlobal.NO);
        if (StrUtil.isNotBlank(foreignId)) {
            qw.eq("foreign_id", foreignId);
        }
        if (StrUtil.isNotBlank(packType)) {
            qw.eq("pack_type", packType);
        }
        if (idList != null) {
            qw.in("foreign_id", idList);
        }
    }

    //测试推送
//    public String getAuthTokenOrSign() {
//        return getAuthTokenOrSignProd();
////        return getAuthTokenOrSignTest();
//    }
//    public HttpResponse sendToLinkMore(String url, String reqArgs) {
//        return sendToLinkMoreProd(url, reqArgs);
////        return sendToLinkMoreTest(url, reqArgs);
//    }
    /************************************************************通用end*******************************************************/

}
