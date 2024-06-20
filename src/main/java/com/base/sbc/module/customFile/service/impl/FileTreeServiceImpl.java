/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.customFile.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.customFile.dto.FileTreeDto;
import com.base.sbc.module.customFile.entity.FileTree;
import com.base.sbc.module.customFile.enums.FileBusinessType;
import com.base.sbc.module.customFile.mapper.FileTreeMapper;
import com.base.sbc.module.customFile.service.FileTreeService;
import com.base.sbc.module.material.service.MaterialService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

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

    @Autowired
    private MaterialService materialService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public String addOrUpdate(FileTreeDto fileTreeDto) {
        if (StringUtils.isEmpty(fileTreeDto.getParentId())){
            throw new OtherException("父级节点不能为空");
        }
        QueryWrapper<FileTree> qw = new QueryWrapper<>();
        qw.lambda().eq(FileTree::getCreateId,companyUserInfo.get().getUserId());
        qw.lambda().eq(FileTree::getName,fileTreeDto.getName());
        qw.lambda().eq(FileTree::getDelFlag,"0");
        qw.lambda().last("limit 1");
        FileTree one = getOne(qw);
        if (null != one && !one.getId().equals(fileTreeDto.getId())){
            throw new OtherException("自定义文件夹名称不能重复！");
        }


        FileBusinessType.getByCode(fileTreeDto.getBusinessType());
        if (StringUtils.isEmpty(fileTreeDto.getId())){
            FileTree fileTree = this.getById(fileTreeDto.getParentId());
            if (null == fileTree && !Constants.ZERO.equals(fileTreeDto.getParentId())){
                throw new OtherException("父级节点不存在");
            }
            fileTreeDto.setId(new IdGen().nextIdStr());
            String parentIds = null == fileTree ? fileTreeDto.getParentId() +  Constants.COMMA : fileTree.getParentIds();
            fileTreeDto.insertInit();
            fileTreeDto.setParentIds(parentIds + fileTreeDto.getId() + Constants.COMMA);
            save(fileTreeDto);
        }else {
            fileTreeDto.updateInit();
            updateById(fileTreeDto);
        }
        return fileTreeDto.getId();
    }

    @Override
    public List<FileTree> queryFileTree(FileTreeDto fileTreeDto) {
        String userId = companyUserInfo.get().getUserId();
        QueryWrapper<FileTree> qw = new QueryWrapper<>();
        qw.lambda().eq(FileTree::getParentId,fileTreeDto.getParentId());
        qw.lambda().in(FileTree::getCreateId,Lists.newArrayList("0",userId));
        qw.lambda().orderByAsc(FileTree::getType);
        List<FileTree> list = list(qw);
        if (CollUtil.isEmpty(list)){
            return list;
        }
        FileBusinessType fileBusinessType = FileBusinessType.getByCode(list.get(0).getBusinessType());
        //类型少暂时用if
        if (fileBusinessType == FileBusinessType.material) {
            list.forEach(item ->{
                List<String> byAllFileIds = getByAllFileIds(item.getId());
                item.setFileCount(materialService.getFileCount(userId,byAllFileIds));
                item.setFileSize(materialService.getFileSize(userId,byAllFileIds));
            });
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class )
    public boolean del(String id) {
        if (StringUtils.isEmpty(id)){
            return true;
        }
        List<String> ids = Lists.newArrayList();

        QueryWrapper<FileTree> qw = new QueryWrapper<>();
        qw.lambda().in(FileTree::getId, ids);
        qw.lambda().eq(FileTree::getType,"0");
        if (count(qw) > 0){
            throw new OtherException("系统级文件夹不允许删除");
        }
        //获取文件下面所有包含的文件夹集合
        for (String s : StringUtils.convertList(id)) {
            ids.addAll(getByAllFileIds(s));
        }
        FileTree fileTree = getById(ids.get(0));
        if (fileTree == null){
            throw new OtherException("文件夹不存在");
        }
        FileBusinessType fileBusinessType = FileBusinessType.getByCode(fileTree.getBusinessType());

        //类型少暂时用if
        if (fileBusinessType == FileBusinessType.material) {
            fileBusinessType.check(ids, materialService::checkFolderRelation);
        }
        UpdateWrapper<FileTree> uw = new UpdateWrapper<>();
        uw.lambda().in(FileTree::getId, ids);
        uw.lambda().set(FileTree::getDelFlag,"1");
        return update(uw);
    }


    public List<String> getByAllFileIds(String id){
        QueryWrapper<FileTree> qw = new QueryWrapper<>();
        qw.lambda().like(FileTree::getParentIds,id);
        qw.lambda().select(FileTree::getId);
        List<FileTree> list = list(qw);
        if (CollUtil.isNotEmpty(list)){
            return list.stream().map(FileTree::getId).collect(Collectors.toList());
        }

        return Lists.newArrayList();
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
