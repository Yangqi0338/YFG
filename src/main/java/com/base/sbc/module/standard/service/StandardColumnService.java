/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.standard.service;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.material.dto.MaterialSaveDto;
import com.base.sbc.module.standard.dto.StandardColumnDto;
import com.base.sbc.module.standard.dto.StandardColumnQueryDto;
import com.base.sbc.module.standard.dto.StandardColumnSaveDto;
import com.base.sbc.module.standard.entity.StandardColumn;

import java.util.List;
import java.util.Map;

/** 
 * 类描述：吊牌&洗唛全量标准表 service类
 * @address com.base.sbc.module.moreLanguage.service.StandardColumnService
 * @author KC
 * @email KC
 * @date 创建时间：2023-11-30 15:01:58
 * @version 1.0  
 */
public interface StandardColumnService extends BaseService<StandardColumn>{
    String save(StandardColumnSaveDto standardColumnSaveDto);

    boolean delByIds(List<String> list);

    List<StandardColumnDto> listQuery(StandardColumnQueryDto standardColumnQueryDto);

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】

	
}
