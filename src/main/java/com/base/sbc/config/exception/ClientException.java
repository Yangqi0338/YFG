package com.base.sbc.config.exception;

/**
 * 远程异常
 * @author xiong
 *
 */
public class ClientException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	private String data;

	public ClientException(int code,String message,String data) {
		super(String.valueOf(code));
		this.code = code;
		this.msg = message;
		this.data = data;
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
