/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.style.dto.LatestCommissioningDateQueryDto;
import com.base.sbc.module.style.entity.LatestCommissioningDate;
import com.base.sbc.module.style.service.LatestCommissioningDateService;
import com.base.sbc.module.style.vo.LatestCommissioningDateExcel;
import com.base.sbc.module.style.vo.LatestCommissioningDateVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 类描述：下单最晚投产日期管理 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.style.web.LatestCommissioningDateController
 * @email your email
 * @date 创建时间：2024-3-11 19:05:30
 */
@RestController
@Api(tags = "下单最晚投产日期管理")
@RequestMapping(value = BaseController.SAAS_URL + "/latestCommissioningDate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class LatestCommissioningDateController {

    @Autowired
    private LatestCommissioningDateService latestCommissioningDateService;

    @ApiOperation(value = "分页查询")
    @GetMapping
    public PageInfo<LatestCommissioningDateVo> page(LatestCommissioningDateQueryDto dto) {
        return latestCommissioningDateService.findPage(dto);
    }

    @ApiOperation(value = "删除-通过id查询,多个逗号分开")
    @DeleteMapping("/{id}")
    public Boolean removeById(@PathVariable("id") String id) {
        List<String> ids = StringUtils.convertList(id);
        return latestCommissioningDateService.removeByIds(ids);
    }

    @ApiOperation(value = "导入Excel")
    @PostMapping("/importExcel")
    public ApiResult importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        List<LatestCommissioningDateExcel> list = ExcelImportUtil.importExcel(file.getInputStream(), LatestCommissioningDateExcel.class, new ImportParams());
        return latestCommissioningDateService.importExcel(list);
    }

    @ApiOperation(value = "数据导出Excel")
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response, LatestCommissioningDateQueryDto dto) throws Exception {
        latestCommissioningDateService.exportExcel(response, dto);
    }

    @ApiOperation(value = "修改")
    @PostMapping("/updateMain")
    public ApiResult updateMain(@RequestBody LatestCommissioningDate vo) {
        return latestCommissioningDateService.updateMain(vo);
    }

}































