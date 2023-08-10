/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.patternmaking.dto.WorkLogSaveDto;
import com.base.sbc.module.patternmaking.dto.WorkLogSearchDto;
import com.base.sbc.module.patternmaking.entity.WorkLog;
import com.base.sbc.module.patternmaking.service.WorkLogService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 类描述：工作小账 Controller类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.web.WorkLogController
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-8-10 19:29:31
 */
@RestController
@Api(tags = "工作小账")
@RequestMapping(value = BaseController.SAAS_URL + "/workLog", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class WorkLogController {

    @Autowired
    private WorkLogService workLogService;

    @ApiOperation(value = "分页查询")
    @GetMapping
    public PageInfo<WorkLog> page(WorkLogSearchDto dto) {
        return workLogService.pageInfo(dto);
    }

    @ApiOperation(value = "明细-通过id查询")
    @GetMapping("/{id}")
    public WorkLog getById(@PathVariable("id") String id) {
        return workLogService.getById(id);
    }

    @ApiOperation(value = "删除-通过id查询,多个逗号分开")
    @DeleteMapping("/{id}")
    public Boolean removeById(@PathVariable("id") String id) {
        List<String> ids = StringUtils.convertList(id);
        return workLogService.removeByIds(ids);
    }

    @ApiOperation(value = "保存")
    @PostMapping
    public WorkLog save(@RequestBody WorkLogSaveDto workLog) {
        return workLogService.saveByDto(workLog);

    }

    @ApiOperation(value = "修改")
    @PutMapping
    public WorkLog update(@RequestBody WorkLog workLog) {
        boolean b = workLogService.updateById(workLog);
        return workLog;
    }

}































