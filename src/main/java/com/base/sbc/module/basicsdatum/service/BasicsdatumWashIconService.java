/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumWashIconDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumWashIcon;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumWashIconVo;
import com.base.sbc.module.common.service.BaseService;
import com.github.pagehelper.PageInfo;

/** 
 * 类描述：基础资料-洗涤图标 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumWashIconService
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-7-27 17:27:54
 * @version 1.0  
 */
public interface BasicsdatumWashIconService extends BaseService<BasicsdatumWashIcon> {

// 自定义方法区 不替换的区域【other_start】

        /**
        * 方法描述：分页查询部件
        *
        * @param queryDto 查询条件
        * @return PageInfo<BasicsdatumComponentVo>
         */
        PageInfo<BasicsdatumWashIconVo> getBasicsdatumWashIconList(QueryDto queryDto);




        /**
        * 方法描述：新增修改基础资料-洗涤图标
        *
        * @param addRevampBasicsdatumWashIconDto 部件Dto类
        * @return boolean
        */
        Boolean addRevampBasicsdatumWashIcon(AddRevampBasicsdatumWashIconDto addRevampBasicsdatumWashIconDto);



        /**
        * 方法描述：删除基础资料-洗涤图标
        *
        * @param id （多个用，）
        * @return boolean
        */
        Boolean delBasicsdatumWashIcon(String id);



        /**
        * 方法描述：启用停止基础资料-洗涤图标
        *
        * @param startStopDto 启用停止Dto类
        * @return boolean
        */
        Boolean startStopBasicsdatumWashIcon( StartStopDto startStopDto);


// 自定义方法区 不替换的区域【other_end】

	
}
