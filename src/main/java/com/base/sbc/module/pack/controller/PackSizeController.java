/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.controller;

import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.config.annotation.OperaLog;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.OperationType;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.pack.dto.*;
import com.base.sbc.module.pack.service.PackInfoStatusService;
import com.base.sbc.module.pack.service.PackSizeConfigService;
import com.base.sbc.module.pack.service.PackSizeService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackSizeConfigVo;
import com.base.sbc.module.pack.vo.PackSizeVo;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

/**
 * 类描述：资料包-尺寸表 Controller类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.web.PackSizeController
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-1 10:14:51
 */
@RestController
@Api(tags = "资料包-尺寸表")
@RequestMapping(value = BaseController.SAAS_URL + "/packSize", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PackSizeController {

    @Autowired
    private PackSizeService packSizeService;
    @Autowired
    private PackSizeConfigService packSizeConfigService;
    @Autowired
    private PackInfoStatusService packInfoStatusService;

    @ApiOperation(value = "分页查询")
    @GetMapping
    public PageInfo<PackSizeVo> page(@Valid PackCommonPageSearchDto dto) {
        return packSizeService.pageInfo(dto);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id,多个逗号分开", required = true, dataType = "String", paramType = "query"),
    })
    public boolean del(@Valid @NotBlank(message = "id不能为空") String id) {
        return packSizeService.delByIds(id);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存单个")
    @OperaLog(value = "尺寸表", operationType = OperationType.INSERT_UPDATE, pathSpEL = PackUtils.pathSqEL, parentIdSpEl = "#p0.foreignId", service = PackSizeService.class)
    public PackSizeVo save(@Valid @RequestBody PackSizeDto dto) {
        return packSizeService.saveByDto(dto);
    }

    @PostMapping("/saveBatch")
    @ApiOperation(value = "保存全部")
    public boolean save(@Valid PackCommonSearchDto commonDto, @RequestBody List<PackSizeDto> dtoList) {
        return packSizeService.saveBatchByDto(commonDto, dtoList);
    }



    @ApiOperation(value = "锁定")
    @GetMapping("/lock")
    public boolean lock(@Valid PackCommonSearchDto dto) {
        return packInfoStatusService.lockSize(dto.getForeignId(), dto.getPackType());
    }

    @ApiOperation(value = "解锁")
    @GetMapping("/unlock")
    public boolean unlock(@Valid PackCommonSearchDto dto) {
        return packInfoStatusService.unlockSize(dto.getForeignId(), dto.getPackType());
    }

    @ApiOperation(value = "提交审批")
    @GetMapping("/startApproval")
    public boolean startReverseApproval(@Valid PackCommonSearchDto dto) {
        return packInfoStatusService.startApprovalForSize(dto.getForeignId(), dto.getPackType());
    }

    @ApiIgnore
    @PostMapping("/approval")
    public boolean approval(@RequestBody AnswerDto dto) {
        return packInfoStatusService.approvalForSize(dto);
    }


    @GetMapping("/sort")
    @ApiOperation(value = "移动排序")
    public boolean sort(@Validated IdsDto dto) {
        return packSizeService.sort(dto.getId(), "sort");
    }

    @GetMapping("/config")
    @ApiOperation(value = "获取尺寸表配置")
    public PackSizeConfigVo getConfig(@Valid PackCommonSearchDto dto) {
        return packSizeConfigService.getConfig(dto.getForeignId(), dto.getPackType());
    }

    @PostMapping("/saveConfig")
    @ApiOperation(value = "保存尺寸表配置")
    public PackSizeConfigVo saveConfig(@RequestBody PackSizeConfigDto dto) {
        return packSizeConfigService.saveConfig(dto);
    }

    @GetMapping("/configList")
    @ApiOperation(value = "尺寸表配置列表分页查询")
    public PageInfo<PackSizeConfigVo> configList(PackSizeConfigSearchDto dto) {
        return packSizeConfigService.pageInfo(dto);
    }

    @GetMapping("/references")
    @ApiOperation(value = "引用")
    public boolean references(PackSizeConfigReferencesDto dto) {
        return packSizeService.references(dto);
    }

}































