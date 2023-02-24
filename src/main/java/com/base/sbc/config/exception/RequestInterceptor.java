package com.base.sbc.config.exception;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 * 请求日志打印
 * @author Fred
 * @data 创建时间:2020/2/3
 */
@Component
public class RequestInterceptor implements HandlerInterceptor{

	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3)
			throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
	    request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		Enumeration<String> enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();  
			log.info("请求参数:"+paraName+": "+request.getParameter(paraName));  
		}
		log.info("请求地址:"+request.getRequestURI());
		return true;
	}

}
