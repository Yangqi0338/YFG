/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.common.dto.GetMaxCodeRedis;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.planning.dto.*;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningChannel;
import com.base.sbc.module.planning.vo.DimensionTotalVo;
import com.base.sbc.module.planning.vo.PlanningSeasonOverviewVo;
import com.base.sbc.module.planning.vo.PlanningSummaryDetailVo;
import com.base.sbc.module.sample.vo.SampleUserVo;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.vo.ChartBarVo;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 类描述：企划-坑位信息 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningCategoryItemService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-3-31 13:40:49
 */
public interface PlanningCategoryItemService extends BaseService<PlanningCategoryItem> {
    @Transactional(readOnly = false)
    public boolean delByPlanningCategory(String companyCode,List<String> ids);
    public boolean delByPlanningBand(String userCompany, String id);

    String selectMaxDesignNo(QueryWrapper qc);




    /**
     * 获取下一个编码
     *
     * @return
     */
    String getNextCode(Object obj);

    /**
     * 获取下一个编码批量
     *
     * @param count 生成数量
     * @return
     */
    List<String> getNextCode(Object obj, int count);

    /**
     * 修改/提交
     *
     * @param item
     */
    void updateCategoryItem(List<PlanningCategoryItemSaveDto> item);

    /**
     * 获取最大流水号
     *
     * @param data
     * @param userCompany
     * @return
     */
    String getMaxDesignNo(GetMaxCodeRedis data, String userCompany);

    /**
     * 获取最大流水号
     *
     * @param data
     * @param userCompany
     * @return
     */
    String getStyleMaxOldDesignNo(GetMaxCodeRedis data, String userCompany);

    /**
     * 分配设计师
     *
     * @param dtoList
     * @return
     */
    boolean allocationDesign(List<AllocationDesignDto> dtoList);

    /**
     * 设置任务等级
     * @param dtoList
     * @return
     */
    boolean setTaskLevel(List<SetTaskLevelDto> dtoList);

    /**
     * 设置任务等级
     * @param dtoList
     * @return
     */
    boolean setSeries(List<SetSeriesDto> dtoList);

    /**
     * 查找坑位信息
     *
     * @param dto
     * @return
     */
    PageInfo<PlanningSeasonOverviewVo> findProductCategoryItem(ProductCategoryItemSearchDto dto);


    /**
     * 坑位信息下发
     *
     * @param categoryItemList
     * @return
     */
    ApiResult send(List<SeatSendDto> categoryItemList);

    /**
     * 查询坑位信息的维度数据
     * @param id
     * @param isSelected
     * @param categoryFlag 用于查询品类或中类 0品类1中类
     * @return
     */
    List<FieldManagementVo> querySeatDimension(String id, String isSelected,String categoryFlag);

    /**
     * 修改图片
     *
     * @param id
     * @param stylePic
     * @return
     */
    boolean updateStylePic(String id, String stylePic);

    boolean updateItemBatch(PlanningCategoryItemBatchUpdateDto dto);

    List<SampleUserVo> getAllDesigner(String userCompany);

    /**
     * 获取坑位数据所有工艺员
     * @param userCompany
     * @return
     */
    List<SampleUserVo> getPatternTechnician(String userCompany);


    /**
     * 维度统计
     *
     * @param qw@return
     */
    List<DimensionTotalVo> dimensionTotal(QueryWrapper qw);

    List<PlanningSummaryDetailVo> planningSummaryDetail(QueryWrapper detailQw);

    List<ChartBarVo> bandSummary(QueryWrapper qw);

    /**
     * 坑位信息下发(坑位信息下发到产品季总览)
     *
     * @param list 坑位信息列表
     * @return
     */
    boolean seatSend(List<PlanningCategoryItemSaveDto> list);


    Map<String, Long> totalSkcByPlanningSeason(List<String> planningSeasonIds);

    Map<String, Long> totalSkcByChannel(List<String> channelIds);

    /**
     * 新建坑位
     *
     * @param dto
     * @return
     */
    boolean addSeat(AddSeatDto dto);

    List<BasicStructureTreeVo> categoryTree(String planningChannelId);

    boolean revoke(String ids);

    boolean del(String id);

    List<String> selectCategoryIdsByBand(QueryWrapper qw);

    /**
     * 按品类展开
     *
     * @param dto
     * @return
     */
    List<BasicStructureTreeVo> expandByCategory(ProductSeasonExpandByCategorySearchDto dto);

    void updateBySampleDesignChange(Style style);

    Map<String, Long> totalBandSkcByPlanningSeason(String planningSeasonId);

    void updateByChannelChange(PlanningChannel planningChannel);

    String getStylePicUrlById(String id);

    ApiResult importPlanningExcel(MultipartFile file, String planningChannelId) throws Exception;


}
