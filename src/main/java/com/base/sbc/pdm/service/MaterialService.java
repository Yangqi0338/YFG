/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.pdm.service;

import com.base.sbc.pdm.mapper.MaterialMapper;
import com.base.sbc.pdm.vo.MaterialAllDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.sbc.config.common.base.BaseDao;
import com.base.sbc.config.common.base.BaseService;

import com.base.sbc.pdm.entity.Material;
import com.base.sbc.pdm.dao.MaterialDao;

import javax.annotation.Resource;
import java.util.List;

/**
 * 类描述：素材库 service类
 * @address com.base.sbc.pdm.service.MaterialService
 * @author lile
 * @email lilemyemail@163.com
 * @date 创建时间：2023-3-24 16:26:15
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class MaterialService extends BaseService<Material> {

	@Autowired
	private MaterialDao materialDao;

	@Resource
	private MaterialMapper materialMapper;


	@Override
	protected BaseDao<Material> getEntityDao() {
		return materialDao;
	}

    public List<MaterialAllDto> listQuery(MaterialAllDto materialAllDto) {
		return materialMapper.listQuery(materialAllDto);
    }
}
