/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sampleFlow.service;

import com.base.sbc.module.common.service.IServicePlus;
import com.base.sbc.module.sampleFlow.entity.SampleFlow;
import com.base.sbc.module.sampleFlow.vo.SampleFlowVo;

import java.util.List;

/**
 * 类描述：样衣流程 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.sampleFlow.service.SampleFlowService
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-9 10:26:38
 */
public interface SampleFlowService extends IServicePlus<SampleFlow> {

/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 方法描述：查询样衣设计下流程
     *
     * @param sampleId 样衣id
     * @return List<SampleFlowVo>
     */
    List<SampleFlowVo> getFlowList(String sampleId);


    /**
     * 方法描述：完成流程节点
     *
     * @param flowId 流程id
     * @return boolean
     */
    boolean accomplishFlow(String flowId);

    /**
     * 方法描述：传入样衣id完成当前走到的流程节点
     *
     * @param sampleId 样衣id
     * @return boolean
     */
    boolean accomplishAdjFlow(String sampleId);

    /**
     * 方法描述：驳回节点
     *
     * @param flowId 流程id
     * @return boolean
     */
    boolean rejectFlow(String flowId);



    /**
     * 方法描述：新增流程
     *
     * @param sampleId 样衣id
     * @return boolean
     */
    boolean addFlow(String sampleId);

/** 自定义方法区 不替换的区域【other_end】 **/


}
