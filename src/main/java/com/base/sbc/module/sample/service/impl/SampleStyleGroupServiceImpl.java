/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.SampleStyleGroupQueryDto;
import com.base.sbc.module.sample.dto.SampleStyleGroupSaveDto;
import com.base.sbc.module.sample.entity.SampleStyleGroup;
import com.base.sbc.module.sample.mapper.SampleStyleGroupMapper;
import com.base.sbc.module.sample.service.SampleStyleGroupColorService;
import com.base.sbc.module.sample.service.SampleStyleGroupService;
import com.base.sbc.module.sample.vo.SampleStyleGroupPageVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
/** 
 * 类描述：款式管理-款式搭配 service类
 * @address com.base.sbc.module.sample.service.SampleStyleGroupService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-3 14:37:23
 * @version 1.0  
 */
@Service
public class SampleStyleGroupServiceImpl extends BaseServiceImpl<SampleStyleGroupMapper, SampleStyleGroup> implements SampleStyleGroupService {

	@Autowired
	private SampleStyleGroupColorService sampleStyleGroupColorService;

	@Override
	public PageInfo<SampleStyleGroupPageVo> getStyleGroupList(SampleStyleGroupQueryDto dto) {
		if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
			PageHelper.startPage(dto);
		}
		dto.setCompanyCode(this.getCompanyCode());
		List<SampleStyleGroupPageVo> list = this.baseMapper.getStyleGroupList(dto);
		return new PageInfo<>(list);
	}

	@Override
	public Boolean delSampleStyleGroup(String id) {
		return this.removeBatchByIds(StringUtils.convertList(id));
	}

	@Override
	public SampleStyleGroupPageVo saveSampleStyleGroup(SampleStyleGroupSaveDto dto) {
		SampleStyleGroup entity = CopyUtil.copy(dto, SampleStyleGroup.class);
		if ("-1".equals(entity.getId())) {
			entity.setId(null);
			// 获取并放入最大code
			entity.setGroupCode(getMaxCode("T"));
		}
		this.saveOrUpdate(entity);
		return getSampleStyleGroup(entity.getId());
	}

	private String getMaxCode(String spx) {
		BaseQueryWrapper<SampleStyleGroup> qc = new BaseQueryWrapper<>();
		qc.select("group_code");
		qc.eq("company_code", this.getCompanyCode());
		qc.orderByDesc(" create_date ");
		qc.last(" limit 1 ");
		SampleStyleGroup one = this.baseMapper.selectOne(qc);
		if (one != null) {
			String code = one.getGroupCode();
			Integer replace = Integer.parseInt(code.substring(code.length() - 5));
			return spx + String.format("%05d", replace + 1);
		} else {
			return spx + "00001";
		}
	}

	@Override
	public SampleStyleGroupPageVo getSampleStyleGroup(String id) {
		SampleStyleGroupQueryDto dto = new SampleStyleGroupQueryDto();
		dto.setCompanyCode(this.getCompanyCode());
		dto.setId(id);
		return this.baseMapper.getSampleStyleGroup(dto);
	}

	@Override
	public List<SampleStyleGroupPageVo> getStyleGroupItemByGroupCode(String groupCode) {
		SampleStyleGroupQueryDto dto = new SampleStyleGroupQueryDto();
		dto.setCompanyCode(this.getCompanyCode());
		dto.setGroupCode(groupCode);
		return this.baseMapper.getStyleGroupItemByGroupCode(dto);
	}

	@Override
	public Boolean delSampleStyleGroupItem(String id) {
		return this.sampleStyleGroupColorService.removeBatchByIds(StringUtils.convertList(id));
	}
	
}
