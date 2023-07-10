/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumCompanyRelation;
import org.apache.ibatis.annotations.Param;

/**
 * 类描述：基础资料-品类关系表 dao类
 * @address com.base.sbc.module.basicsdatum.dao.BasicsdatumCompanyRelationDao
 * @author mengfanjiang  
 * @email  XX.com
 * @date 创建时间：2023-7-10 9:24:27 
 * @version 1.0  
 */
@Mapper
public interface BasicsdatumCompanyRelationMapper extends BaseMapper<BasicsdatumCompanyRelation> {
// 自定义方法区 不替换的区域【other_start】

    Boolean deleteRelation(@Param("dataId") String dataId,@Param("type")  String type);

// 自定义方法区 不替换的区域【other_end】
}