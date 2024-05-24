/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.basicsdatum.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.constant.BaseConstant;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumSupplierDto;
import com.base.sbc.module.basicsdatum.dto.QueryRevampBasicsdatumSupplierDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSupplier;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSupplierService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumSupplierVo;
import com.base.sbc.module.basicsdatum.vo.SelectVo;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
* 类描述：基础资料-供应商 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumSupplierController
* @author mengfanjiang
* @email 2915350015@qq.com
* @date 创建时间：2023-5-22 10:51:07
* @version 1.0
*/
@RestController
@Api(tags = "基础资料-供应商")
@RequestMapping(value = BaseController.SAAS_URL + "/basicsdatumSupplier", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumSupplierController{

	@Autowired
	private BasicsdatumSupplierService basicsdatumSupplierService;

	@Autowired
	private BaseController baseController;

	@ApiOperation(value = "下拉组件查询")
	@GetMapping("/getBasicsdatumSupplierSelect")
	public List<SelectVo> getBasicsdatumSupplierSelect(@RequestParam(value = "name", required = false) String name) {
		List<BasicsdatumSupplier> list = basicsdatumSupplierService
				.list(new QueryWrapper<BasicsdatumSupplier>().select("supplier_code,supplier")
						.eq("company_code", baseController.getUserCompany()).eq("status", "0")
						.like(StringUtils.isNotBlank(name), "supplier", name));// .last("limit 50")
		List<SelectVo> newList = new ArrayList<>();
		SelectVo vo;
		for (BasicsdatumSupplier bs : list) {
			vo = new SelectVo();
			vo.setCode(bs.getSupplierCode());
			vo.setName(bs.getSupplier());
			newList.add(vo);
		}
		return newList;
	}
	@ApiOperation(value = "下拉组件查询")
	@GetMapping("/selectSupplierPage")
	public PageInfo<SelectVo> selectSupplierPage(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany,QueryRevampBasicsdatumSupplierDto queryRevampBasicsdatumSupplierDto) {
		queryRevampBasicsdatumSupplierDto.setCompanyCode(userCompany);
		if(null == queryRevampBasicsdatumSupplierDto.getPageNum() || null == queryRevampBasicsdatumSupplierDto.getPageSize()){
			queryRevampBasicsdatumSupplierDto.setPageNum(Page.PAGE_NUM);
			queryRevampBasicsdatumSupplierDto.setPageSize(Page.PAGE_SIZE);
		}
		return basicsdatumSupplierService.selectSupplierPage(queryRevampBasicsdatumSupplierDto);
	}

	@ApiOperation(value = "下拉组件查询")
	@GetMapping("/selectSupplierExitPage")
	public PageInfo<SelectVo> selectSupplierExitPage(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany,QueryRevampBasicsdatumSupplierDto queryRevampBasicsdatumSupplierDto) {
		queryRevampBasicsdatumSupplierDto.setCompanyCode(userCompany);
		if(null == queryRevampBasicsdatumSupplierDto.getPageNum() || null == queryRevampBasicsdatumSupplierDto.getPageSize()){
			queryRevampBasicsdatumSupplierDto.setPageNum(Page.PAGE_NUM);
			queryRevampBasicsdatumSupplierDto.setPageSize(Page.PAGE_SIZE);
		}
		return basicsdatumSupplierService.selectSupplierPage(queryRevampBasicsdatumSupplierDto);
	}



	@ApiOperation(value = "分页查询")
	@GetMapping("/getBasicsdatumSupplierList")
	public PageInfo<BasicsdatumSupplierVo> getBasicsdatumSupplierList(QueryRevampBasicsdatumSupplierDto queryRevampBasicsdatumSupplierDto) {
		return  basicsdatumSupplierService.getBasicsdatumSupplierList(queryRevampBasicsdatumSupplierDto);
	}

	@ApiOperation(value = "/导入")
	@PostMapping("/basicsdatumSupplierImportExcel")
	@DuplicationCheck(type = 1,time = 3600,message = "文件正在导入中，请稍后再试...")
	public Boolean basicsdatumSupplierImportExcel(@RequestParam("file") MultipartFile file) throws Exception {
		return basicsdatumSupplierService.basicsdatumSupplierImportExcel(file);
	}

	@ApiOperation(value = "/导出")
	@GetMapping("/basicsdatumSupplierDeriveExcel")
	public void basicsdatumSupplierDeriveExcel(QueryRevampBasicsdatumSupplierDto queryRevampBasicsdatumSupplierDto,HttpServletResponse response) throws Exception {
       basicsdatumSupplierService.basicsdatumSupplierDeriveExcel(queryRevampBasicsdatumSupplierDto,response);
	}

	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopBasicsdatumSupplier")
	public Boolean startStopBasicsdatumSupplier(@Valid @RequestBody StartStopDto startStopDto) {
	return basicsdatumSupplierService.startStopBasicsdatumSupplier(startStopDto);
	}


	@ApiOperation(value = "新增修改基础资料-供应商")
	@PostMapping("/addRevampBasicsdatumSupplier")
	public Boolean addRevampBasicsdatumSupplier(@Valid @RequestBody AddRevampBasicsdatumSupplierDto addRevampBasicsdatumSupplierDto) {
	return basicsdatumSupplierService.addRevampBasicsdatumSupplier(addRevampBasicsdatumSupplierDto);
	}

	@ApiOperation(value = "删除基础资料-供应商")
	@DeleteMapping("/delBasicsdatumSupplier")
	public Boolean delBasicsdatumSupplier(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return basicsdatumSupplierService.delBasicsdatumSupplier(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public BasicsdatumSupplier getById(@PathVariable("id") String id) {
		return basicsdatumSupplierService.getById(id);
	}

	@ApiOperation(value = "分页查询(弹窗)")
	@GetMapping("/getSupplierListPopup")
	public PageInfo<BasicsdatumSupplierVo> getSupplierListPopup(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, QueryRevampBasicsdatumSupplierDto queryRevampBasicsdatumSupplierDto) {
		return  basicsdatumSupplierService.getSupplierListPopup(queryRevampBasicsdatumSupplierDto);
	}
}































