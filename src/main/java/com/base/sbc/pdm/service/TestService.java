/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.pdm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.sbc.config.common.base.BaseDao;
import com.base.sbc.config.common.base.BaseService;

import com.base.sbc.pdm.entity.Test;
import com.base.sbc.pdm.dao.TestDao;

/** 
 * 类描述：测试 service类
 * @address com.base.sbc.pdm.service.TestService
 * @author lile
 * @email lilemyemail@163.com
 * @date 创建时间：2023-2-24 14:48:25
 * @version 1.0  
 */
@Service
@Transactional(readOnly = true)
public class TestService extends BaseService<Test> {
	
	@Autowired
	private TestDao testDao;
	
	@Override
	protected BaseDao<Test> getEntityDao() {
		return testDao;
	}
	
}
