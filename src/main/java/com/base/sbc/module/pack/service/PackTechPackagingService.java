/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.module.hangtag.vo.HangTagVO;
import com.base.sbc.module.pack.entity.PackTechPackaging;

/**
 * 类描述：资料包-工艺说明-包装方式和体积重量 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackTechPackagingService
 * @email your email
 * @date 创建时间：2023-7-13 14:24:40
 */
public interface PackTechPackagingService extends PackBaseService<PackTechPackaging> {

// 自定义方法区 不替换的区域【other_start】


    PackTechPackaging savePackaging(PackTechPackaging packaging);


// 自定义方法区 不替换的区域【other_end】

    PackTechPackaging Packaging(String dependDictType,String dependCode);
//同步吊牌详情的包装方式
    String updatePackaging(String bulkStyleNo, String userCompany, String selectType,HangTagVO hangTagVO);


}
