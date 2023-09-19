/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangTag.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.annotation.OperaLog;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.OperationType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.Pinyin4jUtil;
import com.base.sbc.module.hangTag.dto.HangTagDTO;
import com.base.sbc.module.hangTag.dto.HangTagSearchDTO;
import com.base.sbc.module.hangTag.dto.HangTagUpdateStatusDTO;
import com.base.sbc.module.hangTag.dto.UpdatePriceDto;
import com.base.sbc.module.hangTag.entity.HangTag;
import com.base.sbc.module.hangTag.service.HangTagIngredientService;
import com.base.sbc.module.hangTag.service.HangTagLogService;
import com.base.sbc.module.hangTag.service.HangTagService;
import com.base.sbc.module.hangTag.vo.HangTagListVO;
import com.base.sbc.module.material.dto.MaterialSaveDto;
import com.base.sbc.module.material.entity.Material;
import com.base.sbc.module.material.entity.MaterialLabel;
import com.base.sbc.module.pack.service.PackBusinessOpinionService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.smp.SmpService;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.ThreadLocalRandom;

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
    private final FlowableService flowableService;

    @ApiOperation(value = "分页查询")
    @PostMapping("/queryPageInfo")
    public PageInfo<HangTagListVO> queryPageInfo(@RequestBody HangTagSearchDTO hangTagSearchDTO) {
        return hangTagService.queryPageInfo(hangTagSearchDTO, super.getUserCompany());
    }


    @ApiOperation(value = "查询详情")
    @GetMapping("/getDetailsByBulkStyleNo")
    public ApiResult getDetailsByBulkStyleNo(@Valid @NotBlank(message = "大货款号不可为空") String bulkStyleNo, String selectType) {
        return selectSuccess(hangTagService.getDetailsByBulkStyleNo(bulkStyleNo, super.getUserCompany(), selectType));
    }

    @ApiOperation(value = "保存")
    @PostMapping("/save")
    public ApiResult save(@Valid @RequestBody HangTagDTO hangTagDTO) {
        String id = hangTagService.save(hangTagDTO, super.getUserCompany());
        return ApiResult.success("保存成功", id);
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

        styleColor.setTagPrice(updatePriceDto.getTagPrice());
        styleColorService.updateById(styleColor);
        return updateSuccess("修改成功");
    }


    @ApiOperation(value = "提交审核")
    @PostMapping("/updateStatus")
    public ApiResult updateStatus(@Valid @RequestBody HangTagUpdateStatusDTO dto) {
        hangTagService.updateStatus(dto, super.getUserCompany());
        return updateSuccess("更新成功");
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
            hangTag.setStatus("5");
        } else {
            //审核不通过
            hangTag.setStatus("6");
        }
        return hangTagService.updateById(hangTag);
    }
}
