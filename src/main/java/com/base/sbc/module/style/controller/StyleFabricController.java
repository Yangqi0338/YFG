/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.style.dto.StyleFabricSaveDTO;
import com.base.sbc.module.style.service.StyleFabricService;
import com.base.sbc.module.style.vo.StyleFabricVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 类描述：款式面料 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.style.web.StyleFabricController
 * @email your email
 * @date 创建时间：2023-8-24 10:17:48
 */
@RestController
@Api(tags = "款式面料")
@RequestMapping(value = BaseController.SAAS_URL + "/styleFabric", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class StyleFabricController extends BaseController {

    @Autowired
    private StyleFabricService styleFabricService;

    @ApiOperation(value = "获取列表")
    @GetMapping("/getByStyleId")
    public ApiResult getByStyleId(@Valid @NotBlank(message = "设计档案id不可为空") String styleId) {
        List<StyleFabricVO> styleFabrics = styleFabricService.getByStyleId(styleId);
        return selectSuccess(styleFabrics);
    }

    @ApiOperation(value = "通过id删除")
    @GetMapping("deleteById")
    public Boolean removeById(@Valid @NotBlank(message = "款式面料id不可为空") String id) {
        return styleFabricService.removeById(id);
    }

    @ApiOperation(value = "保存")
    @PostMapping("/save")
    public ApiResult save(@RequestBody List<StyleFabricSaveDTO> dtos) {
        styleFabricService.save(dtos);
        return updateSuccess("操作成功");
    }

    @ApiOperation(value = "修改")
    @PostMapping("/update")
    public ApiResult update(@RequestBody StyleFabricSaveDTO dto) {
        styleFabricService.update(dto);
        return updateSuccess("操作成功");
    }


}































