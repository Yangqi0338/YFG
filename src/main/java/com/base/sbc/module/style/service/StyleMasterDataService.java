/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.style.dto.StyleMasterDataSaveDto;
import com.base.sbc.module.style.entity.StyleMasterData;
import com.base.sbc.module.style.vo.StyleMasterDataVo;

/** 
 * 类描述：款式主数据 service类
 * @address com.base.sbc.module.style.service.StyleMasterDataService
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-9-7 13:57:43
 * @version 1.0  
 */
public interface StyleMasterDataService extends BaseService<StyleMasterData> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 生成款式主数据
     *
     * @param styleId 款式id
     * @return
     */
    Boolean createStyleMasterData(String styleId);


    /** 查询明细
     * @param id
     * @return
     */
    StyleMasterDataVo getDetail(String id);


    /**
     * 保存修改款式主数据
     *
     * @param dto
     * @return
     */
    StyleMasterDataVo saveStyle(StyleMasterDataSaveDto dto);


    /**
     * 通过款式id 获取
     *
     * @param styleId
     * @return
     */
    StyleMasterDataVo getByStyleId(String styleId);
// 自定义方法区 不替换的区域【other_end】


}
