/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackProcessPriceDto;
import com.base.sbc.module.pack.entity.PackProcessPrice;
import com.base.sbc.module.pack.vo.PackProcessPriceVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 类描述：资料包-工序工价 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackProcessPriceService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-5 14:12:07
 */
public interface PackProcessPriceService extends BaseService<PackProcessPrice> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    PageInfo<PackProcessPriceVo> pageInfo(PackCommonPageSearchDto dto);

    /**
     * 获取明细
     *
     * @param id
     * @return
     */
    PackProcessPriceVo getDetail(String id);

    /**
     * 保存
     *
     * @param dto
     * @return
     */
    PackProcessPriceVo saveByDto(PackProcessPriceDto dto);

    /**
     * 保存全部
     *
     * @param commonDto
     * @param dtoList
     * @return
     */
    boolean saveBatchByDto(PackCommonSearchDto commonDto, List<PackProcessPriceDto> dtoList);

// 自定义方法区 不替换的区域【other_end】


}
