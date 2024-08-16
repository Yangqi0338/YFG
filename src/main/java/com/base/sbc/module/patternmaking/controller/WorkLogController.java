/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.controller;

import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.patternmaking.dto.WorkLogSaveDto;
import com.base.sbc.module.patternmaking.dto.WorkLogSearchDto;
import com.base.sbc.module.patternmaking.service.WorkLogService;
import com.base.sbc.module.patternmaking.vo.WorkLogVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.locks.ReentrantLock;

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
    private final ReentrantLock lock = new ReentrantLock();
    @ApiOperation(value = "分页查询")
    @GetMapping
    public PageInfo<WorkLogVo> page(WorkLogSearchDto dto) {
        return workLogService.pageInfo(dto);
    }

    @ApiOperation(value = "明细-通过id查询")
    @GetMapping("/{id}")
    public WorkLogVo getById(@PathVariable("id") String id) {
        return BeanUtil.copyProperties(workLogService.getById(id), WorkLogVo.class);
    }

    @ApiOperation(value = "删除-通过id查询,多个逗号分开")
    @DeleteMapping()
    //@OperaLog(value = "工作小账", operationType = OperationType.DELETE , delIdSpEL = "#p0.id",service = WorkLogService.class)
    public Boolean removeById(RemoveDto removeDto) {
        return workLogService.removeByIds(removeDto);
    }

    @ApiOperation(value = "保存")
    @PostMapping
    //@OperaLog(value = "工作小账", operationType = OperationType.INSERT_UPDATE, service = WorkLogService.class)
    public WorkLogVo save(@RequestBody WorkLogSaveDto workLog) {
        WorkLogVo vo = null;
        lock.lock();
        try {
            vo = workLogService.saveByDto(workLog);
        }catch (Exception e){
            throw e;
        }finally {
            lock.unlock();
        }
        return vo;
    }

    @ApiOperation(value = "修改")
    @PutMapping
    //@OperaLog(value = "工作小账", operationType = OperationType.INSERT_UPDATE, service = WorkLogService.class)
    public WorkLogVo update(@RequestBody WorkLogSaveDto workLog) {
        boolean b = workLogService.updateById(workLog, "工作小账");
        return BeanUtil.copyProperties(workLog, WorkLogVo.class);
    }

    @ApiOperation(value = "/导出")
    @GetMapping("/workLogDeriveExcel")
    public void workLogDeriveExcel(WorkLogSearchDto dto,HttpServletResponse response) throws Exception {
        workLogService.workLogDeriveExcel(dto,response);
    }

}































