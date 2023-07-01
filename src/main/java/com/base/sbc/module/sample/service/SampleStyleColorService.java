/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;

import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.sample.dto.AddRevampSampleStyleColorDto;
import com.base.sbc.module.sample.dto.QuerySampleStyleColorDto;
import com.base.sbc.module.sample.entity.SampleStyleColor;
import com.base.sbc.module.sample.vo.SampleStyleColorVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 类描述：样衣-款式配色 service类
 * @address com.base.sbc.module.sample.service.SampleStyleColorService
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-6-28 15:02:46
 * @version 1.0
 */
public interface SampleStyleColorService extends BaseService<SampleStyleColor> {

// 自定义方法区 不替换的区域【other_start】

        /**
        * 方法描述：分页查询部件
        *
        * @param queryDto 查询条件
        * @return PageInfo<BasicsdatumComponentVo>
         */
        PageInfo<SampleStyleColorVo> getSampleStyleColorList(QuerySampleStyleColorDto queryDto);

        /**
         * 方法描述: 获取款式或配饰
         * @param styleNo 款式编号
         * @return
         */
        List<SampleStyleColorVo> getStyleAccessoryBystyleNo(String designNo);

        /**
         * 方法描述: 批量新增款式配色-款式配色
         * @param
         * @return
         */
        Boolean  batchAddSampleStyleColor(List<AddRevampSampleStyleColorDto> list);


        /**
        * 方法描述：新增修改样衣-款式配色
        *
        * @param addRevampSampleStyleColorDto 部件Dto类
        * @return boolean
        */
        Boolean addRevampSampleStyleColor(AddRevampSampleStyleColorDto addRevampSampleStyleColorDto);



        /**
        * 方法描述：删除样衣-款式配色
        *
        * @param id （多个用，）
        * @return boolean
        */
        Boolean delSampleStyleColor(String id);



        /**
        * 方法描述：启用停止样衣-款式配色
        *
        * @param startStopDto 启用停止Dto类
        * @return boolean
        */
        Boolean startStopSampleStyleColor( StartStopDto startStopDto);


// 自定义方法区 不替换的区域【other_end】


}
