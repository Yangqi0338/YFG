package com.base.sbc.config.security;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.UserCompanyUtils;
import com.base.sbc.module.common.dto.VirtualDept;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import javax.servlet.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

/**
 * @author Fred
 * @data 创建时间:2020/2/3
 */
@Service
@Order(-1)
@RequiredArgsConstructor
public class UrlFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

	private final FilterInvocationSecurityMetadataSource securityMetadataSource;

	private final UserCompanyUtils userCompanyUtils;
	private final AmcService amcService;

	@Autowired
	public void setMyAccessDecisionManager(DecideAccessDecisionManager accessDecisionManager) {
		super.setAccessDecisionManager(accessDecisionManager);
	}

	@Override
	public void init(FilterConfig filterConfig) {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		FilterInvocation fi = new FilterInvocation(request, response, chain);
		invoke(fi);
	}

	public void invoke(FilterInvocation fi) throws IOException, ServletException {
		// fi里面有一个被拦截的url
		// 里面调用MyInvocationSecurityMetadataSource的getAttributes(Object
		// object)这个方法获取fi对应的所有权限
		// 再调用MyAccessDecisionManager的decide方法来校验用户的权限是否足够
		InterceptorStatusToken token = super.beforeInvocation(fi);
		try {
			//初始化用户数据
			this.initUserData();
			// 执行下一个拦截器
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());

		} finally {
			super.afterInvocation(token, null);

		}
	}
	@Override
	public void destroy() {

	}

	@Override
	public Class<?> getSecureObjectClass() {
		return FilterInvocation.class;
	}

	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.securityMetadataSource;
	}


	/**
	 * 初始化用户数据
	 */
	private void initUserData(){
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		UserCompany userCompany = userCompanyUtils.getCompanyUser();
		if (userCompany == null) {

			userCompany = new UserCompany();
			userCompany.setAliasUserName("无token用户");
			userCompany.setUserId("0");
			userCompany.setCompanyName("0");
			userCompany.setCompanyCode("0");
		}
		if (Objects.nonNull(authentication) && Objects.nonNull(authentication.getPrincipal())) {
			//当前登录者账号
			String username = authentication.getPrincipal().toString();
			userCompany.setUsername(username);
		}
		if(StrUtil.isNotBlank(userCompany.getUserId()) && !"0".equals(userCompany.getUserId())){
			ApiResult<List<VirtualDept>> virtualDeptByUserId = amcService.getVirtualDeptByUserId(userCompany.getUserId());
			List<VirtualDept> data = virtualDeptByUserId.getData();
			userCompany.setVirtualDeptIds(data.stream().map(VirtualDept::getId).collect(Collectors.toList()));
		}

		companyUserInfo.set(userCompany);
	}
}
