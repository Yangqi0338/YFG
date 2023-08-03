/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.sample.dto.SampleCirculatePageDto;
import com.base.sbc.module.sample.entity.SampleCirculate;
import com.base.sbc.module.sample.vo.SampleCirculateVo;
import com.base.sbc.module.sample.vo.SampleReturnDetailsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：样衣借还 dao类
 *
 * @address com.base.sbc.module.sample.dao.SampleCirulateMapper
 */
@Mapper
public interface SampleCirculateMapper extends BaseMapper<SampleCirculate> {
    List<SampleCirculateVo> getList(SampleCirculatePageDto dto);

    SampleCirculateVo getDetail(String id);

    /**
     * 获取归还明细
     *
     * @param sampleItemIds
     * @return
     */
    List<SampleReturnDetailsVO> getSampleReturnDetailsVO(@Param("sampleItemIds") List<String> sampleItemIds);
}

