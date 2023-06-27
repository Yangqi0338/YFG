package com.base.sbc.config.security;

import com.base.sbc.client.oauth.service.OauthService;
import com.base.sbc.config.constant.BaseConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
/**
 * @author Fred
 * @data 创建时间:2020/2/3
 */
@Service
public class DecideAccessDecisionManager implements AccessDecisionManager {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private OauthService oAuth2Service;
	/**
	 * decide 方法是判定是否拥有权限的决策方法， authentication 是释CustomUserService中循环添加到
	 * GrantedAuthority 对象中的权限信息集合. object 包含客户端发起的请求的requset信息，可转换为
	 * HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
	 * configAttributes 为MyInvocationSecurityMetadataSource的getAttributes(Object
	 * object)这个方法返回的结果，此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide
	 * 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
	 * 参数authentication是从spring的全局缓存SecurityContextHolder中拿到的，里面是用户的权限信息
	 * 参数object是url 参数configAttributes所需的权限
	 */
	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		// 如果请求所需权限为空（即 这里的请求没有saas路径的）
		if (null == configAttributes || configAttributes.size() <= 0) {
			return;
		}
		// 所需的地址 和 请求的方法
		String v = configAttributes.toString();
		String auths = v.substring(1, v.length() - 1);
		HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
		String companyId = request.getHeader(BaseConstant.USER_COMPANY);
		log.info("\n 当前用户企业ID ： "+ companyId);
			return;

	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
}
