package com.base.sbc.module.basicsdatum.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.SizeBulkStyleDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType;
import com.base.sbc.module.basicsdatum.entity.SizeBulkStyle;
import com.base.sbc.module.basicsdatum.service.BasicsdatumModelTypeService;
import com.base.sbc.module.basicsdatum.service.SizeBulkStyleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/9/20 15:15:12
 * @mail 247967116@qq.com
 */
@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL + "/sizeBulkStyle", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class SizeBulkStyleController extends BaseController {
    private final SizeBulkStyleService sizeBulkStyleService;
    private final BasicsdatumModelTypeService basicsdatumModelTypeService;


    @PostMapping("/save")
    public ApiResult save(@RequestBody SizeBulkStyle sizeBulkStyle) {
        QueryWrapper<SizeBulkStyle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("size_id",sizeBulkStyle.getSizeId());
        queryWrapper.eq("bulk_style_no",sizeBulkStyle.getBulkStyleNo());
        sizeBulkStyleService.saveOrUpdate(sizeBulkStyle,queryWrapper);
        return updateSuccess("保存成功");
    }


    @PostMapping("/saveBatch")
    public ApiResult saveBatch(@RequestBody List<SizeBulkStyle> sizeBulkStyleList) {

        sizeBulkStyleService.saveOrUpdateBatch(sizeBulkStyleList);
        return updateSuccess("保存成功");
    }

    /**
     * 根据大货款号和号型类型查询
     */
    @GetMapping("/listByBulkStyleNoAndSizeType")
    public ApiResult listByBulkStyleNoAndSizeType(SizeBulkStyleDto sizeBulkStyleDto) {
        BasicsdatumModelType modelType = basicsdatumModelTypeService.getOne(new QueryWrapper<BasicsdatumModelType>().eq("code", sizeBulkStyleDto.getSizeType()));
        if (modelType == null) {
            return selectSuccess(null);
        }
        String size = modelType.getSizeIds();
        if (size == null) {
            return selectSuccess(null);
        }
        String[] split = size.split(",");
        if (split.length == 0) {
            return selectSuccess(null);
        }
        PageHelper.startPage(sizeBulkStyleDto);
        return selectSuccess(new PageInfo<>(sizeBulkStyleService.listByBulkStyleNoAndSizeType( split, sizeBulkStyleDto.getBulkStyleNo(),sizeBulkStyleDto.getType())));
    }

}
