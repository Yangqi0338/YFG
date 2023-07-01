/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.pack.dto.PackBomVersionDto;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackBomVersionService;
import com.base.sbc.module.pack.vo.PackBomVersionVo;
import com.github.pagehelper.PageInfo;
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

/**
 * 类描述：资料包-物料清单 Controller类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.web.PackBomController
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-1 16:37:22
 */
@RestController
@Api(tags = "资料包-物料清单")
@RequestMapping(value = BaseController.SAAS_URL + "/packBom", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PackBomController {

    @Autowired
    private PackBomService packBomService;

    @Autowired
    private PackBomVersionService packBomVersionService;

    @ApiOperation(value = "版本列表")
    @GetMapping("/version")
    public PageInfo<PackBomVersionVo> versionPage(@Valid PackCommonPageSearchDto dto) {
        return packBomVersionService.pageInfo(dto);
    }

    @ApiOperation(value = "保存版本信息")
    @PostMapping("/version")
    public PackBomVersionVo saveVersion(@Valid @RequestBody PackBomVersionDto dto) {
        return packBomVersionService.saveVersion(dto);
    }

    @ApiOperation(value = "启用停用")
    @GetMapping("/changeVersionStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "版本id", required = true, dataType = "String", paramType = "query"),
    })
    public boolean changeVersionStatus(@Valid @NotBlank(message = "id不能为空") String id) {
        return packBomVersionService.changeVersionStatus(id);
    }

}































