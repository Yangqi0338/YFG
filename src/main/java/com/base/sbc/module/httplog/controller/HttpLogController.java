package com.base.sbc.module.httplog.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.httplog.dto.QueryHttpLogDto;
import com.base.sbc.module.httplog.entity.HttpLog;
import com.base.sbc.module.httplog.service.HttpLogService;
import com.github.pagehelper.Page;
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
    public ApiResult<PageInfo<HttpLog>> list(QueryHttpLogDto queryHttpLogDto){
        Page<HttpLog> page = queryHttpLogDto.startPage();
        httpLogService.list(buildQueryWrapper(queryHttpLogDto));

        return selectSuccess(page.toPageInfo());
    }

    private LambdaQueryWrapper<HttpLog> buildQueryWrapper(QueryHttpLogDto queryHttpLogDto){
        return new BaseLambdaQueryWrapper<HttpLog>()
                .notEmptyEq(HttpLog::getThreadId, queryHttpLogDto.getThreadId())
                .notEmptyEq(HttpLog::getReqName, queryHttpLogDto.getReqName())
                .notEmptyEq(HttpLog::getExceptionFlag, queryHttpLogDto.getExceptionFlag())
                .notEmptyLike(HttpLog::getUrl, queryHttpLogDto.getUrl())
                .notEmptyLike(HttpLog::getCreateName, queryHttpLogDto.getUserName())
                .between(HttpLog::getStartTime, queryHttpLogDto.getStartTime())
                .orderByDesc(HttpLog::getStartTime)
        ;
    }

    @GetMapping("/listByUser")
    @ApiOperation(value = "查询请求日志-根据用户")
    public ApiResult<PageInfo<HttpLog>> listByUser(QueryHttpLogDto queryHttpLogDto){
        Page<HttpLog> page = queryHttpLogDto.startPage();
        queryHttpLogDto.setExceptionFlag(BaseGlobal.YES);
        List<HttpLog> list = httpLogService.list(buildQueryWrapper(queryHttpLogDto));

        for (HttpLog httpLog : list) {
            if(StrUtil.isNotEmpty(httpLog.getRespBody())){
                //获取ApiResult 中的message
                JSONObject jsonObject = JSONObject.parseObject(httpLog.getRespBody());
                String message = jsonObject.getString("message");
                httpLog.setRespBody(message);
            }else{
                httpLog.setRespBody("出现错误,但未捕获");
            }
        }
        return selectSuccess(page.toPageInfo());
    }
}
