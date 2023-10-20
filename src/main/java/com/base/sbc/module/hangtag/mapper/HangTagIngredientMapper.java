/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.hangtag.entity.HangTagIngredient;
import com.base.sbc.module.hangtag.vo.HangTagIngredientVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：吊牌成分表 dao类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.hangTag.dao.HangTagIngredientDao
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:55
 */
@Mapper
public interface HangTagIngredientMapper extends BaseMapper<HangTagIngredient> {
    // 自定义方法区 不替换的区域【other_start】

    /**
     * 通过吊牌id获取
     *
     * @param hangTagId
     * @param companyCode
     * @return
     */
    List<HangTagIngredientVO> getIngredientListByHangTagId(@Param("hangTagId") String hangTagId, @Param("companyCode") String companyCode);


// 自定义方法区 不替换的区域【other_end】
}

