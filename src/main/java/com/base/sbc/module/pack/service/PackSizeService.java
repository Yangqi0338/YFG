/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackSizeConfigReferencesDto;
import com.base.sbc.module.pack.dto.PackSizeDto;
import com.base.sbc.module.pack.entity.PackSize;
import com.base.sbc.module.pack.vo.PackSizeVo;
import com.base.sbc.open.dto.OpenPackSizeDto;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 类描述：资料包-尺寸表 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackSizeService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-1 10:14:51
 */
public interface PackSizeService extends PackBaseService<PackSize> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    PageInfo<PackSizeVo> pageInfo(PackCommonPageSearchDto dto);

    /**
     * 保存
     *
     * @param dto
     * @return
     */
    PackSizeVo saveByDto(PackSizeDto dto);


    /**
     * 保存全部
     *
     * @param dto
     * @return
     */
    boolean saveBatchByDto(PackCommonSearchDto commonDto, List<PackSizeDto> dtoList);

    /**
     * 将尺寸表转为html table
     *
     * @return
     */
    void sizeToHtml(PackCommonSearchDto commonDto);

    boolean references(PackSizeConfigReferencesDto dto);


// 自定义方法区 不替换的区域【other_end】

    ApiResult openSaveBatch(OpenPackSizeDto openPackSizeDto);


}

