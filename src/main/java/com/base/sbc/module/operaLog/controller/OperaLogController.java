package com.base.sbc.module.operaLog.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.operaLog.dto.OperaLogDto;
import com.base.sbc.module.operaLog.entity.OperaLogEntity;
import com.base.sbc.module.operaLog.service.OperaLogService;
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
 * @date 2023/6/19 16:53:33
 * @mail 247967116@qq.com
 */
@RestController
@Api(tags = "操作日志")
@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL + "/operaLog", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OperaLogController extends BaseController {

    private final OperaLogService operaLogService;

    @GetMapping("/listPage")
    public ApiResult listPage(OperaLogDto operaLogDto) {
        BaseQueryWrapper<OperaLogEntity> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.notEmptyLike("document_id",operaLogDto.getDocumentId());
        queryWrapper.notEmptyLike("document_name",operaLogDto.getDocumentName());
        queryWrapper.notEmptyLike("document_code",operaLogDto.getDocumentCode());
        queryWrapper.notEmptyLike("parent_id",operaLogDto.getParentId());
        queryWrapper.notEmptyLike("type",operaLogDto.getType());
        queryWrapper.notEmptyLike("name",operaLogDto.getName());
        queryWrapper.between("create_date",operaLogDto.getCreateDate());
        queryWrapper.orderByDesc("create_date");
        PageHelper.startPage(operaLogDto);
        List<OperaLogEntity> list = operaLogService.list(queryWrapper);
        return selectSuccess(new PageInfo<>(list));
    }
}
