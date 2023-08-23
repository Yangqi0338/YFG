/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.fabric.dto.FabricDevMainSaveDTO;
import com.base.sbc.module.fabric.dto.FabricDevSearchDTO;
import com.base.sbc.module.fabric.service.FabricDevInfoService;
import com.base.sbc.module.fabric.service.FabricDevMainInfoService;
import com.base.sbc.module.fabric.vo.FabricDevConfigInfoVO;
import com.base.sbc.module.fabric.vo.FabricDevMainListVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 类描述：面料开发信息 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.web.FabricDevInfoController
 * @email your email
 * @date 创建时间：2023-8-17 9:57:39
 */
@RestController
@Api(tags = "面料开发信息")
@RequestMapping(value = BaseController.SAAS_URL + "/fabricDevInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class FabricDevInfoController extends BaseController {

    @Autowired
    private FabricDevMainInfoService fabricDevMainInfoService;
    @Autowired
    private FabricDevInfoService fabricDevInfoService;

    @ApiOperation(value = "分页查询")
    @PostMapping("/getDevApplyList")
    public PageInfo<FabricDevMainListVO> getDevApplyList(@Valid @RequestBody FabricDevSearchDTO dto) {
        return fabricDevMainInfoService.getDevList(dto);
    }

    @ApiOperation(value = "修改")
    @PostMapping("/update")
    public ApiResult update(@Valid @RequestBody FabricDevMainSaveDTO fabricDevMainSaveDTO) {
        fabricDevMainInfoService.update(fabricDevMainSaveDTO);
        return updateSuccess("修改成功");
    }

    @ApiOperation(value = "获取详情")
    @GetMapping("/getDetail")
    public ApiResult getDetail(@Valid @NotBlank(message = "开发id不可为空") String id) {
        return selectSuccess(fabricDevMainInfoService.getDetail(id));
    }


    @ApiOperation(value = "通过开发主信息id获取")
    @GetMapping("/getByDevMainId")
    public PageInfo<FabricDevConfigInfoVO> getByDevMainId(@Valid @NotBlank(message = "开发主信息id不可为空") String devMainId, Integer pageNum, Integer pageSize) {
        return fabricDevInfoService.getByDevMainId(devMainId, pageNum, pageSize);
    }

    @ApiOperation(value = "更新开发附件")
    @GetMapping("/updateFile")
    public ApiResult updateFile(@Valid @NotBlank(message = "开发id不可为空") String devId,
                                @Valid @NotBlank(message = "附件不可为空") String attachmentUrl) {
        fabricDevInfoService.updateFile(devId, attachmentUrl);
        return updateSuccess("更新成功");
    }

    @ApiOperation(value = "更新开发状态")
    @GetMapping("/updateDevStatus")
    public ApiResult updateDevStatus(@Valid @NotBlank(message = "开发主信息id不可为空") String id,
                                     @Valid @NotBlank(message = "开发不可为空") String devId,
                                     @Valid @NotBlank(message = "开发状态不可为空") String status) {
        fabricDevMainInfoService.updateDevStatus(id, devId, status);
        return updateSuccess("更新成功");
    }

}































