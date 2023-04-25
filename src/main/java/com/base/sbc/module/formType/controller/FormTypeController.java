/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formType.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.formType.dto.*;
import com.base.sbc.module.formType.service.FormTypeGroupService;
import com.base.sbc.module.formType.service.FormTypeService;
import com.base.sbc.module.formType.vo.PagingFormTypeVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 类描述：表单类型 Controller类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.formType.web.FormTypeController
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-15 9:17:01
 */
@RestController
@Api(value = "表单类型", description = "与表单类型相关的所有接口信息", tags = {"表单类型接口"})
@RequestMapping(value = BaseController.SAAS_URL + "/formTypes", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FormTypeController {

    @Autowired
    private FormTypeService formTypeService;
    @Autowired
    private FormTypeGroupService formTypeGroupService;

    /*
     * 分页获取表单类型及分组
     * */
    @ApiOperation(value = "分页获取表单类型", notes = "")
    @GetMapping("/getFormTypeIsGroup")
    public PageInfo<PagingFormTypeVo> getFormTypeIsGroup(@Valid QueryFormTypeDto queryFormTypeDto) {
        return formTypeService.getFormTypeIsGroup(queryFormTypeDto);
    }


    /*
     * 保存修改表单分组
     * */
    @ApiOperation(value = "保存修改表单分组", notes = "")
    @PostMapping("/saveUpdateGroup")
    public ApiResult saveUpdateGroup(@Valid @RequestBody SaveUpdateFormTypeGroupDto formTypeGroupDto) {
        return formTypeGroupService.saveUpdateGroup(formTypeGroupDto);
    }

    /*
     * 保存修改表单类型
     * */
    @ApiOperation(value = "保存修改表单类型", notes = "")
    @PostMapping("/saveUpdateType")
    public ApiResult saveUpdateType(@Valid @RequestBody SaveUpdateFormTypeDto saveUpdateFormTypeDto) {
        return formTypeService.saveUpdateType(saveUpdateFormTypeDto);
    }


    /**
     * 表单类型及分组 启动 停止
     */
    @ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
    @PostMapping("/formStartStop")
    public ApiResult formStartStop(@Valid @RequestBody FormStartStopDto formStartStopDto) {
        return formTypeService.formStartStop(formStartStopDto);
    }

	/**
	 * 表单类型及分组 删除
	 */
	@ApiOperation(value = "表单类型及分组 删除", notes = "")
	@PostMapping("/formDelete")
	public ApiResult formDelete(@Valid @RequestBody FormDeleteDto formDeleteDto) {
		return formTypeService.formDelete(formDeleteDto);
	}


    /**
     *  获取数据库名及表单分组
     */
    @ApiOperation(value = "获取数据库名及表单分组", notes = "")
    @GetMapping("/getGroupIsCoding")
    public ApiResult getGroupIsCoding() {
        return formTypeGroupService.getGroupIsCoding();
    }

    /**
     *  获表单类型表单类型
     */
    @ApiOperation(value = "获取数据库名及表单分组", notes = "")
    @GetMapping("/getFormType")
    public ApiResult getFormType(  QueryFormTypeDto queryFormTypeDto) {
        return formTypeService.getFormType(queryFormTypeDto);
    }

}
