/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.storageSpace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisAmcUtils;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.storageSpace.dto.StorageSpacePersonDto;
import com.base.sbc.module.storageSpace.entity.StorageSpace;
import com.base.sbc.module.storageSpace.entity.StorageSpacePerson;
import com.base.sbc.module.storageSpace.mapper.StorageSpacePersonMapper;
import com.base.sbc.module.storageSpace.service.StorageSpacePersonService;
import com.base.sbc.module.storageSpace.service.StorageSpaceService;
import com.base.sbc.module.storageSpace.vo.StorageSpacePersonBo;
import com.base.sbc.module.storageSpace.vo.StorageSpacePersonVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;

/**
 * 类描述：个人空间划分 service类
 * @address com.base.sbc.module.storageSpace.service.StorageSpacePersonService
 * @author your name
 * @email your email
 * @date 创建时间：2024-6-27 10:26:28
 * @version 1.0
 */
@Service
public class StorageSpacePersonServiceImpl extends BaseServiceImpl<StorageSpacePersonMapper, StorageSpacePerson> implements StorageSpacePersonService {

    @Autowired
    private StorageSpaceService storageSpaceService;

    @Autowired
    private AmcService amcService;
    @Resource
    private RedisAmcUtils redisAmcUtils;

    @Resource
    private RedisUtils redisUtils ;

    private final String key = "MATERIAL_UPLOAD_UPDATE";

