/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.common.service.BaseService;
import com.github.pagehelper.PageInfo;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumRangeDifference;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumRangeDifferenceVo;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumRangeDifferenceDto;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import java.io.IOException;

/** 
 * 类描述：基础资料-档差 service类
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumRangeDifferenceService
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-18 19:42:16
 * @version 1.0  
 */
public interface BasicsdatumRangeDifferenceService extends BaseService<BasicsdatumRangeDifference> {

/** 自定义方法区 不替换的区域【other_start】 **/

        /**
        * 方法描述：分页查询部件
        *
        * @param queryDto 查询条件
        * @return PageInfo<BasicsdatumComponentVo>
         */
        PageInfo<BasicsdatumRangeDifferenceVo> getList(QueryDto queryDto);


        /**
        * 基础资料-档差导入
        * @param file
        * @return
        */
        Boolean importExcel(MultipartFile file) throws IOException, Exception;

        /**
        * 基础资料-档差导出
        * @param response
        * @return
        */
        void deriveExcel(HttpServletResponse response) throws Exception;


        /**
        * 方法描述：新增修改基础资料-档差
        *
        * @param addRevampBasicsdatumRangeDifferenceDto 部件Dto类
        * @return boolean
        */
        Boolean addRevamp(AddRevampBasicsdatumRangeDifferenceDto addRevampBasicsdatumRangeDifferenceDto);



        /**
        * 方法描述：删除基础资料-档差
        *
        * @param id （多个用，）
        * @return boolean
        */
        Boolean del(String id);



        /**
        * 方法描述：启用停止基础资料-档差
        *
        * @param startStopDto 启用停止Dto类
        * @return boolean
        */
        Boolean startStop( StartStopDto startStopDto);


/** 自定义方法区 不替换的区域【other_end】 **/

	
}
