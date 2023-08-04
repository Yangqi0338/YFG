/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;

import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.sample.dto.*;
import com.base.sbc.module.sample.entity.SampleStyleColor;
import com.base.sbc.module.sample.vo.SampleStyleColorVo;
import com.github.pagehelper.PageInfo;

import java.security.Principal;
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
        PageInfo<SampleStyleColorVo> getSampleStyleColorList(Principal user, QuerySampleStyleColorDto queryDto);

        /**
         * 方法描述: 获取款式或配饰
         * @param designNo 款式编号
         * @return
         */
        List<SampleStyleColorVo> getStyleAccessoryBystyleNo(String designNo);


        /**
         * 修改吊牌价-款式配色
         * @param updateTagPriceDto
         * @return
         */
        Boolean updateTagPrice( UpdateTagPriceDto updateTagPriceDto);

        /**
         *  大货款号查询
         * @param querySampleStyleColorDto
         * @return
         */
        List<SampleStyleColorVo> getByStyleNo(QuerySampleStyleColorDto querySampleStyleColorDto);

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
        * @param id （多个用，） sampleDesignId 样衣id
        * @return boolean
        */
        Boolean delSampleStyleColor(String id,String sampleDesignId);

        /**
         * 方法描述：删除样衣-款式配色
         *
         * @param id （多个用，）
         * @return boolean
         */
        Boolean delStyleColor(String id);

        /**
        * 方法描述：启用停止样衣-款式配色
        *
        * @param startStopDto 启用停止Dto类
        * @return boolean
        */
        Boolean startStopSampleStyleColor( StartStopDto startStopDto);

        /**
         * 方法描述 下发scm
         * @param ids
         * @return
         */
        Boolean  issueScm(String ids);


        /**
         * 方法描述 获取款式下的颜色
         * @param sampleDesignId 样衣id
         */
        List<String> getStyleColorId(String sampleDesignId);

        /**
         * 方法描述 关联bom
         * @param relevanceBomDto
         * @return
         */
        Boolean relevanceBom(RelevanceBomDto relevanceBomDto);

        /**
         * 方法描述 修改大货款号,波段
         * @param updateStyleNoBandDto
         * @return
         */
        Boolean updateStyleNoBand(UpdateStyleNoBandDto updateStyleNoBandDto);


        /**
         * 方法描述 验证配色是否可修改
         * @param id
         * @return
         */
        Boolean verification(String id);
// 自定义方法区 不替换的区域【other_end】


}
