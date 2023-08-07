/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.module.basicsdatum.dto.AddRevampMeasurementDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMeasurement;
import com.base.sbc.module.common.service.BaseService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/** 
 * 类描述：基础资料-测量点 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumMeasurementService
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-17 9:35:14
 * @version 1.0  
 */
public interface BasicsdatumMeasurementService extends BaseService<BasicsdatumMeasurement> {


    /**
     * 方法描述：分页查询测量点
     *
     * @param queryDto 查询条件
     * @return PageInfo<BasicsdatumTechnologyVo>
     */
    PageInfo getMeasurement(QueryDto queryDto);


    /**
     * 方法描述：传入Excel导入
     *
     * @param file 文件
     * @return boolean
     */
    Boolean importExcel(MultipartFile file) throws Exception;



    /**
     * 基础资料-测量点导出
     * @param response
     * @return
     */
    void deriveExcel(HttpServletResponse response) throws Exception;

    /**
     * 方法描述：新增修改基础资料-部件
     *
     * @param addRevampMeasurementDto 部件Dto类
     * @return boolean
     */
    Boolean addRevampMeasurement(AddRevampMeasurementDto addRevampMeasurementDto);

    /**
     * 方法描述：删除部件数据
     *
     * @param id 部件id （多个用，）
     * @return boolean
     */
    Boolean delMeasurement(String id);

    /**
     * 方法描述：启用停止
     *
     * @param startStopDto 启用停止Dto类
     * @return boolean
     */
    Boolean measurementStartStop( StartStopDto startStopDto);


	
}
