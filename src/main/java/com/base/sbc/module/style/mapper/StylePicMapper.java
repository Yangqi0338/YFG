/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.base.sbc.module.style.entity.StylePic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：款式设计-设计款图 dao类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.style.dao.StylePicDao
 * @email your email
 * @date 创建时间：2023-10-20 13:15:21
 */
@Mapper
public interface StylePicMapper extends BaseMapper<StylePic> {
// 自定义方法区 不替换的区域【other_start】

    List<Integer> getSorts(@Param("styleId") String styleId);


// 自定义方法区 不替换的区域【other_end】
}