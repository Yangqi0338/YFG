package com.base.sbc.config;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 *
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2021年9月4日 上午12:58:25
 * @version 1.0
 */
@Data
public final class ContextHolder {

	private static ThreadLocalContext context = new ThreadLocalContext();

	/** 用户编码 */
	private String userCode;

	/** 团队编码 */
	private String teamCode;

	/** 线程Id */
	private String threadId;

	/** IP地址 */
	private String ip;

	/** 物理地址 */
	private String address;

	/** 文档名称 */
	private String requestName;

	/** 权限编码 */
	private String authCode;

	/** 菜单权限名称 */
	private String authName;

	/** 开始时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date startTime;

	/** 持续时间ms */
	private Long intervalNum;

	/** 接口地址 */
	private String url;

	/** 方法类型 */
	private String method;

	/** 请求body */
	private String requestBody;

	/** 响应数据 */
	private String responseBody;


	/** 是否异常(0正常1异常) */
	private boolean exceptionFlag;

	/** 异常信息 */
	private Throwable throwableException;

	/** 请求url的参数 */
	private Map<String, String> requestQueryMap;
	/** 请求的cookie */
	private Map<String, String> cookieMap;
	/** 请求的头部信息 */
	private Map<String, String> headerMap;
	/** 请求sql */
	private Map<String, Object> sqlDataMap;
	/** 业务具体日志 */
	private Map<String, Object> businessDataMap;

	private ContextHolder() {
		this.requestQueryMap = new ConcurrentHashMap<String, String>();
		this.cookieMap = new ConcurrentHashMap<String, String>();
		this.headerMap = new ConcurrentHashMap<String, String>();
		this.sqlDataMap = new ConcurrentHashMap<String, Object>();
		this.businessDataMap = new ConcurrentHashMap<String, Object>();
	}

	public static ContextHolder ctx() {
		return context.get();
	}

	/**
	 * 清空线程
	 */
	public static void remove() {
		context.remove();
	}

	public static void setContext(ContextHolder contextHolder) {
		context.set(contextHolder);
	}

	public static ThreadLocalContext getContext() {
		return context;
	}

	/**
	 * 放入业务数据
	 *
	 * @param name
	 * @param businessData
	 * @return
	 */
	public ContextHolder setBusinessData(String name, Object businessData) {
		if(businessData!=null) {
			this.businessDataMap.put(name, businessData);
		}else {
			this.businessDataMap.put(name, "null");
		}
		return this;
	}

	private static class ThreadLocalContext extends ThreadLocal<ContextHolder> {

		private ThreadLocalContext() {
		}

		@Override
		protected ContextHolder initialValue() {
			return new ContextHolder();
		}
	}
}
