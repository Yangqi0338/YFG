package com.base.sbc.module.storageSpace.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.storageSpace.entity.StorageSpace;
import com.base.sbc.module.storageSpace.service.StorageSpacePersonService;
import com.base.sbc.module.storageSpace.service.StorageSpaceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * @Create : 2024/6/27 10:28
 **/
@RestController
@Api(value = "与素材库相关的所有接口信息", tags = {"素材库接口"})
@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL + "/storageSpace", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class StorageSpaceController {

    @Autowired
    private StorageSpaceService storageSpaceService;

    @Autowired
    private StorageSpacePersonService storageSpacePersonService;


    @PostMapping("")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "空间设置查询", notes = "空间设置查询")
    public ApiResult storageSpace(StorageSpace dto){
        if (StringUtils.isEmpty(dto.getStorageType())){
            throw new OtherException("存储类型不能为空");
        }
        QueryWrapper<StorageSpace> qw = new QueryWrapper<>();
        qw.lambda().eq(StorageSpace::getStorageType,dto.getStorageType());
        qw.lambda().eq(StorageSpace::getDelFlag,"0");
        qw.lambda().last("limit 1");

        StorageSpace one = storageSpaceService.getOne(qw);
        return  ApiResult.success("查询成功",one);
    }

    @PostMapping("/update")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "空间设置更新", notes = "空间设置更新")
    public ApiResult storageSpaceUpdate(@RequestBody StorageSpace dto){
        boolean b = storageSpaceService.updateById(dto);
        return b ? ApiResult.success("更新成功") : ApiResult.error("更新失败",500);
    }
}
