/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package ${javapackage}.${project}.controller;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.utils.StringUtils;
import ${javapackage}.${project}.entity.${className};
import ${javapackage}.${project}.dto.${className}QueryDto;
import ${javapackage}.${project}.vo.${className}Vo;
import ${javapackage}.${project}.service.${className}Service;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* 类描述：${title} Controller类
* @address ${javapackage}.${project}.web.${className}Controller
* @author ${author}
* @email ${email}
* @date 创建时间：${.now}
* @version 1.0
*/
@RestController
@Api(tags = "${title}")
@RequestMapping(value = BaseController.SAAS_URL + "/${smallClassName}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ${className}Controller{

	@Autowired
	private ${className}Service ${smallClassName}Service;

	@ApiOperation(value = "分页查询")
	@GetMapping
	public PageInfo<${className}Vo> page(${className}QueryDto dto) {
		return ${smallClassName}Service.findPage(dto);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public ${className} getById(@PathVariable("id") String id) {
		return ${smallClassName}Service.getById(id);
	}

	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
	@DeleteMapping("/{id}")
	public Boolean removeById(@PathVariable("id") String id) {
		List<String> ids = StringUtils.convertList(id);
		return ${smallClassName}Service.removeByIds(ids);
	}

	@ApiOperation(value = "保存")
	@PostMapping
	public ${className} save(@RequestBody ${className} ${smallClassName}) {
		${smallClassName}Service.save(${smallClassName});
		return ${smallClassName};
	}

	@ApiOperation(value = "修改")
	@PutMapping
	public ${className} update(@RequestBody ${className} ${smallClassName}) {
		boolean b = ${smallClassName}Service.updateById(${smallClassName});
		if (!b) {
			//影响行数为0（数据未改变或者数据不存在）
			//返回影响行数需要配置jdbcURL参数useAffectedRows=true
		}
		return ${smallClassName};
	}

}































