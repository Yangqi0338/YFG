package com.base.sbc.config.exception;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.module.common.entity.HttpLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingResponseWrapper;


/**
 * 请求日志打印
 * @author Fred
 * @data 创建时间:2020/2/3
 */
@Component
public class RequestInterceptor implements HandlerInterceptor{


	Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3) throws Exception {
		//获取所有请求头
		JSONObject jsonObject =new JSONObject();
		Collection<String> headerNames = response.getHeaderNames();
		for (String headerName : response.getHeaderNames()) {
			String header = response.getHeader(headerName);
			jsonObject.put(headerName,header);
		}
		String headers = jsonObject.toJSONString();
		//获取请求体
		StringBuilder requestBody = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			requestBody.append(line);
		}

		//获取所有请求参数
		String parameter = JSON.toJSONString(request.getParameterMap());




		HttpLog httpLog =new HttpLog();
		httpLog.setType(2);
		httpLog.setRequestMethod(request.getMethod());
		httpLog.setRequestHeaders(headers);
		httpLog.setRequestUrl(request.getRequestURI());
		httpLog.setRequestBody(requestBody.toString());
		httpLog.setRequestParameters(parameter);



		// 处理响应内容
		//String responseBody = new String(responseContent, responseWrapper.getCharacterEncoding());
		Collection<String> responseHeaderNames = response.getHeaderNames();
		System.out.println(headerNames);
		for (String headerName : response.getHeaderNames()) {
			String header = response.getHeader(headerName);
			System.out.println(headerName+":"+header);
		}
		int status = response.getStatus();

		// ... 处理响应内容的逻辑
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
