/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.esorderbook.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.module.esorderbook.dto.EsOrderBookQueryDto;
import com.base.sbc.module.esorderbook.dto.EsOrderBookSaveDto;
import com.base.sbc.module.esorderbook.entity.EsOrderBook;
import com.base.sbc.module.esorderbook.service.EsOrderBookService;
import com.base.sbc.module.esorderbook.vo.EsOrderBookItemVo;
import com.base.sbc.module.esorderbook.vo.EsOrderBookVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;

/**
 * 类描述：ES订货本 Controller类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.esorderbook.web.EsOrderBookController
 * @email your email
 * @date 创建时间：2024-3-28 16:21:15
 */
@RestController
@Api(tags = "ES订货本")
@RequestMapping(value = BaseController.SAAS_URL + "/esOrderBook", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class EsOrderBookController {

    @Autowired
    private EsOrderBookService esOrderBookService;

    @ApiOperation(value = "查询树结构")
    @GetMapping("/headList")
    public List<EsOrderBook> queryYearBrandTree(EsOrderBookQueryDto dto){
        LambdaQueryWrapper<EsOrderBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EsOrderBook::getDelFlag, BaseGlobal.NO);
        queryWrapper.eq(EsOrderBook::getSeasonId, dto.getSeasonId());
        return esOrderBookService.list(queryWrapper);
    }

    @ApiOperation(value = "分页查询")
    @GetMapping
    public PageInfo<EsOrderBookItemVo> page(EsOrderBookQueryDto dto) {
        return esOrderBookService.findPage(dto);
    }



    @ApiOperation(value = "删除-通过id查询,多个逗号分开")
    @DeleteMapping("/del")
    public ApiResult removeById(@RequestBody List<EsOrderBookItemVo> list) {
        esOrderBookService.del(list);
        return ApiResult.success("操作成功");
    }

    @ApiOperation(value = "删除-通过id查询,多个逗号分开")
    @DeleteMapping("/delItem")
    public ApiResult delItem(@RequestBody List<EsOrderBookItemVo> list) {
        esOrderBookService.delItem(list);
        return ApiResult.success("操作成功");
    }

    @ApiOperation(value = "删除-通过id查询,多个逗号分开")
    @DeleteMapping("/delHead/{id}")
    public ApiResult delHead(@PathVariable String id) {
        esOrderBookService.delHead(id);
        return ApiResult.success("操作成功");
    }

    @ApiOperation(value = "批量新增")
    @PostMapping("/saveMain")
    public ApiResult saveMain(@RequestBody EsOrderBookVo dto) {
        EsOrderBook esOrderBook = esOrderBookService.saveMain(dto);
        return ApiResult.success("操作成功", esOrderBook);
    }

    @ApiOperation(value = "修改组名")
    @PostMapping("/updateHeadName")
    public ApiResult updateHeadName(@RequestBody EsOrderBookItemVo vo) {
        esOrderBookService.updateHeadName(vo);
        return ApiResult.success("操作成功");
    }

    @ApiOperation(value = "批量新增行")
    @PostMapping("/saveItemList")
    public ApiResult saveItemList(@RequestBody EsOrderBookSaveDto dto) {
        esOrderBookService.saveItemList(dto);
        return ApiResult.success("操作成功");
    }

    @ApiOperation(value = "mango导入图片")
    @PostMapping("/uploadStyleColorPics")
    public ApiResult uploadStyleColorPics(Principal user, @RequestParam("file") MultipartFile file,
                                          @RequestParam("planningSeason") String planningSeason,
                                          @RequestParam("headId") String headId,
                                          @RequestParam("groupName") String groupName) {
        EsOrderBookItemVo vo = new EsOrderBookItemVo();
        vo.setPlanningSeason(planningSeason);
        vo.setHeadId(headId);
        vo.setGroupName(groupName);
        return esOrderBookService.uploadStyleColorPics(user, file, vo);
    }

    @ApiOperation(value = "锁定")
    @PostMapping("/lock")
    public ApiResult lock(@RequestBody List<EsOrderBookItemVo> list) {
        esOrderBookService.lock(list);
        return ApiResult.success("操作成功");
    }

    @ApiOperation(value = "解锁")
    @PostMapping("/unLock")
    public ApiResult unLock(@RequestBody List<EsOrderBookItemVo> list) {
        esOrderBookService.unLock(list);
        return ApiResult.success("操作成功");
    }

    @ApiOperation(value = "数据导出Excel")
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response, EsOrderBookQueryDto dto) throws Exception {
        esOrderBookService.exportExcel(response, dto);
    }

    @ApiOperation(value = "修改行顺序")
    @PostMapping("/updateItemList")
    public ApiResult updateItemList(@RequestBody List<EsOrderBookItemVo> list) {
        esOrderBookService.updateItemList(list);
        return ApiResult.success("操作成功");
    }

}































