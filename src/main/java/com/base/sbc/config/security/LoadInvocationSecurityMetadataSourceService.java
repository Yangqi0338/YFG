package com.base.sbc.config.security;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.BusinessException;
import com.base.sbc.config.utils.StringUtils;
/**
 * @author Fred
 * @data 创建时间:2020/2/3
 */
@Service
public class LoadInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {

	Logger log = LoggerFactory.getLogger(getClass());
	
	/** 分隔符 */
	public static final String V = "^";
	
	/** api */
	public static final String API = "/api/";
	
	/** saas */
	public static final String SAAS = "/saas/";
	
	/** token */
	public static final String TOKEN = "/token/";
	
	/** open */
	public static final String OPEN = "/open/";
	/** 空 */
	public static final String NULL = "null";

	/**
	 * 此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
	 * 返回空表示： 请求的权限再总权限里面没有。
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException  {
		HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();

		//应用归属企业ID
		String applyCompany = request.getHeader(BaseConstant.USER_COMPANY);

		//saas 头部需要包含userCompany  如果请求的是saas模块，直接转至decide 方法
		if(request.getRequestURI().indexOf(SAAS)!=-1) {
			//用户当前选择企业的ID
			String userCompany = request.getHeader(BaseConstant.USER_COMPANY);
			if(StringUtils.isBlank(userCompany)) {
				userCompany = request.getParameter(BaseConstant.USER_COMPANY);
			}
			if(StringUtils.isBlank(userCompany)) {
				throw new BusinessException(BaseErrorEnum.ERR_LACK_HEAD_PARAMS_USER_COMPANY);
			}
			Collection<ConfigAttribute> array = new ArrayList<>();
			String auth = request.getRequestURI()+V+request.getMethod();
			ConfigAttribute cfg  = new SecurityConfig(auth);
			array.add(cfg);
			
			log.info("\n 转发给decide方法，请求包含/saas/："+auth);
			return array;
		}
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
}
