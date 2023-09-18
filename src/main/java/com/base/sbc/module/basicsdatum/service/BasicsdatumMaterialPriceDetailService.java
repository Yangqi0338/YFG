/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.base.sbc.module.basicsdatum.dto.SupplierDetailPriceDto;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialPriceDetailVo;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialPriceDetail;
import com.base.sbc.module.pack.vo.BomSelMaterialVo;

import java.util.List;

/**
 * 类描述：基础资料-物料档案-供应商报价- service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialPriceDetailService
 * @author your name
 * @email your email
 * @date 创建时间：2023-7-27 17:53:42
 * @version 1.0
 */
public interface BasicsdatumMaterialPriceDetailService extends IService<BasicsdatumMaterialPriceDetail> {

// 自定义方法区 不替换的区域【other_start】
    /**
     * 通过物料编码复制
     * @param materialCode
     * @return
     */
    void copyByMaterialCode(String materialCode, String newMaterialCode);

    /**
     * 获取供应商规格颜色
     * @param supplierDetailPriceDto
     * @return
     */
    List<BasicsdatumMaterialPriceDetailVo>  gatSupplierWidthColorList(SupplierDetailPriceDto supplierDetailPriceDto);


    /**
     * 查询供应商规格
     * @param materialCodeList
     * @return
     */
    List<BomSelMaterialVo> querySupplierWidth(List<String> materialCodeList);



// 自定义方法区 不替换的区域【other_end】


}

