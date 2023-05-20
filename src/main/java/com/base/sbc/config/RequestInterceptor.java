package com.base.sbc.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.config.common.Ip2regionAnalysis;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.module.common.entity.HttpLog;
import com.base.sbc.module.common.service.HttpLogService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;


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

    public static ThreadLocal<UserCompany> companyUserInfo = new ThreadLocal<>();

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3) throws Exception {
        HttpLog httpLog = companyUserInfo.get().getHttpLog();
        httpLog.setStatusCode(response.getStatus());

        JSONObject jsonObject =new JSONObject();
        for (String headerName : response.getHeaderNames()) {
            String header = response.getHeader(headerName);
            jsonObject.put(headerName,header);
        }

        httpLog.setRespHeaders(jsonObject.toJSONString());
        httpLog.setIntervalNum(System.currentTimeMillis() - httpLog.getStartTime().getTime());
        httpLogService.save(httpLog);
        //返回数据之前删除线程缓存
        companyUserInfo.remove();
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
        //获取所有请求头
        JSONObject reqHeaders = new JSONObject();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String header = request.getHeader(headerName);
            reqHeaders.put(headerName, header);
        }

        //获取请求体
        StringBuilder requestBody = new StringBuilder();
        //获取请求体
        if (request.getHeader("content-type") != null && !request.getHeader("content-type").contains("multipart/form-data")) {
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }

        //获取所有请求参数
        String parameter = JSON.toJSONString(request.getParameterMap());

        HttpLog httpLog = new HttpLog();
        httpLog.setStartTime(new Date());
        httpLog.setThreadId(UUID.randomUUID().toString());
        httpLog.setReqBody(requestBody.toString());
        httpLog.setMethod(request.getMethod());
        httpLog.setUrl(request.getRequestURI());
        httpLog.setType(2);
        httpLog.setReqHeaders(reqHeaders.toJSONString());
        httpLog.setReqQuery(parameter);
        httpLog.setIp(request.getRemoteAddr());
        String address = Ip2regionAnalysis.getStringAddressByIp(request.getRemoteAddr());
        httpLog.setAddress(address);
        httpLog.setUserCode(userCompany.getUserCode());
        companyUserInfo.get().setHttpLog(httpLog);
        return true;
    }

}
