package com.base.sbc.config;

import com.alibaba.fastjson2.JSON;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.module.httplog.entity.HttpLog;
import com.base.sbc.module.httplog.service.HttpLogService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;


/**
 * 请求日志打印
 *
 * @author Fred
 * @data 创建时间:2020/2/3
 */
@RequiredArgsConstructor
@Component
public class RequestInterceptor implements HandlerInterceptor {


    Logger log = LoggerFactory.getLogger(getClass());

    private final HttpLogService httpLogService;



    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3) throws Exception {
        //HttpLog httpLog = companyUserInfo.get().getHttpLog();
        //httpLog.setStatusCode(response.getStatus());
        //
        //JSONObject jsonObject =new JSONObject();
        //for (String headerName : response.getHeaderNames()) {
        //    String header = response.getHeader(headerName);
        //    jsonObject.put(headerName,header);
        //}

        //BodyHttpServletRequestWrapper servletRequestWrapper =new BodyHttpServletRequestWrapper(request);
        ////获取所有请求头
        //JSONObject reqHeaders = new JSONObject();
        //Enumeration<String> headerNames = servletRequestWrapper.getHeaderNames();
        //while (headerNames.hasMoreElements()) {
        //    String headerName = headerNames.nextElement();
        //    String header = servletRequestWrapper.getHeader(headerName);
        //    reqHeaders.put(headerName, header);
        //}
        //
        //
        ////获取请求体
        //String reqBody = null;
        ////获取请求体
        //if (servletRequestWrapper.getHeader("content-type") != null && !servletRequestWrapper.getHeader("content-type").contains("multipart/form-data")) {
        //
        //    byte[] requestData = servletRequestWrapper.getRequestData();
        //    reqBody =new String(requestData,servletRequestWrapper.getCharacterEncoding());
        //}
        ////
        ////获取所有请求参数
        //String parameter = JSON.toJSONString(servletRequestWrapper.getParameterMap());
        //
        //
        //httpLog.setReqBody(reqBody);
        //httpLog.setMethod(servletRequestWrapper.getMethod());
        //httpLog.setUrl(servletRequestWrapper.getRequestURI());
        //httpLog.setType(2);
        //httpLog.setReqHeaders(reqHeaders.toJSONString());
        //httpLog.setReqQuery(parameter);
        //httpLog.setIp(servletRequestWrapper.getRemoteAddr());
        //String address = Ip2regionAnalysis.getStringAddressByIp(servletRequestWrapper.getRemoteAddr());
        //httpLog.setAddress(address);
        //httpLog.setRespHeaders(jsonObject.toJSONString());
        //httpLog.setIntervalNum(System.currentTimeMillis() - httpLog.getStartTime().getTime());
        //httpLogService.save(httpLog);
        //返回数据之前删除线程缓存
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3) {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws IOException {
        UserCompany userCompany = companyUserInfo.get();
        if (userCompany==null){
            userCompany=new UserCompany();
            userCompany.setAliasUserName("无token用户");
            userCompany.setUserId("0");
            userCompany.setCompanyName("0");
            companyUserInfo.set(userCompany);
        }

        HttpLog httpLog = new HttpLog();
        httpLog.setStartTime(new Date());
        httpLog.setType(2);
        httpLog.setReqContentType(request.getContentType());
                //multipart/form-data
        httpLog.setUserCode(userCompany.getUserCode());
        httpLog.setReqQuery(JSON.toJSONString(request.getParameterMap()));
        httpLog.setThreadId(Thread.currentThread().getId() + "");
        httpLog.setUserCode(userCompany.getUserCode());

        //获取swagger注解value值
        if(arg2 instanceof HandlerMethod) {
            Method method = ((HandlerMethod) arg2).getMethod();
            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            if(apiOperation != null) {
                String value = apiOperation.value();
                httpLog.setReqName(value);
            }
        }

        companyUserInfo.get().setHttpLog(httpLog);
        return true;
    }

}
