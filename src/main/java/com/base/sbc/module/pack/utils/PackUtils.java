package com.base.sbc.module.pack.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.entity.PackBomVersion;

/**
 * 类描述：资料包帮助类
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.utils.PackUtils
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-01 10:37
 */
public class PackUtils {


    /**
     * 资料包类型 设计资料包
     */
    public static final String PACK_TYPE_DESIGN = "packDesign";
    /**
     * 资料包类型 大货资料包
     */
    public static final String PACK_TYPE_BIG_GOODS = "packBigGoods";


    /**
     * 公共条件
     *
     * @param qw
     * @param dto
     */
    public static void commonQw(AbstractWrapper qw, Object dto) {
        String foreignId = BeanUtil.getProperty(dto, "foreignId");
        qw.eq("del_flag", BaseGlobal.NO);
        if (StrUtil.isNotBlank(foreignId)) {
            qw.eq("foreign_id", foreignId);
        }
        String packType = BeanUtil.getProperty(dto, "packType");
        if (StrUtil.isNotBlank(packType)) {
            qw.eq("pack_type", packType);
        }
    }

    /**
     * 设置bom 的版本信息
     *
     * @param version
     * @param bom
     */
    public static void setBomVersionInfo(PackBomVersion version, PackBom bom) {
        bom.setBomVersionId(version.getId());
        bom.setForeignId(version.getForeignId());
        bom.setPackType(version.getPackType());
    }

}
