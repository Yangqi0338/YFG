package com.base.sbc.module.storageSpace.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.material.service.MaterialService;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.storageSpace.dto.StorageSpacePersonDto;
import com.base.sbc.module.storageSpace.entity.StorageSpace;
import com.base.sbc.module.storageSpace.entity.StorageSpacePerson;
import com.base.sbc.module.storageSpace.service.StorageSpacePersonService;
import com.base.sbc.module.storageSpace.service.StorageSpaceService;
import com.base.sbc.module.storageSpace.vo.StorageSpacePersonVo;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
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

    @Autowired
    private MaterialService materialService;


    @GetMapping("")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "空间设置查询", notes = "空间设置查询")
    public ApiResult storageSpace(StorageSpace dto){
        if (StringUtils.isEmpty(dto.getStorageType())){
            throw new OtherException("存储类型不能为空");
        }
        StorageSpace one = storageSpaceService.getByStorageType(dto.getStorageType());
        return  ApiResult.success("查询成功",one);
    }

    @PostMapping("/update")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "空间设置更新", notes = "空间设置更新")
    public ApiResult storageSpaceUpdate(@RequestBody StorageSpace dto){
        StorageSpace byId = storageSpaceService.getById(dto.getId());
        boolean b = storageSpaceService.updateById(dto);
        if (b){
            OperaLogEntity operaLogEntity = new OperaLogEntity();
            operaLogEntity.setName("空间设置");
            operaLogEntity.setDocumentId(dto.getId());
            operaLogEntity.setType("更新");
            storageSpaceService.saveOrUpdateOperaLog(dto,byId,operaLogEntity);
        }
        return b ? ApiResult.success("更新成功") : ApiResult.error("更新失败",500);
    }


    @GetMapping("personListQueryPage")
    @ApiOperation(value = "获取个人空间列表", notes = "获取个人空间列表")
    public ApiResult personListQueryPage(StorageSpacePersonDto dto){
        PageInfo<StorageSpacePersonVo> page = storageSpacePersonService.listQueryPage(dto);
        return  ApiResult.success("查询成功",page);
    }


    @PutMapping("personUpdate")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "修改个人空间", notes = "修改个人空间")
    public ApiResult personUpdate(List<StorageSpacePerson> list){
        Boolean result = storageSpacePersonService.personUpdate(list);
        return  result ? ApiResult.success("修改成功") : ApiResult.error("修改失败",500);
    }


    @GetMapping("personDel")
    @ApiOperation(value = "删除个人空间", notes = "删除个人空间")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult personDel(String ids){

        if (StringUtils.isEmpty(ids)){
            return ApiResult.success();
        }
        List<String> idList = StringUtils.convertList(ids);
        List<StorageSpacePerson> personList = storageSpacePersonService.listByIds(idList);
        if (CollUtil.isEmpty(personList)){
            return ApiResult.success();
        }
        Set<String> parentSpaceIdSet = personList.stream().map(StorageSpacePerson::getParentSpaceId).collect(Collectors.toSet());
        if (parentSpaceIdSet.size() > 1){
            throw new OtherException("批量仅允许同种类型的操作！");
        }
        StorageSpace storageSpace = storageSpaceService.getById(personList.get(0).getParentSpaceId());
        List<String> userIds = personList.stream().map(StorageSpacePerson::getOwnerId).collect(Collectors.toList());
        //素材库相关
        if ("1".equals(storageSpace.getStorageType())){
            materialService.delMaterialPersonSpace(userIds);
        }
        UpdateWrapper<StorageSpacePerson> uw = new UpdateWrapper<>();
        uw.lambda().in(StorageSpacePerson::getId,idList);
        uw.lambda().set(StorageSpacePerson::getDelFlag,"1");
        boolean result = storageSpacePersonService.update(uw);
        return  result ? ApiResult.success("删除成功") : ApiResult.error("删除失败",500);
    }



}
