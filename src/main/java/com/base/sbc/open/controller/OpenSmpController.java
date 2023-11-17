package com.base.sbc.open.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialIngredient;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSupplier;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialIngredientService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSupplierService;
import com.base.sbc.module.hangtag.service.HangTagService;
import com.base.sbc.module.smp.dto.SmpSampleDto;
import com.base.sbc.module.smp.entity.TagPrinting;
import com.base.sbc.open.dto.MtBpReqDto;
import com.base.sbc.open.entity.*;
import com.base.sbc.open.service.EscmMaterialCompnentInspectCompanyService;
import com.base.sbc.open.service.MtBqReqService;
import com.base.sbc.open.service.OpenSmpService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/5/10 9:27:47
 * @mail 247967116@qq.com
 * smp开放接口
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = BaseController.OPEN_URL + "/smp", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OpenSmpController extends BaseController {

    private final MtBqReqService mtBqReqService;

    private final BasicsdatumSupplierService basicsdatumSupplierService;

    private final AmcService amcService;

    private final HangTagService hangTagService;


    private final EscmMaterialCompnentInspectCompanyService escmMaterialCompnentInspectCompanyService;

    private final OpenSmpService openSmpService;

    private final BasicsdatumMaterialIngredientService basicsdatumMaterialIngredientService;

    private final BasicsdatumMaterialService basicsdatumMaterialService;


    /**
     * bp供应商
     */
    @PostMapping("/supplierSave")
    @ApiOperation(value = "bp供应商新增或者修改", notes = "bp供应商新增或者修改")
    @Transactional(rollbackFor = {Throwable.class})
    public ApiResult supplierSave(@RequestBody JSONObject jsonObject) {
        MtBpReqDto mtBpReqDto = JSONObject.parseObject(jsonObject.toJSONString(), MtBpReqDto.class);


        //先保存传过来的数据对象
        MtBqReqEntity mtBqReqEntity = mtBpReqDto.toMtBqReqEntity();
        mtBqReqService.saveOrUpdate(mtBqReqEntity, new QueryWrapper<MtBqReqEntity>().eq("partner", mtBqReqEntity.getPartner()));
        //再存入供应商
        BasicsdatumSupplier basicsdatumSupplier = mtBqReqEntity.toBasicsdatumSupplier();
        basicsdatumSupplierService.saveOrUpdate(basicsdatumSupplier, new QueryWrapper<BasicsdatumSupplier>().eq("supplier_code", basicsdatumSupplier.getSupplierCode()));
        return insertSuccess(null);
    }

    /**
     * hr-人员
     */
    @PostMapping("/hrUserSave")
    @ApiOperation(value = "hr-人员新增或者修改", notes = "hr-人员新增或者修改")
    public ApiResult hrSave(@RequestBody JSONObject jsonObject) {
        SmpUser smpUser = JSONObject.parseObject(jsonObject.toJSONString(), SmpUser.class);
        smpUser.preInsert();
        smpUser.setCreateName("smp请求");
        smpUser.setUpdateName("smp请求");
        smpUser.setCompanyCode(smpUser.getCompanyId());
        amcService.hrUserSave(smpUser);
        return insertSuccess(null);
    }

    /**
     * hr-部门
     */
    @PostMapping("/hrDeptSave")
    @ApiOperation(value = "hr-部门新增或者修改", notes = "hr-部门新增或者修改")
    public ApiResult hrDeptSave(@RequestBody JSONObject jsonObject) {
        SmpDept smpDept = JSONObject.parseObject(jsonObject.toJSONString(), SmpDept.class);
        smpDept.preInsert();
        smpDept.setCreateName("smp请求");
        smpDept.setUpdateName("smp请求");
        amcService.hrDeptSave(smpDept);
        return insertSuccess(null);
    }

    /**
     * hr-岗位
     */
    @PostMapping("/hrPostSave")
    @ApiOperation(value = "hr-岗位新增或者修改", notes = "hr-岗位新增或者修改")
    public ApiResult hrPostSave(@RequestBody JSONObject jsonObject) {
        SmpPost smpPost = JSONObject.parseObject(jsonObject.toJSONString(), SmpPost.class);
        amcService.hrPostSave(smpPost);
        return insertSuccess(null);
    }


    /**
     * smp-样衣（第一期不做）
     */
    @PostMapping("/smpSampleSave")
    @ApiOperation(value = "smp-样衣新增或者修改", notes = "smp-样衣新增或者修改")
    public ApiResult smpSampleSave(@RequestBody JSONObject jsonObject) {
        SmpSampleDto smpSampleDto = JSONObject.parseObject(jsonObject.toJSONString(), SmpSampleDto.class);
        System.out.println(smpSampleDto);
        return insertSuccess(null);
    }

    /**
     * smp-物料
     */
    @PostMapping("/smpMaterial")
    @ApiOperation(value = "smp-物料", notes = "smp-物料")

    public ApiResult smpMaterial(@RequestBody JSONObject jsonObject) {
        openSmpService.smpMaterial(jsonObject);
        return insertSuccess(null);
    }


    /**
     * 吊牌打印
     */
    @GetMapping("/TagPrinting")
    @ApiOperation(value = "吊牌打印获取", notes = "吊牌打印获取")
    public ApiResult tagPrinting(String id, boolean bl) {
        List<TagPrinting> tagPrintings1 = hangTagService.hangTagPrinting(id, bl);
        return selectSuccess(tagPrintings1);
    }


    /**
     * 面料成分检测数据接口
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/escmMaterialCompnentInspectCompany")
    public ApiResult EscmMaterialCompnentInspectCompanyDto(@RequestBody JSONObject jsonObject){
        EscmMaterialCompnentInspectCompanyDto escmMaterialCompnentInspectCompanyDto = jsonObject.toJavaObject(EscmMaterialCompnentInspectCompanyDto.class);
        escmMaterialCompnentInspectCompanyService.saveOrUpdate(escmMaterialCompnentInspectCompanyDto,new QueryWrapper<EscmMaterialCompnentInspectCompanyDto>().eq("materials_no",escmMaterialCompnentInspectCompanyDto.getMaterialsNo()));

        basicsdatumMaterialIngredientService.remove(new QueryWrapper<BasicsdatumMaterialIngredient>().eq("material_code",escmMaterialCompnentInspectCompanyDto.getMaterialsNo()));
        String quanlityInspectContent="";

        for (int i = 0; i < escmMaterialCompnentInspectCompanyDto.getDetailList().size(); i++) {
            BasicsdatumMaterialIngredient basicsdatumMaterialIngredient =new BasicsdatumMaterialIngredient();
            basicsdatumMaterialIngredient.setMaterialCode(escmMaterialCompnentInspectCompanyDto.getMaterialsNo());
            basicsdatumMaterialIngredient.setCompanyCode(BaseConstant.DEF_COMPANY_CODE);
            basicsdatumMaterialIngredient.setSay(escmMaterialCompnentInspectCompanyDto.getDetailList().get(i).getRemark());
            basicsdatumMaterialIngredient.setRatio(BigDecimal.valueOf(Float.parseFloat(escmMaterialCompnentInspectCompanyDto.getDetailList().get(i).getContentProportion().replace("%",""))));
            basicsdatumMaterialIngredient.setType("0");
            basicsdatumMaterialIngredient.setName(escmMaterialCompnentInspectCompanyDto.getDetailList().get(i).getInspectContent());
            basicsdatumMaterialIngredientService.save(basicsdatumMaterialIngredient);

            quanlityInspectContent+=escmMaterialCompnentInspectCompanyDto.getDetailList().get(i).getContentProportion()+
                    escmMaterialCompnentInspectCompanyDto.getDetailList().get(i).getInspectContent();
            if (StringUtils.isNotEmpty( escmMaterialCompnentInspectCompanyDto.getDetailList().get(i).getRemark())){
                quanlityInspectContent+=escmMaterialCompnentInspectCompanyDto.getDetailList().get(i).getRemark();
            }



            if (i!=escmMaterialCompnentInspectCompanyDto.getDetailList().size()-1){
                quanlityInspectContent+=",\n";
            }
        }

        BasicsdatumMaterial basicsdatumMaterial = basicsdatumMaterialService.getOne(new QueryWrapper<BasicsdatumMaterial>().eq("material_code", escmMaterialCompnentInspectCompanyDto.getMaterialsNo()));

         // quanlityInspectContent = escmMaterialCompnentInspectCompanyDto.getQuanlityInspectContent();
        // if (!StringUtils.isEmpty(quanlityInspectContent)){
        //     quanlityInspectContent = quanlityInspectContent.replace(" ", ",");
        // }
        basicsdatumMaterial.setIngredient(quanlityInspectContent);
        basicsdatumMaterialService.updateById(basicsdatumMaterial);
        return insertSuccess(null);
    }
}
