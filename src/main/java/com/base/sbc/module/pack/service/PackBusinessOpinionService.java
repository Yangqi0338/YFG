/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.pack.dto.PackBusinessOpinionDto;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.entity.PackBusinessOpinion;
import com.base.sbc.module.pack.vo.PackBusinessOpinionVo;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：资料包-业务意见 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackBusinessOpinionService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-5 11:09:08
 */
public interface PackBusinessOpinionService extends BaseService<PackBusinessOpinion> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    PageInfo<PackBusinessOpinionVo> pageInfo(PackCommonPageSearchDto dto);

    /**
     * 获取明细
     *
     * @param id
     * @return
     */
    PackBusinessOpinionVo getDetail(String id);

    /**
     * 保存
     *
     * @param dto
     * @return
     */
    PackBusinessOpinionVo saveByDto(PackBusinessOpinionDto dto);


// 自定义方法区 不替换的区域【other_end】


}
