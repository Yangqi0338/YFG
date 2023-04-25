/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formType.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.formType.dto.QueryFormTypeDto;
import com.base.sbc.module.formType.vo.PagingFormTypeVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import com.base.sbc.module.formType.entity.FormType;

import java.util.List;
import java.util.Map;

/**
 * 类描述：表单类型 dao类
 * @address com.base.sbc.module.formType.dao.FormTypeDao
 * @author lxl  
 * @email  lxl.fml@gmail.com
 * @date 创建时间：2023-4-15 9:17:01 
 * @version 1.0  
 */
@Mapper
public interface FormTypeMapper extends BaseMapper<FormType> {
    /**
     * 自定义方法区 不替换的区域【other_start】
     **/

    List<PagingFormTypeVo> getFormTypeIsGroup(QueryFormTypeDto queryFormTypeDto);

    List<String> getCoding();

/** 自定义方法区 不替换的区域【other_end】 **/
}