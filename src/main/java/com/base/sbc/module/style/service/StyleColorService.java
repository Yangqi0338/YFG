/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.style.dto.*;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.vo.StyleColorVo;
import com.github.pagehelper.PageInfo;

import java.security.Principal;
import java.util.List;

/**
 * 类描述：款式-款式配色 service类
 * @address com.base.sbc.module.sample.service.SampleStyleColorService
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-6-28 15:02:46
 * @version 1.0
 */
public interface StyleColorService extends BaseService<StyleColor> {

// 自定义方法区 不替换的区域【other_start】

        /**
        * 方法描述：分页查询配色
        *
        * @param queryDto 查询条件
        * @return PageInfo<BasicsdatumComponentVo>
         */
        PageInfo<StyleColorVo> getSampleStyleColorList(Principal user, QueryStyleColorDto queryDto);
        /**
         * 方法描述: 获取款式或配饰
         * @param designNo 款式编号
         * @return
         */
        List<StyleColorVo> getStyleAccessoryBystyleNo(String designNo);


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
        List<StyleColorVo> getByStyleNo(QueryStyleColorDto querySampleStyleColorDto);

        /**
         * 方法描述: 批量新增款式配色-款式配色
         * @param
         * @return
         */
        Boolean  batchAddSampleStyleColor(List<AddRevampStyleColorDto> list);


        /**
        * 方法描述：新增修改款式-款式配色
        *
        * @param addRevampStyleColorDto 部件Dto类
        * @return boolean
        */
        Boolean addRevampSampleStyleColor(AddRevampStyleColorDto addRevampStyleColorDto);



        /**
         * 方法描述：删除款式-款式配色
         *
         * @param id （多个用，） styleId 款式id
         * @return boolean
         */
        Boolean delSampleStyleColor(String id, String styleId);

        /**
         * 方法描述：删除款式-款式配色
         *
         * @param id （多个用，）
         * @return boolean
         */
        Boolean delStyleColor(String id);

        /**
        * 方法描述：启用停止款式-款式配色
        *
        * @param startStopDto 启用停止Dto类
        * @return boolean
        */
        Boolean startStopSampleStyleColor( StartStopDto startStopDto);

        /**
         * 方法描述: 修改颜色
         * @param updateColorDto
         * @return
         */
        Boolean updateColor(UpdateColorDto updateColorDto);
        /**
         * 方法描述 下发scm
         * @param ids
         * @return
         */
        ApiResult issueScm(String ids);


        /**
         * 方法描述 获取款式下的颜色
         * @param styleId 款式id
         */
        List<String> getStyleColorId(String styleId);

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


        /**
         * 方法描述 配色解锁
         * @param publicStyleColorDto
         * @return
         */
        Boolean unlockStyleColor(PublicStyleColorDto publicStyleColorDto);

        /**
         * 方法描述 新增次品款
         * @param publicStyleColorDto
         * @return
         */
        Boolean addDefective(PublicStyleColorDto publicStyleColorDto);

        /**
         * 方法描述 更新下单标记
         * @param publicStyleColorDto
         * @return
         */
        Boolean updateOrderFlag(PublicStyleColorDto publicStyleColorDto);


// 自定义方法区 不替换的区域【other_end】


}
