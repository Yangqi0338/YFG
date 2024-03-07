package com.base.sbc.module.style.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.style.dto.StyleAnalyseQueryDto;
import com.base.sbc.module.style.service.StyleAnalyseService;
import com.base.sbc.module.style.vo.StyleAnalyseVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "款式-款式分析")
@RequestMapping(value = BaseController.SAAS_URL + "/styleAnalyse", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class StyleAnalyseController {

    @Autowired
    private StyleAnalyseService styleAnalyseService;

    @ApiOperation(value = "分页查询")
    @GetMapping("/findDesignPage")
    public PageInfo<StyleAnalyseVo> findDesignPage(StyleAnalyseQueryDto dto) {
        return styleAnalyseService.findDesignPage(dto);
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/findStylePage")
    public PageInfo<StyleAnalyseVo> findStylePage(StyleAnalyseQueryDto dto) {
        return styleAnalyseService.findStylePage(dto);
    }

}
