/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pokaYoke.service;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.pokaYoke.entity.PokaYokeConfig;
import com.base.sbc.module.pokaYoke.dto.PokaYokeConfigQueryDto;
import com.base.sbc.module.pokaYoke.vo.PokaYokeConfigVo;
import com.github.pagehelper.PageInfo;

/** 
 * 类描述：防呆防错配置 service类
 * @address com.base.sbc.module.pokaYoke.service.PokaYokeConfigService
 * @author your name
 * @email your email
 * @date 创建时间：2024-7-17 14:23:30
 * @version 1.0  
 */
public interface PokaYokeConfigService extends BaseService<PokaYokeConfig>{

    PageInfo<PokaYokeConfigVo> queryPage(PokaYokeConfigQueryDto dto);

    Boolean savePokaYokeConfig(PokaYokeConfigVo vo);

    Boolean updatePokaYokeConfig(PokaYokeConfigVo vo);

    Boolean del(String id);

    String queryCondition(PokaYokeConfigQueryDto dto);


// 自定义方法区 不替换的区域【other_start】


// 自定义方法区 不替换的区域【other_end】

	
}