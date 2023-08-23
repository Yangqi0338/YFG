/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service;

import com.base.sbc.config.common.base.Page;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.fabric.dto.DelDTO;
import com.base.sbc.module.fabric.dto.EnableOrDeactivateDTO;
import com.base.sbc.module.fabric.dto.FabricDevConfigInfoSaveDTO;
import com.base.sbc.module.fabric.entity.FabricDevConfigInfo;
import com.base.sbc.module.fabric.vo.FabricDevConfigInfoListVO;
import com.base.sbc.module.fabric.vo.FabricDevConfigInfoVO;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：面料开发配置信息 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricDevConfigInfoService
 * @email your email
 * @date 创建时间：2023-8-7 11:01:33
 */
public interface FabricDevConfigInfoService extends BaseService<FabricDevConfigInfo> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 分页获取
     *
     * @param page
     * @return
     */
    PageInfo<FabricDevConfigInfoListVO> getDevConfigList(Page page);

    /**
     * 保存
     *
     * @param dto
     */
    String devConfigSave(FabricDevConfigInfoSaveDTO dto);

    /**
     * 启用停用
     *
     * @param dto
     */
    void enableOrDeactivate(EnableOrDeactivateDTO dto);

    /**
     * 删除
     *
     * @param dto
     */
    void del(DelDTO dto);


    /**
     * 获取开发申请配置信息
     *
     * @param devApplyCode
     * @return
     */
    PageInfo<FabricDevConfigInfoVO> getDevApplyConfigList(String devApplyCode, Integer pageNum, Integer pageSize);

    /**
     * 获取开发信息配置
     *
     * @param devCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<FabricDevConfigInfoVO> getDevConfigList(String devCode, Integer pageNum, Integer pageSize);


// 自定义方法区 不替换的区域【other_end】


}
