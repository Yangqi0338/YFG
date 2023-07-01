package com.base.sbc.module.pack.utils;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;

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
    public static void commonQw(QueryWrapper<?> qw, PackCommonSearchDto dto) {
        qw.eq(StrUtil.isNotBlank(dto.getFId()), "foreign_id", dto.getFId());
        qw.eq(StrUtil.isNotBlank(dto.getPackType()), "pack_type", dto.getPackType());
    }

    /**
     * 公共条件
     *
     * @param qw
     * @param dto
     */
    public static void commonQw(QueryWrapper<?> qw, PackCommonPageSearchDto dto) {
        qw.eq(StrUtil.isNotBlank(dto.getFId()), "foreign_id", dto.getFId());
        qw.eq(StrUtil.isNotBlank(dto.getPackType()), "pack_type", dto.getPackType());
    }
}
