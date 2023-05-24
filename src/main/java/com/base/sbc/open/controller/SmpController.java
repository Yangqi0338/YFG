package com.base.sbc.open.controller;

import com.alibaba.fastjson.JSONObject;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSupplier;
import com.base.sbc.module.smp.dto.SmpSampleDto;
import com.base.sbc.open.dto.MtBpReqDto;
import com.base.sbc.open.dto.SmpDeptDto;
import com.base.sbc.open.dto.SmpPostDto;
import com.base.sbc.open.dto.SmpUserDto;
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
    public ApiResult supplierSave(@RequestBody JSONObject jsonObject) {
        MtBpReqDto mtBpReqDto = JSONObject.parseObject(jsonObject.toJSONString(), MtBpReqDto.class);
        BasicsdatumSupplier basicsdatumSupplier =new BasicsdatumSupplier();
        basicsdatumSupplier.setSupplierCode(mtBpReqDto.getGeneralData().getPartner());
        System.out.println(mtBpReqDto);
        return insertSuccess(null);
    }
    /**
     * hr-人员
     */
    @PostMapping("/hrUserSave")
    public ApiResult hrSave(@RequestBody JSONObject jsonObject) {
        SmpUserDto smpUserDto = JSONObject.parseObject(jsonObject.toJSONString(), SmpUserDto.class);
        System.out.println(smpUserDto);
        return insertSuccess(null);
    }

    /**
     * hr-部门
     */
    @PostMapping("/hrDeptSave")
    public ApiResult hrDeptSave(@RequestBody JSONObject jsonObject) {
        SmpDeptDto smpDeptDto = JSONObject.parseObject(jsonObject.toJSONString(), SmpDeptDto.class);
        System.out.println(smpDeptDto);
        return insertSuccess(null);
    }

    /**
     * hr-岗位
     */
    @PostMapping("/hrPostSave")
    public ApiResult hrPostSave(@RequestBody JSONObject jsonObject) {
        SmpPostDto smpPostDto = JSONObject.parseObject(jsonObject.toJSONString(), SmpPostDto.class);
        System.out.println(smpPostDto);
        return insertSuccess(null);
    }


    /**
     * smp-样衣
     */
    @PostMapping("/smpSampleSave")
    public ApiResult smpSampleSave(@RequestBody JSONObject jsonObject){
        SmpSampleDto smpSampleDto = JSONObject.parseObject(jsonObject.toJSONString(), SmpSampleDto.class);
        System.out.println(smpSampleDto);
        return insertSuccess(null);
    }
}
