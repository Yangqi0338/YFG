/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.pack.dto.PackInfoSearchPageDto;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.vo.PackInfoListVo;
import com.base.sbc.module.pack.vo.SampleDesignPackInfoListVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 类描述：资料包 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackInfoService
 * @email your email
 * @date 创建时间：2023-7-6 17:13:01
 */
public interface PackInfoService extends BaseService<PackInfo> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 通过样衣设计查询
     *
     * @param pageDto
     * @return
     */
    PageInfo<SampleDesignPackInfoListVo> pageBySampleDesign(PackInfoSearchPageDto pageDto);

    /**
     * 通过样衣设计创建BOM基础信息
     *
     * @param id
     * @return
     */
    PackInfoListVo createBySampleDesign(String id);

    /**
     * 查询
     *
     * @param foreignIds
     * @param packType
     * @return
     */
    Map<String, List<PackInfoListVo>> queryListToMapGroupByForeignId(List<String> foreignIds, String packType);

    /**
     * 记录日志
     *
     * @param name      模块名称
     * @param foreignId 父id
     * @param id        数据id
     * @param content   修改内容
     */
    void log(String name, String foreignId, String id, String content);

// 自定义方法区 不替换的区域【other_end】


}
