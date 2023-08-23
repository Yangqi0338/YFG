/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.style.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.style.dto.*;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.vo.StyleColorVo;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
* 类描述：款式-款式配色 Controller类
* @address com.base.sbc.module.sample.web.SampleStyleColorController
* @author mengfanjiang
* @email XX.com
* @date 创建时间：2023-6-28 15:02:46
* @version 1.0
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
		return  styleColorService.getSampleStyleColorList(user,querySampleStyleColorDto);
	}
	@ApiOperation(value = "款式编号查找款式配色")
	@GetMapping("/getStyleAccessoryBystyleNo")
	public  List<StyleColorVo> getStyleAccessoryBystyleNo(@Valid @NotBlank(message = "款式编号不能为空") String designNo) {
		return  styleColorService.getStyleAccessoryBystyleNo(designNo);
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
	public Boolean batchAddSampleStyleColor(@Valid @RequestBody List<AddRevampStyleColorDto> list) {
		return styleColorService.batchAddSampleStyleColor(list);
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
	public Boolean delStyleColor(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return styleColorService.delStyleColor(id);
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



	@ApiOperation(value = "下发-款式配色")
	@PostMapping("/issueScm")
	public ApiResult issueScm(@Valid @RequestBody QueryStyleColorDto querySampleStyleColorDto) {
		return styleColorService.issueScm(querySampleStyleColorDto.getIds());
	}

    @ApiOperation(value = "获取款式下的颜色id")
    @GetMapping("/getStyleColorId")
    public List<String> getStyleColorId(@Valid @NotBlank(message = "款式id") String styleId) {
        return styleColorService.getStyleColorId(styleId);
    }

	@ApiOperation(value = "关联Bom")
	@PostMapping("/relevanceBom")
	public Boolean relevanceBom(@Valid @RequestBody RelevanceBomDto relevanceBomDto) {
		return styleColorService.relevanceBom(relevanceBomDto);
	}

	@ApiOperation(value = "修改大货款号,波段")
	@PostMapping("/updateStyleNoBand")
	public Boolean updateStyleNoBand(@Valid @RequestBody UpdateStyleNoBandDto updateStyleNoBandDto) {
		return styleColorService.updateStyleNoBand(updateStyleNoBandDto);
	}

	@ApiOperation(value = "验证配色是否可修改")
	@PostMapping("/verification")
	public Boolean verification( @Valid @RequestBody PublicStyleColorDto publicStyleColorDto) {
		return styleColorService.verification(publicStyleColorDto.getId());
	}

	@ApiOperation(value = "解锁配色")
	@PostMapping("/unlockStyleColor")
	public Boolean unlockStyleColor( @Valid @RequestBody PublicStyleColorDto publicStyleColorDto) {
		return styleColorService.unlockStyleColor(publicStyleColorDto);
	}

	@ApiOperation(value = "新增次品款")
	@PostMapping("/addDefective")
	public Boolean addDefective( @Valid @RequestBody PublicStyleColorDto publicStyleColorDto) {
		return styleColorService.addDefective(publicStyleColorDto);
	}

	@ApiOperation(value = "更新下单标记")
	@PostMapping("/updateOrderFlag")
	public Boolean updateOrderFlag( @Valid @RequestBody PublicStyleColorDto publicStyleColorDto) {
		return styleColorService.updateOrderFlag(publicStyleColorDto);
	}

}

