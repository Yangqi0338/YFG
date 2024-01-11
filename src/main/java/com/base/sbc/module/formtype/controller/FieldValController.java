package com.base.sbc.module.formtype.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.formtype.dto.FieldValPageDto;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.formtype.service.FieldValService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.base.sbc.module.formtype.utils.FieldValDataGroupConstant.PLANNING_CATEGORY_ITEM_DIMENSION;

/**
 * @author 卞康
 * @date 2023/11/18 14:28:08
 * @mail 247967116@qq.com
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = BaseController.SAAS_URL + "/fieldVal", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

public class FieldValController extends BaseController {

    private final FieldValService fieldValService;

    @GetMapping("queryPage")
    public ApiResult queryPage(FieldValPageDto dto) {
        PageHelper.startPage(dto);
        QueryWrapper<FieldVal> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.eq("data_group",PLANNING_CATEGORY_ITEM_DIMENSION);
        List<FieldVal> list = fieldValService.list(queryWrapper);
        return selectSuccess(new PageInfo<>(list));
    }
}
