/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.module.fabric.dto.DelDTO;
import com.base.sbc.module.fabric.dto.EnableOrDeactivateDTO;
import com.base.sbc.module.fabric.dto.FabricDevConfigInfoSaveDTO;
import com.base.sbc.module.fabric.service.FabricDevConfigInfoService;
import com.base.sbc.module.fabric.vo.FabricDevConfigInfoVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 类描述：面料开发配置信息 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.web.FabricDevConfigInfoController
 * @email your email
 * @date 创建时间：2023-8-7 11:01:33
 */
@RestController
@Api(tags = "面料开发配置信息")
@RequestMapping(value = BaseController.SAAS_URL + "/fabricDevConfigInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class FabricDevConfigInfoController extends BaseController {

    @Autowired
    private FabricDevConfigInfoService fabricDevConfigInfoService;

    @ApiOperation(value = "获取开发配置列表")
    @PostMapping("/getDevConfigList")
    public PageInfo<FabricDevConfigInfoVO> getDevConfigList(@Valid @RequestBody Page page) {
        return fabricDevConfigInfoService.getDevConfigList(page);
    }

    @ApiOperation(value = "保存开发配置")
    @PostMapping("/devConfigSave")
    public ApiResult getDevConfigList(@Valid @RequestBody FabricDevConfigInfoSaveDTO dto) {
        return updateSuccess(fabricDevConfigInfoService.devConfigSave(dto));
    }


    @ApiOperation(value = "开发配置-启用或者停用")
    @PostMapping("/enableOrDeactivate")
    public ApiResult enableOrDeactivate(@Valid @RequestBody EnableOrDeactivateDTO dto) {
        fabricDevConfigInfoService.enableOrDeactivate(dto);
        return updateSuccess("操作成功");
    }

    @ApiOperation(value = "开发配置-删除")
    @PostMapping("/del")
    public ApiResult del(@Valid @RequestBody DelDTO dto) {
        fabricDevConfigInfoService.del(dto);
        return updateSuccess("操作成功");
    }


}































