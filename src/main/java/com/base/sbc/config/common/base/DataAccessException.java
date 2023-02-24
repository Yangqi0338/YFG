/******************************************************************************
 * Copyright (C) 2018 celizi.com 
 * All Rights Reserved.
 * 本软件为网址：celizi.com 开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.config.common.base;


/**
 * @Description 
 * @author shenzhixiong 
 * @date 2017-3-8 上午10:41:36
 * @version v1.0  
 */
@SuppressWarnings("serial")
public class DataAccessException extends RuntimeException {

    public DataAccessException(String msg) {
        super(msg); 
    }
    public DataAccessException(Throwable cause) {
        super(cause);
    }
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}

