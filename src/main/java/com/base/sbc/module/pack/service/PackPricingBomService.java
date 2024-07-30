/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackPricingBomDto;
import com.base.sbc.module.pack.entity.PackPricingBom;
import com.base.sbc.module.pack.dto.PackPricingBomQueryDto;
import com.base.sbc.module.pack.vo.PackPricingBomVo;
import com.github.pagehelper.PageInfo;
import org.springframework.scheduling.annotation.Async;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/** 
 * 类描述：资料包-物料清单 service类
 * @address com.base.sbc.module.pack.service.PackPricingBomService
 * @author 孟繁江
 * @email your email
 * @date 创建时间：2024-7-29 17:56:54
 * @version 1.0  
 */
public interface PackPricingBomService extends BaseService<PackPricingBom>{

// 自定义方法区 不替换的区域【other_start】

    /**
     * 查询核价清单
     * @param dto
     * @return
     */
    PageInfo pageInfo(PackCommonPageSearchDto dto);

    /**
     * 计算核价总成本
     * @param dto
     * @return
     */
    BigDecimal calculateCosts(PackCommonSearchDto dto);

    /**
     * 复制
     * @param sourceForeignId
     * @param sourcePackType
     * @param targetForeignId
     * @param targetPackType
     * @param map 物料清单idmap
     * @return
     */
    boolean copy(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType, Map<String,String> map);


    /**
     * 修改保存
     * @param list
     * @return
     */
    boolean saveOrUpdate(List<PackPricingBomDto> list);

// 自定义方法区 不替换的区域【other_end】

	
}