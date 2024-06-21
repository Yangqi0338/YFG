/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.formtype.dto.QueryFieldManagementDto;
import com.base.sbc.module.planning.dto.DimensionLabelsSearchDto;
import com.base.sbc.module.planning.dto.UpdateDimensionalityDto;
import com.base.sbc.module.planning.entity.PlanningDimensionality;
import com.base.sbc.module.planning.vo.DimensionalityListVo;
import com.base.sbc.module.planning.vo.PlanningDimensionalityVo;

import java.util.List;

/**
 * 类描述：企划-维度表 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningDimensionalityService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-27 11:15:30
 */
public interface PlanningDimensionalityService extends BaseService<PlanningDimensionality> {

    /**
     * 自定义方法区 不替换的区域【other_start】
     **/

    DimensionalityListVo getDimensionalityList(DimensionLabelsSearchDto DimensionLabelsSearchDto);

    ApiResult getFormDimensionality(DimensionLabelsSearchDto DimensionLabelsSearchDto);


    Boolean saveBatchDimensionality(List<UpdateDimensionalityDto> list);

    ApiResult delDimensionality(String id,String sortId);


    ApiResult saveDimensionality(UpdateDimensionalityDto updateDimensionalityDto);

    /**
     * 批量保存修改
     * @param dimensionalityDtoList
     * @return
     */
    List<PlanningDimensionality>  batchSaveDimensionality(List<UpdateDimensionalityDto> dimensionalityDtoList);


    /**
     * 修改排序
     * @param queryFieldManagementDto
     * @return
     */
    Boolean regulateSort(QueryFieldManagementDto queryFieldManagementDto);

    /**
     *获取围度系数数据
     * @param dto
     * @return
     */
    List<PlanningDimensionalityVo>  getCoefficient(DimensionLabelsSearchDto dto);


    /**
     * 系数模板引用
     * @param dto
     * @return
     */
   boolean templateReference(DimensionLabelsSearchDto dto);
    List<PlanningDimensionality> copyDimensionality(DimensionLabelsSearchDto dimensionLabelsSearchDto);

    List<PlanningDimensionalityVo> getMaterialCoefficient(DimensionLabelsSearchDto queryDemandDimensionalityDto);

    List<PlanningDimensionality> batchSaveMaterial(List<UpdateDimensionalityDto> dimensionalityDtoList);


/** 自定义方法区 不替换的区域【other_end】 **/


}
