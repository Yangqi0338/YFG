/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.business.HangTagStatusEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.fabric.dto.UpdateDTO;
import com.base.sbc.module.hangtag.dto.HangTagDTO;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageDTO;
import com.base.sbc.module.hangtag.dto.HangTagSearchDTO;
import com.base.sbc.module.hangtag.dto.HangTagUpdateStatusDTO;
import com.base.sbc.module.hangtag.dto.InspectCompanyDto;
import com.base.sbc.module.hangtag.dto.UpdatePriceDto;
import com.base.sbc.module.hangtag.entity.HangTag;
import com.base.sbc.module.hangtag.service.HangTagIngredientService;
import com.base.sbc.module.hangtag.service.HangTagLogService;
import com.base.sbc.module.hangtag.service.HangTagService;
import com.base.sbc.module.hangtag.vo.HangTagListVO;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.open.entity.EscmMaterialCompnentInspectCompanyDto;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 类描述：吊牌表 Controller类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.hangTag.web.HangTagController
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:53
 */
@RestController
@Api(tags = "吊牌表")
@RequestMapping(value = BaseController.SAAS_URL + "/hangTag", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
@RequiredArgsConstructor
public class HangTagController extends BaseController {

    @Autowired
    private HangTagService hangTagService;
    @Autowired
    private HangTagLogService hangTagLogService;
    @Autowired
    private HangTagIngredientService hangTagIngredientService;
    private final StyleColorService styleColorService;

    @ApiOperation(value = "分页查询")
    @GetMapping("/queryPageInfo")
    public PageInfo<HangTagListVO> queryPageInfoByLine(HangTagSearchDTO hangTagSearchDTO) {
        return hangTagService.queryPageInfoByLine(hangTagSearchDTO, super.getUserCompany());
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/queryPageInfo")
    public PageInfo<HangTagListVO> queryPageInfo(@RequestBody HangTagSearchDTO hangTagSearchDTO) {
        return hangTagService.queryPageInfo(hangTagSearchDTO, super.getUserCompany());
    }


    @ApiOperation(value = "导出", notes = "")
    @GetMapping("/deriveExcel")
    @DuplicationCheck(type = 1,message = "服务正在导出请稍等",time = 120)
    public void deriveExcel(HttpServletResponse response, HangTagSearchDTO hangTagSearchDTO) throws IOException {
        hangTagService.deriveExcel(response,hangTagSearchDTO, super.getUserCompany());
    }

    @ApiOperation(value = "查询详情")
    @GetMapping("/getDetailsByBulkStyleNo")
    public ApiResult getDetailsByBulkStyleNo(@Valid @NotBlank(message = "大货款号不可为空") String bulkStyleNo, String selectType) {
        return selectSuccess(hangTagService.getDetailsByBulkStyleNo(bulkStyleNo, super.getUserCompany(), selectType));
    }


    @ApiOperation(value = "刷新")
    @GetMapping("/getRefresh")
    @Deprecated
    public ApiResult getRefresh(@Valid @NotBlank(message = "大货款号不可为空") String bulkStyleNo, String selectType){

        return selectSuccess(hangTagService.getRefresh(bulkStyleNo,super.getUserCompany(),selectType));
    }

    @ApiOperation(value = "查询详情多语言")
    @GetMapping("/getMoreLanguageDetailsByBulkStyleNo")
    public ApiResult getMoreLanguageDetailsByBulkStyleNo(@Valid HangTagMoreLanguageDTO hangTagMoreLanguageDTO) {
        return selectSuccess(hangTagService.getMoreLanguageDetailsByBulkStyleNo(hangTagMoreLanguageDTO));
    }

    @ApiOperation(value = "保存")
    @PostMapping("/save")
    @DuplicationCheck
    public ApiResult save(@RequestBody HangTagDTO hangTagDTO) {
        String id = hangTagService.save(hangTagDTO, super.getUserCompany());
        return ApiResult.success("保存成功", id);
    }

    @ApiOperation(value = "修改二检包装形式")
    @PostMapping("/updateSecondPackagingFormById")
    @DuplicationCheck
    public ApiResult updateSecondPackagingFormById(@RequestBody HangTagDTO hangTagDTO) {
        hangTagService.updateSecondPackagingFormById(hangTagDTO);
        return ApiResult.success("修改成功");
    }

    /**
     * 修改吊牌价
     */
    @PostMapping("/updatePrice")
    public ApiResult updatePrice(@RequestBody UpdatePriceDto updatePriceDto) {
        String bulkStyleNo = updatePriceDto.getBulkStyleNo();
        StyleColor styleColor = styleColorService.getOne(new QueryWrapper<StyleColor>().eq("style_no", bulkStyleNo));
        if (styleColor == null) {
            throw new OtherException("大货款号:" + bulkStyleNo + " 不存在");
        }
        ////判断是否下发,下发的需要去验证是否可以修改
        //if ("1".equals(styleColor.getScmSendFlag())) {
        //    if (!smpService.checkUpdatePrice(updatePriceDto)) {
        //        throw new OtherException("该大货款号已下发并且使用,无法修改吊牌价");
        //    }
        //}
        StyleColor styleColor1 =new StyleColor();
        BeanUtil.copyProperties(styleColor, styleColor1);
        styleColor.setTagPrice(updatePriceDto.getTagPrice());
        styleColorService.updateById(styleColor);
        styleColorService.saveOperaLog("修改", "修改吊牌价", styleColor, styleColor1);
        return updateSuccess("修改成功");
    }


    @ApiOperation(value = "提交审核")
    @PostMapping("/updateStatus")
    public ApiResult updateStatus(@Valid @RequestBody HangTagUpdateStatusDTO dto) {
        dto.setUserCompany(super.getUserCompany());
        hangTagService.updateStatus(dto, false, new ArrayList<>());
        return updateSuccess("更新成功");
    }

    @ApiOperation(value = "提交审核")
    @PostMapping("/oneUpdateStatus")
    public ApiResult oneUpdateStatus(@Valid @RequestBody UpdateDTO updateDTO) {
        return hangTagService.oneUpdateStatus(updateDTO.getIds());
    }


    @ApiOperation(value = "查询操作日志")
    @GetMapping("/getHangTagLog")
    public ApiResult getHangTagLog(@Valid @NotBlank(message = "吊牌id不可为空") String hangTagId) {
        return selectSuccess(hangTagLogService.getByHangTagId(hangTagId, super.getUserCompany()));
    }

    @ApiOperation(value = "查询成分信息")
    @GetMapping("/getIngredientList")
    public ApiResult getIngredientList(@Valid @NotBlank(message = "吊牌id不可为空") String hangTagId) {
        return selectSuccess(hangTagIngredientService.getIngredientListByHangTagId(hangTagId, super.getUserCompany()));
    }

    @ApiOperation(value = "通过大货款号获取工艺包PDF")
    @GetMapping("/getTechSpecFileByStyleNo")
    public ApiResult getTechSpecFileByStyleNo(@Valid @NotBlank(message = "大货款号不可为空") String styleNo) {
        return selectSuccess(hangTagService.getTechSpecFileByStyleNo(styleNo));
    }


    /**
     * 审批回调
     */
    @PostMapping("/toExamine")
    public boolean toExamine(@RequestBody AnswerDto dto) {
        HangTag hangTag = hangTagService.getOne(new QueryWrapper<HangTag>().eq("bulk_style_no", dto.getBusinessKey()));
        if (BaseConstant.APPROVAL_PASS.equals(dto.getApprovalType())) {
            //审核通过
            hangTag.setStatus(HangTagStatusEnum.FINISH);
            hangTag.setTranslateConfirmDate(new Date());
        } else {
            //审核不通过
            hangTag.setStatus(HangTagStatusEnum.SUSPEND);
        }
        return hangTagService.updateById(hangTag);
    }

    /**
     * 反审
     */
    @PostMapping("/counterReview")
    public ApiResult counterReview(@RequestBody HangTag hangTag) {
        boolean update = hangTagService.counterReview(hangTag);

        return updateSuccess("反审成功");
    }


    @ApiOperation(value = "通过物料编码获取检查报告")
    @GetMapping("/getInspectReport")
    public List<EscmMaterialCompnentInspectCompanyDto> getInspectReport(InspectCompanyDto dto) {
        return hangTagService.getInspectReport(dto);
    }

}
