package com.base.sbc.open.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.SmpProperties;
import com.base.sbc.config.enums.business.PushRespStatus;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.resttemplate.RequestLoggingInterceptor;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageSystemDTO;
import com.base.sbc.module.pushrecords.entity.PushRecords;
import com.base.sbc.module.pushrecords.service.PushRecordsService;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.open.dto.StyleColorRiskAssessmentDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@Api(tags = "款式配色表")
@RequestMapping(value = BaseController.OPEN_URL + "/styleColor", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
@RequiredArgsConstructor
public class OpenStyleColorController {
    private static final Logger log = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    @Autowired
    private StyleColorService styleColorService;

    @Autowired
    private  PushRecordsService pushRecordsService;

    @ApiOperation(value = "ESCM风险评估")
    @PostMapping("/riskAssessment")
    public ApiResult riskAssessment(@RequestBody @Valid StyleColorRiskAssessmentDto dto) {

        QueryWrapper<StyleColor> qw = new QueryWrapper();
        qw.lambda().eq(StyleColor::getStyleNo,dto.getStyleNo());
        qw.lambda().eq(StyleColor::getDelFlag,"0");
        List<StyleColor> list = styleColorService.list(qw);
        if (CollUtil.isEmpty(list)){
            throw new OtherException("大货款号不存在");
        }
        StyleColor styleColor = list.get(0);

        UpdateWrapper<StyleColor> uw = new UpdateWrapper<>();
        uw.lambda().eq(StyleColor::getId,styleColor.getId());
        uw.lambda().set(StyleColor::getStyleDifficulty,dto.getStyleDifficulty());
        uw.lambda().set(StyleColor::getFabricDifficulty,dto.getFabricDifficulty());
        uw.lambda().set(StyleColor::getProcessDifficulty,dto.getProcessDifficulty());
        boolean b = styleColorService.update(uw);
        return b ? ApiResult.success("修改成功") : ApiResult.error("修改失败",500);

    }

    @ApiOperation(value = "ESCM风险评估")
    @PostMapping("/riskAssessmentBatch")
    public ApiResult riskAssessmentBatch(@RequestBody @Valid List<StyleColorRiskAssessmentDto> list) {
        if (CollUtil.isEmpty(list)){
            return ApiResult.success("修改成功");
        }
        PushRespStatus pushStatus = PushRespStatus.SUCCESS;
        String responseMessage = "";
        try {
            List<String> styleNos = list.stream().map(StyleColorRiskAssessmentDto::getStyleNo).collect(Collectors.toList());
            QueryWrapper<StyleColor> qw = new QueryWrapper();
            qw.lambda().in(StyleColor::getStyleNo,styleNos);
            qw.lambda().eq(StyleColor::getDelFlag,"0");
            List<StyleColor> styleColorList = styleColorService.list(qw);
            if (CollUtil.isEmpty(styleColorList)){
                throw new OtherException("大货款号不存在");
            }
            Map<String, StyleColorRiskAssessmentDto> dtoMap = list.stream().collect(Collectors.toMap(StyleColorRiskAssessmentDto::getStyleNo, i -> i));
            for (StyleColor styleColor : styleColorList) {
                StyleColorRiskAssessmentDto dto = dtoMap.get(styleColor.getStyleNo());
                if (null != dto){
                    if (StringUtils.isNotBlank(dto.getStyleDifficulty())){
                        styleColor.setStyleDifficulty(dto.getStyleDifficulty());
                    }
                    if (StringUtils.isNotBlank(dto.getFabricDifficulty())){
                        styleColor.setFabricDifficulty(dto.getFabricDifficulty());
                    }
                    if (StringUtils.isNotBlank(dto.getProcessDifficulty())){
                        styleColor.setProcessDifficulty(dto.getProcessDifficulty());
                    }
                }
            }
            boolean b = styleColorService.updateBatchById(styleColorList);
            if (!b){
                pushStatus = PushRespStatus.FAILURE;
            }
            return b ? ApiResult.success("修改成功") : ApiResult.error("修改失败",500);
        }catch (Exception e){
            pushStatus = PushRespStatus.FAILURE;
            responseMessage = e.getMessage();
            e.printStackTrace();
            log.error("riskAssessmentBatch error = {}",e.getMessage());
            throw e;
        }finally {
            PushRecords pushRecords = new PushRecords();
            String code = String.valueOf(System.currentTimeMillis());
            pushRecords.setRelatedId(code);
            pushRecords.setRelatedName("ESCM风险评估推送接收");
            pushRecords.setBusinessId(code);
            pushRecords.setBusinessCode(code);
            pushRecords.setModuleName("ESCM风险评估推送接收");
            pushRecords.setFunctionName("ESCM风险评估推送接收");
            pushRecords.setPushAddress("/api/open/styleColor/riskAssessmentBatch");
            pushRecords.setPushContent(JSON.toJSONString(list));
            pushRecords.setPushCount(1);
            pushRecords.setPushStatus(pushStatus);
            pushRecords.setResponseMessage(responseMessage);
            pushRecordsService.save(pushRecords);
        }

    }
}
