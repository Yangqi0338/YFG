/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.api.saas.pdm;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.base.sbc.pdm.entity.MaterialCollect;
import com.base.sbc.pdm.service.MaterialCollectService;
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
 * 类描述：素材收藏表 Controller类
 * @address com.base.sbc.pdm.web.MaterialCollectController
 * @author lile
 * @email lilemyemail@163.com
 * @date 创建时间：2023-3-24 9:44:55
 * @version 1.0
 */
@RestController
@Api(value = "与素材收藏相关的所有接口信息", tags = {"素材收藏表接口"})
@RequestMapping(value = BaseController.SAAS_URL + "/materialCollect", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MaterialCollectController {

	QueryCondition qc = new QueryCondition();

	@Autowired
	private MaterialCollectService materialCollectService;

	@Resource
	private UserUtils userUtils;


	@ApiOperation(value="查询素材收藏表", notes="根据url的id来获取素材收藏表详细信息")
    @GetMapping(value = "/{id}")
    public ApiResult get(@PathVariable String id) throws Exception {
    	List<String> ids = StringUtils.convertList(id);
    	List<MaterialCollect> list = Lists.newArrayList();
		if(ids.size()!=1) {
			qc.init();
			qc.andIn(BaseGlobal.ID, ids);
			list = materialCollectService.findByCondition(qc);
		}else {
			MaterialCollect db = materialCollectService.getById(id);//如果 查询1个
			if(db!=null) {
				list.add(db);
			}
		}
    	if(list==null || list.size()==0) {

    		return ApiResult.error("找不到数据",HttpStatus.NOT_FOUND.value());
    	}
        return ApiResult.success("success",list);
    }


	@ApiOperation(value="多数据查询", notes="分页获取所有的素材收藏表")
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
			Page<MaterialCollect> page = PageHelper.startPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
			materialCollectService.findByCondition(qc);
			PageInfo<MaterialCollect> pages = page.toPageInfo();
			List<MaterialCollect> list = pages.getList();
			if(list!=null && list.size()>0) {
				return ApiResult.success("success", pages);
			}
			return ApiResult.error(HttpStatus.NOT_FOUND.getReasonPhrase(),HttpStatus.NOT_FOUND.value());
		}else {
			List<MaterialCollect> list = materialCollectService.findByCondition(qc);
			if(list!=null && list.size()>0) {
				return ApiResult.success("success", list);
			}
			return ApiResult.error(HttpStatus.NOT_FOUND.getReasonPhrase(),HttpStatus.NOT_FOUND.value());
		}
	}

    @ApiOperation(value="素材收藏", notes="素材收藏")
    @ApiImplicitParam(name = "materialCollect", value = "素材收藏表", required = true, dataType = "MaterialCollect")
    @PostMapping("/add")
    public String save(@RequestBody MaterialCollect materialCollect) throws Exception {
		String userId = userUtils.getUserId();
		QueryCondition qc=new QueryCondition();
		qc.andEqualTo("material_id",materialCollect.getMaterialId());
		qc.andEqualTo("user_id",userId);
		MaterialCollect materialCollect1 = materialCollectService.getByCondition(qc);
		if (materialCollect1!=null){
			throw new OtherException("请勿重复收藏");
		}
		materialCollect.preInsert();
		materialCollect.setUserId(userId);
		materialCollectService.insert(materialCollect);
        return "新增成功";
    }



    @ApiOperation(value="删除素材收藏表", notes="根据url的id来指定删除对象(逗号隔开删除多个)")
	@DeleteMapping(value = "/{id}")
    public ApiResult delete(@PathVariable String id) throws Exception {
		List<String> ids = StringUtils.convertList(id);
		int i = 0;
		if(ids.size()!=1) {
			qc.init();
			qc.andIn(BaseGlobal.ID, ids);
	        i = materialCollectService.deleteByCondition(qc);
		}else {
			i = materialCollectService.deleteById(id);//如果只删除一个
		}
        if(i>0) {
        	return ApiResult.success("success",i);
        }else {
        	return ApiResult.error(HttpStatus.NOT_FOUND.getReasonPhrase(),HttpStatus.NOT_FOUND.value());
        }

    }

    @ApiOperation(value="更新素材收藏表", notes="根据url的id来指定更新对象，并根据传过来的素材收藏表信息来更新详细信息,注意不存在将会清空  id必填")
    @ApiImplicitParam(name = "materialCollect", value = "素材收藏表", required = true, dataType = "MaterialCollect")
    @PutMapping
    public ApiResult updateAll(@Valid @RequestBody MaterialCollect materialCollect) throws Exception {
    	MaterialCollect d = materialCollectService.getById(materialCollect.getId());
        if(d==null) {
        	return ApiResult.error(HttpStatus.NOT_FOUND.getReasonPhrase(),HttpStatus.NOT_FOUND.value());
        }
        BeanUtils.copyProperties(materialCollect, d, BaseGlobal.ID);//忽略ID
        materialCollectService.updateAll(d);
        return ApiResult.success("success",d);
    }

    @ApiOperation(value="更新素材收藏表(只修改不为空字段)", notes="根据url的id来指定更新对象，并根据传过来的信息来更新素材收藏表详细信息  id必填")
    @ApiImplicitParam(name = "materialCollect", value = "素材收藏表", required = true, dataType = "MaterialCollect")
    @PatchMapping
    public ApiResult update(@RequestBody MaterialCollect materialCollect) throws Exception {
    	if(StringUtils.isBlank(String.valueOf(materialCollect.getId()))) {
    		return ApiResult.error(HttpStatus.NOT_FOUND.getReasonPhrase(),HttpStatus.NOT_FOUND.value());
    	}
    	List<String> ids = StringUtils.convertList(String.valueOf(materialCollect.getId()));
		qc.init();
		qc.andIn(BaseGlobal.ID, ids);
		materialCollect.setId(null);//必须把不要的字段设空
		qc.setT(materialCollect);//设置除id外 不为空的属性  ，修改新值，值为这个实体传过来的值
		return ApiResult.success("success",materialCollectService.batchUpdateByCondition(qc));
    }

}
