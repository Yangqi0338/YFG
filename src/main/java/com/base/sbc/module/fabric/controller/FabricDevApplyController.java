/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.fabric.dto.FabricDevApplySearchDTO;
import com.base.sbc.module.fabric.entity.FabricDevApply;
import com.base.sbc.module.fabric.service.FabricDevApplyService;
import com.base.sbc.module.fabric.vo.FabricDevApplyListVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 类描述：面料开发申请（主表） Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.web.FabricDevApplyController
 * @email your email
 * @date 创建时间：2023-8-7 11:01:20
 */
@RestController
@Api(tags = "面料开发申请（主表）")
@RequestMapping(value = BaseController.SAAS_URL + "/fabricDevApply", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class FabricDevApplyController {

    @Autowired
    private FabricDevApplyService fabricDevApplyService;

    @ApiOperation(value = "获取开发申请列表")
    @PostMapping("/getDevApplyList")
    public PageInfo<FabricDevApplyListVO> getDevApplyList(@Valid @RequestBody FabricDevApplySearchDTO fabricDevApplySearchDTO) {
        return fabricDevApplyService.getDevApplyList(fabricDevApplySearchDTO);
    }


    @ApiOperation(value = "明细-通过id查询")
    @GetMapping("/{id}")
    public FabricDevApply getById(@PathVariable("id") String id) {
        return fabricDevApplyService.getById(id);
    }

    @ApiOperation(value = "删除-通过id查询,多个逗号分开")
    @DeleteMapping("/{id}")
    public Boolean removeById(@PathVariable("id") String id) {
        List<String> ids = StringUtils.convertList(id);
        return fabricDevApplyService.removeByIds(ids);
    }

    @ApiOperation(value = "保存")
    @PostMapping
    public FabricDevApply save(@RequestBody FabricDevApply fabricDevApply) {
        fabricDevApplyService.save(fabricDevApply);
        return fabricDevApply;
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public FabricDevApply update(@RequestBody FabricDevApply fabricDevApply) {
        boolean b = fabricDevApplyService.updateById(fabricDevApply);
        if (!b) {
            //影响行数为0（数据未改变或者数据不存在）
            //返回影响行数需要配置jdbcURL参数useAffectedRows=true
        }
        return fabricDevApply;
    }

}































