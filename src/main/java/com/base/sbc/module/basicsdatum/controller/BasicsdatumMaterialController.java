/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.basicsdatum.dto.*;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumIngredient;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialIngredient;
import com.base.sbc.module.basicsdatum.enums.BasicsdatumMaterialBizTypeEnum;
import com.base.sbc.module.basicsdatum.service.BasicsdatumIngredientService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialPriceDetailService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.basicsdatum.vo.*;
import com.base.sbc.module.common.dto.GetMaxCodeRedis;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.entity.PackBomVersion;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackBomVersionService;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.vo.BomSelMaterialVo;
import com.base.sbc.module.patternmaking.vo.StylePmDetailVo;
import com.base.sbc.module.report.dto.MaterialColumnHeadDto;
import com.base.sbc.open.entity.EscmMaterialCompnentInspectCompanyDto;
import com.base.sbc.open.service.EscmMaterialCompnentInspectCompanyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 类描述：基础资料-物料档案
 *
 * @author xiong
 * @version 1.0
 * @date 创建时间：2023-6-26 22:45:23
 */
@RestController
@Api(tags = "基础资料-物料档案")
@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL
        + "/basicsdatumMaterial", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumMaterialController extends BaseController {

    @Autowired
    private BasicsdatumMaterialService basicsdatumMaterialService;

    @Autowired
    private BasicsdatumMaterialPriceDetailService basicsdatumMaterialPriceDetailService;

    @Autowired
    private BaseController baseController;
    private final PackInfoService packInfoService;
    private final PackBomService packBomService;
    private final PackBomVersionService packBomVersionService;

    private final EscmMaterialCompnentInspectCompanyService escmMaterialCompnentInspectCompanyService;

    private final CcmFeignService ccmFeignService;

    private final BasicsdatumIngredientService ingredientService;

    Pattern pattern = Pattern.compile("^(.*?)([0-9]*\\.?[0-9]+)%?(.*?)(?:\\(([^)]*)\\))?$");
    Pattern pattern2 = Pattern.compile("(.*?)(\\S+?)(?:\\(([^)]*)\\))?\\s+([0-9]*\\.?[0-9]+)");

    @ApiOperation(value = "主物料成分转换")
    @GetMapping("/formatIngredient")
    public List<BasicsdatumMaterialIngredient> formatIngredient(
            @RequestParam(value = "value", required = false) String value,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "materialCode", required = false) String materialCode,
            @RequestParam(value = "needCode", required = false) Boolean needCode
    ) {
        //String str = IngredientUtils.format(value);
        return formatToList(value, type, materialCode, needCode);
    }

    /**
     * 转换为对象集合
     *
     * @param str
     * @return
     */
    public List<BasicsdatumMaterialIngredient> formatToList(String str, String type, String materialCode, Boolean needCode) {
        String[] strs = str.split(",");
        List<BasicsdatumMaterialIngredient> list = new ArrayList<>();
        List<BasicBaseDict> pd021DictList = ccmFeignService.getDictInfoToList(SecondIngredientController.uniqueDictCode);

        for (String ingredients : strs) {
            Matcher matcher = pattern.matcher(ingredients.trim());
            BasicsdatumMaterialIngredient in = new BasicsdatumMaterialIngredient();
            if (matcher.matches()) {
                String kindName = matcher.group(1).trim();
                BigDecimal ratio = BigDecimalUtil.valueOf(matcher.group(2));
                String name = matcher.group(3) == null ? "" : matcher.group(3).trim();
                String note = matcher.group(4) == null ? "" : matcher.group(4).trim();
                in.setMaterialKindName(kindName);
                if (StrUtil.isNotBlank(kindName)) {
                    pd021DictList.stream().filter(it -> it.getName().equals(kindName)).findFirst().ifPresent(it-> in.setMaterialKindCode(it.getValue()));
                }
                in.setRatio(ratio);
                in.setName(name);
                in.setSay(note);
                in.setType(type);
                in.setMaterialCode(materialCode);
            }
            list.add(in);
        }
        if (list.size() == 1) {
            if (list.get(0).getRatio() == null) {
                list = new ArrayList<>();
                Matcher matcher = pattern2.matcher(str);
                while (matcher.find()) {
                    BasicsdatumMaterialIngredient in = new BasicsdatumMaterialIngredient();
                    String kindName = matcher.group(1).trim();
                    String name = matcher.group(2) == null ? "" : matcher.group(2).trim();
                    String note = matcher.group(3) == null ? "" : matcher.group(3).trim();
                    BigDecimal ratio = BigDecimalUtil.valueOf(matcher.group(4));
                    in.setMaterialKindName(kindName);
                    if (StrUtil.isNotBlank(kindName)) {
                        pd021DictList.stream().filter(it -> it.getName().equals(kindName)).findFirst().ifPresent(it-> in.setMaterialKindCode(it.getValue()));
                    }
                    in.setSay(note);
                    in.setRatio(ratio);
                    in.setName(name);
                    in.setType(type);

                    list.add(in);
                }
            }
        }
        if (needCode) {
            List<BasicBaseDict> ingredientsRemarksDictList = ccmFeignService.getDictInfoToList("IngredientsRemarks");
            List<BasicsdatumIngredient> ingredientList = ingredientService.list(new LambdaQueryWrapper<BasicsdatumIngredient>()
                    .select(BasicsdatumIngredient::getCode, BasicsdatumIngredient::getIngredient)
                    .in(BasicsdatumIngredient::getIngredient, list.stream().map(BasicsdatumMaterialIngredient::getName).collect(Collectors.toList()))
            );
            list.forEach(materialIngredient-> {
                // 成分名称
                ingredientList.stream().filter(it->
                    it.getIngredient().equals(materialIngredient.getName())
                ).findFirst().ifPresent(ingredient-> {
                    materialIngredient.setCode(ingredient.getCode());
                });
                // 成分备注
                ingredientsRemarksDictList.stream().filter(it->
                        it.getName().equals(materialIngredient.getSay())
                ).findFirst().ifPresent(ingredientsRemarks-> {
                    materialIngredient.setSayCode(ingredientsRemarks.getValue());
                });
            });
        }
        return list;
    }

    @ApiOperation(value = "主物料:查询下拉,search:按编码或名称检索,status状态默认全部")
    @GetMapping("/getBasicsdatumMaterialSelect")
    public List<BasicsdatumMaterialSelectVo> getBasicsdatumMaterialSelect(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "status", required = false) String status) {
        List<BasicsdatumMaterial> list = basicsdatumMaterialService.list(new BaseQueryWrapper<BasicsdatumMaterial>()
                .select("id,material_name,material_code").eq("company_code", baseController.getUserCompany())
                .like(StringUtils.isNotBlank(search), "material_code_name", search)
                .eq(StringUtils.isNotBlank(status), "status", status)
                .eq("biz_type", BasicsdatumMaterialBizTypeEnum.MATERIAL.getK())
                .eq("confirm_status", "2")
                .last(" limit 0,50 "));
        return CopyUtil.copy(list, BasicsdatumMaterialSelectVo.class);
    }

    @ApiOperation(value = "主物料:查询列表")
    @GetMapping("/getBasicsdatumMaterialList")
    public PageInfo<BasicsdatumMaterialPageVo> getBasicsdatumMaterialList(MaterialColumnHeadDto dto) {
        return basicsdatumMaterialService.getBasicsdatumMaterialNewList(dto);
    }

    @ApiOperation(value = "主物料:解锁")
    @PostMapping("/unlock")
    public Boolean unlock(@Valid @RequestBody BasicsdatumMaterialSaveDto dto) {
        return basicsdatumMaterialService.unlock(dto.getId());
    }

    @ApiOperation(value = "物料清单-选择物料档案列表")
    @GetMapping("/getBomSelMaterialList")
    public PageInfo<BomSelMaterialVo> getBomSelMaterialList(BasicsdatumMaterialQueryDto dto) {
        return basicsdatumMaterialService.getBomSelMaterialList(dto);
    }

    @ApiOperation(value = "主物料:保存主信息")
    @PostMapping("/saveBasicsdatumMaterial")
    public BasicsdatumMaterialVo saveBasicsdatumMaterial(@Valid @RequestBody BasicsdatumMaterialSaveDto dto) {
        return basicsdatumMaterialService.saveBasicsdatumMaterial(dto);
    }

    @ApiOperation(value = "主物料:停用/启用物料")
    @PostMapping("/startStopBasicsdatumMaterial")
    public Boolean startStopBasicsdatumMaterial(@Valid @RequestBody StartStopDto dto) {
        return basicsdatumMaterialService.startStopBasicsdatumMaterial(dto);
    }

    @ApiOperation(value = "主物料:删除物料及详情")
    @DeleteMapping("/delBasicsdatumMaterial")
    public Boolean delBasicsdatumMaterial(RemoveDto removeDto) {
        return basicsdatumMaterialService.delBasicsdatumMaterial( removeDto);
    }

    @ApiOperation(value = "主物料：按筛选条件导出")
    @GetMapping("/exportBasicsdatumMaterial")
    public void exportBasicsdatumMaterial(HttpServletResponse response, BasicsdatumMaterialQueryDto dto)
            throws Exception {
        basicsdatumMaterialService.exportBasicsdatumMaterial(response, dto);
    }

    @ApiOperation(value = "物料BOM：按筛选条件导出")
    @DuplicationCheck(type = 1,message = "服务正在导出请稍等",time = 60)
    @GetMapping("/exportBasicsdatumMaterialBom")
    public void exportBasicsdatumMaterialAndStyle(HttpServletResponse response, BasicsdatumMaterialPageAndStyleDto dto)
            throws Exception {
        basicsdatumMaterialService.exportBasicsdatumMaterialAndStyle(response, dto);
    }

    @ApiOperation(value = "主物料:查询物料详情(不包括颜色、规格、报价、旧料号)")
    @GetMapping("/getBasicsdatumMaterial")
    public BasicsdatumMaterialVo getBasicsdatumMaterial(
            @RequestParam(value = "id") @NotBlank(message = "编号id不能为空") String id) {
        return basicsdatumMaterialService.getBasicsdatumMaterial(id);
    }

    @ApiOperation(value = "主物料：下拉选择规格后保存到规格组")
    @PostMapping("/saveBasicsdatumMaterialWidths")
    public Boolean saveBasicsdatumMaterialWidths(@Valid @RequestBody BasicsdatumMaterialWidthsSaveDto dto) {
        return basicsdatumMaterialService.saveBasicsdatumMaterialWidths(dto);
    }

    @ApiOperation(value = "物料旧料号：查询旧料号列表")
    @GetMapping("/getBasicsdatumMaterialOldList")
    public PageInfo<BasicsdatumMaterialOldPageVo> getBasicsdatumMaterialOldList(BasicsdatumMaterialOldQueryDto dto) {
        return basicsdatumMaterialService.getBasicsdatumMaterialOldList(dto);
    }

    @ApiOperation(value = "物料旧料号：新增旧料号")
    @PostMapping("/saveBasicsdatumMaterialOld")
    public Boolean saveBasicsdatumMaterialOld(@Valid @RequestBody BasicsdatumMaterialOldSaveDto dto) {
        return basicsdatumMaterialService.saveBasicsdatumMaterialOld(dto);
    }

    @ApiOperation(value = "物料旧料号：删除旧料号")
    @DeleteMapping("/delBasicsdatumMaterialOld")
    public Boolean delBasicsdatumMaterialOld(@RequestParam(value = "id") @NotBlank(message = "id不能为空") String id) {
        return basicsdatumMaterialService.delBasicsdatumMaterialOld(id);
    }

    @ApiOperation(value = "物料规格:查询规格列表")
    @GetMapping("/getBasicsdatumMaterialWidthList")
    public PageInfo<BasicsdatumMaterialWidthPageVo> getBasicsdatumMaterialWidthList(
            BasicsdatumMaterialWidthQueryDto dto) {
        return basicsdatumMaterialService.getBasicsdatumMaterialWidthList(dto);
    }

    @ApiOperation(value = "物料规格:新增规格/修改")
    @PostMapping("/saveBasicsdatumMaterialWidth")
    public Boolean saveBasicsdatumMaterialWidth(@Valid @RequestBody BasicsdatumMaterialWidthSaveDto dto) {
        return basicsdatumMaterialService.saveBasicsdatumMaterialWidth(dto);
    }

    @ApiOperation(value = "物料规格:按规格组清理并批量导入新增规格")
    @PostMapping("/saveBasicsdatumMaterialWidthGroup")
    public Boolean saveBasicsdatumMaterialWidthGroup(@Valid @RequestBody BasicsdatumMaterialWidthGroupSaveDto dto) {
        return basicsdatumMaterialService.saveBasicsdatumMaterialWidthGroup(dto);
    }

    @ApiOperation(value = "物料规格组，检查当前物料是否被引用")
    @GetMapping("/checkMaterialRelyOnBom/{materialCode}")
    public Integer materialRelyOnBom(@PathVariable("materialCode") String materialCode) {
        return basicsdatumMaterialService.materialRelyOnBom(materialCode);
    }

    @ApiOperation(value = "物料规格:停用/启用规格")
    @DuplicationCheck
    @PostMapping("/startStopBasicsdatumMaterialWidth")
    public Boolean startStopBasicsdatumMaterialWidth(@Valid @RequestBody StartStopDto dto) {
        return basicsdatumMaterialService.startStopBasicsdatumMaterialWidth(dto);
    }

    @ApiOperation(value = "物料规格:删除规格")
    @DeleteMapping("/delBasicsdatumMaterialWidth")
    public Boolean delBasicsdatumMaterialWidth(RemoveDto removeDto) {
        return basicsdatumMaterialService.delBasicsdatumMaterialWidth(removeDto);
    }

    @ApiOperation(value = "物料颜色:查询颜色列表")
    @GetMapping("/getBasicsdatumMaterialColorList")
    public PageInfo<BasicsdatumMaterialColorPageVo> getBasicsdatumMaterialColorList(
            BasicsdatumMaterialColorQueryDto dto) {
        return basicsdatumMaterialService.getBasicsdatumMaterialColorList(dto);
    }

    @ApiOperation(value = "物料颜色:保存颜色")
    @PostMapping("/saveBasicsdatumMaterialColor")
    public Boolean saveBasicsdatumMaterialColor(@Valid @RequestBody BasicsdatumMaterialColorSaveDto dto) {
        return basicsdatumMaterialService.saveBasicsdatumMaterialColor(dto);
    }

    @ApiOperation(value = "物料颜色:批量保存颜色")
    @PostMapping("/saveBasicsdatumMaterialColorList")
    public Boolean saveBasicsdatumMaterialColorList(@Valid @RequestBody List<BasicsdatumMaterialColorSaveDto> dtos) {
        return basicsdatumMaterialService.saveBasicsdatumMaterialColorList(dtos);
    }

    @ApiOperation(value = "物料颜色:停用/启用颜色")
    @PostMapping("/startStopBasicsdatumMaterialColor")
    public Boolean startStopBasicsdatumMaterialColor(@Valid @RequestBody StartStopDto dto) {
        return basicsdatumMaterialService.startStopBasicsdatumMaterialColor(dto);
    }

    @ApiOperation(value = " 物料颜色:删除颜色")
    @DeleteMapping("/delBasicsdatumMaterialColor")
    public Boolean delBasicsdatumMaterialColor(RemoveDto removeDto) {
        return basicsdatumMaterialService.delBasicsdatumMaterialColor(removeDto);
    }

    @ApiOperation(value = "物料报价:查询报价列表")
    @GetMapping("/getBasicsdatumMaterialPriceList")
    public PageInfo<BasicsdatumMaterialPricePageVo> getBasicsdatumMaterialPriceList(
            BasicsdatumMaterialPriceQueryDto dto) {
        return basicsdatumMaterialService.getBasicsdatumMaterialPriceList(dto);
    }

    @ApiOperation(value = "物料报价:查询报价列表的颜色尺码选择下拉")
    @GetMapping("/getBasicsdatumMaterialPriceColorWidthSelect")
    public Map<String, Object> getBasicsdatumMaterialPriceColorWidthSelect(
            @RequestParam(value = "materialCode") @NotBlank(message = "物料编码不能为空") String materialCode) {
        return basicsdatumMaterialService.getBasicsdatumMaterialPriceColorWidthSelect(materialCode);
    }

    @ApiOperation(value = "物料报价:保存报价")
    @PostMapping("/saveBasicsdatumMaterialPrice")
    public Boolean saveBasicsdatumMaterialPrice(@Valid @RequestBody BasicsdatumMaterialPriceSaveDto dto) {
        return basicsdatumMaterialService.saveBasicsdatumMaterialPrice(dto);
    }

    @ApiOperation(value = "物料报价:停用/启用报价")
    @PostMapping("/startStopBasicsdatumMaterialPrice")
    public Boolean startStopBasicsdatumMaterialPrice(@Valid @RequestBody StartStopDto dto) {
        return basicsdatumMaterialService.startStopBasicsdatumMaterialPrice(dto);
    }

    @ApiOperation(value = "物料报价:删除报价")
    @DeleteMapping("/delBasicsdatumMaterialPrice")
    public Boolean delBasicsdatumMaterialPrice(@RequestParam(value = "id") @NotBlank(message = "id不能为空") String id) {
        return basicsdatumMaterialService.delBasicsdatumMaterialPrice(id);
    }

    @ApiOperation(value = "采购-选择物料档案列表")
    @GetMapping("/getPurchaseMaterialList")
    public PageInfo<WarehouseMaterialVo> getPurchaseMaterialList(
            @RequestHeader(BaseConstant.USER_COMPANY) String userCompany, BasicsdatumMaterialQueryDto dto) {
        return basicsdatumMaterialService.getPurchaseMaterialList(dto);
    }

    @ApiOperation(value = "修改物料询价编号、货期数据")
    @PostMapping("/updateInquiryNumberDeliveryName")
    public ApiResult updateInquiryNumberDeliveryName(@Valid @RequestBody BasicsdatumMaterialSaveDto dto) {
        if (StringUtils.isEmpty(dto.getId())) {
            return ApiResult.error("物料ID不能为空", 500);
        }
        Boolean flg = basicsdatumMaterialService.updateInquiryNumberDeliveryName(dto);
        return flg ? ApiResult.success() : ApiResult.error("修改物料询价编号、货期数据失败", 500);
    }


    @ApiOperation(value = "保存提交")
    @PostMapping("/saveSubmit")
    public BasicsdatumMaterialVo saveSubmit(@Valid @RequestBody BasicsdatumMaterialSaveDto dto) {
        return basicsdatumMaterialService.saveSubmit(dto);
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
        return basicsdatumMaterialService.approval(dto);
    }

    /**
     * 根据大货款号查询关联的物料
     */
    @GetMapping("/listByStyleNo")
    public ApiResult listByStyleNo(String styleNo, int pageSize, int pageNum) {

        PackInfo packInfo = packInfoService.getOne(new BaseQueryWrapper<PackInfo>().select("id").eq("style_no", styleNo));
        if (packInfo == null) {
            return selectSuccess(new PageInfo<>(new ArrayList<>()));
        }

        PackBomVersion packBomVersion = packBomVersionService.getEnableVersion(packInfo.getId(), "packBigGoods");
        if (packBomVersion==null){
            return selectSuccess(new PageInfo<>(new ArrayList<>()));
        }
        PageHelper.startPage(pageNum, pageSize);
        List<PackBom> packBomList = packBomService.list(new BaseQueryWrapper<PackBom>().eq("foreign_id", packInfo.getId()).eq("pack_type","packBigGoods").eq("bom_version_id", packBomVersion.getId()).eq("unusable_flag", "0"));
        List<String> collect = packBomList.stream().map(PackBom::getMaterialCode).collect(Collectors.toList());
        List<BasicsdatumMaterial> list = basicsdatumMaterialService.list(new QueryWrapper<BasicsdatumMaterial>().in("material_code", collect));
        for (PackBom packBom : packBomList) {
            for (BasicsdatumMaterial basicsdatumMaterial : list) {
                if (packBom.getMaterialCode().equals(basicsdatumMaterial.getMaterialCode())){
                    packBom.setIngredient(basicsdatumMaterial.getIngredient());
                }
            }
        }
        return selectSuccess(new PageInfo<>(packBomList));
    }

    @ApiOperation(value = "生成物料编号")
    @PostMapping("/genMaterialCode")
    public ApiResult genMaterialCode(@RequestBody BasicsdatumMaterial material) {
        if (null == material || StringUtils.isBlank(material.getCategory2Code()) ||StringUtils.isBlank(material.getCategory3Code())
                ||StringUtils.isBlank(material.getSeason()) ||StringUtils.isBlank(material.getYear())) {
            return ApiResult.error("相关参数不能为空：物料二级类目不能为空 || 物料三级类目不能为空 || 年份不能为空 || 季节不能为空", 500);
        }
        return ApiResult.success("新增成功" , basicsdatumMaterialService.genMaterialCode(material));
    }

    @ApiOperation(value = "获取物料编号最大流水号")
    @PostMapping("/maxMaterialCode")
    public String maxMaterialCode(@RequestBody GetMaxCodeRedis data) {
        if (ObjectUtil.isEmpty(data.getValueMap())) {
            throw new OtherException("条件不能为空");
        }
        return basicsdatumMaterialService.getMaxMaterialCode(data,getUserCompany());
    }

    @ApiOperation(value = "获取供应商规格颜色")
    @GetMapping("/gatSupplierWidthColorList")
    public List<BasicsdatumMaterialPriceDetailVo> gatSupplierWidthColorList(SupplierDetailPriceDto supplierDetailPriceDto) {
        return basicsdatumMaterialPriceDetailService.gatSupplierWidthColorList(supplierDetailPriceDto);
    }

    @ApiOperation(value = "获取供应商详情价格")
    @GetMapping("/gatSupplierPrice")
    public BasicsdatumMaterialPriceDetailVo gatSupplierPrice(SupplierDetailPriceDto supplierDetailPriceDto) {
        return basicsdatumMaterialPriceDetailService.gatSupplierPrice(supplierDetailPriceDto);
    }

    @ApiOperation(value = "修改物料主图")
    @PostMapping("/updateMaterialPic")
    public Boolean updateMaterialPic(@Valid @RequestBody BasicsdatumMaterialSaveDto dto) {
        return basicsdatumMaterialService.updateMaterialPic(dto);
    }

    /**
     * 重置物料图片
     * 文件为txt
     * eg
     * FKR02458.jpg
     * FKR02870.JPG
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "重置图片")
    @PostMapping("/resetImgUrl")
    public boolean resetImgUrl(@RequestParam("file") MultipartFile file) {
        return basicsdatumMaterialService.resetImgUrl(file);
    }

    /**
     * 匹配物料图片
     *
     * @return
     */
    @GetMapping("/matchPic")
    public boolean matchPic() {
        long count = basicsdatumMaterialService.count();
        long pages = count / 100;
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 1; i < pages + 2; i++) {
            int finalI = i;
            executorService.submit(() -> {
                basicsdatumMaterialService.matchPic(finalI, 100);
            });

        }

        return true;
    }

    @ApiOperation(value = "物料清单款式报表列表")
    @GetMapping("/getMaterialsBomStylePage")
    public PageInfo<BasicsdatumMaterialPageAndStyleVo> materialsBomStylePage(BasicsdatumMaterialPageAndStyleDto dto) {
        return basicsdatumMaterialService.materialsBomStylePage(dto);
    }

    @ApiOperation(value = "物料未下发，并且未被bom引用修改物料编码和物料名称")
    @PostMapping("/updateMaterialNameAndCode")
    public BasicsdatumMaterialUpdateVo updateMaterialNameAndCode(@Valid @RequestBody BasicsdatumMaterialUpdateDto dto) {
        return basicsdatumMaterialService.updateMaterialProperties(dto);
    }



    /**
     * 初始化物料检测报告图片
     */
    @PostMapping("/initMaterialTestReport")
    public ApiResult initMaterialTestReport() {
        //获取所有的物料编号
        QueryWrapper<BasicsdatumMaterial> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.select("material_code","material_name").last("limit 0,200");
        escmMaterialCompnentInspectCompanyService.physicalDeleteQWrap(new BaseQueryWrapper<EscmMaterialCompnentInspectCompanyDto>().gt("create_date", "2023-10-29"));
        List<BasicsdatumMaterial> basicsdatumMaterialList = basicsdatumMaterialService.list(queryWrapper);
        List<EscmMaterialCompnentInspectCompanyDto> escmMaterialCompnentInspectCompanyDtoList = new ArrayList<>();
        basicsdatumMaterialList.forEach(materialCode -> {
            EscmMaterialCompnentInspectCompanyDto escmMaterialCompnentInspectCompanyDto = new EscmMaterialCompnentInspectCompanyDto();
            escmMaterialCompnentInspectCompanyDto.setMaterialsNo(materialCode.getMaterialCode());
            escmMaterialCompnentInspectCompanyDto.setMaterialsName(materialCode.getMaterialName());


            // TODO 2023/10/29 20:46:39 生成文件地址
            escmMaterialCompnentInspectCompanyDto.setFileUrl("http://www.baidu.com");
            escmMaterialCompnentInspectCompanyDtoList.add(escmMaterialCompnentInspectCompanyDto);
        });
        escmMaterialCompnentInspectCompanyService.saveBatch(escmMaterialCompnentInspectCompanyDtoList,100);
        return null;
    }
}
