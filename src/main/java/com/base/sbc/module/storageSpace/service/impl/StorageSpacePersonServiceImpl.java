/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.storageSpace.service.impl;

import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.storageSpace.dto.StorageSpacePersonDto;
import com.base.sbc.module.storageSpace.entity.StorageSpacePerson;
import com.base.sbc.module.storageSpace.mapper.StorageSpacePersonMapper;
import com.base.sbc.module.storageSpace.service.StorageSpacePersonService;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;

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
    private AmcService amcService;
    @Resource
    private RedisUtils redisUtils;

    private final String key = "MATERIAL_UPLOAD_UPDATE";

    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public PageInfo<StorageSpacePerson> listQueryPage(StorageSpacePersonDto dto) {

        //检查用户在权限的变动
        checkMaterialUploadPermissionUpdate();



        return null;
    }



    private void checkMaterialUploadPermissionUpdate() {

        if (!getIsCheckUpdate()){
            return;
        }
        final String keyLock = "MATERIAL_UPLOAD_UPDATE_LOCK";
        boolean aBoolean = redisUtils.setNx(keyLock, 10);
        if (!aBoolean) {
            throw new OtherException("请稍后重新查询，正在同步数据....");
        }

        try {
            List<UserCompany> byMenuUrlUser = amcService.getByMenuUrlUser("pdm:materialLibrary:myMaterial:btn:systemUpload");
            StorageSpacePerson storageSpacePerson = new StorageSpacePerson();

//            redisUtils.set(key,"0");
        }finally {
            redisUtils.del(key);
        }

    }

    private boolean getIsCheckUpdate(){
        Object o = redisUtils.get(key);
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
