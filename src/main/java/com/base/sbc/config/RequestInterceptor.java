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
//        httpLog.setType(2);
        httpLog.setReqContentType(request.getContentType());
                //multipart/form-data
//        httpLog.setUserCode(userCompany.getUserCode());
        httpLog.setReqQuery(JSON.toJSONString(request.getParameterMap()));
        httpLog.setThreadId(Thread.currentThread().getId() + "");
//        httpLog.setUserCode(userCompany.getUserCode());

        //获取swagger注解value值
        if(arg2 instanceof HandlerMethod) {
            Method method = ((HandlerMethod) arg2).getMethod();
            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            if(apiOperation != null) {
                String value = apiOperation.value();
                httpLog.setReqName(value);
            }
        }

        companyUserInfo.get().setHttpLogId();
        return true;
    }

}
