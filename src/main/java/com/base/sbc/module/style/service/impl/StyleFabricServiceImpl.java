/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.style.dto.StyleFabricSaveDTO;
import com.base.sbc.module.style.entity.StyleFabric;
import com.base.sbc.module.style.mapper.StyleFabricMapper;
import com.base.sbc.module.style.service.StyleFabricService;
import com.base.sbc.module.style.vo.StyleFabricVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：款式面料 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.style.service.StyleFabricService
 * @email your email
 * @date 创建时间：2023-8-24 10:17:48
 */
@Service
public class StyleFabricServiceImpl extends BaseServiceImpl<StyleFabricMapper, StyleFabric> implements StyleFabricService {
    // 自定义方法区 不替换的区域【other_start】
    @Override
    public void save(List<StyleFabricSaveDTO> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return;
        }

        Map<String, String> map = this.getId(dtos.get(0).getStyleId());
        IdGen idGen = new IdGen();
        List<StyleFabric> styleFabrics = dtos.stream()
                .map(e -> {
                    StyleFabric styleFabric = CopyUtil.copy(e, StyleFabric.class);
                    String id = map.get(styleFabric.getFabricLibraryId());
                    styleFabric.setId(StringUtils.isEmpty(id) ? idGen.nextIdStr() : id);
                    styleFabric.setCompanyCode(super.getCompanyCode());
                    styleFabric.insertInit();
                    return styleFabric;
                }).collect(Collectors.toList());
        super.saveOrUpdateBatch(styleFabrics);
    }

    private Map<String, String> getId(String styleId) {
        if (StringUtils.isEmpty(styleId)) {
            return new HashMap<>();
        }
        LambdaQueryWrapper<StyleFabric> qw = new QueryWrapper<StyleFabric>()
                .lambda()
                .eq(StyleFabric::getStyleId, styleId)
                .eq(StyleFabric::getDelFlag, "0")
                .select(StyleFabric::getId, StyleFabric::getFabricLibraryId);
        List<StyleFabric> list = super.list(qw);
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream()
                .collect(Collectors.toMap(StyleFabric::getFabricLibraryId, StyleFabric::getId, (k1, k2) -> k2));
    }

    @Override
    public void update(StyleFabricSaveDTO dto) {
        StyleFabric styleFabric = CopyUtil.copy(dto, StyleFabric.class);
        styleFabric.updateInit();
        super.updateById(styleFabric);
    }

    @Override
    public List<StyleFabricVO> getByStyleId(String styleId) {
        return super.getBaseMapper().getByStyleId(styleId);
    }


// 自定义方法区 不替换的区域【other_end】

}
