package com.base.sbc.open.controller;

import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumSupplierDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialQueryDto;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSupplierService;
import com.base.sbc.module.basicsdatum.vo.WarehouseMaterialVo;
import com.base.sbc.module.purchase.entity.DeliveryNotice;
import com.base.sbc.module.purchase.service.DeliveryNoticeService;
import com.base.sbc.open.dto.OpenMaterialDto;
import com.base.sbc.open.dto.OpenMaterialNoticeDto;
import com.base.sbc.open.service.OpenMaterialService;
import com.base.sbc.open.thirdToken.DsLinkMoreScm;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 领猫对接接口
 * @author Liu YuBin
 * @version 1.0
 * @date 2023/8/15 10:06
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(value = BaseController.OPEN_URL + "/openDs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OpenLinkMoreController extends BaseController{

    @Autowired
    private OpenMaterialService materialService;
    @Autowired
    private BasicsdatumSupplierService supplierService;
    @Autowired
    private DeliveryNoticeService deliveryNoticeService;
    @Autowired
    private DsLinkMoreScm linkMoreScm;

    @ApiOperation(value = "物料推送领猫scm测试接口")
    @GetMapping("/getMaterialList")
    public List<WarehouseMaterialVo> getMaterialList(
            @RequestHeader(BaseConstant.USER_COMPANY) String userCompany, BasicsdatumMaterialQueryDto dto) {
        List<OpenMaterialDto> purchaseMaterialList = materialService.getMaterialList("677447590605750272");
        for (OpenMaterialDto materialDto : purchaseMaterialList) {
            String arges = JSON.toJSONString(materialDto);
            HttpResponse authTokenOrSign = linkMoreScm.sendToLinkMore("v1/BaseInfo/updatematerial", arges);
            String body = authTokenOrSign.body();
            System.out.println(body);
            break;
        }
        return null;
    }

    @ApiOperation(value = "领猫scm推送供应商保存")
    @PostMapping("/saveSupplierLinkMore")
    public ApiResult saveSupplierLinkMore(@RequestBody List<AddRevampBasicsdatumSupplierDto> addDtoList){
        return supplierService.addSupplierBatch(addDtoList);
    }

    @ApiOperation(value = "领猫scm推送送货通知单保存")
    @PostMapping("/saveMaterialNotice")
    public ApiResult saveMaterialNotice(@RequestBody List<OpenMaterialNoticeDto> noticeDtoList){
        return deliveryNoticeService.saveNoticeList(noticeDtoList);
    }

}
