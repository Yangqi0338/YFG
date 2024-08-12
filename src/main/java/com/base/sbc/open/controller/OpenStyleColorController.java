package com.base.sbc.open.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageSystemDTO;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.open.dto.StyleColorRiskAssessmentDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Autowired
    private StyleColorService styleColorService;

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
}
