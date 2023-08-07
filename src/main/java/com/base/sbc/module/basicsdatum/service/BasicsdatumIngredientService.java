/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumIngredientDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumIngredientDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumIngredient;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumIngredientVo;
import com.base.sbc.module.common.service.BaseService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：基础资料-材料成分 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumIngredientService
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-19 19:15:00
 * @version 1.0
 */
public interface BasicsdatumIngredientService extends BaseService<BasicsdatumIngredient> {

/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 方法描述：分页查询部件
        *
        * @param queryDto 查询条件
        * @return PageInfo<BasicsdatumComponentVo>
         */
        PageInfo<BasicsdatumIngredientVo> getBasicsdatumIngredientList(BasicsdatumIngredientDto queryDto);


        /**
        * 基础资料-材料成分导入
        * @param file
        * @return
        */
        Boolean basicsdatumIngredientImportExcel(MultipartFile file) throws IOException, Exception;

        /**
        * 基础资料-材料成分导出
        * @param response
        * @return
        */
        void basicsdatumIngredientDeriveExcel(HttpServletResponse response) throws Exception;


        /**
        * 方法描述：新增修改基础资料-材料成分
        *
        * @param addRevampBasicsdatumIngredientDto 部件Dto类
        * @return boolean
        */
        Boolean addRevampBasicsdatumIngredient(AddRevampBasicsdatumIngredientDto addRevampBasicsdatumIngredientDto);



        /**
        * 方法描述：删除基础资料-材料成分
        *
        * @param id （多个用，）
        * @return boolean
        */
        Boolean delBasicsdatumIngredient(String id);



        /**
        * 方法描述：启用停止基础资料-材料成分
        *
        * @param startStopDto 启用停止Dto类
        * @return boolean
        */
        Boolean startStopBasicsdatumIngredient( StartStopDto startStopDto);


/** 自定义方法区 不替换的区域【other_end】 **/


}
