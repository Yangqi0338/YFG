package com.base.sbc.config.adviceadapter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.Ip2regionAnalysis;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.i18n.LocaleMessages;
import com.base.sbc.module.httplog.entity.HttpLog;
import com.base.sbc.module.httplog.service.HttpLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Collection;

import static com.base.sbc.client.amc.service.AmcFeignService.userPlanningSeasonId;

/**
 * 统一返回
 *
 * @author xiong
 * @email 731139982@qq.com
 * @date 2022年7月4日
 */
@RestControllerAdvice(basePackages = {"com.base.sbc"})
@RequiredArgsConstructor
public class ResponseControllerAdvice implements ResponseBodyAdvice<Object> {


    private final LocaleMessages localeMessages;

    private final HttpLogService httpLogService;

    public static ThreadLocal<UserCompany> companyUserInfo = new TransmittableThreadLocal<>();


    /**
     * 操作成功
     */
    public static final String SUCCESS_OK = "SUCCESS_OK";
    /**
     * 操作异常
     */
    public static final String SUCCESS_ERR = "SUCCESS_ERR";
    /**
     * 找不到数据
     */
    public static final String ERR_SELECT_NOT_FOUND = "ERR_SELECT_NOT_FOUND";

    /**
     * 过滤不需要的返回结果
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果接口返回的类型本身就是Result那就没有必要进行额外的操作，返回false,
//		return !returnType.getGenericParameterType().equals(Result.class);
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        ApiResult apiResult = ApiResult.success(localeMessages.getMessage(SUCCESS_OK), body);

        if (body == null) {
            apiResult= ApiResult.error(localeMessages.getMessage(ERR_SELECT_NOT_FOUND), 404);
        }
        if (body instanceof ApiResult) {
            // 进行国际化处理
            ApiResult result = (ApiResult) body;
            result.setMessage(localeMessages.getMessage(result.getMessage()));
            apiResult = result;
        } else if (body instanceof String) {
            // 这样写远程调用没办法转json
            //  return JsonUtils.beanToJson(ApiResult.success(localeMessages.getMessage(SUCCESS_OK), body));
            apiResult = ApiResult.success(localeMessages.getMessage(SUCCESS_OK), body);
        } else if (body instanceof Boolean) {
            if (body.equals(true)) {
                apiResult = ApiResult.success(localeMessages.getMessage(SUCCESS_OK), true, null);
            } else {
                apiResult = ApiResult.error(localeMessages.getMessage(SUCCESS_ERR), 500, null);
            }
        }
        try {
            this.preHttpLog(request,response, apiResult);
        }catch (Exception e){
            this.preHttpLog(request,response, body);
            e.printStackTrace();
        }

        return apiResult;
    }

    /**
     * 记录请求信息
     *
     * @param request 请求头
     * @param response 响应头
     * @param body     返回响应对象
     */

    private void preHttpLog(ServerHttpRequest request, ServerHttpResponse response, Object body) {
        //记录请求信息
        HttpLog httpLog = companyUserInfo.get().getHttpLog();
        if (httpLog==null){
            httpLog =new HttpLog();
        }
        try {

            URI uri = request.getURI();

            httpLog.setMethod(request.getMethod().toString());
            httpLog.setUrl(request.getURI().toString());
            httpLog.setIp(uri.getHost());
            httpLog.setAddress(Ip2regionAnalysis.getStringAddressByIp(uri.getHost()));
            httpLog.setReqHeaders(JSON.toJSONString(request.getHeaders()));

            //记录响应信息
            HttpServletResponse httpServletResponse = ((ServletServerHttpResponse)response).getServletResponse();
            String jsonString = JSON.toJSONString(body);
            if (jsonString.length()<20000){
                httpLog.setRespBody(JSON.toJSONString(body));
            }
            JSONObject headers=new JSONObject();
            Collection<String> headerNames = httpServletResponse.getHeaderNames();
            for (String headerName : headerNames) {
                headers.put(headerName,httpServletResponse.getHeader(headerName));
            }

            httpLog.setRespHeaders(headers.toJSONString());
            httpLog.setStatusCode(httpServletResponse.getStatus());
            httpLog.setIntervalNum(System.currentTimeMillis() - httpLog.getStartTime().getTime());
            httpLog.setExceptionFlag(0);
            try {
                // System.out.println(JSON.parseObject(jsonString));
                if (!JSON.parseObject(jsonString).getBoolean("success")){
                    httpLog.setExceptionFlag(1);
                    httpLog.setStatusCode(JSON.parseObject(jsonString).getInteger("status"));
                    httpLog.setThrowableException(JSON.parseObject(jsonString).getString("message"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
         e.printStackTrace();
        }finally {
            httpLogService.save(httpLog);
            companyUserInfo.remove();
            userPlanningSeasonId.remove();
        }


    }



}
