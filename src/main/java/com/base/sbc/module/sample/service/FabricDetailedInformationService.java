/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.sample.dto.SaveUpdateFabricDetailedInformationDto;
import com.base.sbc.module.sample.entity.FabricDetailedInformation;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/** 
 * 类描述：面料详细信息 service类
 * @address com.base.sbc.module.sample.service.FabricDetailedInformationService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-19 18:23:28
 * @version 1.0  
 */
public interface FabricDetailedInformationService extends BaseService<FabricDetailedInformation> {

/** 自定义方法区 不替换的区域【other_start】 **/

    ApiResult saveUpdateFabricDetailed  (SaveUpdateFabricDetailedInformationDto saveUpdateFabricBasicDto);

    /**
     * 上传理化报告
     * @param id
     * @param file
     * @param request
     * @return
     * @throws Throwable
     */
    ApiResult uploadingReport (String id, MultipartFile file, HttpServletRequest request) throws Throwable;
/** 自定义方法区 不替换的区域【other_end】 **/

	
}
