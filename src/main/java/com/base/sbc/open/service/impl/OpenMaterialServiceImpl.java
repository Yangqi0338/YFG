/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.open.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialColor;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialWidth;
import com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialColorService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialWidthService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.open.dto.OpenMaterialDto;
import com.base.sbc.open.dto.OpenMaterialSubsVo;
import com.base.sbc.open.mapper.OpenMaterialMapper;
import com.base.sbc.open.service.OpenMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author Liu YuBin
 * @version 1.0
 * @date 2023/8/15 10:06
 */
@Service
@RequiredArgsConstructor
public class OpenMaterialServiceImpl extends BaseServiceImpl<OpenMaterialMapper, OpenMaterialDto> implements OpenMaterialService {

	private final BasicsdatumMaterialWidthService materialWidthService;
	private final BasicsdatumMaterialColorService materialColorService;
	private final BasicsdatumColourLibraryService colourLibraryService;

	@Override
	public List<OpenMaterialDto> getMaterialList(String companyCode) {
		List<OpenMaterialDto> result = baseMapper.getMaterialList(companyCode);
		if (result != null && result.size() != 0){
			List<String> codeList = new ArrayList<>();
			for (OpenMaterialDto material : result) {
				codeList.add(material.getMtCode());
				material.init();
			}
			BaseQueryWrapper<BasicsdatumMaterialWidth> widthQc = new BaseQueryWrapper<>();
			BaseQueryWrapper<BasicsdatumMaterialColor> colorQc = new BaseQueryWrapper<>();
			widthQc.eq("company_code",companyCode);
			colorQc.eq("company_code",companyCode);
			widthQc.in("material_code",codeList);
			colorQc.in("material_code",codeList);
			List<BasicsdatumMaterialWidth> widthList = materialWidthService.list(widthQc);
			List<BasicsdatumMaterialColor> colorList = materialColorService.list(colorQc);
			Map<String, List<BasicsdatumMaterialWidth>> widthMap = new HashMap<>();
			Map<String, List<BasicsdatumMaterialColor>> colorMap = new HashMap<>();

			//组装数据
			Map<String, String> colorImgMap = new HashMap<>();
			if (CollectionUtil.isNotEmpty(colorList)){
				List<String> colorCodeList = new ArrayList<>();
				for (BasicsdatumMaterialColor color : colorList) {
					colorCodeList.add(color.getColorCode());
					if (colorMap.get(color.getMaterialCode()) != null){
						colorMap.get(color.getMaterialCode()).add(color);
					}else{
						List<BasicsdatumMaterialColor> list = new ArrayList<>();
						list.add(color);
						colorMap.put(color.getMaterialCode(),list);
					}
				}
				//查询颜色图片作为物料sub的图片
				BaseQueryWrapper<BasicsdatumColourLibrary> colorImgQc = new BaseQueryWrapper<>();
				colorImgQc.eq("company_code",companyCode);
				colorImgQc.in("colour_code",colorCodeList);
				colorImgMap = colourLibraryService.list(colorImgQc).stream()
						.collect(Collectors.toMap(c -> c.getColourCode(), c -> {
							if (c.getPicture() != null) {
								return c.getPicture();
							} else {
								return "";
							}
						}, (c1, c2) -> c2));
			}
			if (CollectionUtil.isNotEmpty(widthList)){
				widthMap = widthList.stream().collect(Collectors.groupingBy(w -> w.getMaterialCode()));
			}
			List<OpenMaterialSubsVo> subsList;
			OpenMaterialSubsVo subs;
			for (OpenMaterialDto material : result) {
				if ("9999".equals(material.getWidth()) && "无".equals(material.getWidth())){
					material.setWidth("");
				}
				subsList = new ArrayList<>();
				if (colorMap.get(material.getMtCode()) != null
						&& widthMap.get(material.getMtCode()) != null){
					for (BasicsdatumMaterialColor color : colorMap.get(material.getMtCode())) {
						for (BasicsdatumMaterialWidth width : widthMap.get(material.getMtCode())) {
							subs = new OpenMaterialSubsVo();
							subs.init(color,width,material);
							subs.setImgPath(colorImgMap.get(subs.getColorCode()));
							subsList.add(subs);
						}
					}
				}else if (colorMap.get(material.getMtCode()) != null){
					for (BasicsdatumMaterialColor color : colorMap.get(material.getMtCode())) {
						subs = new OpenMaterialSubsVo();
						subs.init(color,null,material);
						subs.setImgPath(colorImgMap.get(subs.getColorCode()));
						subsList.add(subs);
					}
				}else if (widthMap.get(material.getMtCode()) != null){
					for (BasicsdatumMaterialWidth width : widthMap.get(material.getMtCode())) {
						subs = new OpenMaterialSubsVo();
						subs.init(null,width,material);
						subsList.add(subs);
					}
				}else{
					subs = new OpenMaterialSubsVo();
					subs.init(null,null,material);
					subsList.add(subs);
				}
				material.setSubs(subsList);
			}
		}
		return result;
	}

}
