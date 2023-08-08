/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service.impl;

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
import com.base.sbc.module.style.dto.StyleGroupItemSaveDto;
import com.base.sbc.module.style.dto.StyleGroupQueryDto;
import com.base.sbc.module.style.dto.StyleGroupSaveDto;
import com.base.sbc.module.style.entity.StyleGroup;
import com.base.sbc.module.style.entity.StyleGroupColor;
import com.base.sbc.module.style.mapper.StyleGroupMapper;
import com.base.sbc.module.style.service.StyleGroupColorService;
import com.base.sbc.module.style.service.StyleGroupService;
import com.base.sbc.module.style.vo.StyleGroupPageVo;
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
public class StyleGroupServiceImpl extends BaseServiceImpl<StyleGroupMapper, StyleGroup> implements StyleGroupService {

	private final StyleGroupColorService sampleStyleGroupColorService;

	@Override
	public PageInfo<StyleGroupPageVo> getStyleGroupList(StyleGroupQueryDto dto) {
		if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
			PageHelper.startPage(dto);
		}
		dto.setCompanyCode(this.getCompanyCode());
		List<StyleGroupPageVo> list = this.baseMapper.getStyleGroupList(dto);
		return new PageInfo<>(list);
	}

	@Transactional
	@Override
	public Boolean delSampleStyleGroup(String id) {
		List<String> list = StringUtils.convertList(id);
		BaseQueryWrapper<StyleGroup> qc = new BaseQueryWrapper<>();
		qc.select("group_code");
		qc.eq("company_code", this.getCompanyCode());
		qc.in("id", list);
		List<String> list2 = this.list(qc).stream().map(StyleGroup::getGroupCode).collect(Collectors.toList());
		// 删除主表
		this.removeBatchByIds(list);
		// 删除子表
		this.sampleStyleGroupColorService.remove(new QueryWrapper<StyleGroupColor>()
				.eq("company_code", this.getCompanyCode()).in("group_code", list2));
		return true;
	}

	@Override
	public StyleGroupPageVo saveSampleStyleGroup(StyleGroupSaveDto dto) {
		StyleGroup entity = CopyUtil.copy(dto, StyleGroup.class);
		if ("-1".equals(entity.getId())) {
			entity.setId(null);

			long count = this.count(new QueryWrapper<StyleGroup>().eq(COMPANY_CODE, getCompanyCode())
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
		BaseQueryWrapper<StyleGroup> qc = new BaseQueryWrapper<>();
		qc.select("group_code");
		qc.eq("company_code", this.getCompanyCode());
		qc.orderByDesc(" create_date ");
		qc.last(" limit 1 ");
		StyleGroup one = this.baseMapper.selectOne(qc);
		if (one != null) {
			String code = one.getGroupCode();
			Integer replace = Integer.parseInt(code.substring(code.length() - 5));
			return spx + String.format("%05d", replace + 1);
		} else {
			return spx + "00001";
		}
	}

	@Override
	public StyleGroupPageVo getSampleStyleGroup(String id) {
		StyleGroupQueryDto dto = new StyleGroupQueryDto();
		dto.setCompanyCode(this.getCompanyCode());
		dto.setId(id);
		return this.baseMapper.getSampleStyleGroup(dto);
	}

	@Override
	public PageInfo<StyleGroupPageVo> getStyleGroupItemByGroupCode(StyleGroupQueryDto dto) {
		if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
			PageHelper.startPage(dto);
		}
		dto.setCompanyCode(this.getCompanyCode());
		List<StyleGroupPageVo> list = this.baseMapper.getStyleGroupItemByGroupCode(dto);
		return new PageInfo<>(list);
	}

	@Override
	public Boolean saveSampleStyleGroupItem(StyleGroupItemSaveDto dto) {
		if (dto.getStyleNo().indexOf(",") > 0) {
			String[] styles = dto.getStyleNo().split(",");

			long count = this.sampleStyleGroupColorService.count(new QueryWrapper<StyleGroupColor>()
					.eq(COMPANY_CODE, getCompanyCode()).in("style_no", StringUtils.convertList(dto.getStyleNo())));
			if (count > 0) {
				throw new OtherException("款式已被选择：" + dto.getStyleNo());
			}

			StyleGroupColor entity;
			List<StyleGroupColor> list = new ArrayList<>();
			for (int i = 0; i < styles.length; i++) {
				if (StringUtils.isBlank(styles[i])) {
					continue;
				}
				entity = new StyleGroupColor();
				entity.setCompanyCode(this.getCompanyCode());
				entity.setStyleNo(styles[i]);
				entity.setGroupCode(dto.getGroupCode());
				list.add(entity);
			}
			this.sampleStyleGroupColorService.saveBatch(list);
		} else {
			StyleGroupColor entity = CopyUtil.copy(dto, StyleGroupColor.class);
			long count = this.sampleStyleGroupColorService.count(new QueryWrapper<StyleGroupColor>()
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
