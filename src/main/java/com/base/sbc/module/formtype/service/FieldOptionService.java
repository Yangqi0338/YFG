/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formtype.service;

import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.formtype.dto.FieldOptionConfigDto;
import com.base.sbc.module.formtype.dto.QueryFieldOptionConfigDto;
import com.base.sbc.module.formtype.entity.FieldOptionConfig;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/** 
 * 类描述：字段选项配置表 service类
 * @address com.base.sbc.module.formType.service.FieldOptionConfigService
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-9-4 13:00:48
 * @version 1.0  
 */
public interface FieldOptionService extends BaseService<FieldOptionConfig>{

// 自定义方法区 不替换的区域【other_start】

   /**
    * 添加修改配置选项
    * @param optionConfigDtoList
    * @return
    */
   Boolean addFieldOptionConfig( List<FieldOptionConfigDto> optionConfigDtoList);


   /**
    * 查询配置选项
    * @param queryFieldOptionConfigDto
    * @return
    */
   PageInfo getFieldOptionConfigList(QueryFieldOptionConfigDto queryFieldOptionConfigDto);

   /**
    * 删除配置选项
    * @param id
    * @return
    */
   Boolean  delFieldOptionConfig(String id);

   /**
    * 批量启用/停用
    * @param startStopDto
    * @return
    */
   Boolean startStopFieldOptionConfig(StartStopDto startStopDto);


   /**
    * 查询每个字段下的配置选项
    * @return
    */
   Map<String, List<FieldOptionConfig>> getFieldConfig(QueryFieldOptionConfigDto queryFieldOptionConfigDto);

   /**
    * 配置选项导入
    * @param file
    * @param fieldManagementId
    * @return
    */
   Boolean importExcel(MultipartFile file,String fieldManagementId,String formTypeId) throws Exception;

   /**
    * 导出配置列
    * @param response
    * @param fieldManagementId
    */
   void deriveExcel(HttpServletResponse response, String fieldManagementId) throws IOException;
// 自定义方法区 不替换的区域【other_end】

	
}
