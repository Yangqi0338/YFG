/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.pack.entity.PackBomColor;
import com.base.sbc.module.pack.mapper.PackBomColorMapper;
import com.base.sbc.module.pack.service.PackBomColorService;
import com.base.sbc.module.pack.vo.PackBomColorVo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 类描述：资料包-物料清单-配色 service类
 *
 * @author LiZan
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackBomColorService
 * @email 2682766618@qq.com
 * @date 创建时间：2023-8-23 9:44:43
 */
@Service
public class PackBomColorServiceImpl extends AbstractPackBaseServiceImpl<PackBomColorMapper, PackBomColor> implements PackBomColorService {
    @Override
    String getModeName() {
        return "物料颜色";
    }

    // 自定义方法区 不替换的区域【other_start】

    /**
     * 通过bomId查询资料包-物料清单-配色
     * @param bomIds bomId集合
     * @return 资料包-物料清单-配色
     */
    @Override
    public List<PackBomColorVo> getByBomIds(List<String> bomIds) {
        if (CollUtil.isEmpty(bomIds)) {
            return null;
        }
        QueryWrapper<PackBomColor> pbsQw = new QueryWrapper<>();
        pbsQw.in("bom_id", bomIds);
        List<PackBomColor> packBomSizeList = list(pbsQw);
        return BeanUtil.copyToList(packBomSizeList, PackBomColorVo.class);
    }

    /**
     * 资料包-物料清单-配色列表 转换MAP
     * @param bomIds  bomId集合
     * @return 料包-物料清单-配色
     */
    @Override
    public Map<String, List<PackBomColorVo>> getByBomIdsToMap(List<String> bomIds) {
        List<PackBomColorVo> packBomSizeList = getByBomIds(bomIds);
        Map<String, List<PackBomColorVo>> packBomSizeMap = Optional.ofNullable(packBomSizeList).map((pbsl -> {
            return pbsl.stream().collect(Collectors.groupingBy(PackBomColorVo::getBomId));
        })).orElse(new HashMap<>(2));
        return packBomSizeMap;
    }

    @Override
    public List<String> getBomIdByColorCode(String colorCode, String bomVersionId) {
        return super.getBaseMapper().getBomIdByColorCode(colorCode, bomVersionId);
    }

    // 自定义方法区 不替换的区域【other_end】

}

