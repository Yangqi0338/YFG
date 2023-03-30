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


	@Autowired
	private MaterialCollectService materialCollectService;

	@Resource
	private UserUtils userUtils;

    @ApiOperation(value="素材收藏", notes="素材收藏")
    @PostMapping("/add")
    public String add(@RequestBody MaterialCollect materialCollect) throws Exception {
		String userId = userUtils.getUserId();
		QueryCondition qc=new QueryCondition();
		qc.andEqualTo("material_id",materialCollect.getMaterialId());
		qc.andEqualTo("user_id",userId);
		MaterialCollect materialCollect1 = materialCollectService.getByCondition(qc);
		if (materialCollect1!=null){
			throw new OtherException("请勿重复收藏");
		}
		materialCollect.preInsert();
		materialCollect.setCompanyCode(userUtils.getCompanyCode());
		materialCollect.setUserId(userId);
		materialCollect.setDelFlag("0");
		materialCollectService.insert(materialCollect);
        return "新增成功";
    }

	@ApiOperation(value="素材取消收藏", notes="素材取消收藏")
	@DeleteMapping("/del")
	public String del(MaterialCollect materialCollect) throws Exception {
		String userId = userUtils.getUserId();
		QueryCondition qc=new QueryCondition();
		qc.andEqualTo("material_id",materialCollect.getMaterialId());
		qc.andEqualTo("user_id",userId);
		MaterialCollect materialCollect1 = materialCollectService.getByCondition(qc);
		materialCollectService.deleteByIdDelFlag(materialCollect1,materialCollect1.getId());
		return "取消收藏成功";
	}


}
