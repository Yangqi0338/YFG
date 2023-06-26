package com.base.sbc.module.test.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.smp.SmpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController extends BaseController {

    private final SmpService smpService;
    @RequestMapping("/smp")
    public ApiResult Smp(){
        smpService.testBom();
     return   selectSuccess("123");
    }
}
