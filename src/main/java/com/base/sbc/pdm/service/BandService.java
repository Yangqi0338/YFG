/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.pdm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.pdm.mapper.BandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.sbc.config.common.base.BaseDao;
import com.base.sbc.config.common.base.BaseService;

import com.base.sbc.pdm.entity.Band;
import com.base.sbc.pdm.dao.BandDao;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 类描述： service类
 * @address com.base.sbc.pdm.service.BandService
 * @author lile
 * @email lilemyemail@163.com
 * @date 创建时间：2023-3-17 18:08:53
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class BandService extends BaseService<Band> {

	@Autowired
	private BandDao bandDao;
	@Resource
	private BandMapper bandMapper;

	@Resource
	private BaseController baseController;

	@Override
	protected BaseDao<Band> getEntityDao() {
		return bandDao;
	}

	public List<Band> listQuery(Band band) {
		String userCompany = baseController.getUserCompany();
		band.setCompanyCode(userCompany);
		return bandMapper.listQuery(band);
	}
	@Transactional(readOnly = false)
	public Integer delByIds(String[] ids) {
		GroupUser user = baseController.getUser();
		return bandMapper.delByIds(ids,user.getName(),user.getId());
	}


	@Transactional(readOnly = false)
	public Integer bandStartStop( Band band) {
		band.preUpdate();
		return bandMapper.bandStartStop(band);
	}

	@Transactional(readOnly = false)
	public Integer update(Band band) {
		band.preUpdate();
		return bandMapper.update(band);
	}

	@Transactional(readOnly = false)
	public String add(Band band) {
		//波段名称和编码是否重复
		QueryWrapper<Band> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("code", band.getCode()).or().eq("band_name", band.getBandName());
		if (bandMapper.selectOne(queryWrapper)!=null){
			throw new OtherException("编码或者波段重复");
		}
		band.preInsert();
		band.setCompanyCode(baseController.getUserCompany());
		bandMapper.insert(band);
		return band.getId();
	}
}
