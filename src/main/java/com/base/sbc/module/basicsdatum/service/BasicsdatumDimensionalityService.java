/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumDimensionalityDto;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumDimensionality;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/** 
 * 类描述：基础资料-纬度系数表 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumDimensionalityService
 * @author your name
 * @email your email
 * @date 创建时间：2024-1-15 14:34:41
 * @version 1.0  
 */
public interface BasicsdatumDimensionalityService extends BaseService<BasicsdatumDimensionality>{

// 自定义方法区 不替换的区域【other_start】

    /**
     * 获取维度数据
     * @param dto
     * @return
     */
    Map getDimensionality(BasicsdatumDimensionalityDto dto);

    /**
     * 保存/编辑维度标签
     * @param dtoList
     * @return
     */
    boolean batchSaveDimensionality( List<BasicsdatumDimensionalityDto> dtoList);



// 自定义方法区 不替换的区域【other_end】

	
}
