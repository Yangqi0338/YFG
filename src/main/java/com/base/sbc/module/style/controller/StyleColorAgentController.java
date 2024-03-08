/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.style.entity.StyleColorAgent;
import com.base.sbc.module.style.service.StyleColorAgentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 类描述：代理货品资料 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.style.web.StyleColorAgentController
 * @email your email
 * @date 创建时间：2024-2-20 14:45:41
 */
@RestController
@Api(tags = "代理货品资料")
@RequestMapping(value = BaseController.SAAS_URL + "/styleColorAgent", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class StyleColorAgentController {

    @Autowired
    private StyleColorAgentService styleColorAgentService;

    @ApiOperation(value = "分页查询")
    @GetMapping
    public PageInfo<StyleColorAgent> page(Page page) {
        PageHelper.startPage(page);
        List<StyleColorAgent> list = styleColorAgentService.list();
        return new PageInfo<>(list);
    }

    @ApiOperation(value = "明细-通过id查询")
    @GetMapping("/{id}")
    public StyleColorAgent getById(@PathVariable("id") String id) {
        return styleColorAgentService.getById(id);
    }

    @ApiOperation(value = "删除-通过id查询,多个逗号分开")
    @DeleteMapping("/{id}")
    public Boolean removeById(@PathVariable("id") String id) {
        List<String> ids = StringUtils.convertList(id);
        return styleColorAgentService.removeByIds(ids);
    }

    @ApiOperation(value = "保存")
    @PostMapping
    public StyleColorAgent save(@RequestBody StyleColorAgent styleColorAgent) {
        styleColorAgentService.save(styleColorAgent);
        return styleColorAgent;
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public StyleColorAgent update(@RequestBody StyleColorAgent styleColorAgent) {
        boolean b = styleColorAgentService.updateById(styleColorAgent);
        if (!b) {
            //影响行数为0（数据未改变或者数据不存在）
            //返回影响行数需要配置jdbcURL参数useAffectedRows=true
        }
        return styleColorAgent;
    }

}































