/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.controller;

import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.fabric.dto.FabricPlanningSearchDTO;
import com.base.sbc.module.fabric.dto.FabricPoolSaveDTO;
import com.base.sbc.module.fabric.service.FabricPoolService;
import com.base.sbc.module.fabric.vo.FabricPoolListVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * 类描述：面料池 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.web.FabricPoolController
 * @email your email
 * @date 创建时间：2023-8-23 11:02:50
 */
@RestController
@Api(tags = "面料池")
@RequestMapping(value = BaseController.SAAS_URL + "/fabricPool", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class FabricPoolController extends BaseController {

    @Autowired
    private FabricPoolService fabricPoolService;


    @ApiOperation(value = "分页查询")
    @PostMapping("/getFabricPoolList")
    public PageInfo<FabricPoolListVO> getFabricPoolList(@Valid @RequestBody FabricPlanningSearchDTO dto) {
        return fabricPoolService.getFabricPoolList(dto);
    }

    @ApiOperation(value = "获取详情")
    @GetMapping("/getDetail")
    public ApiResult getDetail(@Valid @NotBlank(message = "面料池id不可为空") String id) {
        return selectSuccess(fabricPoolService.getDetail(id));
    }


    @ApiOperation(value = "保存")
    @PostMapping("/save")
    public ApiResult save(@Valid @RequestBody FabricPoolSaveDTO dto) {
        fabricPoolService.save(dto);
        return updateSuccess("操作成功");
    }

    /**
     * 处理审批
     *
     * @param dto
     * @return
     */
    @ApiIgnore
    @PostMapping("/approval")
    public boolean approval(@RequestBody AnswerDto dto) {
        return fabricPoolService.approval(dto);
    }

    @ApiOperation(value = "获取详情")
    @GetMapping("/delete")
    public ApiResult delete(@Valid @NotBlank(message = "面料池id不可为空") String id) {
        fabricPoolService.delete(id);
        return deleteSuccess("删除成功");
    }

}

