package com.base.sbc.config.exception;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.constant.Constants;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.i18n.LocaleMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Fred
 * @data 创建时间:2020/2/3
 */
@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);
    
    @Autowired
	private LocaleMessages localeMessages;
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ApiResult handleMissingServletRequestPartExceptionHandler(MissingServletRequestPartException e) {
        logger.error("请求缺少部分(检查文件名称、内容)", e);
        return error(BaseErrorEnum.ERR_MISSING_SERVLET_REQUEST_PART_EXCEPTION.getErrorMessage(),e.getMessage());
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResult handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        logger.error("缺少请求参数(检查参数名称、内容)", e);
        return error(BaseErrorEnum.ERR_MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION.getErrorMessage(),e.getMessage());
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("参数解析失败(检查参数内容格式)", e);
        return error(BaseErrorEnum.ERR_HTTP_MESSAGE_NOT_READABLE_EXCEPTION.getErrorMessage(),e.getMessage());
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        StringBuilder sb=new StringBuilder();
        for (FieldError error : fieldErrors) {
            // String field = error.getField();
            String code = error.getDefaultMessage();
            sb.append(String.format("%s;", code));
        }
        logger.error("参数验证失败(@Valid对应实体类验证异常)", e);
//        return error(BaseErrorEnum.ERR_METHOD_ARGUMENT_NOT_VALID_EXCEPTION.getErrorMessage(),message);
        return ApiResult.error(sb.toString(),BaseErrorEnum.ERR_METHOD_ARGUMENT_NOT_VALID_EXCEPTION.getErrorCode());
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResult handleServiceException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        Set<String> messages = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
        String message = CollUtil.join(messages, StrUtil.COMMA);
        logger.error("参数验证失败(@Validated验证实体的异常)", e);
//        return error(BaseErrorEnum.ERR_CONSTRAINT_VIOLATIONEXCEPTION.getErrorMessage(),message);
        return ApiResult.error(message, BaseErrorEnum.ERR_CONSTRAINT_VIOLATIONEXCEPTION.getErrorCode());
    }

    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ApiResult handleValidationException(ValidationException e) {
    	 logger.error("参数验证失败(javax基础验证异常)", e);
         return error(BaseErrorEnum.ERR_VALIDATION_EXCEPTION.getErrorMessage(),e.getMessage());
    }
 
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResult handleBindException(BindException e) {
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        List<FieldError> fieldErrors = result.getFieldErrors();
        String message = fieldErrors.stream().map(fieldError -> String.format("%s:%s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining(StrUtil.COMMA));
        logger.error("参数绑定失败(检查参数名称)", e);
//        return error(BaseErrorEnum.ERR_BIND_EXCEPTION.getErrorMessage(),message);
        return ApiResult.error(message, BaseErrorEnum.ERR_BIND_EXCEPTION.getErrorCode());
    }
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResult noHandlerFoundException(NoHandlerFoundException e) {
    	 logger.error(" API地址不存在(检查请求地址)", e);
         return error(BaseErrorEnum.ERR_NO_HANDLER_FOUND_EXCEPTION.getErrorMessage());
    }
    
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
    	 logger.error("不支持当前请求方法(检查请求方法)", e);
         return error(BaseErrorEnum.ERR_HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION.getErrorMessage());
    }
    
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ApiResult handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        logger.error("不支持当前媒体类型(检查下请求头contentType)", e);
        return error(BaseErrorEnum.ERR_HTTP_MEDIA_TYPE_NOT_SUPPORTED_EXCEPTION.getErrorMessage());
    }
    
    
    @ExceptionHandler(ClientException.class)
    public ApiResult handleClientException(ClientException e) {
        logger.error("远程服务器返回异常", e);
        return error(BaseErrorEnum.ERR_INTERNAL_SERVER_ERROR.getErrorMessage());
    }
    @ExceptionHandler(TokenException.class)
    public ApiResult handleClientException(TokenException e) {
        logger.error("头部令牌异常", e);
        return error(BaseErrorEnum.ERR_LACK_PARAMS_TOKEN.getErrorMessage());
    }
    @ExceptionHandler(TokenTimeException.class)
    public ApiResult handleClientException(TokenTimeException e) {
        logger.error("令牌过期异常", e);
        return error(BaseErrorEnum.ERR_LACK_HEAD_PARAMS_TOKEN_EXPIRATION.getErrorMessage());
    }
    
    @ExceptionHandler(UndeclaredThrowableException.class)
    public ApiResult handleUndeclaredThrowableException(UndeclaredThrowableException e) {
        logger.error("未声明的抛出异常", e);
        return ApiResult.error(e.getLocalizedMessage(), 500, appendException(e));
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ApiResult handleClientException(RuntimeException e) {
        logger.error("运行时抛出异常", e);
        return ApiResult.error(e.getLocalizedMessage(), 500, appendException(e));
    }

    @ExceptionHandler(OtherException.class)
    public ApiResult handleOtherException(OtherException e) {
        logger.error("运行时抛出异常-OtherException", e);
        return error(e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ApiResult handleBusinessException(BusinessException e) {
        logger.error("业务异常-OtherException", e);
        return error(e.getMsg());
    }

    /**
     * 格式化错误返回值
     *
     * @param e
     * @return
     */
    private String appendException(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Constants.FOUR; i++) {
            StackTraceElement row = e.getStackTrace()[i];
            sb.append(row.getClassName()).append(".").append(row.getMethodName())
                    .append("(").append(row.getLineNumber()).append(")").append("\n ");
        }
        return sb.toString();
    }
    
    
   /**
    * 其他未知未定义异常
    * @param e
    * @return
    */
   @ExceptionHandler(value = Exception.class)
   private <T> ApiResult defaultErrorHandler(Exception e) {
	   logger.error("\n ---------> 未定义异常!", e);
       return ApiResult.error(e.getLocalizedMessage(), 500, appendException(e));
   }
   
   /**
    * 国际化
    * @param baseErrorEnumMessage
    * @return
    */
   private ApiResult error(String baseErrorEnumMessage) {
       boolean contains = EnumUtil.contains(BaseErrorEnum.class, baseErrorEnumMessage);
       if(contains){
           BaseErrorEnum bre = BaseErrorEnum.valueOf(baseErrorEnumMessage);
           return ApiResult.error(localeMessages.getMessage(bre.getErrorMessage()),bre.getErrorCode());
       }
      return ApiResult.error(baseErrorEnumMessage,500);
   }
   /**
    * 国际化
    * @param baseErrorEnumMessage 错误提示
    * @param object  具体错误信息
    * @return
    */
   private ApiResult error(String baseErrorEnumMessage,Object object) {
	   BaseErrorEnum bre = BaseErrorEnum.valueOf(baseErrorEnumMessage);
	   return ApiResult.error(localeMessages.getMessage(bre.getErrorMessage()),bre.getErrorCode(),object);
   }
    
}
