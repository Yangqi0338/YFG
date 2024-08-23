/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.storageSpace.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.storageSpace.entity.StorageSpace;

/** 
 * 类描述：存储空间 service类
 * @address com.base.sbc.module.storageSpace.service.StorageSpaceService
 * @author your name
 * @email your email
 * @date 创建时间：2024-6-27 10:17:48
 * @version 1.0  
 */
public interface StorageSpaceService extends BaseService<StorageSpace>{


    StorageSpace getByStorageType(String storageType);


// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】

	
}