/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumColourGroupDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourGroup;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumColourGroupVo;
import com.base.sbc.module.common.service.BaseService;

import java.util.List;

/** 
 * 类描述：基础资料-颜色组 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumColourGroupService
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-20 20:23:03
 * @version 1.0  
 */
public interface BasicsdatumColourGroupService extends BaseService<BasicsdatumColourGroup> {

/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 方法描述：分页查询部件
        *
        * @param queryDto 查询条件
        * @return BasicsdatumColourGroupVo
         */
        List<BasicsdatumColourGroupVo> getBasicsdatumColourGroupList(QueryDto queryDto);



        /**
        * 方法描述：新增修改基础资料-颜色组
        *
        * @param addRevampBasicsdatumColourGroupDto 部件Dto类
        * @return boolean
        */
        Boolean addRevampBasicsdatumColourGroup(AddRevampBasicsdatumColourGroupDto addRevampBasicsdatumColourGroupDto);



        /**
        * 方法描述：删除基础资料-颜色组
        *
        * @param id （多个用，）
        * @return boolean
        */
        Boolean delBasicsdatumColourGroup(String id);



        /**
        * 方法描述：启用停止基础资料-颜色组
        *
        * @param startStopDto 启用停止Dto类
        * @return boolean
        */
        Boolean startStopBasicsdatumColourGroup( StartStopDto startStopDto);


/** 自定义方法区 不替换的区域【other_end】 **/

	
}
