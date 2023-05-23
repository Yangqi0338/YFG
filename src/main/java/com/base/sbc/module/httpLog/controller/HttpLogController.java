package com.base.sbc.module.httpLog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.httpLog.dto.QueryHttpLogDto;
import com.base.sbc.module.httpLog.entity.HttpLog;
import com.base.sbc.module.httpLog.service.HttpLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/5/22 13:39:58
 * @mail 247967116@qq.com
 */
@RestController
@RequestMapping(BaseController.SAAS_URL + "/httpLog")
@RequiredArgsConstructor
public class HttpLogController extends BaseController {
    private final HttpLogService httpLogService;
    @GetMapping("/list")
    @ApiOperation(value = "查询请求日志")
    public ApiResult list(QueryHttpLogDto queryHttpLogDto, Page page){
        PageHelper.startPage(page);
        QueryWrapper<HttpLog> queryWrapper=new QueryWrapper<>();;
        queryWrapper.eq(!StringUtils.isEmpty(queryHttpLogDto.getThreadId()),"thread_id",queryHttpLogDto.getThreadId());
        queryWrapper.like(!StringUtils.isEmpty(queryHttpLogDto.getUrl()),"url",queryHttpLogDto.getUrl());
        if(StringUtils.isNotBlank(queryHttpLogDto.getStartTime())){
         List<String> list=   StringUtils.convertList(queryHttpLogDto.getStartTime());
            queryWrapper.lambda().between(HttpLog::getCreateDate, list.get(0), list.get(1));
        }
        queryWrapper.orderByDesc("create_date");
        List<HttpLog> list = httpLogService.list(queryWrapper);
        PageInfo<HttpLog> pageInfo=new PageInfo<>(list);
        return selectSuccess(pageInfo);
    }
}
