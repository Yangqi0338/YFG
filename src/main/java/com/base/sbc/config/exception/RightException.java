package com.base.sbc.config.exception;

import com.base.sbc.config.enums.BaseErrorEnum;
import lombok.Data;

/**
 * 头部信息缺少 异常 声明异常 service的异常
 * 
 * @author xiong
 *
 */
@Data
public class RightException extends RuntimeException {
	/**
	 *
	 */
	private int code;

	public RightException(String s) {
		super(s);
	}
	public RightException(BaseErrorEnum err) {
		super(err.getErrorMessage());
		this.code = err.getErrorCode();
	}

}
