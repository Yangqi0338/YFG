package com.base.sbc.config.exception;

import cn.hutool.http.HttpStatus;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.i18n.LocaleMessages;
import com.base.sbc.config.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
/**
 * @author Fred
 * @data 创建时间:2020/2/3
 */
@ApiIgnore
@RestController
public class FinalExceptionHandler implements ErrorController {

	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private LocaleMessages localeMessages;

	@Autowired
	private ErrorAttributes errorAttributes;

	@Override
	public String getErrorPath() {
		return "/error";
	}

	/**
	 * 主要拦截所有错误请求返回页面    变成Json
	 * 改变service异常的拦截
	 * @param resp
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/error")
	public ApiResult error(HttpServletResponse resp, HttpServletRequest request) throws IOException {
		Map<String, Object> errorAttributes = getErrorAttributes(request, false);
		Integer status = (Integer) errorAttributes.get("status");
		String path = (String) errorAttributes.get("path");
		String messageFound = (String) errorAttributes.get("message");
		log.info(status +"------------" + path+"------------" + messageFound);
		//缺少头部权限信息
		if(status==HttpStatus.HTTP_UNAUTHORIZED) {


			messageFound = BaseErrorEnum.ERR_LACK_HEAD_PARAMS_AUTHORIZATION.getErrorMessage();
			return ApiResult.error(localeMessages.getMessage(messageFound), BaseErrorEnum.valueOf(messageFound).getErrorCode());
		}
		if(status==HttpStatus.HTTP_INTERNAL_ERROR && StringUtils.isNoneBlank(messageFound)&& !messageFound.equals(localeMessages.getMessage(messageFound))) {
			//500 同时错误信息存在于国际化中，即已登记的错误
			return ApiResult.error(localeMessages.getMessage(messageFound), BaseErrorEnum.valueOf(messageFound).getErrorCode());
		}
		// 错误处理逻辑
		return ApiResult.error(messageFound, status, path);
	}

	private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
		WebRequest requestAttributes = new ServletWebRequest(request);
		return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
	}
}
