/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.controller;

import com.base.sbc.config.annotation.OperaLog;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.OperationType;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.dto.IdDto;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackProcessPriceDto;
import com.base.sbc.module.pack.service.PackProcessPriceService;
import com.base.sbc.module.pack.vo.PackProcessPriceVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 类描述：资料包-工序工价 Controller类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.web.PackProcessPriceController
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-5 14:12:07
 */
@RestController
@Api(tags = "资料包-工序工价")
@RequestMapping(value = BaseController.SAAS_URL + "/packProcessPrice", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PackProcessPriceController {

    @Autowired
    private PackProcessPriceService packProcessPriceService;

    @ApiOperation(value = "分页查询")
    @GetMapping
    public PageInfo<PackProcessPriceVo> page(@Valid PackCommonPageSearchDto dto) {
        return packProcessPriceService.pageInfo(dto);
    }

    @ApiOperation(value = "明细-通过id查询")
    @GetMapping("/detail")
    public PackProcessPriceVo getById(@Valid IdDto idDto) {
        return packProcessPriceService.getDetail(idDto.getId());
    }

    @ApiOperation(value = "删除-通过id查询,多个逗号分开")
    @DeleteMapping()
    public Boolean removeById(@Valid IdsDto ids) {
        return packProcessPriceService.removeByIds(StringUtils.convertList(ids.getId()));
    }

    @ApiOperation(value = "保存/修改", notes = "id为空新增、不为空修改")
    @PostMapping
    @OperaLog(value = "工序工价", operationType = OperationType.INSERT_UPDATE, parentIdSpEl = "#p0.foreignId", service = PackProcessPriceService.class)
    public PackProcessPriceVo save(@RequestBody PackProcessPriceDto dto) {
        return packProcessPriceService.saveByDto(dto);
    }

    @PostMapping("/saveBatch")
    @ApiOperation(value = "保存全部")
    public boolean save(@Valid PackCommonSearchDto commonDto, @RequestBody List<PackProcessPriceDto> dtoList) {
        return packProcessPriceService.saveBatchByDto(commonDto, dtoList);
    }

}































