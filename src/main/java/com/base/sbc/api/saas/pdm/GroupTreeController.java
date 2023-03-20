/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.api.saas.pdm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.pdm.entity.GroupTree;
import com.base.sbc.pdm.service.GroupTreeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * 类描述：分组树表 Controller类
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.plm.web.GroupController
 * @email li_xianglin@126.com
 * @date 创建时间：2022-5-23 14:14:39
 */
@RestController
@Api(value = "分组树表", tags = {"分组树表接口"})
@RequestMapping(value = BaseController.SAAS_URL+"/groupTree", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class GroupTreeController extends BaseController {



    @Autowired
    private GroupTreeService groupService;


    @ApiOperation(value = "查询分组树表", notes = "根据url的id来获取分组树表详细信息")
    @GetMapping(value = "/{id}")
    public ApiResult get(@PathVariable String id, Page page) throws Exception {
        List<String> ids = StringUtils.convertList(id);
        List<GroupTree> list = Lists.newArrayList();
        QueryCondition qc = new QueryCondition(getUserCompany());
        if (ids.size() != 1) {
            qc.andIn(BaseGlobal.ID, ids);
            list = groupService.findByCondition(qc);
        } else {
            //如果 查询1个
            GroupTree db = groupService.getById(id);
            if (db != null) {
                list.add(db);
            }
        }
        if (list == null || list.size() == 0) {
            return ApiResult.error("找不到数据", HttpStatus.NOT_FOUND.value());
        }
        return ApiResult.success("success", list);
    }


    @ApiOperation(value = "多数据查询", notes = "分页获取所有的分组树表")
    @GetMapping("/list")
    public ApiResult list(GroupTree groupTree) {
        //初始化查询条件构造器
        QueryCondition qc = new QueryCondition(getUserCompany());
        qc.andEqualTo("company_code",getUserCompany());
        qc.setOrderByClause("sort asc");
        if(StrUtil.isNotBlank(groupTree.getGroupName())){
            qc.andEqualTo("group_name",groupTree.getGroupName());
        }

        if(StrUtil.isNotBlank(groupTree.getParentId())){
            qc.andEqualTo("parent_id",groupTree.getParentId());
        }
        return ApiResult.error(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND.value());
    }

    @ApiOperation(value = "新增/修改分组树表", notes = "有id修改，无id 新增")
    @ApiImplicitParam(name = "group", value = "分组树表", required = true, dataType = "GroupTree")
    @PostMapping
    public ApiResult save(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @Valid @RequestBody GroupTree group) throws Exception {
        if(StringUtils.isAnyBlank(group.getGroupName(),group.getName())){
            return updateAttributeNotRequirements("请填写非空字段");
        }
        if(StringUtils.isNotBlank(group.getId())){
            group.preUpdate();
            return ApiResult.success("success", groupService.updateAll(group));
        }else{
            QueryCondition qc = new QueryCondition(getUserCompany());
            qc.andEqualTo("group_name", group.getGroupName());
            qc.andEqualTo("name",group.getName());
            qc.andEqualTo(COMPANY_CODE,userCompany);
            List<GroupTree> db=groupService.findByCondition(qc);
            if (CollUtil.isNotEmpty(db)) {
                return ApiResult.error("分组重复", 500);
            }
            group.preUpdate();
            group.preInsert();
            group.setCompanyCode(userCompany);
            return ApiResult.success("success", groupService.insert(group));
        }
    }
    @ApiOperation(value = "删除分组树表", notes = "根据url的id来指定删除对象(逗号隔开删除多个)")
    @DeleteMapping(value = "/{id}")
    public ApiResult delete(@PathVariable String id) throws Exception {
        List<String> ids = StringUtils.convertList(id);
        int i = 0;
        if (ids.size() != 1) {
            QueryCondition qc = new QueryCondition(getUserCompany());
            qc.andIn(BaseGlobal.ID, ids);
            i = groupService.deleteByCondition(qc);
        } else {
            //如果只删除一个
            i = groupService.deleteById(id);
        }
        if (i > 0) {
            return ApiResult.success("success", i);
        } else {
            return ApiResult.error(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND.value());
        }

    }


    @ApiOperation(value = "查询分组树", notes = "根据url的groupName查询")
    @ApiImplicitParam(name = "group", value = "分组树表", required = true, dataType = "String")
    @GetMapping("/tree")
    public ApiResult tree(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestParam(value = "groupName",required = true) String groupName,String name,String level) {
        if (StringUtils.isBlank(groupName)) {
            return selectNotFound();
        }
        QueryCondition qc = new QueryCondition(getUserCompany());
        if (StrUtil.isNotBlank(groupName)) {
            qc.andEqualTo("group_name", groupName);
        }
        if (StrUtil.isNotBlank(name)) {
            qc.andEqualTo("name", name);
        }
        if (StrUtil.isNotBlank(level)) {
            qc.andEqualTo("level", level);
        }
        qc.andEqualTo(COMPANY_CODE,userCompany);
        List<GroupTree> groups = groupService.findByCondition(qc);
        if (CollUtil.isEmpty(groups)) {
            return selectNotFound();
        }
        return selectSuccess(groups);
    }


}
