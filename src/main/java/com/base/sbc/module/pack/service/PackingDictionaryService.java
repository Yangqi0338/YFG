package com.base.sbc.module.pack.service;

import com.base.sbc.module.pack.dto.PackingDictionaryDto;
import com.base.sbc.module.pack.entity.PackingDictionary;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestBody;

public interface PackingDictionaryService {
    /**
     * 分页查询包装字典长宽高
     * @param packingDictionaryDto
     * @return
     */
    PageInfo<PackingDictionary> pageInfo(PackingDictionaryDto packingDictionaryDto);

    /**
     * 新增包装字典
     * @param dto
     * @return
     */
    int save(PackingDictionary dto);
}
