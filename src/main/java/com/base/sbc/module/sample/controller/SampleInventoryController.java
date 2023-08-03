/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.sample.dto.SampleInventoryPageDto;
import com.base.sbc.module.sample.dto.SampleInventorySaveDto;
import com.base.sbc.module.sample.service.SampleInventoryService;
import com.base.sbc.module.sample.vo.SampleInventoryVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 类描述：样衣盘点 Controller类
 *
 * @address com.base.sbc.module.sample.web.SampleInventoryController
 */
@RestController
@Api(tags = "样衣盘点相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/sampleInventory", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class SampleInventoryController extends BaseController {

    @Autowired
    private SampleInventoryService sampleInventoryService;

    @ApiOperation(value = "保存")
    @PostMapping("/save")
    public ApiResult save(@RequestBody SampleInventorySaveDto dto) {
        return updateSuccess(sampleInventoryService.save(dto));
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/getList")
    public PageInfo getList(@Valid SampleInventoryPageDto dto) {
        return sampleInventoryService.queryPageInfo(dto);
    }

    @ApiOperation(value = "分页查询-样衣明细维度")
    @GetMapping("/getListBySampleItem")
    public PageInfo getListBySampleItem(@Valid SampleInventoryPageDto dto) {
        return sampleInventoryService.getListBySampleItem(dto);
    }

    @ApiOperation(value = "详情")
    @GetMapping("/{id}")
    public SampleInventoryVo getDetail(@PathVariable("id") String id) {
        return sampleInventoryService.getDetail(id);
    }

    @ApiOperation(value = "更新状态")
    @PostMapping("/updateStatus")
    public SampleInventoryVo updateStatus(@RequestBody SampleInventorySaveDto dto) {
        return sampleInventoryService.updateStatus(dto);
    }

    @ApiOperation(value = "开始盘点")
    @PostMapping("/startInventory")
    public ApiResult startInventory(@Valid @NotNull(message = "盘点id不可为空") @RequestBody List<String> inventoryIds) {
        sampleInventoryService.startInventory(inventoryIds);
        return updateSuccess("操作成功");
    }

    @ApiOperation(value = "结束盘点")
    @PostMapping("/endInventory")
    public ApiResult endInventory(@Valid @NotNull(message = "盘点id不可为空") @RequestBody List<String> inventoryIds) {
        sampleInventoryService.endInventory(inventoryIds);
        return updateSuccess("操作成功");
    }

    @ApiOperation(value = "作废")
    @PostMapping("/cancel")
    public ApiResult cancel(@Valid @NotNull(message = "盘点id不可为空") @RequestBody List<String> inventoryIds) {
        sampleInventoryService.cancel(inventoryIds);
        return updateSuccess("操作成功");
    }
}