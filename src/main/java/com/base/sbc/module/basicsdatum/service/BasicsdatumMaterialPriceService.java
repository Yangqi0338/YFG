/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialPrice;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.pack.dto.MaterialSupplierInfo;
import com.base.sbc.module.pack.vo.BomSelMaterialVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/** 
 * 类描述：基础资料-物料档案-供应商报价 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialPriceService
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 创建时间：2023-6-26 17:57:25
 * @version 1.0  
 */
public interface BasicsdatumMaterialPriceService extends BaseService<BasicsdatumMaterialPrice> {

// 自定义方法区 不替换的区域【other_start】

    List<BomSelMaterialVo> findDefaultToBomSel(List<String> materialCodeList);

    /**
     * 获取默认供应商采购单价
     * @param materialCodes
     * @return
     */
    Map<String, BigDecimal> getDefaultSupplerQuotationPrice(List<String> materialCodes);

    /**
     * 通过物料编码复制
     * @param materialCode
     * @return
     */
    void copyByMaterialCode(String materialCode, String newMaterialCode);

    List<String> getMaterialCodeBySupplierInfo(MaterialSupplierInfo materialSupplierInfo);


// 自定义方法区 不替换的区域【other_end】

	
}

