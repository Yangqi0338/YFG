package com.base.sbc.open.controller;

import com.alibaba.fastjson.JSONObject;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 卞康
 * @date 2023/5/10 9:27:47
 * @mail 247967116@qq.com
 * smp供应商开放接口
 */
@RestController
@RequestMapping(value = BaseController.OPEN_URL + "/smpSupplier", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SmpSupplierController extends BaseController{
    @PostMapping("/save")
    public ApiResult save(@RequestBody JSONObject jsonObject){
        System.out.println(jsonObject);
        return insertSuccess(null);
    }
}
