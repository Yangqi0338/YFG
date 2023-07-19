/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.sample.dto.PreProductionSampleDto;
import com.base.sbc.module.sample.dto.PreProductionSampleSearchDto;
import com.base.sbc.module.sample.dto.PreProductionSampleTaskDto;
import com.base.sbc.module.sample.entity.PreProductionSample;
import com.base.sbc.module.sample.vo.PreProductionSampleTaskDetailVo;
import com.base.sbc.module.sample.vo.PreProductionSampleTaskVo;
import com.base.sbc.module.sample.vo.PreProductionSampleVo;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：产前样 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.sample.service.PreProductionSampleService
 * @email your email
 * @date 创建时间：2023-7-18 11:04:06
 */
public interface PreProductionSampleService extends BaseService<PreProductionSample> {

// 自定义方法区 不替换的区域【other_start】


    /**
     * 通过资料包id获取
     *
     * @param packInfoId
     * @return
     */
    PreProductionSample getByPackInfoId(String packInfoId);

    /**
     * 通过资料包创建
     *
     * @param packInfoId
     * @return
     */
    boolean createByPackInfo(String packInfoId);

    /**
     * 通过资料包创建
     *
     * @param packInfo
     * @return
     */
    boolean createByPackInfo(PackInfo packInfo);

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    PageInfo<PreProductionSampleVo> pageInfo(PreProductionSampleSearchDto dto);

    /**
     * 保存
     *
     * @param dto
     * @return
     */
    boolean saveByDto(PreProductionSampleDto dto);

    /**
     * 保存任务
     *
     * @param dto
     * @return
     */
    PreProductionSampleTaskVo saveTaskByDto(PreProductionSampleTaskDto dto);

    /**
     * 获取任务明细
     *
     * @param id
     * @return
     */
    PreProductionSampleTaskDetailVo getTaskDetailById(String id);

// 自定义方法区 不替换的区域【other_end】


}
