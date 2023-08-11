/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.controller;

import com.alibaba.fastjson2.JSON;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.sample.dto.SamplePageDto;
import com.base.sbc.module.sample.dto.SampleSaveDto;
import com.base.sbc.module.sample.dto.SampleSearchDTO;
import com.base.sbc.module.sample.entity.SampleItemLog;
import com.base.sbc.module.sample.service.SampleItemLogService;
import com.base.sbc.module.sample.service.SampleItemService;
import com.base.sbc.module.sample.service.SampleService;
import com.base.sbc.module.sample.vo.SampleItemVO;
import com.base.sbc.module.sample.vo.SampleVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

/**
 * 类描述：样衣管理 Controller类
 *
 * @address com.base.sbc.module.sample.web.SampleManagerController
 */
@RestController
@Api(tags = "样衣管理相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/sampleManager", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class SampleManagerController extends BaseController {

    @Autowired
    private SampleService sampleService;
    @Autowired
    private SampleItemService sampleItemService;
    @Autowired
    private SampleItemLogService sampleItemLogService;

    @ApiOperation(value = "保存")
    @PostMapping("/save")
    public SampleVo save(@RequestBody SampleSaveDto dto) {
        return sampleService.save(dto);
    }

    @ApiOperation(value = "分页查询-设计款号维度")
    @GetMapping("/getListByDesignNo")
    public PageInfo getListByDesignNo(@Valid SamplePageDto dto) {
        return sampleService.queryPageInfo(dto);
    }

    @ApiOperation(value = "分页查询-样衣明细维度")
    @GetMapping("/getListBySampleItem")
    public PageInfo getListBySampleItem(@Valid SamplePageDto dto) {
        return sampleItemService.queryPageInfo(dto);
    }

    @ApiOperation(value = "详情")
    @GetMapping("/{id}")
    public SampleVo getDetail(@PathVariable("id") String id) {
        return sampleService.getDetail(id);
    }

    @ApiOperation(value = "根据样衣明细ID获取日志")
    @GetMapping("/getLogList")
    public List<SampleItemLog> getLogListBySampleItemId(@Valid @NotBlank(message = "样衣id不可为空") String sampleItemId, String type) {
        return sampleItemLogService.getListBySampleItemId(sampleItemId, type);
    }

    @ApiOperation(value = "导入Excel")
    @PostMapping("/importExcel")
    public ApiResult importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        Boolean b = sampleService.importExcel(file);
        return insertSuccess(b);
    }

    @ApiOperation(value = "更新状态")
    @PostMapping("/updateStatus")
    public SampleVo updateStatus(@RequestBody SampleSaveDto dto) {
        return sampleService.updateStatus(dto);
    }

    @ApiModelProperty("获取样衣列表")
    @PostMapping("/getSampleItemList")
    public PageInfo<SampleItemVO> getSampleItemList(@Valid @RequestBody SampleSearchDTO dto) {
        dto.setCompanyCode(super.getUserCompany());
        return sampleService.getSampleItemList(dto);
    }


    @ApiModelProperty("启用停用")
    @GetMapping("/enableOrDeactivate")
    public ApiResult enableOrDeactivate(@Valid @NotBlank(message = "样衣id不可为空") String id,
                                        @Valid @NotBlank(message = "状态不可为空") String enableFlag) {
        sampleItemService.enableOrDeactivate(id, enableFlag);
        return updateSuccess("操作成功");
    }

    @ApiModelProperty("提交审批")
    @GetMapping("/submit")
    public ApiResult submit(@Valid @NotBlank(message = "id不可为空") String id) {
        sampleService.submit(id);
        return updateSuccess("操作成功");
    }

    /**
     * 处理审批
     *
     * @param dto
     * @return
     */
    @ApiIgnore
    @PostMapping("/approval")
    public boolean approval(@RequestBody AnswerDto dto) {
        logger.info("=======测试====== dto:{}", JSON.toJSONString(dto));
        return true;
    }

}