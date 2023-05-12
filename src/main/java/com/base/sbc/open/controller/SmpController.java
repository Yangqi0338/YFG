package com.base.sbc.open.controller;

import com.alibaba.fastjson.JSONObject;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.open.dto.DTBPReq;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 卞康
 * @date 2023/5/10 9:27:47
 * @mail 247967116@qq.com
 * smp开放接口
 */
@RestController
@RequestMapping(value = BaseController.OPEN_URL + "/smp", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SmpController extends BaseController {

    /**
     * bp供应商
     */
    @PostMapping("/supplierSave")
    public ApiResult supplierSave(@RequestBody DTBPReq dtbpReq) {
        System.out.println(dtbpReq);
        return insertSuccess(null);
    }

    /**
     * hr-人员
     */
    @PostMapping("/hrUserSave")
    public ApiResult hrSave(@RequestBody JSONObject jsonObject) {
        System.out.println(jsonObject);
        return insertSuccess(null);
    }

    /**
     * hr-部门
     */
    @PostMapping("/hrDeptSave")
    public ApiResult hrDeptSave(@RequestBody JSONObject jsonObject) {
        System.out.println(jsonObject);
        return insertSuccess(null);
    }

    /**
     * hr-岗位
     */
    @PostMapping("/hrPostSave")
    public ApiResult hrPostSave(@RequestBody JSONObject jsonObject) {
        System.out.println(jsonObject);
        return insertSuccess(null);
    }
}
