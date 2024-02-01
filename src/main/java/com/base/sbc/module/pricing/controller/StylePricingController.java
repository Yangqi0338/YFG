/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.client.message.utils.MessageUtils;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.service.PackPricingService;
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
import java.math.BigDecimal;
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

    @Autowired
    private PackPricingService packPricingService;


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
    @DuplicationCheck(type = 1,time = 200,message = "正在导出中，请稍后...")
    public void exportStylePricingList(Principal user, StylePricingSearchDTO stylePricingSearchDTO, HttpServletResponse response) throws IOException {
        stylePricingSearchDTO.setDeriveFlag(BaseGlobal.YES);
        PageInfo<StylePricingVO> stylePricingList = stylePricingService.getStylePricingList(user, stylePricingSearchDTO);
        //导出
        ExcelUtils.executorExportExcel(stylePricingList.getList(), StylePricingVO.class,"款式定价.xlsx",stylePricingSearchDTO.getImgFlag(),2000,response,"styleColorPic");
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


    @ApiOperation(value = "计控确认")
    @PostMapping("/controlPlanCostConfirm")
    @DuplicationCheck
    public ApiResult controlPlanCostConfirm(@Valid @RequestBody StylePricingStatusDTO dto) {
        /*获取到款式定价数据*/
        List<StylePricing> stylePricings = stylePricingService.listByField("pack_id", com.base.sbc.config.utils.StringUtils.convertList(dto.getId()));
        /*存在款式定价*/
        if (CollUtil.isNotEmpty(stylePricings)) {
            dto.setIds(stylePricings.get(0).getId());
        } else {
            dto.setIds("packInfo:" + dto.getId());
        }
        dto.setControlConfirm(BaseGlobal.YES);
        return updateStatus(dto);
    }


    @ApiOperation(value = "提交审核")
    @PostMapping("/updateStatus")
    @DuplicationCheck
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult updateStatus( @RequestBody StylePricingStatusDTO dto) {
        if(StringUtils.isEmpty(dto.getIds())){
            throw new OtherException("请选择款式定价");
        }
        String[] split = dto.getIds().split(",");
        List<String> list = new ArrayList<>();
        for (String s : split) {
            //说明stylePricing不存在,新建
            if (s.contains("packInfo:")){
                String packInfoId = s.replace("packInfo:", "");
                StylePricing stylePricing =new StylePricing();
                stylePricing.setPackId(packInfoId);
                stylePricing.setCompanyCode(super.getUserCompany());
                /*企划倍率默认为4*/
                stylePricing.setPlanningRate(new BigDecimal(4));
                stylePricingService.save(stylePricing);
                s=stylePricing.getId();
            }
            list.add(s);
        }
        List<StylePricing> stylePricings = stylePricingService.listByIds(list);
        for (StylePricing stylePricing : stylePricings) {

            if ("1".equals(dto.getWagesConfirm()) &&"1".equals(dto.getControlConfirm()) && "1".equals(stylePricing.getProductHangtagConfirm()) && "1".equals(stylePricing.getControlHangtagConfirm())) {
                throw new OtherException("存在已经提交审核");
            }
            if ("1".equals(dto.getControlConfirm()) && "0".equals(stylePricing.getWagesConfirm())){
                throw new OtherException("工时部确认后计控才能确认成本");
            }
            if ("1".equals(dto.getProductHangtagConfirm()) && "0".equals(stylePricing.getControlConfirm())){
                throw new OtherException("请先计控确认");
            }
            if ("1".equals(dto.getControlHangtagConfirm()) && ("0".equals(stylePricing.getProductHangtagConfirm())  || "0".equals(stylePricing.getControlConfirm()))){
                throw new OtherException("请先商品吊牌确认");
            }
            if (!StringUtils.isEmpty(dto.getWagesConfirm())){
                if (dto.getWagesConfirm().equals(stylePricing.getWagesConfirm())){
                    throw new OtherException("请勿重复提交");
                }
                stylePricing.setWagesConfirm(dto.getWagesConfirm());
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
//            计控确认时设置计控成本价等于总成本
            if (StrUtil.equals(stylePricing.getControlConfirm(),BaseGlobal.YES)){
                stylePricing.setControlPlanCost(packPricingService.countTotalPrice(stylePricing.getPackId(), BaseGlobal.STOCK_STATUS_CHECKED,3));
            }
        }
        /*获取款式下的关联的款*/
        List<String> packIdList = stylePricings.stream().map(StylePricing::getPackId).collect(Collectors.toList());
        List<PackInfo> packInfoList = packInfoService.listByIds(packIdList);
        /*迁移数据不能操作*/
        long count = packInfoList.stream().filter(o -> StrUtil.equals(o.getHistoricalData(), BaseGlobal.YES)).count();
        if(count > 0){
            throw new OtherException("历史数据不能操作");
        }

        stylePricingService.updateBatchById(stylePricings);
        /*计控确认成本消息通知*/
        if(StrUtil.equals(dto.getControlConfirm(), BaseGlobal.STATUS_CLOSE)) {
            for (PackInfo packInfo : packInfoList) {
                messageUtils.stylePricingSendMessage("M商品企划", packInfo.getDesignNo(), packInfo.getPlanningSeasonId(), "1", baseController.getUser());
            }
        }
        //是否计控确认
        int type =0;
        if(StrUtil.equals(dto.getControlConfirm(),BaseGlobal.YES)){
            type = 4;
        }

        //是否计控吊牌确认
        if(StrUtil.equals(dto.getControlHangtagConfirm(),BaseGlobal.YES)){
            type = 5;
        }

        //是否商品吊牌确认
        if(StrUtil.equals(dto.getProductHangtagConfirm(),BaseGlobal.YES)){
            type = 6;
        }
        smpService.tagConfirmDates(list,type,1);

        /*吊牌确认下发*/
        if(StrUtil.equals(dto.getControlHangtagConfirm(), BaseGlobal.STATUS_CLOSE) || StrUtil.equals(dto.getControlConfirm(),BaseGlobal.YES)){
            String[] collect = packInfoList.stream().map(PackInfo::getStyleColorId).filter(StrUtil::isNotBlank).toArray(String[]::new);
            if (collect.length > 0) {
                smpService.goods(collect);
            }
        }
        return updateSuccess("提交成功");
    }


}

