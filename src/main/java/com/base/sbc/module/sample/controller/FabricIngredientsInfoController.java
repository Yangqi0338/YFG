/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.sample.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.dto.IdDto;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.sample.dto.QueryFabricInformationDto;
import com.base.sbc.module.sample.dto.QueryFabricIngredientsInfoDto;
import com.base.sbc.module.sample.entity.FabricIngredientsInfo;
import com.base.sbc.module.sample.service.FabricIngredientsInfoService;
import com.base.sbc.module.sample.dto.AddRevampFabricIngredientsInfoDto;
import com.base.sbc.module.sample.vo.FabricIngredientsInfoVo;
import org.hibernate.validator.constraints.NotBlank;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 类描述：调样-辅料信息 Controller类
* @address com.base.sbc.module.sample.web.FabricIngredientsInfoController
* @author mengfanjiang
* @email XX.com
* @date 创建时间：2023-7-14 17:32:38
* @version 1.0
*/
@RestController
@Api(tags = "调样-辅料信息")
@RequestMapping(value = BaseController.SAAS_URL + "/fabricIngredientsInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class FabricIngredientsInfoController{

	@Autowired
	private FabricIngredientsInfoService fabricIngredientsInfoService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getFabricIngredientsInfoList")
	public PageInfo getFabricIngredientsInfoList(QueryFabricIngredientsInfoDto queryFabricIngredientsInfoDto) {
		return  fabricIngredientsInfoService.getFabricIngredientsInfoList(queryFabricIngredientsInfoDto);
	}

	/**
	 * 查询所有设计师
	 * @return
	 */
	@ApiOperation(value = "查询所有设计师")
	@GetMapping("/getDesignList")
	public List<Map<String,String>> getDesignList() {
		BaseQueryWrapper<FabricIngredientsInfo> queryWrapper =new BaseQueryWrapper<>();
		queryWrapper.isNotNullStr("atactiform_stylist");
		queryWrapper.groupBy("atactiform_stylist");
		List<FabricIngredientsInfo> list = fabricIngredientsInfoService.list(queryWrapper);
		List<Map<String,String>> designList=new ArrayList<>();
		for (FabricIngredientsInfo fabricIngredientsInfo : list) {
			Map<String,String> map =new HashMap<>();
			map.put("name",fabricIngredientsInfo.getAtactiformStylist());
			map.put("userId",fabricIngredientsInfo.getAtactiformStylistUserId());
			designList.add(map);
		}
		return designList;
	}


	@ApiOperation(value = "/导出")
	@GetMapping("/fabricIngredientsInfoDeriveExcel")
	@DuplicationCheck(type = 1,message = "正在导出中，请稍后...")
	public void fabricIngredientsInfoDeriveExcel(HttpServletResponse response, QueryFabricIngredientsInfoDto queryFabricIngredientsInfoDto) throws Exception {
		fabricIngredientsInfoService.fabricIngredientsInfoDeriveExcel(response,queryFabricIngredientsInfoDto);
	}

	@ApiOperation(value = "复制辅料")
	@PostMapping("/copyIngredients")
	public Boolean copyIngredients(@Valid @RequestBody IdDto idDto) {
		return fabricIngredientsInfoService.copyIngredients(idDto.getId());
	}


	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopFabricIngredientsInfo")
	public Boolean startStopFabricIngredientsInfo(@Valid @RequestBody StartStopDto startStopDto) {
	return fabricIngredientsInfoService.startStopFabricIngredientsInfo(startStopDto);
	}


	@ApiOperation(value = "新增修改调样-辅料信息")
	@PostMapping("/addRevampFabricIngredientsInfo")
	public Boolean addRevampFabricIngredientsInfo(@Valid @RequestBody AddRevampFabricIngredientsInfoDto addRevampFabricIngredientsInfoDto) {
	return fabricIngredientsInfoService.addRevampFabricIngredientsInfo(addRevampFabricIngredientsInfoDto);
	}

	@ApiOperation(value = "删除调样-辅料信息")
	@DeleteMapping("/delFabricIngredientsInfo")
	public Boolean delFabricIngredientsInfo(RemoveDto removeDto) {
	return fabricIngredientsInfoService.delFabricIngredientsInfo(removeDto);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public FabricIngredientsInfo getById(@PathVariable("id") String id) {
		return fabricIngredientsInfoService.getById(id);
	}


}































