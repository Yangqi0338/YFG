/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.planning.dto.FieldDisplaySaveDto;
import com.base.sbc.module.planning.service.FieldDisplayConfigService;
import com.base.sbc.module.planning.vo.FieldDisplayVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 类描述：字段显示隐藏配置 Controller类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.web.FieldDisplayConfigController
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-28 14:22:42
 */
@RestController
@Api(tags = "字段显示隐藏配置")
@RequestMapping(value = BaseController.SAAS_URL + "/fieldDisplayConfig", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class FieldDisplayConfigController {

    @Autowired
    private FieldDisplayConfigService fieldDisplayConfigService;

    @ApiOperation(value = "获取配置", notes = "default为默认配置,user为用户配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型：eg：planningBoard企划看板,styleBoard款式看板", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping("/getConfig")
    public List<FieldDisplayVo> getConfig(@Valid @NotBlank(message = "类型不能为空") String type) {
        return fieldDisplayConfigService.getConfig(type);
    }

    @PostMapping("/saveConfig")
    @ApiOperation(value = "保存用户配置")
    public boolean saveConfig(@Valid @RequestBody FieldDisplaySaveDto dto) {
        return fieldDisplayConfigService.saveConfig(dto);
    }

}































