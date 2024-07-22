package com.base.sbc.module.pack.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.entity.PackBomVersion;
import com.base.sbc.module.pack.entity.PackSize;
import com.base.sbc.module.pack.entity.PackSizeDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public static final String PATH_SQ_EL = "'资料包-'+#p0.packType+'-'+#p0.foreignId";

    /**
     * 资料包类型 设计资料包
     */
    public static final String PACK_TYPE_DESIGN = "packDesign";
    /**
     * 资料包类型 大货资料包
     */
    public static final String PACK_TYPE_BIG_GOODS = "packBigGoods";

    /**
     * 资料包类型 大货资料包预填写
     */
    public static final String PACK_TYPE_BIG_GOODS_PRE = "packBigGoodsPre";

    /**
     * 资料包类型 款式设计
     */
    public static final String PACK_TYPE_STYLE = "packStyle";

    /**
     * 工艺说明项目
     */
    public static final List<String> TECH_SPEC_ITEM = CollUtil.newArrayList("裁剪工艺", "基础工艺", "小部件", "注意事项", "整烫包装", "外辅工艺");

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

    public static void bomDefaultVal(PackBom... bom) {
        if (ArrayUtil.isEmpty(bom)) {
            return;
        }
        for (PackBom packBom : bom) {
            packBom.setDelFlag(Opt.ofBlankAble(packBom.getDelFlag()).orElse(BaseGlobal.NO));
            packBom.setStatus(Opt.ofBlankAble(packBom.getStatus()).orElse(BaseGlobal.YES));
            packBom.setUnusableFlag(Opt.ofBlankAble(packBom.getUnusableFlag()).orElse(BaseGlobal.NO));
            packBom.setScmSendFlag(Opt.ofBlankAble(packBom.getScmSendFlag()).orElse(BaseGlobal.NO));
        }
    }

    /**
     * 公共条件
     *
     * @param qw
     * @param foreignId 资料包id
     * @param packType 资料包类型
     */
    public static void commonQw(AbstractWrapper qw, String foreignId, String packType, String status) {
        qw.eq("del_flag", BaseGlobal.NO);
        if (StrUtil.isNotBlank(foreignId)) {
            qw.eq("foreign_id", foreignId);
        }
        if (StrUtil.isNotBlank(packType)) {
            if (!"ALL".equals(packType)) {
                qw.eq("pack_type", packType);
            }

        }
        if (StrUtil.isNotBlank(status)) {
            qw.eq("status", status);
        }
    }

    /**
     * 解析尺寸表数据
     *
     * @return
     */
    public static List<PackSizeDetail> parseSizeDetail(PackSize packSize) {
        List<PackSizeDetail> result = new ArrayList<>(16);

        if (StrUtil.isAllNotBlank(packSize.getStandard(), packSize.getSize())) {
            JSONObject standardMap = JSONObject.parseObject(packSize.getStandard());
            List<String> sizeList = StrUtil.split(packSize.getSize(), CharUtil.COMMA);
            for (String s : sizeList) {
                PackSizeDetail sd = new PackSizeDetail();
                sd.setForeignId(packSize.getForeignId());
                sd.setPackType(packSize.getPackType());
                sd.setPackSizeId(packSize.getId());
                sd.setSize(s);
                sd.setWashing(MapUtil.getStr(standardMap, "washing" + s, ""));
                sd.setTemplate(MapUtil.getStr(standardMap, "template" + s, ""));
                sd.setGarment(MapUtil.getStr(standardMap, "garment" + s, ""));
                result.add(sd);
            }
        }

        return result;
    }

    public static List<String> getTechSpecAttType(String packType) {
        return TECH_SPEC_ITEM.stream().map(item -> {
            return packType + StrUtil.DASHED + item;
        }).collect(Collectors.toList());
    }

    public static List<String> getTechSpecAttType(String packType, List<String> items) {
        return items.stream().map(item -> {
            return packType + StrUtil.DASHED + item;
        }).collect(Collectors.toList());
    }

    public static String getCodeByBomStatus(String bomStatus) {
        return YesOrNoEnum.NO.getValueStr().equals(bomStatus) ? PackUtils.PACK_TYPE_DESIGN : PackUtils.PACK_TYPE_BIG_GOODS;
    }
}
