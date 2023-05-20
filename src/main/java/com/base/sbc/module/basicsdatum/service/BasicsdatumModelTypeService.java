/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.github.pagehelper.PageInfo;
import com.base.sbc.module.common.service.IServicePlus;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumModelTypeVo;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumModelTypeDto;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import java.io.IOException;

/** 
 * 类描述：基础资料-号型类型 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumModelTypeService
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-20 9:31:14
 * @version 1.0  
 */
public interface BasicsdatumModelTypeService extends IServicePlus<BasicsdatumModelType>{

/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 方法描述：分页查询部件
        *
        * @param queryDto 查询条件
        * @return PageInfo<BasicsdatumComponentVo>
         */
        PageInfo<BasicsdatumModelTypeVo> getBasicsdatumModelTypeList(QueryDto queryDto);


        /**
        * 基础资料-号型类型导入
        * @param file
        * @return
        */
        Boolean basicsdatumModelTypeImportExcel(MultipartFile file) throws IOException, Exception;

        /**
        * 基础资料-号型类型导出
        * @param response
        * @return
        */
        void basicsdatumModelTypeDeriveExcel(HttpServletResponse response) throws Exception;


        /**
        * 方法描述：新增修改基础资料-号型类型
        *
        * @param addRevampBasicsdatumModelTypeDto 部件Dto类
        * @return boolean
        */
        Boolean addRevampBasicsdatumModelType(AddRevampBasicsdatumModelTypeDto addRevampBasicsdatumModelTypeDto);



        /**
        * 方法描述：删除基础资料-号型类型
        *
        * @param id （多个用，）
        * @return boolean
        */
        Boolean delBasicsdatumModelType(String id);



        /**
        * 方法描述：启用停止基础资料-号型类型
        *
        * @param startStopDto 启用停止Dto类
        * @return boolean
        */
        Boolean startStopBasicsdatumModelType( StartStopDto startStopDto);


/** 自定义方法区 不替换的区域【other_end】 **/

	
}
