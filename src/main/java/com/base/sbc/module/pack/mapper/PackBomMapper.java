/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.vo.PackBomCalculateBaseVo;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.module.pricing.vo.PricingMaterialCostsVO;
import com.base.sbc.module.sample.dto.BomFabricDto;
import com.base.sbc.module.sample.dto.FabricStyleDto;
import com.base.sbc.module.sample.dto.FabricSummaryDTO;
import com.base.sbc.module.sample.vo.BomFabricVo;
import com.base.sbc.module.sample.vo.FabricStyleVo;
import com.base.sbc.module.sample.vo.FabricSummaryVO;
import com.base.sbc.module.sample.vo.MaterialSampleDesignVO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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
    List<FabricSummaryVO> fabricSummaryList(@Param("dto") FabricSummaryDTO fabricSummaryDTO,
                                            @Param(Constants.WRAPPER) QueryWrapper qw);

    /**
     * 根据物料id查询被应用的样衣-款式
     * @param fabricSummaryDTO 面料汇总DTO层
     * @return 物料下样式设计数据
     */
    List<MaterialSampleDesignVO> querySampleDesignInfoByMaterialId(FabricSummaryDTO fabricSummaryDTO);

    /**
     * 根据物料id查询被应用的样衣-款式分页
     * @param fabricSummaryDTO 面料汇总DTO层
     * @return 物料下样式设计数据
     */
    List<MaterialSampleDesignVO> querySampleDesignInfoByMaterialIdPage(FabricSummaryDTO fabricSummaryDTO);

    /**
     * 根据物料id查询被应用的样衣-款式数量
     * @param fabricSummaryDTO 面料汇总DTO层
     * @return 裁数
     */
    Integer querySampleDesignInfoByMaterialIdCount(FabricSummaryDTO fabricSummaryDTO);


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

    /**
     * 统计数量 删除的也统计
     *
     * @param bomVersionId
     * @return
     */
    Long countByVersion(@Param("bomVersionId") String bomVersionId);

    /**
     * 物料清单查询分页
     *
     * @param packCommonPageSearchDto 资料包-公共筛选条件
     * @return 物料清单
     */
    List<PackBomVo> getPackBomPage(PackCommonPageSearchDto packCommonPageSearchDto);

    /**
     * 查询物料下发状态
     * @return
     */
    @MapKey("foreign_id")
    List<Map<String, String>> getPackSendStatus(@Param("foreignIds") List<String> foreignIds);

    /**
     * 物料清单查询分页(开放接口)
     *
     * @param packCommonPageSearchDto 资料包-公共筛选条件
     * @return 物料清单
     */
    List<PackBomVo> getPackBomListOpen(PackCommonPageSearchDto packCommonPageSearchDto);

    List<PackBom> sumBomCost(@Param(Constants.WRAPPER) QueryWrapper<PackBom> qw);

    List<BomFabricVo> bomFabricList(@Param("dto") BomFabricDto dto,
                                @Param(Constants.WRAPPER)QueryWrapper qw);

    List<FabricStyleVo> fabricStyleList(@Param("dto") FabricStyleDto dto, @Param(Constants.WRAPPER)QueryWrapper qw);

    List<PackBom>  selectByForeignId(@Param(Constants.WRAPPER)QueryWrapper qc);

    int materialBomCount(@Param(Constants.WRAPPER)QueryWrapper qw);

// 自定义方法区 不替换的区域【other_end】
}
