/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.customFile.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.customFile.dto.FileTreeDto;
import com.base.sbc.module.customFile.entity.FileTree;
import com.base.sbc.module.customFile.mapper.FileTreeMapper;
import com.base.sbc.module.customFile.service.FileTreeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 类描述：自定义文件夹 service类
 * @address com.base.sbc.module.customFile.service.FileTreeService
 * @author your name
 * @email your email
 * @date 创建时间：2024-6-11 11:30:33
 * @version 1.0  
 */
@Service
public class FileTreeServiceImpl extends BaseServiceImpl<FileTreeMapper, FileTree> implements FileTreeService {
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public String add(FileTreeDto fileTreeDto) {

        if (StringUtils.isEmpty(fileTreeDto.getParentId())){
            throw new OtherException("父级节点不能为空");
        }
        FileTree fileTree = this.getById(fileTreeDto.getParentId());
        if (null == fileTree && !Constants.ZERO.equals(fileTreeDto.getParentId())){
            throw new OtherException("父级节点不存在");
        }
        fileTreeDto.setId(new IdGen().nextIdStr());
        if (StringUtils.isBlank(fileTreeDto.getParentIds()) && null != fileTree){
            fileTreeDto.setParentIds(fileTree.getParentIds() + fileTreeDto.getParentId() + Constants.COMMA);
        }
        save(fileTreeDto);
        return fileTreeDto.getId();
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
