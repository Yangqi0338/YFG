/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formtype.service;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.formtype.dto.QueryFieldManagementDto;
import com.base.sbc.module.formtype.dto.SaveUpdateFieldManagementDto;
import com.base.sbc.module.formtype.entity.FieldManagement;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.planning.dto.QueryDemandDto;
import com.github.pagehelper.PageInfo;

import java.util.List;

/** 
 * 类描述：字段管理表 service类
 * @address com.base.sbc.module.formType.service.FieldManagementService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-15 18:33:51
 * @version 1.0  
 */
public interface FieldManagementService extends BaseService<FieldManagement> {

    /**
     * 自定义方法区 不替换的区域【other_start】
     **/
    ApiResult saveUpdateField(SaveUpdateFieldManagementDto saveUpdateFieldManagementDto);


    PageInfo<FieldManagementVo> getFieldManagementList(QueryFieldManagementDto queryFieldManagementDto);

    /**
     * 查询维度-字段有配置的选项
     * @param queryDemandDto
     * @return
     */
    List<FieldManagementVo> getFieldConfigList(QueryDemandDto queryDemandDto);

    ApiResult adjustmentOrder(QueryFieldManagementDto queryFieldManagementDto);

    List<FieldManagementVo> getFieldManagementListByIds(List<String> ids,String planningSeasonId,String prodCategory,String channel);

    List<FieldManagementVo> getFieldManagementListMapper(QueryFieldManagementDto dto);

    List<FieldManagementVo> getFieldManagementList1Mapper(QueryFieldManagementDto dto);

    /**
     * 通过表名，品类，季节查询
     *
     * @param formName
     * @param categoryId
     * @param season
     * @return
     */
    List<FieldManagementVo> list(String code, String categoryId, String season);


    /**
     * 设置val
     *
     * @param fieldList
     * @param valueList
     * @return
     */
    void conversion(List<FieldManagementVo> fieldList, List<FieldVal> valueList);

    /**
     * 删除字段
     * @param id
     * @return
     */
    Boolean  removeById(String id);


    /**
     * 获取表单中的字段
     * @param dto
     * @return
     */
    PageInfo<FieldManagementVo> getFieldListByFormCode(QueryFieldManagementDto dto);


/** 自定义方法区 不替换的区域【other_end】 **/


}
