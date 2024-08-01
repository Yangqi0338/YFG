/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.customFile.service.impl;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.customFile.dto.FileTreeDto;
import com.base.sbc.module.customFile.dto.MergeFolderDto;
import com.base.sbc.module.customFile.entity.FileTree;
import com.base.sbc.module.customFile.enums.FileBusinessType;
import com.base.sbc.module.customFile.mapper.FileTreeMapper;
import com.base.sbc.module.customFile.service.FileTreeService;
import com.base.sbc.module.customFile.vo.FileTreeVo;
import com.base.sbc.module.material.dto.MaterialQueryDto;
import com.base.sbc.module.material.service.MaterialCollectService;
import com.base.sbc.module.material.service.MaterialService;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;

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

    @Autowired
    private MaterialCollectService materialCollectService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public String addOrUpdate(FileTreeDto fileTreeDto) {
        if (StringUtils.isEmpty(fileTreeDto.getParentId())){
            throw new OtherException("父级节点不能为空");
        }
        if (StringUtils.isEmpty(fileTreeDto.getName())){
            throw new OtherException("文件夹名称不能为空");
        }
        QueryWrapper<FileTree> qw = new QueryWrapper<>();
        qw.lambda().eq(FileTree::getCreateId,companyUserInfo.get().getUserId());
        qw.lambda().eq(FileTree::getName,fileTreeDto.getName());
        qw.lambda().eq(FileTree::getBusinessType,fileTreeDto.getBusinessType());
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
    public List<FileTreeVo> queryFileTree(FileTreeDto fileTreeDto) {
        String userId = companyUserInfo.get().getUserId();
        QueryWrapper<FileTree> qw = new QueryWrapper<>();
        qw.lambda().eq(FileTree::getParentId,fileTreeDto.getParentId());
        qw.lambda().eq(FileTree::getBusinessType,fileTreeDto.getBusinessType());
        qw.lambda().in(FileTree::getCreateId,Lists.newArrayList("0",userId));
        qw.lambda().orderByAsc(FileTree::getType);
        List<FileTree> list = list(qw);
        if (CollUtil.isEmpty(list)){
            return Lists.newArrayList();
        }
        FileBusinessType fileBusinessType = FileBusinessType.getByCode(list.get(0).getBusinessType());
        List<FileTreeVo> fileTreeVos = BeanUtil.copyToList(list, FileTreeVo.class);
        //类型少暂时用if
//        if (fileBusinessType == FileBusinessType.material) {
//            fileTreeVos.forEach(item ->{
//                if (!"0".equals(item.getType())){
//                    List<String> byAllFileIds = getByAllFileIds(item.getId());
//                    item.setFileCount(materialService.getFileCount(userId,byAllFileIds));
//                    item.setFileSize(materialService.getFileSize(userId,byAllFileIds));
//                }
//            });
//        }

        switch (fileBusinessType){
            case material:
                fillMaterialFileTreeVo(fileTreeVos,false,userId);
                break;
            case material_collect:
                fillMaterialFileTreeVo(fileTreeVos,true,userId);
                break;
            default:
                break;
        }


        return fileTreeVos;
    }


    @Override
    @Transactional(rollbackFor = Exception.class )
    public Boolean mergeFolder(MergeFolderDto dto) {
        if (CollUtil.isEmpty(dto.getMergeFolderIds())){
            throw new OtherException("需要合并的文件夹不能为空");
        }
        if (dto.getMergeFolderIds().size() < 2){
            throw new OtherException("需要合并的文件夹最少为两个");
        }
        QueryWrapper<FileTree> qw = new QueryWrapper<>();
        qw.lambda().in(FileTree::getId,dto.getMergeFolderIds());
        List<FileTree> list = list(qw);
        Map<String, FileTree> fileTreeMap = list.stream().collect(Collectors.toMap(FileTree::getId, item -> item));
        Set<String> set = list.stream().map(FileTree::getBusinessType).collect(Collectors.toSet());
        if (set.size() > 1){
            throw new OtherException("需要合并的文件夹类型必须一致");
        }
        FileBusinessType fileBusinessType = FileBusinessType.getByCode(list.get(0).getBusinessType());
        //列表的第一个文件作为住文件夹
        FileTree fileTree = fileTreeMap.get(dto.getMergeFolderIds().get(0));

        List<FileTree> collect = list.stream().filter(item -> "0".equals(item.getType()) && !item.getId().equals(fileTree.getId())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(collect)){
            throw new OtherException("系统文件夹不允许合并到另外一个文件夹中！");
        }

        if (StringUtils.isNotEmpty(dto.getName()) && !dto.getName().equals(fileTree.getName())){
            //检查名称
            checkFileName(dto.getName(),fileBusinessType.getCode(),dto.getMergeFolderIds());
            fileTree.setName(dto.getName());
        }
        updateById(fileTree);

        //被合并的文件id集合
        List<String> byMergeFolderIds = list.stream().map(FileTree::getId).filter(item -> !item.equals(fileTree.getId())).collect(Collectors.toList());

        //替换文件夹的层级关系
        replaceHiberarchy(fileTree.getId(),byMergeFolderIds);

        switch (fileBusinessType){
            case material:
                 materialService.mergeFolderReplace(fileTree.getId(),byMergeFolderIds);
                break;
            case material_collect:
                materialCollectService.mergeFolderReplace(fileTree.getId(),byMergeFolderIds);
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class )
    public boolean del(String id) {
        if (StringUtils.isEmpty(id)){
            return true;
        }
        List<String> ids = StringUtils.convertList(id);
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
        switch (fileBusinessType){
            case material:
                fileBusinessType.check(ids, materialService::checkFolderRelation);
                break;
            case material_collect:
                fileBusinessType.check(ids, materialCollectService::checkFolderRelation);
                break;
            default:
                break;

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

    private void checkFileName(String name, String businessType, List<String> folderIds) {
        if (StringUtils.isBlank(name)){
            return;
        }
        QueryWrapper<FileTree> qw = new QueryWrapper<>();
        if (CollUtil.isNotEmpty(folderIds)){
            qw.lambda().notIn(FileTree::getId,folderIds);
        }
        qw.lambda().eq(FileTree::getCreateId,companyUserInfo.get().getUserId());
        qw.lambda().eq(FileTree::getName,name);
        qw.lambda().eq(FileTree::getBusinessType,businessType);
        qw.lambda().eq(FileTree::getDelFlag,"0");
        qw.lambda().last("limit 1");
        FileTree one = getOne(qw);
        if (null != one){
            throw new OtherException("文件夹名称不能重复！");
        }
    }

    /**
     * 替换层级关系
     * @param id
     * @param byMergeFolderIds
     */
    private void replaceHiberarchy(String id, List<String> byMergeFolderIds) {
        //删除合并的文件夹
        updateDelById(byMergeFolderIds);

        //todo:现在暂时只有一级文件夹，下方暂时可以注释，后续多级文件夹可以打开

        //替换层级关系
//        UpdateWrapper<FileTree> uw = new UpdateWrapper<>();
//        uw.lambda().in(FileTree::getParentId,byMergeFolderIds);
//        uw.lambda().eq(FileTree::getDelFlag,"0");
//        uw.lambda().set(FileTree::getParentId,id);
//        update(uw);
//
//        byMergeFolderIds.forEach(item ->baseMapper.updateHiberarchy(id,item));

    }


    private boolean updateDelById(List<String> ids){
        UpdateWrapper<FileTree> uw = new UpdateWrapper<>();
        uw.lambda().in(FileTree::getId,ids);
        uw.lambda().set(FileTree::getDelFlag,"1");
        return update(uw);
    }


    private void fillMaterialFileTreeVo(List<FileTreeVo> list, boolean collect, String userId){
        MaterialQueryDto materialQueryDto = new MaterialQueryDto();
        materialQueryDto.setPageNum(1);
        materialQueryDto.setPageSize(5);
        materialQueryDto.setCollect(collect);
        list.forEach(item ->{
            List<String> byAllFileIds = getByAllFileIds(item.getId());

            if ( !collect ){
                item.setFileCount(materialService.getFileCount(userId,"0".equals(item.getType()) ? null : byAllFileIds));
                item.setFileSize("0".equals(item.getType()) ? 0L : materialService.getFileSize(userId,byAllFileIds));
            }else {
                item.setFileCount(materialCollectService.getCollectFileCount(userId,"0".equals(item.getType()) ? null : byAllFileIds));
            }
            materialQueryDto.setCreateId(userId);
            materialQueryDto.setCompanyFlag(collect ? null : "0");
            materialQueryDto.setFolderId("0".equals(item.getType()) ? null : item.getId());
            item.setDataList(materialService.listImgQuery(materialQueryDto));
        });
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
