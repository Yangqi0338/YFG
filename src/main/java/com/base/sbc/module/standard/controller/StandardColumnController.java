package com.base.sbc.module.standard.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.material.dto.CategoryIdDto;
import com.base.sbc.module.material.dto.MaterialQueryDto;
import com.base.sbc.module.material.dto.MaterialSaveDto;
import com.base.sbc.module.material.entity.Material;
import com.base.sbc.module.material.entity.MaterialLabel;
import com.base.sbc.module.material.entity.Test;
import com.base.sbc.module.material.vo.AssociationMaterialVo;
import com.base.sbc.module.standard.dto.StandardColumnQueryDto;
import com.base.sbc.module.standard.dto.StandardColumnSaveDto;
import com.base.sbc.module.standard.service.StandardColumnService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 孔祥基
 * @date 2023/3/22 15:51:24
 */
@RestController
@Api(value = "与标准表相关的所有接口信息", tags = {"标准表接口"})
@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL + "/standard", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class StandardColumnController extends BaseController {

    private final StandardColumnService standardColumnService;

    /**
     * 新增
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "新增标准表", notes = "新增标准表")
    @DuplicationCheck(time = 1)
    public ApiResult save(@Valid @RequestBody StandardColumnSaveDto standardColumnSaveDto) {
        boolean isUpdate = StrUtil.isNotBlank(standardColumnSaveDto.getId());
        String id = standardColumnService.save(standardColumnSaveDto);
        return isUpdate ? updateSuccess(id) : insertSuccess(id);
    }

    /**
     * 根据id删除
     */
    @ApiOperation(value = "根据id数组删除", notes = "根据id数组删除")
    @DeleteMapping("/delByIds")
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult delByIds(String[] ids) {
        return deleteSuccess(standardColumnService.delByIds(Arrays.asList(ids)));
    }

    /**
     * 查询列表
     */
    @ApiOperation(value = "条件查询列表", notes = "条件查询列表")
    @GetMapping("/listQuery")
    public ApiResult listQuery(StandardColumnQueryDto standardColumnQueryDto) {
        if (standardColumnQueryDto == null) {
            throw new OtherException("参数不能为空");
        }
        standardColumnQueryDto.setPageSize(Integer.MAX_VALUE);
        return selectSuccess(new PageInfo<>(standardColumnService.listQuery(standardColumnQueryDto)
                .stream().filter(it-> !MoreLanguageProperties.modelStandardColumnCode.equals(it.getCode())).collect(Collectors.toList())));
    }
    
}
