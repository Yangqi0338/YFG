package com.base.sbc.module.hangtag.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.business.HangTagStatusEnum;
import com.base.sbc.module.hangtag.dto.HangTagDTO;
import com.base.sbc.module.hangtag.dto.HangTagIngredientDTO;
import com.base.sbc.module.hangtag.entity.HangTag;
import com.base.sbc.module.hangtag.entity.HangTagIngredient;
import com.base.sbc.module.hangtag.service.HangTagIngredientService;
import com.base.sbc.module.hangtag.service.HangTagService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
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
    private final HangTagService hangTagService;
    @GetMapping("/queryPage")
    public ApiResult queryPage(HangTagIngredientDTO hangTagIngredientDTO){
        BaseQueryWrapper<HangTagIngredient> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.eq("hang_tag_id",hangTagIngredientDTO.getHangTagId());
        PageHelper.startPage(hangTagIngredientDTO);
        List<HangTagIngredient> list = hangTagIngredientService.list(queryWrapper);
        return selectSuccess(new PageInfo<>(list));
    }

    @PostMapping("/save")
    public ApiResult save(@RequestBody HangTagIngredient hangTagIngredient){
        if (hangTagIngredient.checkPercentageRequired()) {
            hangTagIngredient.setPercentage(new BigDecimal(hangTagIngredient.getPercentageStr()));
        }
        hangTagIngredientService.saveOrUpdate(hangTagIngredient);
        return updateSuccess("保存成功");
    }

    @PostMapping("/saveList")
    @Transactional(rollbackFor = Exception.class)
    @DuplicationCheck
    public ApiResult saveList(@RequestBody HangTagDTO hangTagDTO){

        List<HangTagIngredient> hangTagIngredients = hangTagDTO.getHangTagIngredients();
        hangTagIngredients.stream().filter(HangTagIngredient::checkPercentageRequired).forEach(it-> it.setPercentage(new BigDecimal(it.getPercentageStr())));
        HangTag hangTag =BeanUtil.copyProperties(hangTagDTO, HangTag.class);

        if (StringUtils.isEmpty(hangTag.getId())){
            QueryWrapper<HangTag> queryWrapper =new BaseQueryWrapper<>();
            queryWrapper.eq("bulk_style_no",hangTag.getBulkStyleNo());
            HangTag one = hangTagService.getOne(queryWrapper);
            if (one!=null){
                hangTag = one;
            }
        }

        String id = hangTag.getId();
        if (StringUtils.isEmpty(id)){
            hangTag.setStatus(HangTagStatusEnum.NOT_COMMIT);
            hangTagService.save(hangTag,"新增吊牌");
        }else {
            hangTagIngredientService.remove(new QueryWrapper<HangTagIngredient>().eq("hang_tag_id",hangTag.getId()));
        }
        for (HangTagIngredient hangTagIngredient : hangTagIngredients) {
            hangTagIngredient.setId(null);
            hangTagIngredient.setHangTagId(hangTag.getId());
        }
        hangTagIngredientService.saveBatch(hangTagIngredients);
        return updateSuccess(hangTag.getId());
    }

    @DeleteMapping("/delByIds")
    public ApiResult delByIds(String[] ids){
        hangTagIngredientService.removeByIds(Arrays.asList(ids));
        return deleteSuccess("删除成功");
    }
}
