package com.base.sbc.module.goodscolor.controller;


import com.base.sbc.module.goodscolor.entity.UsingStatusVO;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.base.BaseGlobal;
import  com.base.sbc.config.common.base.Page;
import com.base.sbc.module.goodscolor.entity.GoodsColor;
import com.base.sbc.module.goodscolor.service.GoodsColorService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;


@RestController
@Api(tags="物料颜色库")
@RequestMapping(value = BaseController.SAAS_URL+"/goodsColor", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SaasGoodsColorController extends BaseController {
    @Autowired
    private GoodsColorService goodsColorService;
    @Autowired
    private UserUtils userUtils;






    @ApiOperation(value = "分页查询物料颜色详情", notes = "查询物料颜色详情(params{pageNum:第几页,pageSize:每页条数,状态:(0正常 1不正常),sql:sql条件,search:搜索条件(名称.编码)})")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userCompany", value = "用户企业ID ", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态(0正常 1不正常)", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sql", value = "sql", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "search", value = "搜索条件()", required = false, dataType = "String", paramType = "query")})
    @GetMapping("/selectColorList")
    public ApiResult selectColorList(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, Page page) {
        com.github.pagehelper.Page<GoodsColor> pages = PageHelper.startPage(page.getPageNum(), page.getPageSize());

        // 构造查询条件
        QueryCondition qc = new QueryCondition();
        qc.andEqualTo(COMPANY_CODE, userCompany);
        qc.andEqualTo("del_flag", "0");

        // 搜索条件(颜色、颜色编码)
        if(StringUtils.isNotBlank(page.getSearch())){
            qc.andLikeOr(page.getSearch(), "color", "color_code");
        }
        // 状态(0正常 1不正常)
        if(StringUtils.isNotBlank(page.getStatus())){
            qc.andEqualTo("status", page.getStatus());
        }
        if(StringUtils.isNotBlank(page.getSql())){
            qc.andConditionSql(page.getSql());
        }
        // 排序
        if(StringUtils.isNotBlank(page.getOrder())) {
            qc.setOrderByClause(page.getOrder());
        }else {
            qc.setOrderByClause("update_date DESC");
        }
        return goodsColorService.findPageByCondition(qc,page);
    }

    @ApiOperation(value = "新增物料颜色", notes = "新增物料颜色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userCompany", value = "用户企业ID ", required = true, dataType = "String", paramType = "header")})
    @PostMapping("/insertColor")
    public ApiResult insertColor(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany,
                                 Principal user,
                                 @Valid @RequestBody GoodsColor materialColor) {
        // 构造查询条件
        QueryCondition qc = new QueryCondition();
        qc.andEqualTo(COMPANY_CODE, userCompany);
        qc.andConditionSql(" ( `color_code` = '" + materialColor.getColorCode() + "'  OR `color` = '" + materialColor.getColor() + "') ");
        List<GoodsColor> colorList = goodsColorService.findByCondition(qc);

        // 获取登录用户信息
        GroupUser groupUser = userUtils.getUserBy(user);
        String userId = groupUser.getId();
        String userName = groupUser.getName();

        // 判断颜色是否重复
        if(colorList != null && !colorList.isEmpty()) {
            for (GoodsColor color : colorList) {
                // 如果重复，且这条数据没被删除  提示数据重复
                if("0".equals(color.getDelFlag())) {
                    return insertDataRepeat(materialColor.getColorCode().equals(color.getColorCode()) ? "色号重复" : "颜色名称重复");
                }

                // 否则，修改数据为未删除
                materialColor.setId(color.getId());
                materialColor.setCompanyCode(userCompany);
                materialColor.setEntityBasicInfo(userId, userName, "0");
                if(goodsColorService.update(materialColor, materialColor.getId()) > 0 ) {
                    return insertSuccess(materialColor);
                }
                return updateNotFound("保存失败");
            }
        }

        // 设置必填信息
        materialColor.preInsert();
        materialColor.setCompanyCode(userCompany);
        materialColor.setEntityBasicInfo(userId, userName, "0");

        // 保存数据
        if(goodsColorService.insert(materialColor) > 0) {
            return insertSuccess(materialColor);
        }
        return insertAttributeNotRequirements("保存失败");
    }

    @ApiOperation(value = "批量启用/停用制造流程", notes = "批量启用/停用制造流程 params{ids:制造流程ids(多个用逗号拼接), status:0启用1停用}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userCompany", value = "用户企业ID ", required = true, dataType = "String", paramType = "header")})
    @PatchMapping("/batchUsingStatus")
    public ApiResult batchUsingStatus(@RequestHeader(BaseConstant.USER_COMPANY) String companyCode,
                                      @Valid @RequestBody UsingStatusVO usingStatusVO) {
        // 如果status的值，不等于 0 或者 1
        if(!StringUtils.equalsAny(usingStatusVO.getStatus(), BaseGlobal.DEL_FLAG_NORMAL, BaseGlobal.DEL_FLAG_DELETE)) {
            return updateAttributeNotRequirements("操作失败，状态异常");
        }

        // 构造查询条件
        QueryCondition qc = new QueryCondition();
        qc.andEqualTo(COMPANY_CODE, companyCode);
        qc.andIn("id", StringUtils.convertList(usingStatusVO.getIds()));

        // 设置启用或者禁用
        GoodsColor entity = new GoodsColor();
        entity.setStatus(usingStatusVO.getStatus());
        qc.setT(entity);

        // 批量修改
        if(goodsColorService.batchUpdateByCondition(qc) > 0) {
            return updateSuccess("操作成功");
        }
        return updateNotFound();
    }

    @ApiOperation(value = "批量删除物料颜色", notes = "批量删除物料颜色 params{id:物料颜色id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userCompany", value = "用户企业ID ", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "ids", value = "物料颜色id(多个,隔开)", required = true, dataType = "String", paramType = "query") })
    @DeleteMapping("/batchDeleteColor")
    public ApiResult batchDeleteColor(@NotNull(message = "id不能为空") @RequestParam("ids") String ids) {
        // 构造查询条件
        QueryCondition qc = new QueryCondition();
        qc.andIn("id", StringUtils.convertList(ids));

        // 设置删除标识为已删除
        GoodsColor materialColor = new GoodsColor();
        materialColor.setDelFlag("1");
        qc.setT(materialColor);

        // 逻辑删除
        if (goodsColorService.updateByCondition("update", qc) > 0) {
            return deleteSuccess(ids);
        }
        return deleteNotFound();
    }

    @ApiOperation(value = "修改物料颜色", notes = "修改物料颜色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userCompany", value = "用户企业ID ", required = true, dataType = "String", paramType = "header")})
    @PutMapping("/updateColor")
    public ApiResult updateColor(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany,
                                 Principal user,
                                 @Valid @RequestBody GoodsColor materialColor) {
        // 通过name查询数据  条件：相同企业编码、相同颜色分类id、相同颜色名
        GoodsColor entity = goodsColorService.getByCondition(new QueryCondition()
                .andEqualTo(COMPANY_CODE, userCompany)
                // .andEqualTo("color_type_id", materialColor.getColorTypeId())
                .andEqualTo("color", materialColor.getColor()));
        // 如果查到的数据的id 和 被修改的数据的id 不一致。 说明name重复
        if(entity != null && !entity.getId().equals(materialColor.getId())) {
            return insertDataRepeat("颜色名称重复");
        }

        // 获取登录用户信息
        GroupUser groupUser = userUtils.getUserBy(user);
        // 设置必填信息
        materialColor.preUpdate();
        materialColor.setCompanyCode(userCompany);
        materialColor.setUpdateId(groupUser.getId());
        materialColor.setUpdateName(groupUser.getName());

        // 修改数据
        if(goodsColorService.updateAll(materialColor) > 0) {
            return updateSuccess(null);
        }
        return updateAttributeNotRequirements("修改失败");
    }
}
