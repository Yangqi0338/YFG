/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.sample.entity.PreProductionSampleTaskFob;
import com.base.sbc.module.sample.dto.PreProductionSampleTaskFobQueryDto;
import com.base.sbc.module.sample.vo.PreProductionSampleTaskFobVo;
import com.github.pagehelper.PageInfo;

/** 
 * 类描述：产前样-任务 service类
 * @address com.base.sbc.module.sample.service.PreProductionSampleTaskFobService
 * @author your name
 * @email your email
 * @date 创建时间：2024-7-10 11:25:56
 * @version 1.0  
 */
public interface PreProductionSampleTaskFobService extends BaseService<PreProductionSampleTaskFob>{

// 自定义方法区 不替换的区域【other_start】

    PageInfo<PreProductionSampleTaskFobVo> findPage(PreProductionSampleTaskFobQueryDto dto);

// 自定义方法区 不替换的区域【other_end】

	
}