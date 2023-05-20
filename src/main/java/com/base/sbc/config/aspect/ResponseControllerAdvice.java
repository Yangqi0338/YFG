package com.base.sbc.config.aspect;

import com.alibaba.fastjson2.JSON;
import com.base.sbc.config.RequestInterceptor;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.i18n.LocaleMessages;
import com.base.sbc.module.common.entity.HttpLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一返回
 *
 * @author xiong
 * @email 731139982@qq.com
 * @date 2022年7月4日
 */
@RestControllerAdvice(basePackages = {"com.base.sbc"})
public class ResponseControllerAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private LocaleMessages localeMessages;

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
        this.preHttpLog(request,response, body);
        if (body == null) {
            return ApiResult.error(localeMessages.getMessage(ERR_SELECT_NOT_FOUND), 404);
        }
        if (body instanceof ApiResult) {
            // 进行国际化处理
            ApiResult result = (ApiResult) body;
            result.setMessage(localeMessages.getMessage(result.getMessage()));
            return result;
        } else if (body instanceof String) {
            // 这样写远程调用没办法转json
            //  return JsonUtils.beanToJson(ApiResult.success(localeMessages.getMessage(SUCCESS_OK), body));
            return ApiResult.success(localeMessages.getMessage(SUCCESS_OK), body);
        } else if (body instanceof Boolean) {
            if (body.equals(true)) {
                return ApiResult.success(localeMessages.getMessage(SUCCESS_OK), null);
            } else {
                return ApiResult.error(localeMessages.getMessage(SUCCESS_ERR), 404);
            }
        }
        return ApiResult.success(localeMessages.getMessage(SUCCESS_OK), body);
    }

    /**
     * 记录响应头信息
     *
     * @param request
     * @param response 响应头
     * @param body     返回响应对象
     */

    private void preHttpLog(ServerHttpRequest request, ServerHttpResponse response, Object body) {

        HttpLog httpLog = RequestInterceptor.companyUserInfo.get().getHttpLog();
        httpLog.setRespBody(JSON.toJSONString(body));
        //记录响应头
    }


}
