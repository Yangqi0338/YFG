package com.base.sbc.module.pack.service.impl;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.pack.dto.PackingDictionaryDto;
import com.base.sbc.module.pack.entity.PackingDictionary;
import com.base.sbc.module.pack.mapper.PackingDictionaryMapper;
import com.base.sbc.module.pack.service.PackingDictionaryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.openqa.selenium.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PackingDictionaryServiceImpl implements PackingDictionaryService {

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
    public PackingDictionary queryPackingDictionary(PackingDictionary dto) throws NotFoundException {
        PackingDictionary packingDictionary =  packingDictionaryMapper.queryPacking(dto.getParentId(),dto.getName());
        if (packingDictionary == null) {
            return new PackingDictionary();
        }
        return packingDictionary;
    }

    @Override
    public boolean update(PackingDictionary dto) {
        PackingDictionary packingDictionary = packingDictionaryMapper.queryPackingDictionary(dto);
        if(packingDictionary!=null){
            packingDictionaryMapper.updatePacking(dto);
          return  true;
        }else{
//            int i = packingDictionaryMapper.updatePacking(dto);
            throw new OtherException("已存在相同包装袋标准和形式，无法修改");
        }
    }

    @Override
    public boolean dele(PackingDictionary dto) {
        int i = packingDictionaryMapper.updatePacking(dto);
        return true;
    }
}
