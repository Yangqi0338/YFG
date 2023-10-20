/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service.impl;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.module.style.mapper.StyleOrderBookColorMapper;
import com.base.sbc.module.style.service.StyleGroupService;
import com.base.sbc.module.style.service.StyleOrderBookColorService;
import com.base.sbc.module.style.service.StyleOrderBookService;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.vo.StyleOrderBookPageVo;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.style.dto.StyleOrderBookDesignUpdateDto;
import com.base.sbc.module.style.dto.StyleOrderBookPlanUpdateDto;
import com.base.sbc.module.style.dto.StyleOrderBookPriceUpdateDto;
import com.base.sbc.module.style.dto.StyleOrderBookProductPlanUpdateDto;
import com.base.sbc.module.style.dto.StyleOrderBookQueryDto;
import com.base.sbc.module.style.dto.StyleOrderBookSaveDto;
import com.base.sbc.module.style.dto.StyleOrderBookUpdateDto;
import com.base.sbc.module.style.dto.StyleOrderBookUserUpdateDto;
import com.base.sbc.module.style.entity.StyleGroup;
import com.base.sbc.module.style.entity.StyleOrderBook;
import com.base.sbc.module.style.entity.StyleOrderBookColor;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.mapper.StyleOrderBookMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
public class StyleOrderBookServiceImpl extends BaseServiceImpl<StyleOrderBookMapper, StyleOrderBook>
		implements StyleOrderBookService {

	private final StyleOrderBookColorService sampleStyleOrderBookColorService;
	private final StyleGroupService sampleStyleGroupService;
	private final StyleColorService sampleStyleColorService;

	private final StyleOrderBookColorMapper styleOrderBookColorMapper;
	private final DataPermissionsService dataPermissionsService;

	@Override
	public PageInfo<StyleOrderBookPageVo> getStyleOrderBookList(StyleOrderBookQueryDto dto) {
		if (dto.getPageNum() != 0 && dto.getPageSize() != 0) {
			PageHelper.startPage(dto);
		}
		// 获取当前用户id
		String userId = companyUserInfo.get().getUserId();
		dto.setUserId(userId);
		dto.setCompanyCode(this.getCompanyCode());
		QueryWrapper qw = new QueryWrapper();
		dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.style_order_book.getK(), "t2.");

		List<StyleOrderBookPageVo> list = this.baseMapper.getStyleOrderBookList(dto, qw);
		return new PageInfo<>(list);
	}

	@Override
	public Boolean saveSampleStyleOrderBook(StyleOrderBookSaveDto dto) {
		if ("-1".equals(dto.getId())) {
			// 创建订货本
			StyleOrderBook ob = new StyleOrderBook();
			ob.setCompanyCode(this.getCompanyCode());
			ob.setOrderBookCode(getMaxCode("M"));
			this.save(ob);
			// 保存明细
			saveItem(ob.getOrderBookCode(), dto.getStyleNo());
		} else {
			StyleOrderBook bk = this.getById(dto.getId());
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
		// 如果大货款号已经上订货本禁止再上会
		long l = sampleStyleOrderBookColorService.count(new QueryWrapper<StyleOrderBookColor>()
				.eq("company_code", getCompanyCode()).in("style_no", convertList));
		if (l > 0) {
			throw new OtherException("款式已上订货本");
		}

		// 款号 - 套装编号
		Map<String, String> map = new HashMap<>();
		// 查询款式对应的套装
		List<StyleGroup> list = sampleStyleGroupService.list(new QueryWrapper<StyleGroup>()
				.select("group_code,style_no").eq("company_code", getCompanyCode()).in("style_no", convertList));
		if (list != null) {
			map = list.stream().distinct()
					.collect(Collectors.toMap(StyleGroup::getStyleNo, StyleGroup::getGroupCode));
		}
		List<StyleOrderBookColor> dbList = new ArrayList<>();
		// 遍历款号创建对象，判断是套装进行标记
		for (String item : convertList) {
			StyleOrderBookColor obc = new StyleOrderBookColor();
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
		BaseQueryWrapper<StyleOrderBook> qc = new BaseQueryWrapper<>();
		qc.select("order_book_code");
		qc.eq("company_code", this.getCompanyCode());
		qc.orderByDesc(" create_date ");
		qc.last(" limit 1 ");
		StyleOrderBook one = this.baseMapper.selectOne(qc);
		if (one != null) {
			String code = one.getOrderBookCode();
			Integer replace = Integer.parseInt(code.substring(code.length() - 5));
			return spx + String.format("%05d", replace + 1);
		} else {
			return spx + "00001";
		}
	}

	@Override
	@Transactional(rollbackFor = {Exception.class})
	public Boolean updateSampleStyleOrderBook(StyleOrderBookUpdateDto dto) {
		if (StringUtils.isBlank(dto.getImageUrl()) && StringUtils.isBlank(dto.getLockFlag())
				&& StringUtils.isBlank(dto.getMeetFlag())) {
			return false;
		}
		this.baseMapper.update(null, new UpdateWrapper<StyleOrderBook>()
				.set(StringUtils.isNotBlank(dto.getImageUrl()), "image_url", dto.getImageUrl())
				.set(StringUtils.isNotBlank(dto.getLockFlag()), "lock_flag", dto.getLockFlag())
				.set(StringUtils.isNotBlank(dto.getMeetFlag()), "meet_flag", dto.getMeetFlag())
				.in("id", StringUtils.convertList(dto.getId())));
		/**
		 * 上会时同步配色
		 * 获取订货本编号 查询订货本  使用大货款号查询配色
		 */
		if(StringUtils.isNotBlank(dto.getMeetFlag())){
			List<String> styleNo = styleOrderBookColorMapper.getStyleNo(dto.getId());
			if (!CollectionUtils.isEmpty(styleNo)) {
				UpdateWrapper updateWrapper = new UpdateWrapper();
				updateWrapper.set("meet_flag", dto.getMeetFlag());
				updateWrapper.in("style_no", styleNo);
				sampleStyleColorService.update(updateWrapper);
			}
		}
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
	public Boolean updateSampleStyleOrderBookPrice(StyleOrderBookPriceUpdateDto dto) {
		if ("1".equals(dto.getGroupFlag())) {
			sampleStyleGroupService.update(new UpdateWrapper<StyleGroup>().set("tag_price", dto.getTagPrice())
					.eq(COMPANY_CODE, getCompanyCode()).eq("group_code", dto.getGroupCode()));
		} else {
			sampleStyleColorService.update(new UpdateWrapper<StyleColor>().set("tag_price", dto.getTagPrice())
					.eq(COMPANY_CODE, getCompanyCode()).eq("style_no", dto.getStyleNo()));
		}
		return true;
	}

	@Override
	public Boolean updateSampleStyleOrderBookUser(StyleOrderBookUserUpdateDto dto) {
		return this.sampleStyleOrderBookColorService.update(new UpdateWrapper<StyleOrderBookColor>()
				.set("order_book_status", "1").set("design_status", "0").set("plan_status", "0")
				.set("product_plan_status", "0").set("plan_user_id", dto.getPlanUserId())
				.set("plan_user_name", dto.getPlanUserName()).set("product_plan_user_id", dto.getProductPlanUserId())
				.set("product_plan_user_name", dto.getProductPlanUserName())
				.eq(COMPANY_CODE, getCompanyCode()).in("id", StringUtils.convertList(dto.getId())));
	}

	@Override
	public Boolean updateSampleStyleOrderBookDesign(StyleOrderBookDesignUpdateDto dto) {
		StyleOrderBookColor copy = CopyUtil.copy(dto, StyleOrderBookColor.class);
		// 修改已上传
		copy.setDesignStatus("1");
		return this.sampleStyleOrderBookColorService.updateById(copy);
	}

	@Override
	public Boolean updateSampleStyleOrderBookPlan(StyleOrderBookPlanUpdateDto dto) {
		StyleOrderBookColor copy = CopyUtil.copy(dto, StyleOrderBookColor.class);
		// 修改已上传
		copy.setPlanStatus("1");
		return this.sampleStyleOrderBookColorService.updateById(copy);
	}

	@Override
	public Boolean updateSampleStyleOrderBookProductPlan(StyleOrderBookProductPlanUpdateDto dto) {
		StyleOrderBookColor copy = CopyUtil.copy(dto, StyleOrderBookColor.class);
		// 修改已上传
		copy.setProductPlanStatus("1");
		return this.sampleStyleOrderBookColorService.updateById(copy);
	}

}
