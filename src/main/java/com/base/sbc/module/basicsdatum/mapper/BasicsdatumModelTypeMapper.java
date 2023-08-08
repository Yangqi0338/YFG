/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumModelTypeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：基础资料-号型类型 dao类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.dao.BasicsdatumModelTypeDao
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-20 9:31:14
 */
@Mapper
public interface BasicsdatumModelTypeMapper extends BaseMapper<BasicsdatumModelType> {
    /** 自定义方法区 不替换的区域【other_start】 **/

    String getNameById(@Param("id") String id);

/** 自定义方法区 不替换的区域【other_end】 **/
}