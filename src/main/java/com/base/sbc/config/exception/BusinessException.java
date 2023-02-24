package com.base.sbc.config.exception;

import com.base.sbc.config.enums.BaseErrorEnum;

/**
 * 头部信息缺少 异常 声明异常 service的异常
 * 
 * @author xiong
 *
 */
public class BusinessException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;

	public BusinessException(BaseErrorEnum err) {
		super(err.getErrorMessage());
		this.code = err.getErrorCode();
		this.msg = err.getErrorMessage();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
