/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.common.dto.IdDto;
import com.base.sbc.module.fabricsummary.entity.FabricSummaryPrintLog;
import com.base.sbc.module.pack.dto.*;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.vo.PackBomCalculateBaseVo;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.module.pricing.vo.PricingMaterialCostsVO;
import com.base.sbc.module.sample.dto.*;
import com.base.sbc.module.sample.vo.*;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
     *bom模板引用新增
     * @param bomTemplateSaveDto
     * @return
     */
    Boolean bomTemplateSave(BomTemplateSaveDto bomTemplateSaveDto);

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
     * @param fabricSummaryDTO 物料ID
     * @return 物料下样式设计数据
     */
    PageInfo<MaterialSampleDesignVO> querySampleDesignInfoByMaterialId(FabricSummaryDTO fabricSummaryDTO);


    /**
     * 通过主id获取核价物料信息
     *
     * @param foreignId
     * @return
     */
    List<PricingMaterialCostsVO> getPricingMaterialCostsByForeignId(String foreignId);

    /**
     * 获取物料计算
     * @param foreignIds
     * @return
     */
    List<PackBomCalculateBaseVo> getPackBomCalculateBaseVo(List<String> foreignIds);


    /**
     * 查询物料下发状态
     *
     * @param stringList
     * @return
     */
    Map<String, String> getPackSendStatus(List<String> stringList);

    void querySubList(List<PackBomVo> list);

    List<PackBomVo> list(String foreignId, String packType, String bomVersionId);


    /**
     * 解锁
     * @param idDto
     * @return
     */
    Boolean unlock(IdDto idDto);
// 自定义方法区 不替换的区域【other_end】


    /**
     * 根据多个颜色查询数据（跳过部分校验）
     *
     * @param dto
     * @return
     */
    List<PackBomVo> getPackBomVoList(PackCommonPageSearchDto dto);

    BigDecimal sumBomCost(PackBomPageSearchDto dto);

    ApiResult packBomMaterialColor(String companyCode, String id, String colorCode);

    /**
     * 统计数量，删除的也统计
     *
     * @param id 版本id
     * @return
     */
    Long countByVersion(String id);

    /**
     * 用于物料修改替换删除时计算成本是否改变
     * 如果改变判断款式定价的状态都通过，发送消息，修改款式定价的状态未为未通过
     *
     * @param packInfoId
     * @param cost
     */
    void costUpdate(String packInfoId, BigDecimal cost);

    /**
     * BOM使用物料列表
     * @param bomFabricDto
     * @param isPictureShow 是否图片展示
     * @return
     */
    PageInfo<BomFabricVo> bomFabricList(BomFabricDto bomFabricDto, boolean isPictureShow);

    /**
     * 保存物料汇总添加
     * @param feedSummarySaveDTO
     * @return
     */
    boolean saveFabricSummary(FabricSummarySaveDTO feedSummarySaveDTO);

    /**
     * 物料汇总列表
     * @param dto
     * @return
     */
    PageInfo<FabricSummaryInfoVo> fabricSummaryListV2(FabricSummaryV2Dto dto);

    /**
     * 物料汇总修改
     * @param dto
     * @return
     */
    boolean updateFabricSummary(FabricSummaryV2Dto dto);

    /**
     * 面料款式列表
     * @param dto
     * @return
     */
    PageInfo<FabricStyleVo> fabricStyleList(FabricStyleDto dto);

    /**
     * 添加物料汇总款式
     * @param dto
     * @return
     */
    boolean saveFabricSummaryStyle(FabricSummaryStyleSaveDto dto);

    /**
     * 修改物料汇总款式
     * @param
     * @return
     */
    boolean updateFabricSummaryStyle(List<FabricSummaryStyleDto> fabricSummaryStyleDtoList);

    /**
     * 删除物料汇总款式
     * @param id
     * @return
     */
    boolean deleteFabricSummaryStyle(String id);

    void fabricSummaryExcel( HttpServletResponse response, FabricSummaryV2Dto dto);

    /**
     * 面料汇总删除
     * @param dtoList
     * @return
     */
    boolean deleteFabricSummary(String dtoList);

    PageInfo<FabricSummaryPrintLog> printFabricSummaryLog(PrintFabricSummaryLogDto dto);

    boolean ifNeedUpdate(String id);
}
