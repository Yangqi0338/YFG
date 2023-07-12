/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.SampleStyleGroupItemSaveDto;
import com.base.sbc.module.sample.dto.SampleStyleGroupQueryDto;
import com.base.sbc.module.sample.dto.SampleStyleGroupSaveDto;
import com.base.sbc.module.sample.entity.SampleStyleGroup;
import com.base.sbc.module.sample.entity.SampleStyleGroupColor;
import com.base.sbc.module.sample.mapper.SampleStyleGroupMapper;
import com.base.sbc.module.sample.service.SampleStyleGroupColorService;
import com.base.sbc.module.sample.service.SampleStyleGroupService;
import com.base.sbc.module.sample.vo.SampleStyleGroupPageVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.RequiredArgsConstructor;
/** 
 * 类描述：款式管理-款式搭配 service类
 * @address com.base.sbc.module.sample.service.SampleStyleGroupService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-3 14:37:23
 * @version 1.0  
 */
@Service
@RequiredArgsConstructor
public class SampleStyleGroupServiceImpl extends BaseServiceImpl<SampleStyleGroupMapper, SampleStyleGroup> implements SampleStyleGroupService {

	private final SampleStyleGroupColorService sampleStyleGroupColorService;

	@Override
	public PageInfo<SampleStyleGroupPageVo> getStyleGroupList(SampleStyleGroupQueryDto dto) {
		if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
			PageHelper.startPage(dto);
		}
		dto.setCompanyCode(this.getCompanyCode());
		List<SampleStyleGroupPageVo> list = this.baseMapper.getStyleGroupList(dto);
		return new PageInfo<>(list);
	}

	@Transactional
	@Override
	public Boolean delSampleStyleGroup(String id) {
		List<String> list = StringUtils.convertList(id);
		BaseQueryWrapper<SampleStyleGroup> qc = new BaseQueryWrapper<>();
		qc.select("group_code");
		qc.eq("company_code", this.getCompanyCode());
		qc.in("id", list);
		List<String> list2 = this.list(qc).stream().map(SampleStyleGroup::getGroupCode).collect(Collectors.toList());
		// 删除主表
		this.removeBatchByIds(list);
		// 删除子表
		this.sampleStyleGroupColorService.remove(new QueryWrapper<SampleStyleGroupColor>()
				.eq("company_code", this.getCompanyCode()).in("group_code", list2));
		return true;
	}

	@Override
	public SampleStyleGroupPageVo saveSampleStyleGroup(SampleStyleGroupSaveDto dto) {
		SampleStyleGroup entity = CopyUtil.copy(dto, SampleStyleGroup.class);
		if ("-1".equals(entity.getId())) {
			entity.setId(null);

			long count = this.count(new QueryWrapper<SampleStyleGroup>().eq(COMPANY_CODE, getCompanyCode())
					.eq("style_no", dto.getStyleNo()));
			if (count > 0) {
				throw new OtherException("款式已被选择：" + dto.getStyleNo());
			}
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
	public PageInfo<SampleStyleGroupPageVo> getStyleGroupItemByGroupCode(SampleStyleGroupQueryDto dto) {
		if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
			PageHelper.startPage(dto);
		}
		dto.setCompanyCode(this.getCompanyCode());
		List<SampleStyleGroupPageVo> list = this.baseMapper.getStyleGroupItemByGroupCode(dto);
		return new PageInfo<>(list);
	}

	@Override
	public Boolean saveSampleStyleGroupItem(SampleStyleGroupItemSaveDto dto) {
		if (dto.getStyleNo().indexOf(",") > 0) {
			String[] styles = dto.getStyleNo().split(",");

			long count = this.sampleStyleGroupColorService.count(new QueryWrapper<SampleStyleGroupColor>()
					.eq(COMPANY_CODE, getCompanyCode()).in("style_no", StringUtils.convertList(dto.getStyleNo())));
			if (count > 0) {
				throw new OtherException("款式已被选择：" + dto.getStyleNo());
			}

			SampleStyleGroupColor entity;
			List<SampleStyleGroupColor> list = new ArrayList<>();
			for (int i = 0; i < styles.length; i++) {
				if (StringUtils.isBlank(styles[i])) {
					continue;
				}
				entity = new SampleStyleGroupColor();
				entity.setCompanyCode(this.getCompanyCode());
				entity.setStyleNo(styles[i]);
				entity.setGroupCode(dto.getGroupCode());
				list.add(entity);
			}
			this.sampleStyleGroupColorService.saveBatch(list);
		} else {
			SampleStyleGroupColor entity = CopyUtil.copy(dto, SampleStyleGroupColor.class);
			long count = this.sampleStyleGroupColorService.count(new QueryWrapper<SampleStyleGroupColor>()
					.eq(COMPANY_CODE, getCompanyCode()).eq("style_no", dto.getStyleNo()));
			if (count > 0) {
				throw new OtherException("款式已被选择：" + dto.getStyleNo());
			}

			entity.setCompanyCode(this.getCompanyCode());
			this.sampleStyleGroupColorService.save(entity);

		}
		return true;
	}

	@Override
	public Boolean delSampleStyleGroupItem(String id) {
		return this.sampleStyleGroupColorService.removeBatchByIds(StringUtils.convertList(id));
	}

	
}
