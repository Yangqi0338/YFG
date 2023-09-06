/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formType.service;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.formType.dto.FieldOptionConfigDto;
import com.base.sbc.module.formType.dto.QueryFieldOptionConfigDto;
import com.base.sbc.module.formType.entity.FieldOptionConfig;
import com.base.sbc.module.formType.vo.FieldOptionConfigVo;
import com.github.pagehelper.PageInfo;

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
public interface FieldOptionConfigService extends BaseService<FieldOptionConfig>{

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
// 自定义方法区 不替换的区域【other_end】

	
}
