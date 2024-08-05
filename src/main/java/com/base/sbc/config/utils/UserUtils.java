package com.base.sbc.config.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.client.oauth.service.OauthService;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.Constants;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.module.common.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Principal;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

/**
 * @author Fred
 * @data 创建时间:2020/2/3
 */
@Component
public class UserUtils {

	@Autowired
	private RedisUtils redisUtils;

	@Autowired
	private OauthService oauthService;

	public static final String USER_ID = "userId:";
	public static final String NAME = "name:";

	public static final String USER_INFO = "USER_INFO:";

	/**
	 * 获取当前登录用户所有础信息
	 * 岗位、部门、权限、菜单等
	 * @return 用户所有基础信息
	 */

	public UserInfo getUserInfo(){
			return null;
	}


	public GroupUser getUserBy(Principal user) {
		String userName = getUserId();
		if (user != null) userName = user.getName();
		GroupUser users = (GroupUser) redisUtils.get(USER_ID+userName);
		if(users==null) {
			String retomeResult = oauthService.getUserInfo();
			JSONObject jsonx = JSON.parseObject(retomeResult);
			String data =  jsonx.getJSONObject("data").toJSONString();
			if(jsonx.getBoolean(Constants.SUCCESS)) {
				users = (GroupUser) JsonUtils.jsonToBean(data, GroupUser.class);
				redisUtils.set(USER_ID+userName, users);
			}
		}
		return users;
	}

	/**
	 * 获取当前登录用户信息，传入参数必须为当前用户id，否则写入redis后，因为id值不一致可能会导致数据错乱
	 * @param userId  当前用户id
	 */
	public GroupUser getUser(String userId){
		GroupUser user = (GroupUser)redisUtils.get(USER_INFO +userId);
		if (user==null){
			String retomeResult = oauthService.getUserInfo();
			JSONObject jsonx = JSON.parseObject(retomeResult);
			String data =  jsonx.getJSONObject("data").toJSONString();
			if(jsonx.getBoolean(Constants.SUCCESS)) {
				user = (GroupUser) JsonUtils.jsonToBean(data, GroupUser.class);
				redisUtils.set(USER_INFO +userId, user);
			}
		}
		return user;
	}

	public String getUserId(){
		return this.getUserCompany().getUserId();
	}

	public String getAliasUserName(){
		return this.getUserCompany().getAliasUserName();
	}

	public String getCompanyCode(){
		if (this.getUserCompany()==null){
			return null;
		}
		return this.getUserCompany().getCompanyCode();
	}

	public UserCompany getUserCompany(){
		return companyUserInfo.get();
	}

	public String getDeptName(){
		return this.getUserCompany().getDeptName();
	}
}
