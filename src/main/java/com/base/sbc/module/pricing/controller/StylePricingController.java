/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.message.utils.MessageUtils;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pricing.dto.StylePricingSaveDTO;
import com.base.sbc.module.pricing.dto.StylePricingSearchDTO;
import com.base.sbc.module.pricing.dto.StylePricingStatusDTO;
import com.base.sbc.module.pricing.entity.StylePricing;
import com.base.sbc.module.pricing.service.StylePricingService;
import com.base.sbc.module.pricing.vo.StylePricingVO;
import com.base.sbc.module.smp.SmpService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：款式定价 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pricing.web.StylePricingController
 * @email your email
 * @date 创建时间：2023-7-20 11:10:33
 */
@RestController
@Api(tags = "款式定价")
@RequestMapping(value = BaseController.SAAS_URL + "/stylePricing", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class StylePricingController extends BaseController {

    @Autowired
    private StylePricingService stylePricingService;

    @Autowired
    private MessageUtils messageUtils;

    @Autowired
    private BaseController baseController;

    @Autowired
    private PackInfoService packInfoService;

    @Autowired
    @Lazy
    private SmpService smpService;


    @ApiOperation(value = "获取款式定价列表")
    @PostMapping("/getStylePricingList")
    public PageInfo<StylePricingVO> getStylePricingList(Principal user, @Valid @RequestBody StylePricingSearchDTO stylePricingSearchDTO) {
        return stylePricingService.getStylePricingList(user,stylePricingSearchDTO);
    }

    /**
     * 导出款式定价列表
     */
    @ApiOperation(value = "导出款式定价列表")
    @GetMapping("/exportStylePricingList")
    public void exportStylePricingList(Principal user, StylePricingSearchDTO stylePricingSearchDTO, HttpServletResponse response) throws IOException {
        PageInfo<StylePricingVO> stylePricingList = stylePricingService.getStylePricingList(user, stylePricingSearchDTO);
        //导出
        // EasyExcel.write(response.getOutputStream(), StylePricingVO.class).excelType(ExcelTypeEnum.CSV).sheet("款式定价").doWrite(stylePricingList.getList());
        ExcelUtils.exportExcel(stylePricingList.getList(),  StylePricingVO.class, "款式定价.xlsx",new ExportParams("title","sheetName",ExcelType.HSSF) ,response);
    }


    @ApiOperation(value = "通过资料包id获取")
    @GetMapping("/getByPackId")
    public ApiResult getByPackId(@Valid @NotBlank(message = "资料包id不可为空") String packId) {
        return selectSuccess(stylePricingService.getByPackId(packId, super.getUserCompany()));
    }

    @ApiOperation(value = "保存")
    @PostMapping("/insertOrUpdate")
    @DuplicationCheck
    public ApiResult insertOrUpdate(@Valid @RequestBody StylePricingSaveDTO stylePricingSaveDTO) {
        stylePricingService.insertOrUpdate(stylePricingSaveDTO, super.getUserCompany());
        return updateSuccess("修改成功");
    }

    @ApiOperation(value = "批量保存")
    @PostMapping("/insertOrUpdateBatch")
    @DuplicationCheck
    public ApiResult insertOrUpdateBatch(@Valid @RequestBody List<StylePricingSaveDTO> stylePricingSaveDTO) {
        stylePricingService.insertOrUpdateBatch(stylePricingSaveDTO, super.getUserCompany());
        return updateSuccess("修改成功");
    }
    @ApiOperation(value = "提交审核")
    @PostMapping("/updateStatus")
    @DuplicationCheck
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult updateStatus( @RequestBody StylePricingStatusDTO dto) {
        String[] split = dto.getIds().split(",");
        List<String> list = new ArrayList<>();
        for (String s : split) {
            //说明stylePricing不存在,新建
            if (s.contains("packInfo:")){
                String packInfoId = s.replace("packInfo:", "");
                StylePricing stylePricing =new StylePricing();
                stylePricing.setPackId(packInfoId);
                stylePricing.setCompanyCode(super.getUserCompany());
                stylePricingService.save(stylePricing);
                s=stylePricing.getId();
            }
            list.add(s);
        }
        List<StylePricing> stylePricings = stylePricingService.listByIds(list);
        for (StylePricing stylePricing : stylePricings) {

            if ("1".equals(dto.getControlConfirm()) && "1".equals(stylePricing.getProductHangtagConfirm()) && "1".equals(stylePricing.getControlHangtagConfirm())) {
                throw new OtherException("存在已经提交审核");
            }
            if ("1".equals(dto.getProductHangtagConfirm()) && "0".equals(stylePricing.getControlConfirm())){
                throw new OtherException("请先计控确认");
            }
            if ("1".equals(dto.getControlHangtagConfirm()) && ("0".equals(stylePricing.getProductHangtagConfirm())  || "0".equals(stylePricing.getControlConfirm()))){
                throw new OtherException("请先商品吊牌确认");
            }
            if (!StringUtils.isEmpty(dto.getControlConfirm())){
                if (dto.getControlConfirm().equals(stylePricing.getControlConfirm())){
                    throw new OtherException("请勿重复提交");
                }
                stylePricing.setControlConfirm(dto.getControlConfirm());
                stylePricing.setControlConfirmTime(new Date());
            }
            if (!StringUtils.isEmpty(dto.getProductHangtagConfirm())){
                if (dto.getProductHangtagConfirm().equals(stylePricing.getProductHangtagConfirm())){
                    throw new OtherException("请勿重复提交");
                }
                stylePricing.setProductHangtagConfirm(dto.getProductHangtagConfirm());
                stylePricing.setControlConfirmTime(new Date());
            }
            if (!StringUtils.isEmpty(dto.getControlHangtagConfirm())){
                if (dto.getControlHangtagConfirm().equals(stylePricing.getControlHangtagConfirm())){
                    throw new OtherException("请勿重复提交");
                }
                stylePricing.setControlHangtagConfirm(dto.getControlHangtagConfirm());
                stylePricing.setControlConfirmTime(new Date());
            }
        }
        /*迁移数据不能操作*/
      List<String> packIds = stylePricings.stream().map(StylePricing::getPackId).collect(Collectors.toList());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("id",packIds);
        queryWrapper.eq("historical_data",BaseGlobal.YES);
        List packInfoList1 = packInfoService.list(queryWrapper);
        if(CollUtil.isNotEmpty(packInfoList1)){
            throw new OtherException("历史数据不能操作");
        }

        stylePricingService.updateBatchById(stylePricings);
        List<String> packIdList = stylePricings.stream().map(StylePricing::getPackId).collect(Collectors.toList());
        /*获取款式下的关联的款*/
        List<PackInfo> packInfoList = packInfoService.listByIds(packIdList);
        /*计控确认成本消息通知*/
        if(StrUtil.equals(dto.getControlConfirm(), BaseGlobal.STATUS_CLOSE)) {
            for (PackInfo packInfo : packInfoList) {
                messageUtils.stylePricingSendMessage("M商品企划", packInfo.getDesignNo(), packInfo.getPlanningSeasonId(), "1", baseController.getUser());
            }
        }
        /*吊牌确认下发*/
        if(StrUtil.equals(dto.getControlHangtagConfirm(), BaseGlobal.STATUS_CLOSE)){
            String collect = packInfoList.stream().filter(f -> StrUtil.isNotBlank(f.getStyleColorId())).map(PackInfo::getStyleColorId).collect(Collectors.joining(","));
            if (StrUtil.isNotBlank(collect)) {
                smpService.goods(collect.split(","));
            }
        }
        return updateSuccess("提交成功");
    }


}

