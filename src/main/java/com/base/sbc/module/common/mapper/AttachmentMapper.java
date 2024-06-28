/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.common.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.vo.AttachmentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：附件 dao类
 * @address com.base.sbc.module.common.dao.AttachmentDao
 * @author lxl  
 * @email  lxl.fml@gmail.com
 * @date 创建时间：2023-5-12 16:05:59 
 * @version 1.0  
 */
@Mapper
public interface AttachmentMapper extends BaseMapper<Attachment> {
/**
 * 自定义方法区 不替换的区域【other_start】
 **/

List<AttachmentVo> findByFId(@Param("foreignId") String foreignId, @Param("type") String type, @Param("typeLikeStart") String typeLikeStart);

    List<AttachmentVo> findByQw(@Param(Constants.WRAPPER) QueryWrapper wrappe);

    List<AttachmentVo> findByFIds(@Param("foreignIds") List<String> foreignIds, @Param("type") String type, @Param("typeLikeStart") String typeLikeStart);
/** 自定义方法区 不替换的区域【other_end】 **/
}