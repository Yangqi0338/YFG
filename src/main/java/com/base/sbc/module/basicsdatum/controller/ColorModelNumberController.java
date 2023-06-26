package com.base.sbc.module.basicsdatum.controller;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.ColorModelNumberDto;
import com.base.sbc.module.basicsdatum.entity.ColorModelNumber;
import com.base.sbc.module.basicsdatum.service.ColorModelNumberService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * @author 卞康
 * @date 2023/6/26 10:23
 * @mail 247967116@qq.com
 */
@RestController
@Api(tags = "基础资料-色号和色型")
@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL + "/colorModelNumber", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ColorModelNumberController extends BaseController {

    private final ColorModelNumberService colorModelNumberService;

    /**
     * 查询列表
     *
     * @param colorModelNumberDto 请求参数
     * @return 列表
     */
    @GetMapping("/queryList")
    public ApiResult queryList(ColorModelNumberDto colorModelNumberDto) {
        BaseQueryWrapper<ColorModelNumber> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("file_name",colorModelNumberDto.getFileName());
        PageHelper.startPage(colorModelNumberDto);
        List<ColorModelNumber> list = colorModelNumberService.list(queryWrapper);
        return selectSuccess(new PageInfo<>(list));
    }

    /**
     * 单个新增或者修改
     */
    @PostMapping("/save")
    public ApiResult save(@RequestBody ColorModelNumber colorModelNumber) {
        return updateSuccess(colorModelNumberService.saveColorModelNumber(colorModelNumber));
    }


    /**
     * 批量修改
     */
    @PutMapping("/updateList")
    public ApiResult updateList(List<ColorModelNumber> colorModelNumberList) {
        colorModelNumberService.updateBatchById(colorModelNumberList);
        return updateSuccess("操作成功");
    }

    /**
     * 启用或者停用
     */
    @PutMapping("/startStop")
    public ApiResult startStop(@RequestBody ColorModelNumberDto colorModelNumberDto) {
        UpdateWrapper<ColorModelNumber> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", colorModelNumberDto.getStatus());
        updateWrapper.in("id", Arrays.asList(colorModelNumberDto.getIds().split(",")));
        colorModelNumberService.update(updateWrapper);
        return updateSuccess("操作成功");
    }

    /**
     * 根据Ids删除
     */
    @PutMapping("/detByIds")
    public ApiResult detByIds(String[] ids) {
        return deleteSuccess(colorModelNumberService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 导入
     */
    @ApiOperation(value = "导入")
    @PostMapping("/importExcel")
    public ApiResult importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        Boolean b = colorModelNumberService.importExcel(file);
        return insertSuccess(b);
    }
}
