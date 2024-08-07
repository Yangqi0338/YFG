/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.storageSpace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.storageSpace.dto.StorageSpacePersonDto;
import com.base.sbc.module.storageSpace.entity.StorageSpacePerson;
import com.base.sbc.module.storageSpace.vo.StorageSpacePersonVo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：个人空间划分 dao类
 * @address com.base.sbc.module.storageSpace.dao.StorageSpacePersonDao
 * @author your name  
 * @email  your email
 * @date 创建时间：2024-6-27 10:26:28 
 * @version 1.0  
 */
@Mapper
public interface StorageSpacePersonMapper extends BaseMapper<StorageSpacePerson> {
    List<String> selectOwnerIds();

    List<StorageSpacePersonVo> listQueryMaterialPage(StorageSpacePersonDto dto);

    Double getAllocationSpace(@Param("parentSpaceId") String parentSpaceId);
// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
}