/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumBrandSeasonDto;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumBrandSeasonVo;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumBrandSeason;
import com.github.pagehelper.PageInfo;

/** 
 * 类描述：品牌-季度表 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumBrandSeasonService
 * @author 谭博文
 * @email your email
 * @date 创建时间：2024-4-9 9:42:49
 * @version 1.0  
 */
public interface BasicsdatumBrandSeasonService extends BaseService<BasicsdatumBrandSeason>{

// 自定义方法区 不替换的区域【other_start】

    /**
     * 新增和更新 BasicsdatumBrandSeason
     *
     * @param basicsdatumBrandSeasonDto
     * @return
     */
    ApiResult addAndUpdateBasicsdatumBrandSeason(BasicsdatumBrandSeason basicsdatumBrandSeasonDto);

    /**
     * 分页查询
     */
    PageInfo<BasicsdatumBrandSeason> queryPage(BasicsdatumBrandSeasonDto basicsdatumBrandSeasonDto);

// 自定义方法区 不替换的区域【other_end】

	
}
