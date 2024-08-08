/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.workload.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.module.workload.service.WorkloadRatingConfigService;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigQO;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类描述：工作量评分选项配置 Controller类
 * @address com.base.sbc.module.workload.web.WorkloadRatingConfigController
 * @author KC
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-7-27 16:19:17
 * @version 1.0
 */
@RestController
@Api(tags = "工作量评分选项配置")
@RequestMapping(value = BaseController.SAAS_URL + "/workloadRatingConfig", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WorkloadRatingConfigController extends BaseController {

    @Autowired
    private WorkloadRatingConfigService workloadRatingConfigService;

    @ApiOperation(value = "分页查询")
    @GetMapping("queryList")
    public ApiResult<List<WorkloadRatingConfigVO>> queryList(@Validated WorkloadRatingConfigQO qo) {
        qo.setIsConfigShow(YesOrNoEnum.YES);
        return selectSuccess(workloadRatingConfigService.queryList(qo));
    }

//    @ApiOperation(value = "明细-通过id查询")
//    @GetMapping("/detail/{id}")
//    public ApiResult<WorkloadRatingConfigDTO> detail(@NotBlank(message = "id不能为空") @PathVariable("id") String id) {
//        return selectSuccess(workloadRatingConfigService.detail(id));
//    }

//    @ApiOperation(value = "保存")
//    @PostMapping("save")
//    public ApiResult<WorkloadRatingConfigDTO> save(@Validated @RequestBody WorkloadRatingConfigDTO workloadRatingConfig) {
//        workloadRatingConfigService.save(workloadRatingConfig);
//        return updateSuccess(workloadRatingConfig);
//    }

}































