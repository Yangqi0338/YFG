/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.controller;

import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.dto.IdDto;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.operaLog.entity.OperaLogEntity;
import com.base.sbc.module.pack.dto.*;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.service.PackInfoStatusService;
import com.base.sbc.module.pack.vo.BigGoodsPackInfoListVo;
import com.base.sbc.module.pack.vo.PackInfoListVo;
import com.base.sbc.module.pack.vo.PricingSelectListVO;
import com.base.sbc.module.pack.vo.StylePackInfoListVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * 类描述：资料包 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.web.PackInfoController
 * @email your email
 * @date 创建时间：2023-7-6 17:13:01
 */
@RestController
@Api(tags = "资料包-基础信息")
@RequestMapping(value = BaseController.SAAS_URL + "/packInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PackInfoController {

	@Autowired
	private PackInfoService packInfoService;
	@Autowired
	private PackInfoStatusService packInfoStatusService;

    @ApiOperation(value = "设计BOM管理列表-分页查询")
    @GetMapping
    public PageInfo<StylePackInfoListVo> pageBySampleDesign(@Valid PackInfoSearchPageDto pageDto) {
        return packInfoService.pageBySampleDesign(pageDto);
    }

	@ApiOperation(value = "资料包明细")
	@GetMapping("/getDetail")
	public PackInfoListVo getDetail(@Valid PackInfoDetailSearchDto dto) {
		return packInfoService.getDetail(dto.getId(), dto.getPackType());
	}

	@ApiOperation(value = "资料包启用停用")
	@PostMapping("/enableFlagSetting")
	public boolean enableFlagSetting(@Valid @RequestBody EnableFlagSettingDto dto) {
		return packInfoStatusService.enableFlagSetting(dto.getForeignId(), dto.getPackType(), dto.getEnableFlag());
	}

	@ApiOperation(value = "标准资料包-分页查询")
	@GetMapping("/pageByBigGoods")
	public PageInfo<BigGoodsPackInfoListVo> pageByBigGoods(@Valid PackInfoSearchPageDto pageDto) {
		return packInfoService.pageByBigGoods(pageDto);
	}


	@ApiOperation(value = "新建BOM(通过款式设计)")
    @GetMapping("/createBySampleDesign")
	public PackInfoListVo createBySampleDesign(@Valid IdDto idDto) {
		return packInfoService.createBySampleDesign(idDto.getId());
	}

	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
	@DeleteMapping()
	public Boolean removeById(@Valid IdsDto ids) {
		return packInfoService.removeByIds(StringUtils.convertList(ids.getId()));
	}

	@ApiOperation(value = "变更日志")
	@GetMapping("/operationLog")
	public PageInfo<OperaLogEntity> operationLog(@Valid PackCommonPageSearchDto pageDto) {
		return packInfoService.operationLog(pageDto);
	}

	@ApiOperation(value = "转大货")
	@GetMapping("/toBigGoods")
	public boolean toBigGoods(@Valid PackCommonSearchDto dto) {
		return packInfoService.toBigGoods(dto);
	}

	@ApiOperation(value = "反审")
	@GetMapping("/startReverseApproval")
	public boolean startReverseApproval(@Valid IdDto idDto) {
		return packInfoService.startReverseApproval(idDto.getId());
	}
	/**
	 * 处理反审批
	 *
	 * @param dto
	 * @return
	 */
	@ApiIgnore
	@PostMapping("/reverseApproval")
	public boolean reverseApproval(@RequestBody AnswerDto dto) {
		return packInfoService.reverseApproval(dto);
	}


	@ApiOperation(value = "提交审核")
	@GetMapping("/startApproval")
	public boolean startApproval(@Valid IdDto idDto) {
		return packInfoService.startApproval(idDto.getId());
	}

	@ApiIgnore
	@PostMapping("/approval")
	public boolean approval(@RequestBody AnswerDto dto) {
		return packInfoService.approval(dto);
	}

	/**
	 * 核价管理选择制版单列表
	 *
	 * @param pricingSelectSearchDTO
	 * @return
	 */
	@ApiIgnore
	@PostMapping("/pricingSelectList")
	public PageInfo<PricingSelectListVO> pricingSelectList(@RequestBody PricingSelectSearchDTO pricingSelectSearchDTO) {
		return packInfoService.pricingSelectList(pricingSelectSearchDTO);
	}


	/**
	 * 样衣id查询bom
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "查询设计款号夏的bom")
	@GetMapping("/getInfoListByDesignNo")
	public PageInfo<PackInfoListVo> pageBySampleDesign(@Valid PricingSelectSearchDTO pricingSelectSearchDTO) {
		return packInfoService.getInfoListByDesignNo(pricingSelectSearchDTO.getDesignNo());
	}
}































