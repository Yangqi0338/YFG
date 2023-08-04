/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;

import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.formType.vo.FieldManagementVo;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.module.planning.dto.PlanningBoardSearchDto;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningChannel;
import com.base.sbc.module.planning.vo.PlanningSummaryVo;
import com.base.sbc.module.planning.vo.ProductCategoryTreeVo;
import com.base.sbc.module.sample.dto.*;
import com.base.sbc.module.sample.entity.SampleDesign;
import com.base.sbc.module.sample.vo.*;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 类描述：样衣设计 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.sample.service.SampleService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-9 11:16:15
 */
public interface SampleDesignService extends BaseService<SampleDesign> {


/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 保存样衣
     *
     * @param dto
     * @return
     */
    SampleDesign saveSampleDesign(SampleDesignSaveDto dto);

    /**
     * 新增样衣设计
     * @param dto
     * @return
     */
    SampleDesign saveNewSampleDesign(SampleDesignSaveDto dto);

    /**
     * 分页查询
     * @param dto
     * @return
     */
    PageInfo queryPageInfo(SampleDesignPageDto dto);

    /**
     * 查询样衣设计及款式配色
     * @param dto
     * @return
     */
    PageInfo sampleSampleStyle(SampleDesignPageDto dto);
    /**
     * 发起审批
     * @param id
     * @return
     */
    boolean startApproval(String id);

    /**
     * 处理审批回调
     * @param dto
     * @return
     */
    boolean approval(AnswerDto dto);

    /**
     * 发送打板指令
     * @param dto
     * @return
     */
    boolean sendMaking(SendSampleMakingDto dto);

    SampleDesign checkedSampleDesignExists(String id);

    /**
     * 查询明细数据
     * @param id
     * @return
     */
    SampleDesignVo getDetail(String id);

    /**
     * 设计档案左侧树（0级:年份季节,1级:波段,2级:大类,3级:品类）
     * @param designDocTreeVo
     * @return
     */
    List<DesignDocTreeVo> queryDesignDocTree(DesignDocTreeVo designDocTreeVo);

    /**
     * 查询维度标签
     *
     * @param dto
     * @return
     */
    List<FieldManagementVo> queryDimensionLabels(DimensionLabelsSearchDto dto);

    /**
     * 查询维度标签
     *
     * @param id 样衣设计id
     * @return
     */
    List<FieldManagementVo> queryDimensionLabelsBySdId(String id);

    List<SampleUserVo> getDesignerList(String companyCode);

    /**
     * 产品季总览-波段汇总统计图表
     *
     * @param month
     * @return
     */
    List getBandChart(String month);

    List getCategoryChart(String category);

    Map getDesignDataOverview(String time);


    PlanningSummaryVo categoryBandSummary(PlanningBoardSearchDto dto);

    List<StyleBoardCategorySummaryVo> categorySummary(PlanningBoardSearchDto dto);

    CategoryStylePlanningVo categoryStylePlanning(PlanningBoardSearchDto dto);

    List<ProductCategoryTreeVo> getProductCategoryTree(ProductCategoryTreeVo vo);


    void updateBySeatChange(PlanningCategoryItem item);

    void updateByChannelChange(PlanningChannel planningChannel);

    PageInfo<PackBomVo> bomList(SampleDesignBomSearchDto dto);

    Boolean delBom(String id);

    Boolean saveBom(SampleDesignBomSaveDto dto);
/** 自定义方法区 不替换的区域【other_end】 **/


}

