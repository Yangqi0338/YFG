/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.controller;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.NumberUtil;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.dto.IdDto;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.fabricsummary.entity.FabricSummaryPrintLog;
import com.base.sbc.module.pack.dto.*;
import com.base.sbc.module.pack.service.PackBaseService;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackBomVersionService;
import com.base.sbc.module.pack.vo.PackBomVersionVo;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.module.sample.dto.*;
import com.base.sbc.module.sample.vo.BomFabricVo;
import com.base.sbc.module.sample.vo.FabricStyleVo;
import com.base.sbc.module.sample.vo.FabricSummaryInfoVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：资料包-物料清单 Controller类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.web.PackBomController
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-1 16:37:22
 */
@RestController
@Api(tags = "资料包-物料清单")
@RequestMapping(value = BaseController.SAAS_URL + "/packBom", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PackBomController extends BaseController{

    @Autowired
    private PackBomService packBomService;

    @Autowired
    private PackBomVersionService packBomVersionService;

    @ApiOperation(value = "版本列表")
    @GetMapping("/version")
    public PageInfo<PackBomVersionVo> versionPage(@Valid PackCommonPageSearchDto dto) {
        return packBomVersionService.pageInfo(dto);
    }

    @ApiOperation(value = "保存版本信息")
    @PostMapping("/version")
    public PackBomVersionVo saveVersion(@Valid @RequestBody PackBomVersionDto dto) {
        return packBomVersionService.saveVersion(dto);
    }

    @ApiOperation(value = "版本发起审批")
    @GetMapping("/version/startApproval")
    public boolean startApproval(@Valid IdDto ids) {
        return packBomVersionService.startApproval(ids.getId());
    }

    @ApiOperation(value = "版本审批")
    @ApiIgnore
    @PostMapping("/version/approval")
    public boolean approval(@RequestBody AnswerDto dto) {
        return packBomVersionService.approval(dto);
    }

    @ApiOperation(value = "版本锁定")
    @GetMapping("/version/lock")
    public boolean versionLock(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @Valid IdsDto ids) {
        return packBomVersionService.lockChange(userCompany, ids.getId(), BaseGlobal.YES);
    }

    @ApiOperation(value = "版本解锁")
    @GetMapping("/version/unlock")
    public boolean versionUnlock(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @Valid IdsDto ids) {
        return packBomVersionService.lockChange(userCompany, ids.getId(), BaseGlobal.NO);
    }

    @ApiOperation(value = "版本启用/停用")
    @GetMapping("/version/changeStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "版本id", required = true, dataType = "String", paramType = "query"),
    })
    public boolean changeVersionStatus(@Valid @NotBlank(message = "id不能为空") String id) {
        return packBomVersionService.changeVersionStatus(id);
    }


    @ApiOperation(value = "物料清单分页查询")
    @GetMapping()
    public ApiResult packBomPage(@Valid PackBomPageSearchDto dto) {
        PageInfo<PackBomVo> packBomVoPageInfo = packBomService.pageInfo(dto);
        BigDecimal costTotal = packBomService.sumBomCost(dto);
        Map<String, Object> attributes = new HashMap<>(8);
        attributes.put("costTotal", Opt.ofNullable(costTotal).map(NumberUtil::toStr).orElse(""));
        return ApiResult.success(null, packBomVoPageInfo, attributes);
    }

    @ApiOperation(value = "统计成本")
    @GetMapping("/queryBomCost")
    public BigDecimal queryBomCost(@Valid PackBomPageSearchDto dto) {
        return packBomService.sumBomCost(dto);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存单个物料清单")
    @DuplicationCheck(type = 1)
    public PackBomVo save(@Valid @RequestBody PackBomDto dto) {
        return packBomService.saveByDto(dto);
    }

    @PostMapping("/saveBatch")
    @ApiOperation(value = "保存全部物料清单")
    public boolean save(@Valid PackBomSearchDto search, @RequestBody List<PackBomDto> dtoList) {
        return packBomService.saveBatchByDto(search.getBomVersionId(), search.getOverlayFlg(), dtoList);
    }

    @PostMapping("/bomTemplateSave")
    @ApiOperation(value = "bom模板引用新增")
    public boolean bomTemplateSave(@RequestBody BomTemplateSaveDto bomTemplateSaveDto) {
        return packBomService.bomTemplateSave(bomTemplateSaveDto);
    }

    @ApiOperation(value = "物料不可用")
    @GetMapping("/unusable")
    public boolean bomUnusable(@Valid IdsDto dto) {
        return packBomService.unusableChange(dto.getId(), BaseGlobal.YES);
    }

    @ApiOperation(value = "物料可用")
    @GetMapping("/usable")
    public boolean usable(@Valid IdsDto dto) {
        return packBomService.unusableChange(dto.getId(), BaseGlobal.NO);
    }

    @ApiOperation(value = "删除物料清单")
    @DeleteMapping("/delBom")
    public boolean delBom(@Valid IdsDto dto) {
        return packBomService.delByIds(dto.getId());
    }


    @PostMapping("/fabricSummaryList")
    @ApiOperation(value = "面料汇总列表")
    public ApiResult fabricSummaryList(@RequestBody FabricSummaryDTO fabricSummaryDTO) {
        fabricSummaryDTO.setCompanyCode(super.getUserCompany());
        return ApiResult.success("查询成功", packBomService.fabricSummaryList(fabricSummaryDTO));
    }

    @PostMapping("/querySampleDesignInfoByMaterialId")
    @ApiOperation(value = "查询物料被那些样衣应用")
    public ApiResult querySampleDesignInfoByMaterialId(@RequestBody FabricSummaryDTO fabricSummaryDTO) {
        fabricSummaryDTO.setCompanyCode(super.getUserCompany());
        return ApiResult.success("查询成功", packBomService.querySampleDesignInfoByMaterialId(fabricSummaryDTO));
    }

    @GetMapping("/moveUp")
    @ApiOperation(value = "上移")
    public boolean moveUp(@Valid IdDto dto) {
        return packBomService.move(dto.getId(), "sort", PackBaseService.MOVE_UP);
    }

    @GetMapping("/moveDown")
    @ApiOperation(value = "下移")
    public boolean moveDown(@Valid IdDto dto) {
        return packBomService.move(dto.getId(), "sort", PackBaseService.MOVE_DOWN);
    }

    @PostMapping("/dragSort")
    @ApiOperation(value = "拖拽")
    public Boolean dragSort(@RequestBody IdDto dto) {
        return packBomService.sort(dto.getId(),"sort");
    }

    @PostMapping("/unlock")
    @ApiOperation(value = "物料解锁")
    public Boolean unlock(@RequestBody IdDto dto) {
        return packBomService.unlock(dto);
    }

    @GetMapping("/packBomMaterialColor")
    @ApiOperation(value = "根据设计BomId获取物料清单的指定颜色的配色")
    public ApiResult packBomMaterialColor(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, String id, String colorCode){
        if(StringUtils.isBlank(id) || StringUtils.isBlank(colorCode)){
            return selectAttributeNotRequirements("id, color");
        }
        return packBomService.packBomMaterialColor(userCompany, id, colorCode);
    }

    @PostMapping("/bomFabricList")
    @ApiOperation(value = "BOM使用物料列表")
    public PageInfo<BomFabricVo> bomFabricList(@RequestBody @Valid BomFabricDto bomFabricDto) {
        bomFabricDto.setCompanyCode(super.getUserCompany());
        return  packBomService.bomFabricList(bomFabricDto,true);
    }

    @PostMapping("/fabricSummary")
    @ApiOperation(value = "面料汇总-添加")
    public boolean saveFabricSummary(@RequestBody @Valid  FabricSummarySaveDTO feedSummarySaveDTO) {
        feedSummarySaveDTO.setCompanyCode(super.getUserCompany());
        return packBomService.saveFabricSummary(feedSummarySaveDTO);
    }

    @GetMapping("/fabricSummary")
    @ApiOperation(value = "面料汇总列表")
    public PageInfo<FabricSummaryInfoVo> fabricSummaryListV2(FabricSummaryV2Dto dto) {
        dto.setCompanyCode(super.getUserCompany());
        return packBomService.fabricSummaryListV2(dto);
    }

    @PutMapping("/updateFabricSummary")
    @ApiOperation(value = "面料汇总列表修改")
    public boolean updateFabricSummary(@RequestBody @Valid List<FabricSummaryV2Dto> dtoList) {
        return packBomService.updateFabricSummary(dtoList);
    }

    @DeleteMapping ("/fabricSummary")
    @ApiOperation(value = "面料汇总-删除")
    public boolean deleteFabricSummary(@RequestBody @Valid List<FabricSummaryV2Dto> dtoList) {
        return packBomService.deleteFabricSummary(dtoList);
    }


    @PostMapping("/fabricStyleList")
    @ApiOperation(value = "物料对应款式列表")
    public  PageInfo<FabricStyleVo> fabricStyleList(@RequestBody @Valid  FabricStyleDto dto) {
        dto.setCompanyCode(super.getUserCompany());
        return  packBomService.fabricStyleList(dto);
    }

    @PostMapping("/fabricSummaryStyle")
    @ApiOperation(value = "面料汇总-款式添加")
    public boolean addFabricSummaryStyle(@RequestBody @Valid  FabricSummaryStyleSaveDto dto) {
        return  packBomService.saveFabricSummaryStyle(dto);
    }

    @PutMapping("/fabricSummaryStyle")
    @ApiOperation(value = "面料汇总-款式修改")
    public boolean updateFabricSummaryStyle(@RequestBody @Valid  List<FabricSummaryStyleDto> fabricSummaryStyleDtoList) {
        return  packBomService.updateFabricSummaryStyle(fabricSummaryStyleDtoList);
    }

    @DeleteMapping ("/fabricSummaryStyle")
    @ApiOperation(value = "面料汇总-款式删除")
    public boolean deleteFabricSummaryStyle(@RequestBody @Valid  FabricSummaryStyleDto dto) {
        return  packBomService.deleteFabricSummaryStyle(dto);
    }


    @ApiOperation(value = "/面料汇总导出")
    @GetMapping("/fabricSummaryExcel")
    @DuplicationCheck(type = 1,message = "服务已存在导出，请稍后...")
    public void fabricSummaryExcel(HttpServletResponse response , FabricSummaryV2Dto dto) throws Exception {
        dto.setCompanyCode(super.getUserCompany());
        packBomService.fabricSummaryExcel(response,dto);
    }

    @GetMapping("/printFabricSummaryLog")
    @ApiOperation(value = "面料汇总-款式添加")
    public PageInfo<FabricSummaryPrintLog> printFabricSummaryLog(PrintFabricSummaryLogDto dto) {
        return  packBomService.printFabricSummaryLog(dto);
    }


}































