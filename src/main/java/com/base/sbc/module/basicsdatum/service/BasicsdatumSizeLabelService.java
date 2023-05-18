/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.module.basicsdatum.dto.AddRevampSizeLabelDto;
import com.base.sbc.module.basicsdatum.dto.QueryDasicsdatumSizeDto;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumSizeLabelVo;
import com.base.sbc.module.common.service.IServicePlus;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSizeLabel;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 类描述：基础资料-尺码标签 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumSizeLabelService
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-17 14:01:35
 */
public interface BasicsdatumSizeLabelService extends IServicePlus<BasicsdatumSizeLabel> {

    /**
     * @param queryDasicsdatumSizeDto
     * @return
     */
    List<BasicsdatumSizeLabelVo> getSizeLabelList(QueryDasicsdatumSizeDto queryDasicsdatumSizeDto);

    /**
     * 新增修改 尺码标签
     * @param addRevampSizeLabelDto
     * @return
     */
    Boolean addRevampSizeLabel(AddRevampSizeLabelDto addRevampSizeLabelDto);

    /**
     * 删除尺码标签
     * @param id
     * @return
     */
    Boolean delSizeLabel(String id);

}
