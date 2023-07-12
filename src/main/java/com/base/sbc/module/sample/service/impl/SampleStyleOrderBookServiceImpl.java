/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import static com.base.sbc.config.adviceAdapter.ResponseControllerAdvice.companyUserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.SampleStyleOrderBookDesignUpdateDto;
import com.base.sbc.module.sample.dto.SampleStyleOrderBookPlanUpdateDto;
import com.base.sbc.module.sample.dto.SampleStyleOrderBookPriceUpdateDto;
import com.base.sbc.module.sample.dto.SampleStyleOrderBookProductPlanUpdateDto;
import com.base.sbc.module.sample.dto.SampleStyleOrderBookQueryDto;
import com.base.sbc.module.sample.dto.SampleStyleOrderBookSaveDto;
import com.base.sbc.module.sample.dto.SampleStyleOrderBookUpdateDto;
import com.base.sbc.module.sample.dto.SampleStyleOrderBookUserUpdateDto;
import com.base.sbc.module.sample.entity.SampleStyleColor;
import com.base.sbc.module.sample.entity.SampleStyleGroup;
import com.base.sbc.module.sample.entity.SampleStyleOrderBook;
import com.base.sbc.module.sample.entity.SampleStyleOrderBookColor;
import com.base.sbc.module.sample.mapper.SampleStyleOrderBookMapper;
import com.base.sbc.module.sample.service.SampleStyleColorService;
import com.base.sbc.module.sample.service.SampleStyleGroupService;
import com.base.sbc.module.sample.service.SampleStyleOrderBookColorService;
import com.base.sbc.module.sample.service.SampleStyleOrderBookService;
import com.base.sbc.module.sample.vo.SampleStyleOrderBookPageVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.RequiredArgsConstructor;

/**
 * 
 * 类描述：款式管理-订货本 service类
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月11日
 */
