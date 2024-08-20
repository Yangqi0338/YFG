/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSize;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSizeService;
import com.base.sbc.module.pack.entity.PackBomSize;
import com.base.sbc.module.pack.mapper.PackBomSizeMapper;
import com.base.sbc.module.pack.service.PackBomSizeService;
import com.base.sbc.module.pack.vo.PackBomSizeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：资料包-物料清单-配码 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackBomSizeService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-1 16:37:24
 */
@Service
public class PackBomSizeServiceImpl extends AbstractPackBaseServiceImpl<PackBomSizeMapper, PackBomSize> implements PackBomSizeService {

    // 自定义方法区 不替换的区域【other_start】

    @Autowired
    public BasicsdatumSizeService basicsdatumSizeService;

    @Override
    public Map<String, List<PackBomSizeVo>> getByBomIdsToMap(List<String> bomIds) {
        List<PackBomSizeVo> packBomSizeList = getByBomIds(bomIds);
        Map<String, List<PackBomSizeVo>> packBomSizeMap = Optional.ofNullable(packBomSizeList).map((pbsl -> {
            return pbsl.stream().collect(Collectors.groupingBy(PackBomSizeVo::getBomId));
        })).orElse(new HashMap<>(2));
        return packBomSizeMap;
    }

    @Override
    public List<PackBomSizeVo> getByBomIds(List<String> bomIds) {
        if (CollUtil.isEmpty(bomIds)) {
            return null;
        }
        QueryWrapper<PackBomSize> pbsQw = new QueryWrapper<>();
        pbsQw.in("bom_id", bomIds);
        List<PackBomSize> packBomSizeList = list(pbsQw);
        List<PackBomSizeVo> packBomSizeVos = BeanUtil.copyToList(packBomSizeList, PackBomSizeVo.class);
        //查询基表给返回数据做一下排序
        List<BasicsdatumSize> list = basicsdatumSizeService.list();
        Map<String, String> collect = list.stream().collect(Collectors.toMap(BaseEntity::getId, BasicsdatumSize::getCode));

        for (PackBomSizeVo packBomSizeVo : packBomSizeVos) {
            if(collect.containsKey(packBomSizeVo.getSizeId())) {
                packBomSizeVo.setSort(collect.get(packBomSizeVo.getSizeId()));
            }
        }
        packBomSizeVos.sort(Comparator.comparing(o-> StrUtil.isNotBlank(o.getSort())?o.getSort():""));
        return packBomSizeVos;
    }

    @Override
    String getModeName() {
        return "物料配码";
    }


// 自定义方法区 不替换的区域【other_end】

}
