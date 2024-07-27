/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.fabric.dto.DelDTO;
import com.base.sbc.module.workload.dto.WorkloadRatingConfigDTO;
import com.base.sbc.module.workload.service.WorkloadRatingConfigService;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigQO;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * 类描述：工作量评分配置 Controller类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.workload.web.WorkloadRatingConfigController
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 13:27:45
 */
@RestController
@Api(tags = "工作量评分配置")
@RequestMapping(value = BaseController.SAAS_URL + "/workloadRatingConfig", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WorkloadRatingConfigController extends BaseController {

    @Autowired
    private WorkloadRatingConfigService workloadRatingConfigService;

    @ApiOperation(value = "分页查询")
    @GetMapping("queryPageInfo")
    public ApiResult<PageInfo<WorkloadRatingConfigVO>> queryPageInfo(@Validated WorkloadRatingConfigQO qo) {
        return selectSuccess(workloadRatingConfigService.queryPageInfo(qo));
    }

    @ApiOperation(value = "明细-通过id查询")
    @GetMapping("/detail/{id}")
    public ApiResult<WorkloadRatingConfigDTO> detail(@NotBlank(message = "id不能为空") @PathVariable("id") String id) {
        return selectSuccess(workloadRatingConfigService.detail(id));
    }

    @ApiOperation(value = "删除")
    @PostMapping("delByIds")
    public ApiResult<Boolean> delByIds(@Validated @RequestBody DelDTO delDto) {
        return deleteSuccess(workloadRatingConfigService.delByIds(delDto.getIds()));
    }

    @ApiOperation(value = "保存")
    @PostMapping("save")
    public ApiResult<WorkloadRatingConfigDTO> save(@Validated @RequestBody WorkloadRatingConfigDTO workloadRatingConfig) {
        workloadRatingConfigService.save(workloadRatingConfig);
        return updateSuccess(workloadRatingConfig);
    }

}































