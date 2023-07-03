package com.base.sbc.module.pack.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;

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
     * 公共条件
     *
     * @param qw
     * @param dto
     */
    public static void commonQw(AbstractWrapper qw, Object dto) {
        String foreignId = BeanUtil.getProperty(dto, "foreignId");
        if (StrUtil.isNotBlank(foreignId)) {
            qw.eq("foreign_id", foreignId);
        }
        String packType = BeanUtil.getProperty(dto, "packType");
        if (StrUtil.isNotBlank(packType)) {
            qw.eq("pack_type", packType);
        }
    }


}
