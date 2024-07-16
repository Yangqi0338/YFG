package com.base.sbc.module.pack.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pack.dto.PackingDictionaryDto;
import com.base.sbc.module.pack.entity.PackingDictionary;
import com.base.sbc.module.pack.mapper.PackingDictionaryMapper;
import com.base.sbc.module.pack.service.PackingDictionaryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PackingDictionaryServiceImpl extends BaseServiceImpl<PackingDictionaryMapper, PackingDictionary> implements PackingDictionaryService {

    @Autowired
    private PackingDictionaryMapper packingDictionaryMapper;

    @Override
    public PageInfo<PackingDictionary> pageInfo(PackingDictionaryDto packingDictionaryDto) {
        try {
            Page<PackingDictionary> page = packingDictionaryDto.startPage();
            List<PackingDictionary> packingDictionaries = packingDictionaryMapper.listPackingDictionary();
//            PageInfo<PackingDictionary> page = new PageInfo<>(packingDictionaries);
//            page.setPageNum(packingDictionaryDto.getPageNum()); // 设置当前页码为 1
//            page.setPageSize(packingDictionaryDto.getPageSize()); // 设置页大小为查询到的列表大小
//            page.setTotal(packingDictionaries.size());// 设置总记录数为查询到的列表大小
            return page.toPageInfo();
        } catch (Exception e) {
            // 异常处理逻辑，例如记录日志或返回默认值
            e.printStackTrace(); // 记录异常信息
            return new PageInfo<>(Collections.emptyList()); // 返回一个空的 PageInfo 对象
        }
    }

    @Override
    public boolean save(PackingDictionary dto) {
        PackingDictionary packingDictionary = packingDictionaryMapper.queryPacking(dto.getParentId(),dto.getName());
        if(packingDictionary!=null){
          throw new OtherException("已存在相同包装袋标准和形式，无法添加");
        }
        int i = packingDictionaryMapper.addPacking(dto);
        return true;
    }

    @Override
    public PackingDictionary queryPackingDictionary(PackingDictionary dto) {
        PackingDictionary packingDictionary =  packingDictionaryMapper.queryPacking(dto.getParentId(),dto.getName());
        if (packingDictionary == null) {
            return new PackingDictionary();
        }
        return packingDictionary;
    }

    @Override
    public boolean update(PackingDictionary dto) {
//        PackingDictionary packingDictionary = packingDictionaryMapper.selectById(dto.getId());
//        if(packingDictionary == null) {
//            throw new OtherException("不存在对应的包装袋标准和形式");
//        }
//        boolean existsUniqueCode = packingDictionaryMapper.exists(new BaseLambdaQueryWrapper<PackingDictionary>().notNullNe(PackingDictionary::getId, dto.getId())
//                .eq(PackingDictionary::getParentId, dto.getParentId()).eq(PackingDictionary::getPackagingForm, dto.getPackagingForm()));
//        if (existsUniqueCode) throw new OtherException("已存在相同包装袋标准和形式，无法修改");
        PackingDictionary packingDictionary = packingDictionaryMapper.queryPackingDictionary(dto);
        if(packingDictionary!=null){
            throw new OtherException("已存在相同包装袋标准和形式，无法修改");
        }else{
            packingDictionaryMapper.updatePacking(dto);
            return  true;
        }
    }

    @Override
    public boolean dele(PackingDictionary dto) {
        int i = packingDictionaryMapper.updatePacking(dto);
        return true;
    }
}
