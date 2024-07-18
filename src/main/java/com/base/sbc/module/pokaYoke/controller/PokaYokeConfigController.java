package com.base.sbc.module.pokaYoke.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.orderbook.dto.OrderBookQueryDto;
import com.base.sbc.module.pokaYoke.dto.PokaYokeConfigQueryDto;
import com.base.sbc.module.pokaYoke.service.PokaYokeConfigService;
import com.base.sbc.module.pokaYoke.vo.PokaYokeConfigVo;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kotlin.jvm.Volatile;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL + "/pokaYoke", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@Api(tags = "防呆防错")
public class PokaYokeConfigController extends BaseController{



    @Autowired
    private PokaYokeConfigService pokaYokeConfigService;


    @ApiOperation(value = "查询")
    @GetMapping("/queryPage")
    public ApiResult queryPage( PokaYokeConfigQueryDto dto) {
        PageInfo<PokaYokeConfigVo> result = pokaYokeConfigService.queryPage(dto);
        return selectSuccess(result);
    }


    @ApiOperation(value = "新增")
    @PostMapping("/save")
    public ApiResult save(@RequestBody @Valid PokaYokeConfigVo vo) {
        Boolean result = pokaYokeConfigService.savePokaYokeConfig(vo);
        return result ? ApiResult.success("新增成功") : ApiResult.error("新增失败",500);
    }


    @ApiOperation(value = "更新")
    @PostMapping("/update")
    public ApiResult update(@RequestBody PokaYokeConfigVo vo) {
        if (StringUtils.isEmpty(vo.getId())){
            throw new OtherException("id不存在");
        }
        Boolean result = pokaYokeConfigService.updatePokaYokeConfig(vo);
        return result ? ApiResult.success("修改成功") : ApiResult.error("修改失败",500);
    }

    @ApiOperation(value = "删除")
    @GetMapping("/del")
    public ApiResult del( String id) {
        Boolean result = pokaYokeConfigService.del(id);
        return result ? ApiResult.success("删除成功") : ApiResult.error("删除失败",500);
    }



    @ApiOperation(value = "查询条件")
    @GetMapping("/queryCondition")
    public ApiResult queryCondition( PokaYokeConfigQueryDto dto) {
        String result = pokaYokeConfigService.queryCondition(dto);
        return selectSuccess(result);
    }



}