@Service
@RequiredArgsConstructor
public class SampleStyleOrderBookServiceImpl extends BaseServiceImpl<SampleStyleOrderBookMapper, SampleStyleOrderBook>
		implements SampleStyleOrderBookService {

	private final SampleStyleOrderBookColorService sampleStyleOrderBookColorService;
	private final SampleStyleGroupService sampleStyleGroupService;
	private final SampleStyleColorService sampleStyleColorService;

	@Override
	public PageInfo<SampleStyleOrderBookPageVo> getStyleOrderBookList(SampleStyleOrderBookQueryDto dto) {
		if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
			PageHelper.startPage(dto);
		}
		// 获取当前用户id
		String userId = companyUserInfo.get().getUserId();
		dto.setUserId(userId);
		dto.setCompanyCode(this.getCompanyCode());
		List<SampleStyleOrderBookPageVo> list = this.baseMapper.getStyleOrderBookList(dto);
		return new PageInfo<>(list);
	}

	@Override
	public Boolean saveSampleStyleOrderBook(SampleStyleOrderBookSaveDto dto) {
		if ("-1".equals(dto.getId())) {
			// 创建订货本
			SampleStyleOrderBook ob = new SampleStyleOrderBook();
			ob.setCompanyCode(this.getCompanyCode());
			ob.setOrderBookCode(getMaxCode("M"));
			this.save(ob);
			// 保存明细
			saveItem(ob.getOrderBookCode(), dto.getStyleNo());
		} else {
			SampleStyleOrderBook bk = this.getById(dto.getId());
			if (bk != null) {
				saveItem(bk.getOrderBookCode(), dto.getStyleNo());
			}
		}
		return true;
	}

	/**
	 * 保存订货本明细
	 * 
	 * @param orderBookCode
	 * @param styleNo
	 * @return
	 */
	private void saveItem(String orderBookCode, String styleNo) {
		// 页面选择的款号集合
		List<String> convertList = StringUtils.convertList(styleNo).stream().distinct().collect(Collectors.toList());
		// 款号 - 套装编号
		Map<String, String> map = new HashMap<>();
		// 查询款式对应的套装
		List<SampleStyleGroup> list = sampleStyleGroupService.list(new QueryWrapper<SampleStyleGroup>()
				.select("group_code,style_no").eq("company_code", getCompanyCode()).in("style_no", convertList));
		if (list != null) {
			map = list.stream().distinct()
					.collect(Collectors.toMap(SampleStyleGroup::getStyleNo, SampleStyleGroup::getGroupCode));
		}
		List<SampleStyleOrderBookColor> dbList = new ArrayList<>();
		// 遍历款号创建对象，判断是套装进行标记
		for (String item : convertList) {
			SampleStyleOrderBookColor obc = new SampleStyleOrderBookColor();
			obc.setCompanyCode(getCompanyCode());
			obc.setOrderBookCode(orderBookCode);
			obc.setStyleNo(item);
			if (map.get(item) != null) {
				obc.setGroupFlag("1");
				obc.setGroupCode(map.get(item));
			}
			dbList.add(obc);
		}
		this.sampleStyleOrderBookColorService.saveBatch(dbList);
	}

	/**
	 * 获取最大编号
	 * 
	 * @param spx
	 * @return
	 */
	private String getMaxCode(String spx) {
		BaseQueryWrapper<SampleStyleOrderBook> qc = new BaseQueryWrapper<>();
		qc.select("order_book_code");
		qc.eq("company_code", this.getCompanyCode());
		qc.orderByDesc(" create_date ");
		qc.last(" limit 1 ");
		SampleStyleOrderBook one = this.baseMapper.selectOne(qc);
		if (one != null) {
			String code = one.getOrderBookCode();
			Integer replace = Integer.parseInt(code.substring(code.length() - 5));
			return spx + String.format("%05d", replace + 1);
		} else {
			return spx + "00001";
		}
	}

	@Override
	public Boolean updateSampleStyleOrderBook(SampleStyleOrderBookUpdateDto dto) {
		if (StringUtils.isBlank(dto.getImageUrl()) && StringUtils.isBlank(dto.getLockFlag())
				&& StringUtils.isBlank(dto.getMeetFlag())) {
			return false;
		}
		this.baseMapper.update(null, new UpdateWrapper<SampleStyleOrderBook>()
				.set(StringUtils.isNotBlank(dto.getImageUrl()), "image_url", dto.getImageUrl())
				.set(StringUtils.isNotBlank(dto.getLockFlag()), "lock_flag", dto.getLockFlag())
				.set(StringUtils.isNotBlank(dto.getMeetFlag()), "meet_flag", dto.getMeetFlag()).eq("id", dto.getId()));
		return true;
	}

	@Override
	public Boolean delSampleStyleOrderBookItem(String id) {
		return this.sampleStyleOrderBookColorService.removeBatchByIds(StringUtils.convertList(id));
	}

	/**
	 * 修改吊牌价
	 */
	@Override
	public Boolean updateSampleStyleOrderBookPrice(SampleStyleOrderBookPriceUpdateDto dto) {
		if ("1".equals(dto.getGroupFlag())) {
			sampleStyleGroupService.update(new UpdateWrapper<SampleStyleGroup>().set("tag_price", dto.getTagPrice())
					.eq(COMPANY_CODE, getCompanyCode()).eq("group_code", dto.getGroupCode()));
		} else {
			sampleStyleColorService.update(new UpdateWrapper<SampleStyleColor>().set("tag_price", dto.getTagPrice())
					.eq(COMPANY_CODE, getCompanyCode()).eq("style_no", dto.getStyleNo()));
		}
		return true;
	}

	@Override
	public Boolean updateSampleStyleOrderBookUser(SampleStyleOrderBookUserUpdateDto dto) {
		return this.sampleStyleOrderBookColorService.update(new UpdateWrapper<SampleStyleOrderBookColor>()
				.set("order_book_status", "1").set("design_status", "0").set("plan_status", "0")
				.set("product_plan_status", "0").set("plan_user_id", dto.getPlanUserId())
				.set("plan_user_name", dto.getPlanUserName()).set("product_plan_user_id", dto.getProductPlanUserId())
				.set("product_plan_user_name", dto.getProductPlanUserName())
				.eq(COMPANY_CODE, getCompanyCode()).in("id", StringUtils.convertList(dto.getId())));
	}

	@Override
	public Boolean updateSampleStyleOrderBookDesign(SampleStyleOrderBookDesignUpdateDto dto) {
		SampleStyleOrderBookColor copy = CopyUtil.copy(dto, SampleStyleOrderBookColor.class);
		// 修改已上传
		copy.setDesignStatus("1");
		return this.sampleStyleOrderBookColorService.updateById(copy);
	}

	@Override
	public Boolean updateSampleStyleOrderBookPlan(SampleStyleOrderBookPlanUpdateDto dto) {
		SampleStyleOrderBookColor copy = CopyUtil.copy(dto, SampleStyleOrderBookColor.class);
		// 修改已上传
		copy.setPlanStatus("1");
		return this.sampleStyleOrderBookColorService.updateById(copy);
	}

	@Override
	public Boolean updateSampleStyleOrderBookProductPlan(SampleStyleOrderBookProductPlanUpdateDto dto) {
		SampleStyleOrderBookColor copy = CopyUtil.copy(dto, SampleStyleOrderBookColor.class);
		// 修改已上传
		copy.setProductPlanStatus("1");
		return this.sampleStyleOrderBookColorService.updateById(copy);
	}

}
