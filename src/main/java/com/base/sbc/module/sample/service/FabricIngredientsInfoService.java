/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;

import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.sample.dto.AddRevampFabricIngredientsInfoDto;
import com.base.sbc.module.sample.dto.QueryFabricIngredientsInfoDto;
import com.base.sbc.module.sample.entity.FabricIngredientsInfo;
import com.base.sbc.module.sample.vo.FabricIngredientsInfoVo;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：调样-辅料信息 service类
 * @address com.base.sbc.module.sample.service.FabricIngredientsInfoService
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-7-14 17:32:38
 * @version 1.0
 */
public interface FabricIngredientsInfoService extends BaseService<FabricIngredientsInfo> {

// 自定义方法区 不替换的区域【other_start】

        /**
        * 方法描述：分页查询部件
        *
        * @param queryFabricIngredientsInfoDto 查询条件
        * @return PageInfo<BasicsdatumComponentVo>
         */
        PageInfo getFabricIngredientsInfoList(QueryFabricIngredientsInfoDto queryFabricIngredientsInfoDto);




        /**
        * 方法描述：新增修改调样-辅料信息
        *
        * @param addRevampFabricIngredientsInfoDto 部件Dto类
        * @return boolean
        */
        Boolean addRevampFabricIngredientsInfo(AddRevampFabricIngredientsInfoDto addRevampFabricIngredientsInfoDto);



        /**
        * 方法描述：删除调样-辅料信息
        *
        * @param id （多个用，）
        * @return boolean
        */
        Boolean delFabricIngredientsInfo(RemoveDto removeDto);



        /**
        * 方法描述：启用停止调样-辅料信息
        *
        * @param startStopDto 启用停止Dto类
        * @return boolean
        */
        Boolean startStopFabricIngredientsInfo( StartStopDto startStopDto);


        /**
         * 辅料导出
         * @param response
         * @param queryFabricIngredientsInfoDto
         */
        void fabricIngredientsInfoDeriveExcel(HttpServletResponse response, QueryFabricIngredientsInfoDto queryFabricIngredientsInfoDto) throws IOException;

// 自定义方法区 不替换的区域【other_end】


}
