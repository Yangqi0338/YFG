/******************************************************************************
 * Copyright (C) 2018 celizi.com 
 * All Rights Reserved.
 * 本软件为网址：celizi.com 开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.config.generator.entity;

import com.base.sbc.config.generator.utils.UtilXml;

/** 
 * 类描述：
 * @address com.celizi.base.common.generator.entity.Constants
 * @author shenzhixiong  
 * @email 731139982@qq.com
 * @date 创建时间：2017年6月14日 下午1:57:21 
 * @version 1.0  
 */
public class Constants {
	public static final String DRIVER   = UtilXml.dataSource.getDriver();
	public static final String URL      = UtilXml.dataSource.getUrl();
	public static final String USER     = UtilXml.dataSource.getUsername();
	public static final String PASSWORD = UtilXml.dataSource.getPassword();
}
