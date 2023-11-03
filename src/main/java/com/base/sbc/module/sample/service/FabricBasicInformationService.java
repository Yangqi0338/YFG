/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.sample.dto.QueryDetailFabricDto;
import com.base.sbc.module.sample.dto.QueryFabricInformationDto;
import com.base.sbc.module.sample.dto.SaveUpdateFabricBasicInformationDto;
import com.base.sbc.module.sample.entity.FabricBasicInformation;
import com.base.sbc.module.sample.vo.FabricInformationVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：面料基本信息 service类
 * @address com.base.sbc.module.sample.service.FabricBasicInformationService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-19 18:23:26
 * @version 1.0
 */
public interface FabricBasicInformationService extends BaseService<FabricBasicInformation> {

   /**
    * 查询面料调样单
    * @param queryFabricInformationDto
    * @return
    */
   PageInfo<FabricInformationVo> getFabricInformationList(QueryFabricInformationDto queryFabricInformationDto);

/** 自定义方法区 不替换的区域【other_start】 **/

   /**
    * 新增修改面料吊样单基本信息
    * @param saveUpdateFabricBasicDto
    * @return
    */
   ApiResult saveUpdateFabricBasic(SaveUpdateFabricBasicInformationDto saveUpdateFabricBasicDto);

   /*删除*/
   ApiResult  delFabric(RemoveDto removeDto);

   ApiResult getById(QueryDetailFabricDto queryDetailFabricDto);

   /**
    * 导出面料调样单
    * @param response
    * @param queryFabricInformationDto
    */
   void fabricInformationDeriveExcel(HttpServletResponse response, QueryFabricInformationDto queryFabricInformationDto) throws IOException;


   /**
    * 上传理化报告
    * @param id
    * @param file
    * @param request
    * @return
    * @throws Throwable
    */
   Boolean uploadingReport (String id, MultipartFile file, HttpServletRequest request) throws Throwable;

/** 自定义方法区 不替换的区域【other_end】 **/


}
