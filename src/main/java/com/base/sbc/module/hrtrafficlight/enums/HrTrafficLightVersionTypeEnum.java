package com.base.sbc.module.hrtrafficlight.enums;

import cn.hutool.core.util.ObjectUtil;
import com.base.sbc.config.exception.OtherException;

/**
 * 人事红绿灯版本类型枚举
 *
 * @author XHTE
 * @create 2024-08-15
 */
public enum HrTrafficLightVersionTypeEnum {
    BM(1, "部门"),

    GR(2, "个人"),
    GRXQ_XDCGL(3, "个人详情-下单成功率"),
    GRXQ_DKPJYYJS(4, "个人详情-单款平均样衣件数"),
    GRXQ_TOP_KCS(5, "个人详情-TOP款次数"),
    GRXQ_MXDZQL(6, "个人详情-明细单准确率"),
    GRXQ_MLXDZQL(7, "个人详情-面料详单准确率"),
    GRXQ_MFLQTL(8, "个人详情-面辅料齐套率"),
    GRXQ_YYCKL(9, "个人详情-样衣参考率"),
    GRXQ_XDSDWCL(10, "个人详情-下店市调完成率"),
    GRXQ_CXL_SAK(11, "个人详情-产销率 SA款"),
    YJCGS_CMT_FOB(12, "月均出稿数（cmt+fob）"),
    YJXDS_SKC_CMT_FOB(13, "月均下单数SKC（cmt+fob）"),
    YJXDS_SKU_CMT_FOB(14, "月均下单数SKU（cmt+fob）"),
    ;

    private final Integer code;
    private final String value;

    HrTrafficLightVersionTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static Integer getCode(Integer code) {
        if (ObjectUtil.isNotEmpty(code)) {
            for (HrTrafficLightVersionTypeEnum item : HrTrafficLightVersionTypeEnum.values()) {
                if (item.code.equals(code)) {
                    return item.code;
                }
            }
        }
        throw new OtherException("Excel 类型不存在");
    }

}
