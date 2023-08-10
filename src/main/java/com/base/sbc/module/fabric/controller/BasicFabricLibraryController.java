///******************************************************************************
//* Copyright (C) 2018 广州尚捷科技有限责任公司
//* All Rights Reserved.
//* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
//* 不得使用、复制、修改或发布本软件.
//*****************************************************************************/
//package com.base.sbc.module.fabric.controller;
//import com.base.sbc.config.common.base.BaseController;
//import com.base.sbc.config.common.base.Page;
//import com.base.sbc.config.utils.StringUtils;
//import com.base.sbc.module.fabric.entity.BasicFabricLibrary;
//import com.base.sbc.module.fabric.service.BasicFabricLibraryService;
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
//* 类描述：基础面料库 Controller类
//* @address com.base.sbc.module.fabric.web.BasicFabricLibraryController
//* @author your name
//* @email your email
//* @date 创建时间：2023-8-7 11:01:16
//* @version 1.0
//*/
//@RestController
//@Api(tags = "基础面料库")
//@RequestMapping(value = BaseController.SAAS_URL + "/basicFabricLibrary", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//@Validated
//public class BasicFabricLibraryController{
//
//	@Autowired
//	private BasicFabricLibraryService basicFabricLibraryService;
//
//	@ApiOperation(value = "分页查询")
//	@GetMapping
//	public PageInfo<BasicFabricLibrary> page(Page page) {
//		PageHelper.startPage(page);
//		List<BasicFabricLibrary> list = basicFabricLibraryService.list();
//		return new PageInfo<>(list);
//	}
//
//	@ApiOperation(value = "明细-通过id查询")
//	@GetMapping("/{id}")
//	public BasicFabricLibrary getById(@PathVariable("id") String id) {
//		return basicFabricLibraryService.getById(id);
//	}
//
//	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
//	@DeleteMapping("/{id}")
//	public Boolean removeById(@PathVariable("id") String id) {
//		List<String> ids = StringUtils.convertList(id);
//		return basicFabricLibraryService.removeByIds(ids);
//	}
//
//	@ApiOperation(value = "保存")
//	@PostMapping
//	public BasicFabricLibrary save(@RequestBody BasicFabricLibrary basicFabricLibrary) {
//		basicFabricLibraryService.save(basicFabricLibrary);
//		return basicFabricLibrary;
//	}
//
//	@ApiOperation(value = "修改")
//	@PutMapping
//	public BasicFabricLibrary update(@RequestBody BasicFabricLibrary basicFabricLibrary) {
//		boolean b = basicFabricLibraryService.updateById(basicFabricLibrary);
//		if (!b) {
//			//影响行数为0（数据未改变或者数据不存在）
//			//返回影响行数需要配置jdbcURL参数useAffectedRows=true
//		}
//		return basicFabricLibrary;
//	}
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
