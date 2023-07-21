/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.common.dto.AdTree;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.common.vo.SelectOptionsVo;
import com.base.sbc.module.planning.dto.PlanningBoardSearchDto;
import com.base.sbc.module.planning.dto.PlanningSeasonSaveDto;
import com.base.sbc.module.planning.dto.PlanningSeasonSearchDto;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.vo.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 类描述：企划-产品季 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningSeasonService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-3-27 17:42:08
 */
public interface PlanningSeasonService extends BaseService<PlanningSeason> {

    /**
     * 删除产品季
     */
    public boolean del(String companyCode, String id);

    /**
     * 查询产品季( 产品季总览)
     * @param qw
     * @return
     */
    List<PlanningSeason> selectProductSeason(QueryWrapper qw);

    List<PlanningSeason> queryYs(String companyCode);

    /**
     * 校验名称重复
     * @param dto
     * @param userCompany
     */
    void checkNameRepeat(PlanningSeasonSaveDto dto, String userCompany);

    /**
     * 保存产品季
     * @param dto
     * @return
     */
    PlanningSeason savePlanningSeason(PlanningSeasonSaveDto dto);

    /**
     * 通过名称获取
     *
     * @param name
     * @return
     */
    PlanningSeason getByName(String name, String userCompany);

    /**
     * 产品季分页查询
     * @param dto
     * @param userCompany
     * @return
     */
    PageInfo<PlanningSeasonVo> queryPlanningSeasonPageInfo(PlanningSeasonSearchDto dto, String userCompany);

    /**
     * 检查产品季是否有波段企划
     * @param id
     */
    boolean checkPlanningSeasonHasBand(String id);

    /**
     * 获取企业和产品季的树形结构
     * @return
     */
    List<AdTree> getTree();

    /**
     * 分页查询
     * @param dto
     * @param userCompany
     * @return
     */
    PageInfo queryByPage(PlanningSeasonSearchDto dto, String userCompany);

    /**
     * 校验重复
     * @param dto
     * @param userCompany
     */
    void checkBYSRepeat(PlanningSeasonSaveDto dto, String userCompany);

    /**
     * 删除产品季
     * @param id
     * @return
     */
    boolean delPlanningSeason(String id);

    /**
     * 通过名称获取
     *
     * @param name
     * @return
     */
    PlanningSeasonVo getByName(String name);

    List<SelectOptionsVo> getPlanningSeasonOptions(String userCompany);

    /**
     * 企划汇总
     *
     * @param dto 筛选条件
     * @return
     */
    PlanningSummaryVo planningSummary(PlanningBoardSearchDto dto);

    /**
     * 品类汇总
     *
     * @param dto
     * @return
     */
    List categorySummary(PlanningBoardSearchDto dto);

    /**
     * 历史款明细
     *
     * @param hisDesignNo
     * @return
     */
    PlanningSummaryDetailVo hisDetail(String hisDesignNo);


    /**
     * 获取产品季品类树
     */
    List<ProductCategoryTreeVo> getProductCategoryTree(String userCompany);

    List<YearBrandVo> queryYearBrandTree(String search);
}
