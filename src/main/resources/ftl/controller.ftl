/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package ${javapackage}.api.saas.${project};

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.QueryCondition;
import ${javapackage}.${project}.entity.${className};
import ${javapackage}.${project}.service.${className}Service;
import com.base.sbc.config.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;



/**
 * 类描述：${title} Controller类
 * @address ${javapackage}.${project}.web.${className}Controller
 * @author ${author}
 * @email ${email}
 * @date 创建时间：${.now}
 * @version 1.0
 */
@RestController
@Api(value = "${title}", description = "与${title}相关的所有接口信息",tags={"${title}接口"})
@RequestMapping(value = "/${smallClassName}s")
public class ${className}Controller {

	QueryCondition qc = new QueryCondition();

	@Autowired
	private ${className}Service ${smallClassName}Service;


	@ApiOperation(value="查询${title}", notes="根据url的id来获取${title}详细信息")
    @GetMapping(value = "/{id}")
    public ApiResult get(@PathVariable String id) throws Exception {
    	List<String> ids = StringUtils.convertList(id);
    	List<${className}> list = Lists.newArrayList();
		if(ids.size()!=1) {
			qc.init();
			qc.andIn(BaseGlobal.ID, ids);
			list = ${smallClassName}Service.findByCondition(qc);
		}else {
			${className} db = ${smallClassName}Service.getById(id);//如果 查询1个
			if(db!=null) {
				list.add(db);
			}
		}
    	if(list==null || list.size()==0) {

    		return ApiResult.error("找不到数据",HttpStatus.NOT_FOUND.value());
    	}
        return ApiResult.success("success",list);
    }


	@ApiOperation(value="多数据查询", notes="分页获取所有的${title}")
    @ApiImplicitParams({
	    @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "String",paramType="query"),
	    @ApiImplicitParam(name = "pageSize", value = "每页条数", required = false, dataType = "String",paramType="query"),
	    @ApiImplicitParam(name = "order", value = "排序字段", required = false, dataType = "String",paramType="query"),
	    @ApiImplicitParam(name = "asc", value = "正/倒序(asc/desc)", required = false, dataType = "String",paramType="query")
    	})
    @GetMapping
    public ApiResult getByPage(String pageNum, String pageSize,String order,String asc) {
		qc.init();//初始化查询条件构造器
		if(StringUtils.isNoneBlank(order) && ("asc".equals(asc) || "desc".equals(asc))) {
			qc.setOrderByClause(order + " " + asc);
		}
		if(StringUtils.isNoneBlank(pageNum) && StringUtils.isNoneBlank(pageSize) && Integer.parseInt(pageNum)>0 && Integer.parseInt(pageSize)>0) {
			Page<${className}> page = PageHelper.startPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
			${smallClassName}Service.findByCondition(qc);
			PageInfo<${className}> pages = page.toPageInfo();
			List<${className}> list = pages.getList();
			if(list!=null && list.size()>0) {
				return ApiResult.success("success", pages);
			}
			return ApiResult.error(HttpStatus.NOT_FOUND.getReasonPhrase(),HttpStatus.NOT_FOUND.value());
		}else {
			List<${className}> list = ${smallClassName}Service.findByCondition(qc);
			if(list!=null && list.size()>0) {
				return ApiResult.success("success", list);
			}
			return ApiResult.error(HttpStatus.NOT_FOUND.getReasonPhrase(),HttpStatus.NOT_FOUND.value());
		}
	}

    @ApiOperation(value="新增${title}", notes="根据url的id来获取${title}详细信息")
    @ApiImplicitParam(name = "${smallClassName}", value = "${title}", required = true, dataType = "${className}")
    @PostMapping
    public ApiResult save(@Valid @RequestBody ${className} ${smallClassName}) throws Exception {
    	${className} db = ${smallClassName}Service.getById(${smallClassName}.getId());
    	if(db!=null) {
    		return ApiResult.error("ID已存在",500);
    	}
        return ApiResult.success("success",${smallClassName}Service.insert(${smallClassName}));
    }



    @ApiOperation(value="删除${title}", notes="根据url的id来指定删除对象(逗号隔开删除多个)")
	@DeleteMapping(value = "/{id}")
    public ApiResult delete(@PathVariable String id) throws Exception {
		List<String> ids = StringUtils.convertList(id);
		int i = 0;
		if(ids.size()!=1) {
			qc.init();
			qc.andIn(BaseGlobal.ID, ids);
	        i = ${smallClassName}Service.deleteByCondition(qc);
		}else {
			i = ${smallClassName}Service.deleteById(id);//如果只删除一个
		}
        if(i>0) {
        	return ApiResult.success("success",i);
        }else {
        	return ApiResult.error(HttpStatus.NOT_FOUND.getReasonPhrase(),HttpStatus.NOT_FOUND.value());
        }

    }

    @ApiOperation(value="更新${title}", notes="根据url的id来指定更新对象，并根据传过来的${title}信息来更新详细信息,注意不存在将会清空  id必填")
    @ApiImplicitParam(name = "${smallClassName}", value = "${title}", required = true, dataType = "${className}")
    @PutMapping
    public ApiResult updateAll(@Valid @RequestBody ${className} ${smallClassName}) throws Exception {
    	${className} d = ${smallClassName}Service.getById(${smallClassName}.getId());
        if(d==null) {
        	return ApiResult.error(HttpStatus.NOT_FOUND.getReasonPhrase(),HttpStatus.NOT_FOUND.value());
        }
        BeanUtils.copyProperties(${smallClassName}, d, BaseGlobal.ID);//忽略ID
        ${smallClassName}Service.updateAll(d);
        return ApiResult.success("success",d);
    }

    @ApiOperation(value="更新${title}(只修改不为空字段)", notes="根据url的id来指定更新对象，并根据传过来的信息来更新${title}详细信息  id必填")
    @ApiImplicitParam(name = "${smallClassName}", value = "${title}", required = true, dataType = "${className}")
    @PatchMapping
    public ApiResult update(@RequestBody ${className} ${smallClassName}) throws Exception {
    	if(StringUtils.isBlank(String.valueOf(${smallClassName}.getId()))) {
    		return ApiResult.error(HttpStatus.NOT_FOUND.getReasonPhrase(),HttpStatus.NOT_FOUND.value());
    	}
    	List<String> ids = StringUtils.convertList(String.valueOf(${smallClassName}.getId()));
		qc.init();
		qc.andIn(BaseGlobal.ID, ids);
		${smallClassName}.setId(null);//必须把不要的字段设空
		qc.setT(${smallClassName});//设置除id外 不为空的属性  ，修改新值，值为这个实体传过来的值
		return ApiResult.success("success",${smallClassName}Service.batchUpdateByCondition(qc));
    }

}
