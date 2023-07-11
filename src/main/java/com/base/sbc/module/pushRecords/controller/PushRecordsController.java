package com.base.sbc.module.pushRecords.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.pushRecords.dto.PushRecordsDto;
import com.base.sbc.module.pushRecords.entity.PushRecords;
import com.base.sbc.module.pushRecords.service.PushRecordsService;
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
 * @date 2023/7/11 10:46:08
 * @mail 247967116@qq.com
 */
@RestController
@Api(tags = "推送日志")
@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL + "/pushRecords", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PushRecordsController extends BaseController {
    private final PushRecordsService pushRecordsService;

    @GetMapping("/queryPage")
    public ApiResult queryPage(PushRecordsDto pushRecordsDto) {
        BaseQueryWrapper<PushRecords> queryWrapper = new BaseQueryWrapper<>();
        PageHelper.startPage(pushRecordsDto);
        List<PushRecords> list = pushRecordsService.list(queryWrapper);
        return selectSuccess(new PageInfo<>(list));
    }
}
