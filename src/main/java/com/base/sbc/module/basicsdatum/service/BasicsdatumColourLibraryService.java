/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumColourLibraryDto;
import com.base.sbc.module.basicsdatum.dto.QueryBasicsdatumColourLibraryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.common.vo.SelectOptionsVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/** 
 * 类描述：基础资料-颜色库 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryService
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-20 20:23:02
 * @version 1.0  
 */
public interface BasicsdatumColourLibraryService extends BaseService<BasicsdatumColourLibrary> {

/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 方法描述：分页查询部件
        *
        * @param queryBasicsdatumColourLibraryDto 查询条件
        * @return PageInfo<BasicsdatumComponentVo>
         */
        PageInfo getBasicsdatumColourLibraryList(QueryBasicsdatumColourLibraryDto queryBasicsdatumColourLibraryDto);


        /**
        * 基础资料-颜色库导入
        * @param file
        * @return
        */
        Boolean basicsdatumColourLibraryImportExcel(MultipartFile file) throws IOException, Exception;

        /**
        * 基础资料-颜色库导出
        * @param response
        * @return
        */
        void basicsdatumColourLibraryDeriveExcel(HttpServletResponse response) throws Exception;


        /**
        * 方法描述：新增修改基础资料-颜色库
        *
        * @param addRevampBasicsdatumColourLibraryDto 部件Dto类
        * @return boolean
        */
        Boolean addRevampBasicsdatumColourLibrary(AddRevampBasicsdatumColourLibraryDto addRevampBasicsdatumColourLibraryDto);



        /**
        * 方法描述：删除基础资料-颜色库
        *
        * @param id （多个用，）
        * @return boolean
        */
        Boolean delBasicsdatumColourLibrary(String id);


        /**
         * 方法描述：启用停止基础资料-颜色库
         *
         * @param startStopDto 启用停止Dto类
         * @return boolean
         */
        Boolean startStopBasicsdatumColourLibrary(StartStopDto startStopDto);

    List<SelectOptionsVo> getAllColourSpecification(String status,String isStyle,String isMaterials);

        List<BasicsdatumColourLibrary> listByCode(List<String> colourCodeList);


/** 自定义方法区 不替换的区域【other_end】 **/


}
