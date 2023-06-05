/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.controller;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.patternmaking.dto.PatternMakingWorkLogSaveDto;
import com.base.sbc.module.patternmaking.dto.PatternMakingWorkLogSearchDto;
import com.base.sbc.module.patternmaking.service.PatternMakingWorkLogService;
import com.base.sbc.module.patternmaking.vo.PatternMakingWorkLogVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 类描述：打版管理-工作记录 Controller类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.web.PatternMakingWorkLogController
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-5 19:42:22
 */
@RestController
@Api(tags = "打版管理-工作记录")
@RequestMapping(value = BaseController.SAAS_URL + "/patternMakingWorkLog", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PatternMakingWorkLogController {

    @Autowired
    private PatternMakingWorkLogService patternMakingWorkLogService;

    @ApiOperation(value = "列表查询")
    @GetMapping
    public List<PatternMakingWorkLogVo> list(@Valid PatternMakingWorkLogSearchDto dto) {
        List<PatternMakingWorkLogVo> list = patternMakingWorkLogService.findList(dto);
        return list;
    }

    @ApiOperation(value = "删除-通过id查询,多个逗号分开")
    @DeleteMapping("/{id}")
    public Boolean removeById(@PathVariable("id") String id) {
        List<String> ids = StringUtils.convertList(id);
        return patternMakingWorkLogService.removeByIds(ids);
    }

    @ApiOperation(value = "保存")
    @PostMapping
    public boolean save(@RequestBody PatternMakingWorkLogSaveDto dto) {
        return patternMakingWorkLogService.saveLog(dto);

    }

    @ApiOperation(value = "修改")
    @PutMapping
    public boolean update(@RequestBody PatternMakingWorkLogSaveDto dto) {
        if (StrUtil.isBlank(dto.getId())) {
            throw new OtherException("id不能为空");
        }
        return patternMakingWorkLogService.updateLog(dto);
    }

}































