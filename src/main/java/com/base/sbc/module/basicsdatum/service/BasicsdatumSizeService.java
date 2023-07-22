/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.module.basicsdatum.dto.AddRevampSizeDto;
import com.base.sbc.module.basicsdatum.dto.QueryDasicsdatumSizeDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSize;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumSizeVo;
import com.base.sbc.module.common.service.BaseService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/** 
 * 类描述：基础资料-尺码表 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumSizeService
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-17 14:01:34
 * @version 1.0  
 */
public interface BasicsdatumSizeService extends BaseService<BasicsdatumSize> {

    /**
     * 查询尺码列表
     * @param queryDasicsdatumSizeDto
     * @return
     */
    PageInfo<BasicsdatumSizeVo> getSizeList(QueryDasicsdatumSizeDto queryDasicsdatumSizeDto);

    /**
     * 尺码导入
     * @param file
     * @return
     */
    Boolean importExcel(MultipartFile file) throws IOException, Exception;

    /**
     * 导出
     * @param queryDasicsdatumSizeDto
     * isDerive 1导出，0模板导出,sizeLabelId 为空导出所有数据
     */
   void deriveExcel( QueryDasicsdatumSizeDto queryDasicsdatumSizeDto, HttpServletResponse response) throws IOException;


    /**
     * 新增修改尺码表
     * @param addRevampSizeDto
     * @return
     */
    Boolean addRevampSize(AddRevampSizeDto addRevampSizeDto);

    /**
     * 启动停止尺码
     * @param startStopDto
     * @return
     */
    Boolean sizeStartStop(StartStopDto startStopDto);

    /**
     * 删除
     * @param id
     * @return
     */
    Boolean delSize(String id);

    /**
     * 获取尺码名
     * @param sort
     * @return
     */
   Map<String,String>  getSizeName(String ids);

/** 自定义方法区 不替换的区域【other_end】 **/

	
}
