package com.base.sbc.module.basicsdatum.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.ColorModelNumberDto;
import com.base.sbc.module.basicsdatum.entity.ColorModelNumber;
import com.base.sbc.module.basicsdatum.service.ColorModelNumberService;
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
     * @param colorModelNumberDto 请求参数
     * @return 列表
     */
    @GetMapping
    public ApiResult queryList(ColorModelNumberDto colorModelNumberDto) {
        BaseQueryWrapper<ColorModelNumber> queryWrapper = new BaseQueryWrapper<>();
        PageHelper.startPage(colorModelNumberDto);
        List<ColorModelNumber> list = colorModelNumberService.list(queryWrapper);
        return selectSuccess(new PageInfo<>(list));
    }
}
