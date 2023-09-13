/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.fabric.dto.FabricDevApplyAllocationDTO;
import com.base.sbc.module.fabric.dto.FabricDevApplySaveDTO;
import com.base.sbc.module.fabric.dto.FabricDevSearchDTO;
import com.base.sbc.module.fabric.service.FabricDevApplyService;
import com.base.sbc.module.fabric.service.FabricDevInfoService;
import com.base.sbc.module.fabric.vo.FabricDevApplyListVO;
import com.base.sbc.module.fabric.vo.FabricDevConfigInfoVO;
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
 * 类描述：面料开发申请 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.web.FabricDevApplyController
 * @email your email
 * @date 创建时间：2023-8-17 9:57:28
 */
@RestController
@Api(tags = "面料开发申请")
@RequestMapping(value = BaseController.SAAS_URL + "/fabricDevApply", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class FabricDevApplyController extends BaseController {

    @Autowired
    private FabricDevApplyService fabricDevApplyService;
    @Autowired
    private FabricDevInfoService fabricDevInfoService;

    @ApiOperation(value = "分页查询")
    @PostMapping("/getDevApplyList")
    public PageInfo<FabricDevApplyListVO> getDevApplyList(@Valid @RequestBody FabricDevSearchDTO dto) {
        return fabricDevApplyService.getDevApplyList(dto);
    }

    @ApiOperation(value = "保存")
    @PostMapping("/devAppSave")
    public ApiResult devAppSave(@Valid @RequestBody FabricDevApplySaveDTO fabricDevApplySaveDTO) {
        return updateSuccess(fabricDevApplyService.devAppSave(fabricDevApplySaveDTO));
    }

    @ApiOperation(value = "获取详情")
    @GetMapping("/getDetail")
    public ApiResult getDetail(@Valid @NotBlank(message = "开发申请id不可为空") String id) {
        return selectSuccess(fabricDevApplyService.getDetail(id));
    }

    @ApiOperation(value = "分配任务")
    @PostMapping("/allocationTasks")
    public ApiResult allocationTasks(@Valid @RequestBody FabricDevApplyAllocationDTO fabricDevApplyAssignDTO) {
        fabricDevApplyService.allocationTasks(fabricDevApplyAssignDTO);
        return updateSuccess("操作成功");
    }


    @ApiOperation(value = "获取开发申请信息")
    @GetMapping("/getDevApplyList")
    public PageInfo<FabricDevConfigInfoVO> getDevApplyList(String devApplyCode, Integer pageNum, Integer pageSize) {
        return fabricDevInfoService.getByDevApplyCode(devApplyCode, pageNum, pageSize);
    }
}