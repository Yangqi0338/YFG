/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.basicsdatum.vo.SizeBulkStyleVo;
import org.apache.ibatis.annotations.Mapper;
import com.base.sbc.module.basicsdatum.entity.SizeBulkStyle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：吊牌充绒量和特殊规格额外字段 dao类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.dao.SizeBulkStyleDao
 * @email your email
 * @date 创建时间：2023-9-20 15:13:40
 */
@Mapper
public interface SizeBulkStyleMapper extends BaseMapper<SizeBulkStyle> {

    // 自定义方法区 不替换的区域【other_start】
    List<SizeBulkStyleVo> listByBulkStyleNoAndSizeType(@Param("ids") String[] ids, @Param("bulkStyleNo") String bulkStyleNo,@Param("type") String type);

// 自定义方法区 不替换的区域【other_end】
}
