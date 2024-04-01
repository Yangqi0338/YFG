/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.pack.entity.PackTechPackaging;
import com.base.sbc.module.pack.mapper.PackTechPackagingMapper;
import com.base.sbc.module.pack.service.PackTechPackagingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类描述：资料包-工艺说明-包装方式和体积重量 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackTechPackagingService
 * @email your email
 * @date 创建时间：2023-7-13 14:24:40
 */
@Service
public class PackTechPackagingServiceImpl extends AbstractPackBaseServiceImpl<PackTechPackagingMapper, PackTechPackaging> implements PackTechPackagingService {


// 自定义方法区 不替换的区域【other_start】


    @Override
    public PackTechPackaging savePackaging(PackTechPackaging packaging) {
        if (StringUtils.isAnyBlank(packaging.getPackType(), packaging.getForeignId())) {
            throw new OtherException("PackType、ForeignId必填");
        }
        PackTechPackaging db = get(packaging.getForeignId(), packaging.getPackType());
        if (db == null) {
            packaging.setId(null);
            save(packaging);
            return packaging;
        } else {
            saveOrUpdateOperaLog(packaging, db, genOperaLogEntity(db, "修改"));
            BeanUtil.copyProperties(packaging, db, "id");
            updateById(db);
            return db;
        }

    }

    @Override
    public PackTechPackaging Packaging(PackTechPackaging packaging) {
        PackTechPackaging packTechPackaging = new PackTechPackaging();

        if(packaging.getPackType().equals("C8_PackageSize")){
            Pattern pattern = Pattern.compile("^(.*?):([0-9]+)\\*([0-9]+)cm$");
            Matcher matcher = pattern.matcher(packaging.getPackagingBagStandardName());
            if(matcher.matches()){
                String   value1 = matcher.group(1);
                String   value2 = matcher.group(2);
                String   value3 = matcher.group(3);
                packTechPackaging.setVolumeLength(new BigDecimal(value2));
                packTechPackaging.setVolumeWidth(new BigDecimal(value3));
            } else {
                System.out.println("无法提取值");
            }
            return packTechPackaging;
        } else if(packaging.getPackType().equals("C8_PackageSize")){

        }

        return null;
    }

    @Override
    String getModeName() {
        return "包装方式和体积重量";
    }


// 自定义方法区 不替换的区域【other_end】

}
