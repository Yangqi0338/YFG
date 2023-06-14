/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.common.service.IServicePlus;
import com.base.sbc.module.sample.dto.QueryDetailFabricDto;
import com.base.sbc.module.sample.dto.QueryFabricInformationDto;
import com.base.sbc.module.sample.dto.SaveUpdateFabricBasicInformationDto;
import com.base.sbc.module.sample.entity.FabricBasicInformation;
import com.base.sbc.module.sample.vo.FabricInformationVo;
import com.github.pagehelper.PageInfo;

/** 
 * 类描述：面料基本信息 service类
 * @address com.base.sbc.module.sample.service.FabricBasicInformationService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-19 18:23:26
 * @version 1.0  
 */
public interface FabricBasicInformationService extends IServicePlus<FabricBasicInformation>{

   PageInfo<FabricInformationVo> getFabricInformationList(QueryFabricInformationDto queryFabricInformationDto);

/** 自定义方法区 不替换的区域【other_start】 **/
   ApiResult saveUpdateFabricBasic(SaveUpdateFabricBasicInformationDto saveUpdateFabricBasicDto);

   /*删除*/
   ApiResult  delFabric(String id);

   ApiResult getById(QueryDetailFabricDto queryDetailFabricDto);

/** 自定义方法区 不替换的区域【other_end】 **/

	
}
