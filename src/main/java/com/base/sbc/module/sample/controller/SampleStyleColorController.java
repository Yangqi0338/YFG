/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.sample.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.sample.dto.*;
import com.base.sbc.module.sample.entity.SampleStyleColor;
import com.base.sbc.module.sample.service.SampleStyleColorService;
import com.base.sbc.module.sample.vo.SampleStyleColorVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
* 类描述：样衣-款式配色 Controller类
* @address com.base.sbc.module.sample.web.SampleStyleColorController
* @author mengfanjiang
* @email XX.com
* @date 创建时间：2023-6-28 15:02:46
* @version 1.0
*/
@RestController
@Api(tags = "样衣-款式配色")
@RequestMapping(value = BaseController.SAAS_URL + "/sampleStyleColor", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class SampleStyleColorController{

	@Autowired
	private SampleStyleColorService sampleStyleColorService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getSampleStyleColorList")
	public PageInfo<SampleStyleColorVo> getSampleStyleColorList(Principal user,QuerySampleStyleColorDto querySampleStyleColorDto) {
		return  sampleStyleColorService.getSampleStyleColorList(user,querySampleStyleColorDto);
	}

	@ApiOperation(value = "款式编号查找款式配色")
	@GetMapping("/getStyleAccessoryBystyleNo")
	public  List<SampleStyleColorVo> getStyleAccessoryBystyleNo(@Valid @NotBlank(message = "款式编号不能为空") String designNo) {
		return  sampleStyleColorService.getStyleAccessoryBystyleNo(designNo);
	}

	@ApiOperation(value = "修改吊牌价-款式配色")
	@PostMapping("/updateTagPrice")
	public Boolean updateTagPrice(@Valid @RequestBody UpdateTagPriceDto updateTagPriceDto) {
		return sampleStyleColorService.updateTagPrice(updateTagPriceDto);
	}

	@ApiOperation(value = "大货款号查询-款式配色")
	@GetMapping("/getByStyleNo")
	public List<SampleStyleColorVo> getByStyleNo(QuerySampleStyleColorDto querySampleStyleColorDto) {
		return sampleStyleColorService.getByStyleNo(querySampleStyleColorDto);
	}


	@ApiOperation(value = "批量新增款式配色-款式配色")
	@PostMapping("/batchAddSampleStyleColor")
	public Boolean batchAddSampleStyleColor(@Valid @RequestBody List<AddRevampSampleStyleColorDto> list) {
		return sampleStyleColorService.batchAddSampleStyleColor(list);
	}


	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopSampleStyleColor")
	public Boolean startStopSampleStyleColor(@Valid @RequestBody StartStopDto startStopDto) {
	return sampleStyleColorService.startStopSampleStyleColor(startStopDto);
	}


	@ApiOperation(value = "新增修改款式配色-款式配色")
	@PostMapping("/addRevampSampleStyleColor")
	public Boolean addRevampSampleStyleColor(@Valid @RequestBody AddRevampSampleStyleColorDto addRevampSampleStyleColorDto) {
	return sampleStyleColorService.addRevampSampleStyleColor(addRevampSampleStyleColorDto);
	}

	@ApiOperation(value = "删除款式配色-款式配色")
	@DeleteMapping("/delStyleColor")
	public Boolean delStyleColor(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return sampleStyleColorService.delStyleColor(id);
	}

	@ApiOperation(value = "按颜色id删除样衣下款式配色-款式配色")
    @DeleteMapping("/delSampleStyleColor")
    public Boolean delSampleStyleColor(@Valid @NotBlank(message = "颜色id不能为空") String id, @Valid @NotBlank(message = "样衣id") String styleId) {
        return sampleStyleColorService.delSampleStyleColor(id, styleId);
    }

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public SampleStyleColor getById(@PathVariable("id") String id) {
		return sampleStyleColorService.getById(id);
	}



	@ApiOperation(value = "下发-款式配色")
	@PostMapping("/issueScm")
	public Boolean issueScm(@Valid @RequestBody QuerySampleStyleColorDto querySampleStyleColorDto) {
		return sampleStyleColorService.issueScm(querySampleStyleColorDto.getIds());
	}

    @ApiOperation(value = "获取款式下的颜色id")
    @GetMapping("/getStyleColorId")
    public List<String> getStyleColorId(@Valid @NotBlank(message = "样衣id") String styleId) {
        return sampleStyleColorService.getStyleColorId(styleId);
    }

	@ApiOperation(value = "关联Bom")
	@PostMapping("/relevanceBom")
	public Boolean relevanceBom(@Valid @RequestBody RelevanceBomDto relevanceBomDto) {
		return sampleStyleColorService.relevanceBom(relevanceBomDto);
	}

	@ApiOperation(value = "修改大货款号,波段")
	@PostMapping("/updateStyleNoBand")
	public Boolean updateStyleNoBand(@Valid @RequestBody UpdateStyleNoBandDto updateStyleNoBandDto) {
		return sampleStyleColorService.updateStyleNoBand(updateStyleNoBandDto);
	}

	@ApiOperation(value = "验证配色是否可修改")
	@PostMapping("/verification")
	public Boolean verification( @Valid @RequestBody VerificationDto verificationDto) {
		return sampleStyleColorService.verification(verificationDto.getId());
	}
}































