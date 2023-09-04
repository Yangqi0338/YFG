/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.pack.entity.PackBomColor;
import com.base.sbc.module.pack.entity.PackBomSize;
import com.base.sbc.module.pack.vo.PackBomColorVo;
import com.base.sbc.module.pack.vo.PackBomSizeVo;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

/** 
 * 类描述：资料包-物料清单-配色 service类
 * @address com.base.sbc.module.pack.service.PackBomColorService
 * @author LiZan
 * @email 2682766618@qq.com
 * @date 创建时间：2023-8-23 9:44:43
 * @version 1.0  
 */
public interface PackBomColorService extends  PackBaseService<PackBomColor> {

// 自定义方法区 不替换的区域【other_start】
    /**
     * 通过bomId查询资料包-物料清单-配色
     * @param bomIds bomId集合
     * @return 资料包-物料清单-配色
     */
    List<PackBomColorVo> getByBomIds(List<String> bomIds);

    /**
     * 资料包-物料清单-配色列表 转换MAP
     * @param bomIds  bomId集合
     * @return 料包-物料清单-配色
     */
    Map<String, List<PackBomColorVo>> getByBomIdsToMap(List<String> bomIds);


// 自定义方法区 不替换的区域【other_end】

	
}

