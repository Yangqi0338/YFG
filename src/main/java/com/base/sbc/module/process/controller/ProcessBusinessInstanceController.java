/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.process.controller;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.process.dto.InitiateProcessDto;
import com.base.sbc.module.process.service.ProcessBusinessInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
* 类描述：流程配置-业务实例 Controller类
* @address com.base.sbc.module.process.web.ProcessBusinessInstanceController
* @author mengfanjiang
* @email lxl.fml@gmail.com
* @date 创建时间：2023-6-6 15:03:26
* @version 1.0
*/
@RestController
@Api(tags = "流程配置-业务实例")
@RequestMapping(value = BaseController.SAAS_URL + "/processBusinessInstance", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ProcessBusinessInstanceController{

	@Autowired
	private ProcessBusinessInstanceService processBusinessInstanceService;


	/*发起流程*/
	@ApiOperation(value = "发起流程" )
	@PostMapping("/initiateProcess")
	public Boolean initiateProcess(@Valid @RequestBody InitiateProcessDto initiateProcessDto) {
		return processBusinessInstanceService.initiateProcess(initiateProcessDto);
	}


	/*完成*/
	@ApiOperation(value = "完成" )
	@GetMapping("/complete")
	public Map complete(String businessDataId, String action, Object object) {
		return processBusinessInstanceService.complete(businessDataId,action,object);
	}



}































