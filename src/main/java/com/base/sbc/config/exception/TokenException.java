package com.base.sbc.config.exception;

import com.base.sbc.config.enums.BaseErrorEnum;

/**
 * 令牌异常
 * @author fred
 *
 */
public class TokenException extends RuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	private String data;

	public TokenException(BaseErrorEnum err) {
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

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	

}
