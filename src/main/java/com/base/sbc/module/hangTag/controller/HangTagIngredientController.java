package com.base.sbc.module.hangTag.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.hangTag.dto.HangTagIngredientDTO;
import com.base.sbc.module.hangTag.entity.HangTagIngredient;
import com.base.sbc.module.hangTag.service.HangTagIngredientService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/8/29 21:01:50
 * @mail 247967116@qq.com
 */
@RestController
@Api(tags = "吊牌成分表")
@RequestMapping(value = BaseController.SAAS_URL + "/hangTagIngredient", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class HangTagIngredientController extends BaseController{
    private final HangTagIngredientService hangTagIngredientService;
    @GetMapping("/queryPage")
    public ApiResult queryPage(HangTagIngredientDTO hangTagIngredientDTO){
        BaseQueryWrapper<HangTagIngredient> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.eq("hang_tag_id",hangTagIngredientDTO.getHangTagId());
        PageHelper.startPage(hangTagIngredientDTO);
        List<HangTagIngredient> list = hangTagIngredientService.list(queryWrapper);
        return selectSuccess(new PageInfo<>(list));
    }
}
