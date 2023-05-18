/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.sample.controller;

import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.sample.dto.SamplePageDto;
import com.base.sbc.module.sample.dto.SampleSaveDto;
import com.base.sbc.module.sample.dto.SendSampleMakingDto;
import com.base.sbc.module.sample.entity.Sample;
import com.base.sbc.module.sample.service.SampleService;
import com.base.sbc.module.sample.vo.DesignDocTreeVo;
import com.base.sbc.module.sample.vo.SampleVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* 类描述：样衣 Controller类
* @address com.base.sbc.module.sample.web.SampleController
* @author lxl
* @email lxl.fml@gmail.com
* @date 创建时间：2023-5-9 11:16:15
* @version 1.0
*/
@RestController
@Api(tags = "样衣设计相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/sample", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class SampleController{

	@Autowired
	private SampleService sampleService;

	@ApiOperation(value = "分页查询")
	@GetMapping()
	public PageInfo pageInfo(@Valid SamplePageDto dto){
		return sampleService.queryPageInfo(dto);
	}
	@ApiOperation(value = "明细信息")
	@GetMapping("/{id}")
	public SampleVo getDetail(@PathVariable("id") String id){
		return sampleService.getDetail(id);
	}
	@ApiOperation(value = "保存")
	@PostMapping
	public SampleVo save(@RequestBody SampleSaveDto dto) {
		Sample  sample=sampleService.saveSample(dto);
		return sampleService.getDetail(sample.getId());
	}

	@ApiOperation(value = "发送打板指令")
	@PostMapping("/sendSampleMaking")
	public boolean sendSampleMaking(@Valid @RequestBody SendSampleMakingDto dto){
		return sampleService.sendSampleMaking(dto);
	}
	@ApiOperation(value = "发起审批")
	@GetMapping("/startApproval")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id" ,value = "id",required = true,paramType = "query")
	})
	public boolean startApproval(@NotBlank(message = "编号不能为空") String id){
		return sampleService.startApproval(id);
	}
	/**
	 * 处理审批
	 * @param dto
	 * @return
	 */
	@ApiIgnore
	@PostMapping("/approval")
	public boolean approval(@RequestBody AnswerDto dto){
		return sampleService.approval(dto);
	}


	@ApiOperation(value = "设计档案左侧树",notes = "（0级:年份季节,1级:波段,2级:大类,3级:品类）")
	@GetMapping("/queryDesignDocTree")
	public List<DesignDocTreeVo> queryDesignDocTree(@RequestBody  DesignDocTreeVo designDocTreeVo){
		return sampleService.queryDesignDocTree(designDocTreeVo);
	}
}































