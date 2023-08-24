/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service;

import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.fabric.dto.FabricPlanningSearchDTO;
import com.base.sbc.module.fabric.dto.FabricPoolSaveDTO;
import com.base.sbc.module.fabric.entity.FabricPool;
import com.base.sbc.module.fabric.vo.FabricPoolListVO;
import com.base.sbc.module.fabric.vo.FabricPoolVO;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：面料池 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricPoolService
 * @email your email
 * @date 创建时间：2023-8-23 11:02:50
 */
public interface FabricPoolService extends BaseService<FabricPool> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 获取列表
     *
     * @param dto
     * @return
     */
    PageInfo<FabricPoolListVO> getFabricPoolList(FabricPlanningSearchDTO dto);

    /**
     * 保存
     *
     * @param dto
     */
    void save(FabricPoolSaveDTO dto);


    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    FabricPoolVO getDetail(String id);

    /**
     * 审核处理
     *
     * @param dto
     * @return
     */
    boolean approval(AnswerDto dto);


// 自定义方法区 不替换的区域【other_end】


}
