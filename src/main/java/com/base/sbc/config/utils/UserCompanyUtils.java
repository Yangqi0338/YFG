//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.base.sbc.config.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.oauth.service.OauthService;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.Constants;
import com.base.sbc.config.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Principal;
/**
 * @author xia5800
 * @data 创建时间:2021/4/6
 */
@Component
public class UserCompanyUtils {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private OauthService oauthService;
    public static final String USER_ID = "companyUser:";

    public UserCompanyUtils() {
    }

    /**
     * 获取企业用户信息
     *
     * @param user
     * @return
     */
    public UserCompany getCompanyUser(Principal user) {
        UserCompany userCompanys = new UserCompany();
        String retomeResult = this.oauthService.getCompanyUserInfoByUserName();
        JSONObject jsonx = JSON.parseObject(retomeResult);
        String data = jsonx.getJSONObject("data").toJSONString();
        if (jsonx.getBoolean(Constants.SUCCESS)) {
            userCompanys = (UserCompany) JsonUtils.jsonToBean(data, UserCompany.class);
        }
        return userCompanys;
    }

    /**
     * 获取企业用户信息
     * @return
     */
    public UserCompany getCompanyUser() {
        UserCompany userCompanys = new UserCompany();
        try {
            String retomeResult = this.oauthService.getCompanyUserInfoByUserName();
            JSONObject jsonx = JSON.parseObject(retomeResult);
            if (jsonx.getBoolean(Constants.SUCCESS)) {
                String data = jsonx.getJSONObject("data").toJSONString();
                userCompanys = (UserCompany) JsonUtils.jsonToBean(data, UserCompany.class);
            }
        }catch (Exception e){
            return null;
        }
        return userCompanys;
    }
}
