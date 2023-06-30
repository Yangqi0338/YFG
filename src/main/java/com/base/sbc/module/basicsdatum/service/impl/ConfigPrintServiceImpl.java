/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampConfigPrintDto;
import com.base.sbc.module.basicsdatum.dto.QueryRevampConfigPrintDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.ConfigPrint;
import com.base.sbc.module.basicsdatum.mapper.ConfigPrintMapper;
import com.base.sbc.module.basicsdatum.service.ConfigPrintService;
import com.base.sbc.module.basicsdatum.vo.ConfigPrintVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：打印配置 service类
 * 
 * @address com.base.sbc.module.basicsdatum.service.ConfigPrintService
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 创建时间：2023-6-21 10:29:11
 * @version 1.0
 */
@Service
public class ConfigPrintServiceImpl extends BaseServiceImpl<ConfigPrintMapper, ConfigPrint>
		implements ConfigPrintService {
	// 自定义方法区 不替换的区域【other_start】

	@Autowired
	private BaseController baseController;

	@Override
	public PageInfo<ConfigPrintVo> getConfigPrintList(QueryRevampConfigPrintDto queryDto) {
		if (queryDto.getPageNum() != 0 && queryDto.getPageSize() != 0) {
			PageHelper.startPage(queryDto);
		}
		BaseQueryWrapper<ConfigPrint> qc = new BaseQueryWrapper<>();
		qc.eq("company_code", baseController.getUserCompany());
		qc.notEmptyEq("bill_type", queryDto.getBillType());
		qc.notEmptyLike("code", queryDto.getCode());
		qc.notEmptyLike("name", queryDto.getName());
		qc.notEmptyEq("stop_flag", queryDto.getStopFlag());
		qc.notEmptyEq("status", queryDto.getStatus());
		qc.notEmptyLike("remarks", queryDto.getRemarks());
		List<ConfigPrint> basicsdatumModelTypeList = this.list(qc);
		PageInfo<ConfigPrint> pageInfo = new PageInfo<>(basicsdatumModelTypeList);
		return CopyUtil.copy(pageInfo, ConfigPrintVo.class);
	}

	@Override
	public Boolean startStopConfigPrint(StartStopDto startStopDto) {
		UpdateWrapper<ConfigPrint> uc = new UpdateWrapper<>();
		uc.in("id", StringUtils.convertList(startStopDto.getIds()));
		uc.set("stop_flag", startStopDto.getStatus());
		return this.update(null, uc);
	}

	public static final String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ureport><cell expand=\"None\" name=\"A1\" row=\"1\" col=\"1\"><cell-style font-size=\"10\" align=\"center\" valign=\"middle\"></cell-style><simple-value><![CDATA[]]></simple-value></cell><cell expand=\"None\" name=\"B1\" row=\"1\" col=\"2\"><cell-style font-size=\"10\" align=\"center\" valign=\"middle\"></cell-style><simple-value><![CDATA[]]></simple-value></cell><cell expand=\"None\" name=\"C1\" row=\"1\" col=\"3\"><cell-style font-size=\"10\" align=\"center\" valign=\"middle\"></cell-style><simple-value><![CDATA[]]></simple-value></cell><cell expand=\"None\" name=\"D1\" row=\"1\" col=\"4\"><cell-style font-size=\"10\" align=\"center\" valign=\"middle\"></cell-style><simple-value><![CDATA[]]></simple-value></cell><cell expand=\"None\" name=\"A2\" row=\"2\" col=\"1\"><cell-style font-size=\"10\" align=\"center\" valign=\"middle\"></cell-style><simple-value><![CDATA[]]></simple-value></cell><cell expand=\"None\" name=\"B2\" row=\"2\" col=\"2\"><cell-style font-size=\"10\" align=\"center\" valign=\"middle\"></cell-style><simple-value><![CDATA[]]></simple-value></cell><cell expand=\"None\" name=\"C2\" row=\"2\" col=\"3\"><cell-style font-size=\"10\" align=\"center\" valign=\"middle\"></cell-style><simple-value><![CDATA[]]></simple-value></cell><cell expand=\"None\" name=\"D2\" row=\"2\" col=\"4\"><cell-style font-size=\"10\" align=\"center\" valign=\"middle\"></cell-style><simple-value><![CDATA[]]></simple-value></cell><cell expand=\"None\" name=\"A3\" row=\"3\" col=\"1\"><cell-style font-size=\"10\" align=\"center\" valign=\"middle\"></cell-style><simple-value><![CDATA[]]></simple-value></cell><cell expand=\"None\" name=\"B3\" row=\"3\" col=\"2\"><cell-style font-size=\"10\" align=\"center\" valign=\"middle\"></cell-style><simple-value><![CDATA[]]></simple-value></cell><cell expand=\"None\" name=\"C3\" row=\"3\" col=\"3\"><cell-style font-size=\"10\" align=\"center\" valign=\"middle\"></cell-style><simple-value><![CDATA[]]></simple-value></cell><cell expand=\"None\" name=\"D3\" row=\"3\" col=\"4\"><cell-style font-size=\"10\" align=\"center\" valign=\"middle\"></cell-style><simple-value><![CDATA[]]></simple-value></cell><row row-number=\"1\" height=\"18\"/><row row-number=\"2\" height=\"18\"/><row row-number=\"3\" height=\"18\"/><column col-number=\"1\" width=\"80\"/><column col-number=\"2\" width=\"80\"/><column col-number=\"3\" width=\"80\"/><column col-number=\"4\" width=\"80\"/><paper type=\"A4\" left-margin=\"90\" right-margin=\"90\"\n"
			+ "    top-margin=\"72\" bottom-margin=\"72\" paging-mode=\"fitpage\" fixrows=\"0\"\n"
			+ "    width=\"595\" height=\"842\" orientation=\"portrait\" html-report-align=\"left\" bg-image=\"\" html-interval-refresh-value=\"0\" column-enabled=\"false\"></paper></ureport>";

	@Override
	public Boolean addRevampConfigPrint(AddRevampConfigPrintDto dto) {
		// 验证名称是否重复
		long count = this.count(new QueryWrapper<ConfigPrint>().eq("name", dto.getName()).ne("id", dto.getId()));
		if (count > 0) {
			throw new OtherException("名称重复");
		}
		ConfigPrint print = CopyUtil.copy(dto, ConfigPrint.class);
		print.setFileName(baseController.getUserCompany() + "-" + print.getName() + ".ureport.xml");

		if ("-1".equals(print.getId())) {
			print.setId(null);
			print.setFmtJson(XML);
		}
		return this.saveOrUpdate(print);
	}

	@Override
	public Boolean delConfigPrint(String id) {
		return this.removeBatchByIds(StringUtils.convertList(id));
	}
	// 自定义方法区 不替换的区域【other_end】

}
