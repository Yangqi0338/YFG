/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.client.amc.entity;

import lombok.Data;

import java.util.Date;

/**
 * 类描述：用户 实体类
 * @address com.base.sbc.user.entity.User
 * @author 卞康
 * @email 247967116@qq.com
 * @date 创建时间：2023-6-5 21:10:00
 * @version 1.0
 */
@Data
public class User {

	private static final long serialVersionUID = 1L;
	public User(){
		super();
	};

    /** 账号 */
    private String username;
}

