/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumCategoryMeasureDto;
import com.base.sbc.module.basicsdatum.dto.QueryCategoryMeasureDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumCategoryMeasure;
import com.base.sbc.module.common.service.BaseService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** 
 * 类描述：基础资料-品类测量组 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumCategoryMeasureService
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-20 19:08:55
 * @version 1.0  
 */
public interface BasicsdatumCategoryMeasureService extends BaseService<BasicsdatumCategoryMeasure> {

/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 方法描述：分页查询部件
        *
        * @param queryDto 查询条件
        * @return PageInfo<BasicsdatumComponentVo>
         */
        PageInfo getBasicsdatumCategoryMeasureList(QueryCategoryMeasureDto queryDto);


        /**
        * 基础资料-品类测量组导入
        * @param file
        * @return
        */
        Boolean basicsdatumCategoryMeasureImportExcel(MultipartFile file) throws IOException, Exception;

        /**
        * 基础资料-品类测量组导出
        * @param response
        * @return
        */
        void basicsdatumCategoryMeasureDeriveExcel(HttpServletResponse response,QueryCategoryMeasureDto queryDto) throws Exception;


        /**
        * 方法描述：新增修改基础资料-品类测量组
        *
        * @param addRevampBasicsdatumCategoryMeasureDto 部件Dto类
        * @return boolean
        */
        BasicsdatumCategoryMeasure addRevampBasicsdatumCategoryMeasure(AddRevampBasicsdatumCategoryMeasureDto addRevampBasicsdatumCategoryMeasureDto);



        /**
        * 方法描述：删除基础资料-品类测量组
        *
        * @param id （多个用，）
        * @return boolean
        */
        Boolean delBasicsdatumCategoryMeasure(String id);



        /**
        * 方法描述：启用停止基础资料-品类测量组
        *
        * @param startStopDto 启用停止Dto类
        * @return boolean
        */
        Boolean startStopBasicsdatumCategoryMeasure( StartStopDto startStopDto);


/** 自定义方法区 不替换的区域【other_end】 **/

	
}

