/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollectionUtil;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.dto.IdDto;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.style.dto.*;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.vo.StyleColorAgentVo;
import com.base.sbc.module.style.vo.StyleColorVo;
import com.base.sbc.module.style.vo.StyleMarkingCheckVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * 类描述：款式-款式配色 Controller类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.sample.web.SampleStyleColorController
 * @email XX.com
 * @date 创建时间：2023-6-28 15:02:46
 */
@RestController
@Api(tags = "款式-款式配色")
@RequestMapping(value = BaseController.SAAS_URL + "/styleColor", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class StyleColorController {

    @Autowired
    private StyleColorService styleColorService;

    @ApiOperation(value = "分页查询")
    @GetMapping("/getSampleStyleColorList")
    public PageInfo<StyleColorVo> getSampleStyleColorList(Principal user, QueryStyleColorDto querySampleStyleColorDto) {
        return styleColorService.getSampleStyleColorList(user, querySampleStyleColorDto);
    }

    @ApiOperation(value = "款式编号查找款式配色")
    @GetMapping("/getStyleAccessoryBystyleNo")
    public List<StyleColorVo> getStyleAccessoryBystyleNo(@Valid @NotBlank(message = "款式编号不能为空") String designNo) {
        return styleColorService.getStyleAccessoryBystyleNo(designNo);
    }

    @ApiOperation(value = "修改吊牌价-款式配色")
    @PostMapping("/updateTagPrice")
    public Boolean updateTagPrice(@Valid @RequestBody UpdateTagPriceDto updateTagPriceDto) {
        return styleColorService.updateTagPrice(updateTagPriceDto);
    }

    @ApiOperation(value = "大货款号查询-款式配色")
    @GetMapping("/getByStyleNo")
    public List<StyleColorVo> getByStyleNo(QueryStyleColorDto querySampleStyleColorDto) {
        return styleColorService.getByStyleNo(querySampleStyleColorDto);
    }


    @ApiOperation(value = "批量新增款式配色-款式配色")
    @PostMapping("/batchAddSampleStyleColor")
    public Boolean batchAddSampleStyleColor(Principal user, @Valid @RequestBody List<AddRevampStyleColorDto> list) throws Exception {
        return styleColorService.batchAddSampleStyleColor(user, list);
    }


    @ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
    @PostMapping("/startStopSampleStyleColor")
    public Boolean startStopSampleStyleColor(@Valid @RequestBody StartStopDto startStopDto) {
        return styleColorService.startStopSampleStyleColor(startStopDto);
    }

    @ApiOperation(value = "修改颜色-款式配色")
    @PostMapping("/updateColor")
    public Boolean updateColor(@Valid @RequestBody UpdateColorDto updateColorDto) {
        return styleColorService.updateColor(updateColorDto);
    }

    @ApiOperation(value = "新增修改款式配色-款式配色")
    @PostMapping("/addRevampSampleStyleColor")
    public Boolean addRevampSampleStyleColor(@Valid @RequestBody AddRevampStyleColorDto addRevampStyleColorDto) {
        return styleColorService.addRevampSampleStyleColor(addRevampStyleColorDto);
    }

    @ApiOperation(value = "删除款式配色-款式配色")
    @DeleteMapping("/delStyleColor")
    public Boolean delStyleColor(RemoveDto removeDto) {
        return styleColorService.delStyleColor(removeDto);
    }

    @ApiOperation(value = "按颜色id删除款式下款式配色-款式配色")
    @DeleteMapping("/delSampleStyleColor")
    public Boolean delSampleStyleColor(@Valid @NotBlank(message = "颜色id不能为空") String id, @Valid @NotBlank(message = "款式id") String styleId) {
        return styleColorService.delSampleStyleColor(id, styleId);
    }

    @ApiOperation(value = "明细-通过id查询")
    @GetMapping("/{id}")
    public StyleColor getById(@PathVariable("id") String id) {
        return styleColorService.getById(id);
    }

    @ApiOperation(value = "检查-配饰是否关联主款")
    @PostMapping("/checkAccessoryRelatedMainStyle")
    public ApiResult checkAccessoryRelatedMainStyle(@Valid @RequestBody QueryStyleColorDto querySampleStyleColorDto) {
        return styleColorService.checkAccessoryRelatedMainStyle(querySampleStyleColorDto.getIds());
    }

    @ApiOperation(value = "下发-款式配色")
    @PostMapping("/issueScm")
    public ApiResult issueScm(@Valid @RequestBody QueryStyleColorDto querySampleStyleColorDto) {
        return styleColorService.issueScm(querySampleStyleColorDto.getIds());
    }

    @ApiOperation(value = "获取款式下的颜色id")
    @GetMapping("/getStyleColorId")
    public List<String> getStyleColorId(@Valid @NotBlank(message = "款式主数据id") String styleId) {
        return styleColorService.getStyleColorId(styleId);
    }

    @ApiOperation(value = "关联Bom")
    @PostMapping("/relevanceBom")
    public Boolean relevanceBom(@Valid @RequestBody RelevanceBomDto relevanceBomDto) {
        return styleColorService.relevanceBom(relevanceBomDto);
    }

    @ApiOperation(value = "修改大货款号,波段")
    @PostMapping("/updateStyleNoBand")
    public Boolean updateStyleNoBand(Principal user, @Valid @RequestBody UpdateStyleNoBandDto updateStyleNoBandDto) {
        return styleColorService.updateStyleNoBand(user, updateStyleNoBandDto);
    }

    @ApiOperation(value = "验证配色是否可修改")
    @PostMapping("/verification")
    public Boolean verification(@Valid @RequestBody PublicStyleColorDto publicStyleColorDto) {
        return styleColorService.verification(publicStyleColorDto.getId());
    }

    @ApiOperation(value = "解锁配色")
    @PostMapping("/unlockStyleColor")
    public Boolean unlockStyleColor(@Valid @RequestBody PublicStyleColorDto publicStyleColorDto) {
        return styleColorService.unlockStyleColor(publicStyleColorDto);
    }

    @ApiOperation(value = "新增次品款")
    @PostMapping("/addDefective")
    public Boolean addDefective(@Valid @RequestBody PublicStyleColorDto publicStyleColorDto,Principal user) {
        return styleColorService.addDefective(publicStyleColorDto,user);
    }

    @ApiOperation(value = "更新下单标记")
    @PostMapping("/updateOrderFlag")
    public Boolean updateOrderFlag(@Valid @RequestBody PublicStyleColorDto publicStyleColorDto) {
        return styleColorService.updateOrderFlag(publicStyleColorDto);
    }

    @ApiOperation(value = "取消关联Bom")
    @PostMapping("/disassociateBom")
    public Boolean disassociateBom(@Valid @RequestBody PublicStyleColorDto publicStyleColorDto) {
        return styleColorService.disassociateBom(publicStyleColorDto);
    }

    @ApiOperation(value = "查询款式配色设计维度数据", notes = "")
    @GetMapping("/getStyleColorDynamicDataById")
    public List<FieldManagementVo> getStyleColorDynamicDataById(String id) {
        return styleColorService.getStyleColorDynamicDataById(id);
    }

    @ApiOperation(value = "保存配色维度数据")
    @PostMapping("/saveStyleColorDynamicData")
    public Boolean saveStyleColorDynamicData(@Valid @RequestBody List<FieldVal> technologyInfo) {
        return styleColorService.saveStyleColorDynamicData(technologyInfo);
    }

    @ApiOperation(value = "查询款式配色主款配饰数据", notes = "")
    @GetMapping("/getStyleMainAccessoriesList")
    public PageInfo<StyleColorVo> getStyleMainAccessoriesList(Principal user, QueryStyleColorDto dto) {
        return styleColorService.getStyleMainAccessoriesList(user, dto);
    }

    @ApiOperation(value = "配色增加复制功能")
    @PostMapping("/copyStyleColor")
    public Boolean copyStyleColor(@Valid @RequestBody IdDto idDto, Principal user) {
        return styleColorService.copyStyleColor(idDto, user);
    }

    @ApiOperation(value = "/款式列表导出")
    @GetMapping("/styleListDeriveExcel")
    @DuplicationCheck(type = 1, message = "服务已存在导出，请稍后...")
    public void styleListDeriveExcel(Principal user, HttpServletResponse response, QueryStyleColorDto dto) throws Exception {
        styleColorService.styleListDeriveExcel(user, response, dto);
    }

    @ApiOperation(value = "/款式配色导出")
    @GetMapping("/styleColorListDeriveExcel")
    @DuplicationCheck(type = 1, message = "服务已存在导出，请稍后...")
    public void styleColorListDeriveExcel(Principal user, HttpServletResponse response, QueryStyleColorDto dto) throws Exception {
        styleColorService.styleColorListDeriveExcel(user, response, dto);
    }


    @ApiOperation(value = "企划选择款式-款式配色")
    @GetMapping("/getByStyleList")
    public PageInfo<StyleColorVo> getByStyleList(StyleColorsDto dto) {
        return styleColorService.getByStyleList(dto);
    }

    @ApiOperation(value = "保存正确样样衣码字段")
    @PostMapping("/saveCorrectBarCode")
    public ApiResult saveCorrectBarCode(@Valid @RequestBody StyleColor styleColor) {
        styleColorService.saveCorrectBarCode(styleColor);
        return ApiResult.success();
    }

    @ApiOperation(value = "保存设计时间")
    @PostMapping("/saveDesignDate")
    public ApiResult saveDesignDate(@Valid @RequestBody AddRevampStyleColorDto styleColor) {
        styleColorService.saveDesignDate(styleColor);
        return ApiResult.success();
    }

    @ApiOperation(value = "/款式打标导出")
    @GetMapping("/markingDeriveExcel")
    @DuplicationCheck(type = 1, message = "服务已存在导出，请稍后...")
    public void markingDeriveExcel(Principal user, HttpServletResponse response, QueryStyleColorDto dto) throws Exception {
        styleColorService.markingDeriveExcel(user, response, dto);
    }

    @ApiOperation(value = "/打标检查列表查询")
    @GetMapping("/markingCheckPage")
    public PageInfo<StyleMarkingCheckVo> markingCheckPage(QueryStyleColorDto dto) throws Exception {
        return styleColorService.markingCheckPage(dto);
    }

    @ApiOperation(value = "保存配色逾期原因")
    @PostMapping("/saveOverdueReason")
    public ApiResult saveCorrectBarCode(@Valid @RequestBody StyleColorOverdueReasonDto styleColorOverdueReasonDto) {
        styleColorService.updateStyleColorOverdueReason(styleColorOverdueReasonDto);
        return ApiResult.success();
    }

    @ApiOperation(value = "mango导出Excel模板")
    @GetMapping("/mangoExportExcel")
    public void exportExcel(HttpServletResponse response) throws Exception {
        MangoStyleColorExeclDto mangoStyleColorExeclDto = new MangoStyleColorExeclDto();
        mangoStyleColorExeclDto.setYear("2099");
        mangoStyleColorExeclDto.setSeason("S");
        mangoStyleColorExeclDto.setBrandName("MANGO");
        mangoStyleColorExeclDto.setProdCategoryName("裤子1");
        mangoStyleColorExeclDto.setStyleColorNo("大货款号");
        mangoStyleColorExeclDto.setOutsideColorCode("9");
        mangoStyleColorExeclDto.setOutsideSizeCode("34");
        mangoStyleColorExeclDto.setOutsideBarcode("8447034227768");
        mangoStyleColorExeclDto.setGender("男士/女士");
        mangoStyleColorExeclDto.setPackagingForm("叠装/挂装");
        mangoStyleColorExeclDto.setStyleTypeName("服装/配色");
        mangoStyleColorExeclDto.setProdCategory1stName("下装");
        mangoStyleColorExeclDto.setProdCategory2ndName("牛仔裤");
        mangoStyleColorExeclDto.setSizeRangeName("男裤");
        mangoStyleColorExeclDto.setTagPrice("299");
        mangoStyleColorExeclDto.setPlanCostPrice("199");
        ExcelUtils.exportExcel(CollectionUtil.newArrayList(mangoStyleColorExeclDto), MangoStyleColorExeclDto.class, "代理货品资料模板.xlsx", new ExportParams(), response);
    }

    @ApiOperation(value = "代理货品资料-分页查询")
    @GetMapping("/agentPageList")
    public PageInfo<StyleColorAgentVo> agentPageList(QueryStyleColorAgentDto querySampleStyleColorDto) {
        return styleColorService.agentPageList(querySampleStyleColorDto);
    }

    @ApiOperation(value = "代理货品资料-查询需要更新的图片")
    @GetMapping("/agentStyleNoList")
    public List<String> agentPageList() {
        return styleColorService.agentStyleNoList();
    }

    @ApiOperation(value = "mango导入Excel")
    @PostMapping("/mangoImportExcel")
    public ApiResult importExcel(@RequestParam("file") MultipartFile file, @RequestParam(value = "isUpdate", required = false, defaultValue = "false") Boolean isUpdate) throws Exception {
        List<MangoStyleColorExeclDto> list = ExcelImportUtil.importExcel(file.getInputStream(), MangoStyleColorExeclDto.class, new ImportParams());
        return styleColorService.mangoExeclImport(list, isUpdate);
    }

    @ApiOperation(value = "mango数据导出Excel")
    @GetMapping("/exportAgentExcel")
    public void exportAgentExcel(HttpServletResponse response, QueryStyleColorAgentDto querySampleStyleColorDto) throws Exception {
        styleColorService.exportAgentExcel(response, querySampleStyleColorDto);
    }

    @ApiOperation(value = "代理货品资料-删除")
    @GetMapping("/agentDelete")
    public ApiResult agentDelete(String id) {
        styleColorService.agentDelete(id);
        return ApiResult.success("操作成功");
    }

    @ApiOperation(value = "代理货品资料-解锁")
    @GetMapping("/agentUnlock")
    public ApiResult agentUnlock(String[] ids) {
        styleColorService.agentUnlock(ids);
        return ApiResult.success("操作成功");
    }

    @ApiOperation(value = "代理货品资料-停用")
    @GetMapping("/agentStop")
    public ApiResult agentStop(String id) {
        styleColorService.agentStop(id);
        return ApiResult.success("操作成功");
    }

    @ApiOperation(value = "代理货品资料-启用")
    @GetMapping("/agentEnable")
    public ApiResult agentEnable(String id) {
        styleColorService.agentEnable(id);
        return ApiResult.success("操作成功");
    }

    @ApiOperation(value = "代理货品资料-卡控")
    @GetMapping("/agentControl")
    public ApiResult agentControl(String id) {
        styleColorService.agentControl(id);
        return ApiResult.success("操作成功");
    }

    @ApiOperation(value = "代理货品资料-解控")
    @GetMapping("/agentUnControl")
    public ApiResult agentUnControl(String id) {
        styleColorService.agentUnControl(id);
        return ApiResult.success("操作成功");
    }

    @ApiOperation(value = "代理货品资料-编辑")
    @PostMapping("/agentUpdate")
    public ApiResult agentUpdate(@RequestBody StyleColorAgentVo styleColorAgentVo) {
        styleColorService.agentUpdate(styleColorAgentVo);
        return ApiResult.success("操作成功");
    }

    @ApiOperation(value = "代理货品资料-同步")
    @GetMapping("/agentSync")
    public ApiResult agentSync(String[] ids) {
        return styleColorService.agentSync(ids);
    }

	@ApiOperation(value = "mango导入图片")
	@PostMapping("/uploadStyleColorPics")
	public ApiResult uploadStyleColorPics(Principal user, @RequestParam("files") MultipartFile[] files) {
		return styleColorService.uploadStyleColorPics(user,files);
	}

    /**
     * 修改轻奢款
     */
    @ApiOperation(value = "修改轻奢款")
    @PostMapping("/updateLight")
    public ApiResult updateLight(@RequestBody StyleColorLightDto dto) {

        System.out.println(dto);
        // 先去校验是否已下发
        StyleColor styleColor = styleColorService.getByOne("style_no", dto.getBulkStyleNo());
        if ("1".equals(styleColor.getScmSendFlag())) {
            throw new RuntimeException("已下发，不能修改");
        }
        String styleNo = styleColor.getStyleNo();
        if ("1".equals(dto.getHigh())) {
            String replace = styleNo.replace("Q", "");
            styleColor.setStyleNo(replace+"Q");
        }else {
            styleColor.setStyleNo(styleNo.replace("Q", ""));
        }
        styleColorService.updateById(styleColor);
        return ApiResult.success("修改成功");
    }
}

