/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.vo.PackBomCalculateBaseVo;
import com.base.sbc.module.pricing.vo.PricingMaterialCostsVO;
import com.base.sbc.module.sample.dto.FabricSummaryDTO;
import com.base.sbc.module.sample.vo.FabricSummaryVO;
import com.base.sbc.module.sample.vo.MaterialSampleDesignVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：资料包-物料清单 dao类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.dao.PackBomDao
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-1 16:37:22
 */
@Mapper
public interface PackBomMapper extends BaseMapper<PackBom> {
// 自定义方法区 不替换的区域【other_start】

    List<String> getBomIdsByVersionId(@Param("bomVersionId") String bomVersionId);

    /**
     * 面料汇总列表
     * @param fabricSummaryDTO
     * @return
     */
    List<FabricSummaryVO> fabricSummaryList(FabricSummaryDTO fabricSummaryDTO);

    /**
     * 根据物料id查询被应用的样衣-款式
     * @param materialId 物料ID
     * @return 物料下样式设计数据
     */
    List<MaterialSampleDesignVO> querySampleDesignInfoByMaterialId(@Param("materialId") String materialId);

    /**
     * 通过主id获取核价物料信息
     *
     * @param foreignId
     * @return
     */
    List<PricingMaterialCostsVO> getPricingMaterialCostsByForeignId(@Param("foreignId") String foreignId);

    /**
     * 获取物料计算
     *
     * @param foreignIds
     * @return
     */
    List<PackBomCalculateBaseVo> getPackBomCalculateBaseVo(@Param("foreignIds") List<String> foreignIds);

    Long countByVersion(@Param("bomVersionId") String bomVersionId);


// 自定义方法区 不替换的区域【other_end】
}