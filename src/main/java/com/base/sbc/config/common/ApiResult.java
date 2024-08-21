package com.base.sbc.config.common;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * 项目统一返回对象
 * @author xiong
 *
 */
@Data
public class ApiResult<T>  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static final int SUCCESS = 0;
	
	/** 是否成功 默认：true */
	private Boolean success;
	
	/** 状态码 */
	private int status;
	
	/** 提示信息  */
	private String message; 
	
	/** 返回对象(公用规则验证对象,如不为空,则提醒) */
	private T data = null;
	
	/** 返回其他参数(导入数据重复或在数据中未查询到提醒对象，如不为空，则提醒) */
	private Map<String, Object> attributes;

	private Map<String, String> messageObjects;


	public Map<String, Object> setAttribute(String key, Object value) {
		if (attributes == null) {
			attributes = Maps.newHashMap();
		}
		attributes.put(key, value);
		return attributes;
	}

	/**
	 *  请求错误
	 * @param msg
	 * @return
	 */
	public static <T> ApiResult<T>  error(String msg) {
		ApiResult<T> result = new ApiResult<>();
		result.setSuccess(Boolean.FALSE);
		result.setStatus(400);
		result.setMessage(msg);
		return result;
	}

	/**
	 * 请求成功/错误
	 * @param status
	 * @return
	 */
	public static <T> ApiResult<T>  status(Boolean status) {
		if (status) {
			return ApiResult.success("操作成功");
		} else {
			return ApiResult.error("操作失败");
		}
	}

	/**
	 *  请求错误
	 * @param msg
	 * @return
	 */
	public static <T> ApiResult<T>  error(String msg,int code) {
		ApiResult<T> result = new ApiResult<>();
		result.setSuccess(Boolean.FALSE);
		result.setStatus(code);
		result.setMessage(msg);
		return result;
	}
	
	/**
	 *  请求错误 带数据
	 * @param msg
	 * @param data
	 * @return
	 */
	public static <T> ApiResult<T> error(String msg, int code, T data) {
		ApiResult<T> result = new ApiResult<>();
		result.setSuccess(Boolean.FALSE);
		result.setMessage(msg);
		result.setStatus(code);
		result.setData(data);
		return result;
	}
	
	/**
	 * 请求错误
	 * @param msg
	 * @param attributes 键值对数据
	 * @return
	 */
	public static <T> ApiResult<T> error(String msg,int code, Map<String, Object> attributes) {
		ApiResult<T> result = new ApiResult<>();
		result.setSuccess(Boolean.FALSE);
		result.setMessage(msg);
		result.setStatus(code);
		result.setAttributes(attributes);
		return result;
	}


	/**
	 * 请求成功
	 *
	 * @param data 返回数据
	 * @return
	 */
	public static <T> ApiResult<T> data(T data) {
		ApiResult<T> result = new ApiResult<>();
		result.setSuccess(Boolean.TRUE);
		result.setStatus(ApiResult.SUCCESS);
		result.setMessage("操作成功");
		result.setData(data);
		return result;
	}
	
	/**
	 * 请求成功
	 * @return
	 */
	public static <T> ApiResult<T> success() {
		ApiResult<T> result = new ApiResult<>();
		result.setSuccess(Boolean.TRUE);
		result.setStatus(ApiResult.SUCCESS);
		return result;
	}
	
	/**
	 * 请求成功  带提示信息
	 * @param msg 提示信息
	 * @return
	 */
	public static <T> ApiResult<T> success(String msg) {
		ApiResult<T> result = new ApiResult<>();
		result.setSuccess(Boolean.TRUE);
		result.setStatus(ApiResult.SUCCESS);
		result.setMessage(msg);
		return result;
	}

	/**
	 * 请求成功
	 * @param msg 提示信息
	 * @param data 返回的数据    表格或者实体 或者list等
	 * @return
	 */
	public static <T> ApiResult<T> success(String msg, T data) {
		ApiResult<T> result = new ApiResult<>();
		result.setSuccess(Boolean.TRUE);
		result.setStatus(ApiResult.SUCCESS);
		result.setMessage(msg);
		result.setData(data);
		return result;
	}
	
	/**
	 * 请求成功
	 * @param msg 提示信息
	 * @param attributes 附加键值对参数
	 * @return
	 */
	public static <T> ApiResult<T> success(String msg, Map<String, Object> attributes) {
		ApiResult<T> result = new ApiResult<>();
		result.setSuccess(Boolean.TRUE);
		result.setStatus(ApiResult.SUCCESS);
		result.setMessage(msg);
		result.setAttributes(attributes);
		return result;
	}
	
	
	/**
	 * 请求成功
	 * @param msg 提示信息
	 * @param attributes 附加键值对参数
	 * @return
	 */
	public static <T> ApiResult<T> success(String msg, T data, Map<String, Object> attributes) {
		ApiResult<T> result = new ApiResult<>();
		result.setSuccess(Boolean.TRUE);
		result.setStatus(ApiResult.SUCCESS);
		result.setMessage(msg);
		result.setData(data);
		result.setAttributes(attributes);
		return result;
	}

	/**
	 * 请求成功
	 * @param data 提示信息
	 * @param messageObjects 附加键值
	 * @return
	 */
	public static <T> ApiResult<T> successMessage(T data, Map<String, String> messageObjects) {
		ApiResult<T> result = new ApiResult<>();
		result.setSuccess(Boolean.TRUE);
		result.setStatus(ApiResult.SUCCESS);
		result.setData(data);
		result.setMessageObjects(messageObjects);
		return result;
	}
}
