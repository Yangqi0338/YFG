/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.customFile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.customFile.entity.FileTree;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 类描述：自定义文件夹 dao类
 * @address com.base.sbc.module.customFile.dao.FileTreeDao
 * @author your name  
 * @email  your email
 * @date 创建时间：2024-6-11 11:30:33 
 * @version 1.0  
 */
@Mapper
public interface FileTreeMapper extends BaseMapper<FileTree> {
    void updateHiberarchy(@Param("newId") String newId, @Param("oldId")String oldId);
// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
}