package com.base.sbc.module.operalog.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.module.operalog.dto.OperaLogDto;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.operalog.service.OperaLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    public ApiResult listPage(OperaLogDto operaLogDto ,@RequestHeader(BaseConstant.USER_COMPANY) String userCompany) {
        BaseQueryWrapper<OperaLogEntity> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.notEmptyLike("document_id",operaLogDto.getDocumentId());
        queryWrapper.notEmptyLike("document_name",operaLogDto.getDocumentName());
        queryWrapper.notEmptyLike("document_code",operaLogDto.getDocumentCode());
        queryWrapper.notEmptyIn("document_code",operaLogDto.getDocumentCodeList());
        queryWrapper.notEmptyLike("parent_id",operaLogDto.getParentId());
        queryWrapper.notEmptyLike("type",operaLogDto.getType());
        queryWrapper.notEmptyLike("name",operaLogDto.getName());
        queryWrapper.notEmptyLike("create_name",operaLogDto.getCreateName());
        queryWrapper.eq("company_code",userCompany);
        queryWrapper.between("create_date",operaLogDto.getCreateDate());
        queryWrapper.orderByDesc("create_date");
        PageHelper.startPage(operaLogDto);
        List<OperaLogEntity> list = operaLogService.list(queryWrapper);
        return selectSuccess(new PageInfo<>(list));
    }

    @PostMapping("/save")
    public ApiResult save(@RequestBody OperaLogEntity operaLogEntity) {
        return insertSuccess(operaLogService.save(operaLogEntity));
    }
}
