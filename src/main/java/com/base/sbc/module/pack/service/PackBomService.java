/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.module.pack.dto.PackBomDto;
import com.base.sbc.module.pack.dto.PackBomPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.module.pricing.dto.FabricSummaryDTO;
import com.base.sbc.module.pricing.vo.FabricSummaryVO;
import com.base.sbc.module.pricing.vo.MaterialSampleDesignVO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 类描述：资料包-物料清单 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackBomService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-1 16:37:22
 */
public interface PackBomService extends PackBaseService<PackBom> {

// 自定义方法区 不替换的区域【other_start】


    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    PageInfo<PackBomVo> pageInfo(PackBomPageSearchDto dto);

    /**
     * 物料清单-保存单个
     *
     * @param dto
     * @return
     */
    PackBomVo saveByDto(PackBomDto dto);

    /**
     * 物料清单-全部保存
     *
     * @param bomVersionId
     * @param overlayFlg
     * @param dtoList
     * @return
     */
    boolean saveBatchByDto(String bomVersionId, String overlayFlg, List<PackBomDto> dtoList);


    /**
     * 通过版本id 查询bom id
     *
     * @param bomVersionId
     * @return
     */
    List<String> getBomIdsByVersionId(String bomVersionId);

    /**
     * 物料不可用状态改变
     *
     * @param id
     * @param unusableFlag
     * @return
     */
    boolean unusableChange(String id, String unusableFlag);

    BigDecimal calculateCosts(PackCommonSearchDto dto);

    List<PackBom> getListByVersionId(String versionId, String unusableFlag);

    PageInfo<PackBomVo> pageInfo(PackCommonPageSearchDto dto);

    /**
     * 面料汇总列表
     * @param fabricSummaryDTO 参数
     * @return 数据列表
     */
    PageInfo<FabricSummaryVO> fabricSummaryList(FabricSummaryDTO fabricSummaryDTO);

    /**
     * 根据物料id查询被应用的样衣-款式
     * @param materialId 物料ID
     * @return 物料下样式设计数据
     */
    List<MaterialSampleDesignVO> querySampleDesignInfoByMaterialId(@Param("materialId") String materialId);

// 自定义方法区 不替换的区域【other_end】


}
