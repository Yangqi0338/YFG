/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.fabric.dto.BasicFabricLibrarySaveDTO;
import com.base.sbc.module.fabric.dto.BasicFabricLibrarySearchDTO;
import com.base.sbc.module.fabric.entity.BasicFabricLibrary;
import com.base.sbc.module.fabric.vo.BasicFabricLibraryListVO;
import com.base.sbc.module.fabric.vo.BasicFabricLibraryVO;
import com.base.sbc.module.fabric.vo.FabricDevMainVO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 类描述：基础面料库 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.BasicFabricLibraryService
 * @email your email
 * @date 创建时间：2023-8-17 9:57:23
 */
public interface BasicFabricLibraryService extends BaseService<BasicFabricLibrary> {

    // 自定义方法区 不替换的区域【other_start】

    /**
     * 保存
     *
     * @param fabricDevMainVO
     */
    void saveBasicFabric(FabricDevMainVO fabricDevMainVO);

    /**
     * 获取列表
     *
     * @param basicFabricLibrarySearchDTO
     * @return
     */
    PageInfo<BasicFabricLibraryListVO> getBasicFabricLibraryList(BasicFabricLibrarySearchDTO basicFabricLibrarySearchDTO);

    /**
     * 获取详情
     *
     * @return
     */
    BasicFabricLibraryVO getDetail(String id);

    /**
     * 更新
     *
     * @param dto
     */
    void update(BasicFabricLibrarySaveDTO dto);

    /**
     * 生成物料档案
     *
     * @param id
     */
    void generateMaterial(String id);

    /**
     * 转至物料审核处理
     *
     * @param materialId
     */
    void materialApproveProcessing(String materialId, String approveStatus, String materialCode);

    /**
     * 通过物料编码获取
     *
     * @param materialCodes
     * @return
     */
    Map<String ,BasicFabricLibraryListVO> getByMaterialCodes(List<String> materialCodes);


// 自定义方法区 不替换的区域【other_end】


}
