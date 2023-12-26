/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.style.dto.QueryStyleColorCorrectDto;
import com.base.sbc.module.style.entity.StyleColorCorrectInfo;
import com.base.sbc.module.style.vo.StyleColorCorrectInfoVo;
import com.github.pagehelper.PageInfo;

/** 
 * 类描述：正确样管理 service类
 * @address com.base.sbc.module.style.service.StyleColorCorrectInfoService
 * @author your name
 * @email your email
 * @date 创建时间：2023-12-26 10:13:31
 * @version 1.0  
 */
public interface StyleColorCorrectInfoService extends BaseService<StyleColorCorrectInfo>{

    PageInfo<StyleColorCorrectInfoVo> findList(QueryStyleColorCorrectDto page);

    void saveMain(StyleColorCorrectInfo styleColorCorrectInfo);
}
