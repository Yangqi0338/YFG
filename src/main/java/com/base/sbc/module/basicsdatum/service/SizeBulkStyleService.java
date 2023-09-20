/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.base.sbc.module.basicsdatum.vo.SizeBulkStyleVo;
import com.base.sbc.module.basicsdatum.entity.SizeBulkStyle;

import java.util.List;

/**
 * 类描述：吊牌充绒量和特殊规格额外字段 service类
 * @address com.base.sbc.module.basicsdatum.service.SizeBulkStyleService
 * @author your name
 * @email your email
 * @date 创建时间：2023-9-20 15:13:40
 * @version 1.0
 */
public interface SizeBulkStyleService extends IService<SizeBulkStyle> {


// 自定义方法区 不替换的区域【other_start】

    List<SizeBulkStyleVo> listByBulkStyleNoAndSizeType( String[]  ids, String bulkStyleNo, String type);

// 自定义方法区 不替换的区域【other_end】


}
