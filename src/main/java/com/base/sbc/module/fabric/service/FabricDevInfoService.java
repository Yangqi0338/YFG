/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.fabric.entity.FabricDevInfo;
import com.base.sbc.module.fabric.vo.FabricDevConfigInfoVO;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;

/**
 * 类描述：面料开发信息 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricDevInfoService
 * @email your email
 * @date 创建时间：2023-8-17 9:57:39
 */
public interface FabricDevInfoService extends BaseService<FabricDevInfo> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 获取开发信息
     *
     * @param devApplyCode
     * @return
     */
    FabricDevConfigInfoVO getByDevConfigId(String devConfigId, String devApplyCode);


    /**
     * 通过开发主信息id获取
     *
     * @param devMainId
     * @return
     */
    PageInfo<FabricDevConfigInfoVO> getByDevMainId(String devMainId, Integer pageNum, Integer pageSize);

    /**
     * 保存
     *
     * @param devConfigId
     * @param expectStartDate
     * @param expectEndDate
     */
    void saveDevInfo(String devConfigId, Date expectStartDate, Date expectEndDate, String devMainId);


    /**
     * 更新附件
     *
     * @param id
     * @param attachmentUrl
     */
    void updateFile(String id, String attachmentUrl);

    /**
     * 更新状态
     *
     * @param devId
     * @param status
     */
    void updateStatus(String devId, String status);

    /**
     * 获取全部通过数据
     *
     * @param devMainId
     * @return
     */
    List<FabricDevInfo> getAllPass(String devMainId);

// 自定义方法区 不替换的区域【other_end】


}