    private final ReentrantLock lock = new ReentrantLock();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StorageSpacePersonBo listQueryPage(StorageSpacePersonDto dto) {

        if (StringUtils.isEmpty(dto.getStorageType())){
            throw new OtherException("存储类型不能为空");
        }

        StorageSpace storageSpace = storageSpaceService.getByStorageType(dto.getStorageType());
        dto.setParentSpaceId(storageSpace.getId());
        Page<StorageSpacePersonVo> page = PageHelper.startPage(dto);
        if ("1".equals(storageSpace.getStorageType())){
            //检查用户在权限的变动
            checkMaterialUploadPermissionUpdate(storageSpace);
            baseMapper.listQueryMaterialPage(dto);

        }

//        QueryWrapper<StorageSpacePerson> qw = new QueryWrapper<>();
//        qw.lambda().eq(StorageSpacePerson::getParentSpaceId,storageSpace.getId());
//        qw.lambda().eq(StorageSpacePerson::getDelFlag,"0");
//        qw.lambda().like(StringUtils.isNotBlank(dto.getUserName()),StorageSpacePerson::getUserName,dto.getUserName());
//        qw.lambda().like(StringUtils.isNotBlank(dto.getOwnerName()),StorageSpacePerson::getOwnerName,dto.getOwnerName());
//        qw.lambda().like(StringUtils.isNotBlank(dto.getOwnerSpace()),StorageSpacePerson::getOwnerSpace,dto.getOwnerSpace());
//        qw.lambda().like(StringUtils.isNotBlank(dto.getInitSpace()),StorageSpacePerson::getInitSpace,dto.getInitSpace());
//        qw.lambda().like(StringUtils.isNotBlank(dto.getMagnification()),StorageSpacePerson::getMagnification,dto.getMagnification());
//        list(qw);
        StorageSpacePersonBo pageVo = BeanUtil.copyProperties(page.toPageInfo(),StorageSpacePersonBo.class);
        pageVo.setAllocationSpace(baseMapper.getAllocationSpace(storageSpace.getId()));
        return pageVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean personUpdate(List<StorageSpacePerson> list) {
        //查询旧数据
        List<StorageSpacePerson> oldPersonList = listByIds(list.stream().map(StorageSpacePerson::getId).collect(Collectors.toList()));
        //批量修改
        list.forEach(BaseDataEntity::updateInit);
        boolean b = updateBatchById(list);
        if (b){
            OperaLogEntity operaLogEntity = new OperaLogEntity();
            operaLogEntity.setName("操作个人空间");
            operaLogEntity.setType("更新");
            updateBatchOperaLog(list,oldPersonList,operaLogEntity);
        }
        return b;
    }

    @Override
    public void checkPersonSpacer(long needSpacer, String storageType, String userId) {
        StorageSpace storageSpace = storageSpaceService.getByStorageType(storageType);
        if (null == storageSpace){
            throw new OtherException("存储空间不存在");
        }
        QueryWrapper<StorageSpacePerson> qw = new QueryWrapper<>();
        qw.lambda().eq(StorageSpacePerson::getParentSpaceId,storageSpace.getId());
        qw.lambda().eq(StorageSpacePerson::getDelFlag,"0");
        qw.lambda().eq(StorageSpacePerson::getOwnerId,userId);
        qw.lambda().last("limit 1");
        StorageSpacePerson spacePerson = getOne(qw);
        if (null == spacePerson){
            return;
        }
        long ownSpacer = StringUtils.isEmpty(spacePerson.getOwnerSpace()) ?
                Long.parseLong(spacePerson.getInitSpace()) : Long.parseLong(spacePerson.getOwnerSpace());

        if (needSpacer > ownSpacer * 1073741824){
            throw new OtherException("个人可用空间不足，请扩容个人空间");
        }

    }


    private void checkMaterialUploadPermissionUpdate(StorageSpace storageSpace) {

        if (!getIsCheckUpdate()){
            return;
        }
        final String keyLock = "MATERIAL_UPLOAD_UPDATE_LOCK";
        boolean aBoolean = redisUtils.setNx(keyLock, 10);
        if (!aBoolean) {
            throw new OtherException("请稍后重新查询，正在同步数据....");
        }
        try {
            if (!getIsCheckUpdate()){
                return;
            }
            List<UserCompany> byMenuUrlUser = amcService.getByMenuUrlUser("pdm:materialLibrary:myMaterial:btn:systemUpload");
            if (CollUtil.isEmpty(byMenuUrlUser)){
                return;
            }
            List<String> userIds = baseMapper.selectOwnerIds();
            if (CollUtil.isEmpty(userIds)){
                addStorageSpacePersonInfo(byMenuUrlUser,storageSpace);
            }else {
                addStorageSpacePersonInfo(byMenuUrlUser.stream().filter(item ->!userIds.contains(item.getId())).collect(Collectors.toList()),storageSpace);
            }
            redisAmcUtils.set(key,"0");
        }finally {
            redisUtils.del(keyLock);
        }

    }

    private void addStorageSpacePersonInfo(List<UserCompany> list,StorageSpace storageSpace){
        if (CollUtil.isEmpty(list)){
            return;
        }
        List<StorageSpacePerson> personList = Lists.newArrayList();
        IdGen idGen = new IdGen();
        list.forEach(item ->{
            StorageSpacePerson storageSpacePerson = new StorageSpacePerson();
            storageSpacePerson.setOwnerId(item.getId());
            storageSpacePerson.setOwnerName(item.getName());
            storageSpacePerson.setUserName(item.getUsername());
            storageSpacePerson.setParentSpaceId(storageSpace.getId());
            storageSpacePerson.setInitSpace(storageSpace.getInitSpace());
            storageSpacePerson.setMagnification(storageSpace.getInitMagnification());
            storageSpacePerson.setId(idGen.nextIdStr());
            storageSpacePerson.insertInit();
            personList.add(storageSpacePerson);
        });

        saveBatch(personList);


    }


    private boolean getIsCheckUpdate(){
        Object o = redisAmcUtils.get(key);
        boolean isCheckUpdate = false;
        if (null == o){
            isCheckUpdate = true;
        }else {
            isCheckUpdate = !"0".equals(o.toString());
        }
        return isCheckUpdate;
    }

// 自定义方法区 不替换的区域【other_start】


// 自定义方法区 不替换的区域【other_end】

}
