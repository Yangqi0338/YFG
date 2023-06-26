/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formType.service;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.formType.dto.FormDeleteDto;
import com.base.sbc.module.formType.dto.FormStartStopDto;
import com.base.sbc.module.formType.dto.QueryFormTypeDto;
import com.base.sbc.module.formType.dto.SaveUpdateFormTypeDto;
import com.base.sbc.module.formType.entity.FormType;
import com.base.sbc.module.formType.vo.PagingFormTypeVo;
import com.github.pagehelper.PageInfo;

/** 
 * 类描述：表单类型 service类
 * @address com.base.sbc.module.formType.service.FormTypeService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-15 9:17:01
 * @version 1.0  
 */
public interface FormTypeService extends BaseService<FormType> {

    /**
     * 表单类型及分组 启动 停止
     */
    ApiResult formStartStop(FormStartStopDto formStartStopDto);

    /*
     * 保存修改表单类型
     * */
    ApiResult  saveUpdateType(SaveUpdateFormTypeDto saveUpdateFormTypeDto);

    /*
     * 分页获取表单类型及分组
     * */
    PageInfo<PagingFormTypeVo>  getFormTypeIsGroup(QueryFormTypeDto queryFormTypeDto);

    /**
     * 表单类型及分组 删除
     */
    ApiResult formDelete(FormDeleteDto formDeleteDto);

    /*h*/
    ApiResult getFormType(QueryFormTypeDto queryFormTypeDto);
}
