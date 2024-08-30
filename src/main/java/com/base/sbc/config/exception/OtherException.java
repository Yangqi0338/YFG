package com.base.sbc.config.exception;

import com.base.sbc.config.enums.BaseErrorEnum;

/**
 * 类描述： 自定义异常
 * @address com.base.sbc.config.exception.OtherException
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2022-03-01 17:05
 * @version 1.0
 */
public class OtherException  extends RuntimeException {
    private int code;

    public OtherException(String s) {
        super(s);
    }
    public OtherException(BaseErrorEnum err) {
        super(err.getErrorMessage());
        this.code = err.getErrorCode();
    }

    public OtherException(String s,int code) {
        super(s);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
