package com.base.sbc.config.utils;

import java.security.Principal;

import com.base.sbc.config.constant.Constants;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.client.oauth.service.OauthService;
import com.base.sbc.config.redis.RedisUtils;
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
	
	public String getUserIdBy(Principal user) {
		String userName = user.getName();
		String userId = (String)redisUtils.get(USER_ID+userName);
		if(StringUtils.isBlank(userId)) {
			String retomeResult = oauthService.getUserInfo();
			JSONObject jsonx = JSON.parseObject(retomeResult);
			String data =  jsonx.getJSONObject("data").toJSONString(); 
			if(jsonx.getBoolean(Constants.SUCCESS)) {
				GroupUser gu = (GroupUser)JsonUtils.jsonToBean(data, GroupUser.class);
				redisUtils.set(USER_ID+userName, gu.getId());
				userId = gu.getId();
			}
		}
		return userId;
	}
	public GroupUser getUserBy(Principal user) {
		String userName = user.getName();
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
}
