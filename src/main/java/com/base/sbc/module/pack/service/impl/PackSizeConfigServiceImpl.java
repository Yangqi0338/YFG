/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.module.pack.dto.PackSizeConfigDto;
import com.base.sbc.module.pack.dto.PackSizeConfigSearchDto;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackSizeConfig;
import com.base.sbc.module.pack.mapper.PackSizeConfigMapper;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.service.PackSizeConfigService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackSizeConfigVo;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.service.StyleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 类描述：资料包-尺寸表配置 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackSizeConfigService
 * @email your email
 * @date 创建时间：2023-9-1 14:07:14
 */
@Service
public class PackSizeConfigServiceImpl extends PackBaseServiceImpl<PackSizeConfigMapper, PackSizeConfig> implements PackSizeConfigService {

// 自定义方法区 不替换的区域【other_start】

    @Autowired
    private PackInfoService packInfoService;
    @Autowired
    @Lazy
    private StyleService styleService;




    @Override
    String getModeName() {
        return "尺寸表配置";
    }


    @Override
    public PackSizeConfigVo getConfig(String foreignId, String packType) {
        PackSizeConfig packSizeConfig = get(foreignId, packType);
        if (packSizeConfig == null) {
            PackInfo packInfo = packInfoService.getById(foreignId);
            Style style = styleService.getById(packInfo.getForeignId());
            packSizeConfig = createByStyle(foreignId, packType, style);
        }

        return BeanUtil.copyProperties(packSizeConfig, PackSizeConfigVo.class);
    }

    @Override
    public PackSizeConfig createByStyle(String foreignId, String packType, Style style) {
        PackSizeConfig packSizeConfig = BeanUtil.copyProperties(style, PackSizeConfig.class);
        CommonUtils.resetCreateUpdate(packSizeConfig);
        packSizeConfig.setId(null);
        packSizeConfig.setForeignId(foreignId);
        packSizeConfig.setPackType(packType);
        packSizeConfig.setActiveSizes(style.getProductSizes());
        save(packSizeConfig);
        return packSizeConfig;
    }

    @Override
    public PageInfo<PackSizeConfigVo> pageInfo(PackSizeConfigSearchDto dto) {
        BaseQueryWrapper<PackSizeConfig> qw = new BaseQueryWrapper<>();
        qw.eq("tpsc.company_code", getCompanyCode());
        qw.eq("tpsc.pack_type", PackUtils.PACK_TYPE_BIG_GOODS);
        qw.eq("tpsc.del_flag", BaseGlobal.STATUS_NORMAL);
        qw.isNotNull("tpi.style_no");
        qw.ne("tpi.style_no","");
        qw.andLike(dto.getSearch(), "product_sizes", "size_range_name", "size_codes", "size_range");
        Page<PackSizeConfigVo> objects = PageHelper.startPage(dto);
        baseMapper.sizeConfigList(qw);
        PageInfo<PackSizeConfigVo> packSizeConfigPageInfo = objects.toPageInfo();
        return packSizeConfigPageInfo;
    }

    @Override
    public PackSizeConfigVo saveConfig(PackSizeConfigDto dto) {
        PackSizeConfigVo config = getConfig(dto.getForeignId(), dto.getPackType());
        saveOrUpdateOperaLog(dto, config, genOperaLogEntity(config, "修改"));
        BeanUtil.copyProperties(dto, config, "id");
        updateById(config);
        return BeanUtil.copyProperties(config, PackSizeConfigVo.class);
    }


// 自定义方法区 不替换的区域【other_end】

}

