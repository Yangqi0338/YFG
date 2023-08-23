/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.fabric.dto.BasicFabricLibrarySaveDTO;
import com.base.sbc.module.fabric.dto.BasicFabricLibrarySearchDTO;
import com.base.sbc.module.fabric.service.BasicFabricLibraryService;
import com.base.sbc.module.fabric.vo.BasicFabricLibraryListVO;
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
 * 类描述：基础面料库 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.web.BasicFabricLibraryController
 * @email your email
 * @date 创建时间：2023-8-17 9:57:23
 */
@RestController
@Api(tags = "基础面料库")
@RequestMapping(value = BaseController.SAAS_URL + "/basicFabricLibrary", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicFabricLibraryController extends BaseController {
    @Autowired
    private BasicFabricLibraryService basicFabricLibraryService;

    @ApiOperation(value = "分页查询")
    @PostMapping("/getBasicFabricLibraryList")
    public PageInfo<BasicFabricLibraryListVO> getBasicFabricLibraryList(@Valid @RequestBody BasicFabricLibrarySearchDTO dto) {
        return basicFabricLibraryService.getBasicFabricLibraryList(dto);
    }

    @ApiOperation(value = "更新")
    @PostMapping("/update")
    public ApiResult update(@Valid @RequestBody BasicFabricLibrarySaveDTO dto) {
        basicFabricLibraryService.update(dto);
        return updateSuccess("操作成功");
    }


    @ApiOperation(value = "获取详情")
    @GetMapping("/getDetail")
    public ApiResult getDetail(@Valid @NotBlank(message = "基础面料库id不可为空") String id) {
        return selectSuccess(basicFabricLibraryService.getDetail(id));
    }

    @ApiOperation(value = "生成物料档案")
    @GetMapping("/generateMaterial")
    public ApiResult generateMaterial(@Valid @NotBlank(message = "基础面料库id不可为空") String id) {
        basicFabricLibraryService.generateMaterial(id);
        return selectSuccess("操作成功");
    }


}































