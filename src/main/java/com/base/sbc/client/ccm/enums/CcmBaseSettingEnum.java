package com.base.sbc.client.ccm.enums;

/**
 * CCM配置key
 * @author lizan
 * @date 2023-08-26 16:22
 */
public enum CcmBaseSettingEnum {
    // 单款多色开关
    STYLE_MANY_COLOR("STYLE_MANY_COLOR","单款多色开关"),
    ISSUED_TO_EXTERNAL_SMP_SYSTEM_SWITCH("ISSUED_TO_EXTERNAL_SMP_SYSTEM_SWITCH","控制是否下发外部SMP系统开关"),
    DESIGN_BOM_TO_BIG_GOODS_CHECK_SWITCH("DESIGN_BOM_TO_BIG_GOODS_CHECK_SWITCH","设计bom转大货校验开关"),
    SEND_FLAG("SEND_FLAG","下发下游系统开关"),
    DESIGN_BOM_TO_BIG_GOODS_IS_ONLY_ONCE_SWITCH("DESIGN_BOM_TO_BIG_GOODS_IS_ONLY_ONCE_SWITCH","设计bom转大货是否只转一次开关"),
    ADD_PLANNING_SEASON_DEFAULT_INSERT_TEAM_SWITCH("ADD_PLANNING_SEASON_DEFAULT_INSERT_TEAM_SWITCH","新增产品季默认添加团队开关"),
    DESIGN_DISHA_DATA_PACKAGE_COUNT("DESIGN_DISHA_DATA_PACKAGE_COUNT","笛莎资料包计算开关"),
    MATERIAL_CREATE_PURCHASEDEMAND("MATERIAL_CREATE_PURCHASEDEMAND","笛莎物料清单转仓库配置项"),
    COMMODITY_PLANNING_SELECT_BAND_ALL("COMMODITY_PLANNING_SELECT_BAND_ALL","商品企划-编辑坑位是否选择所有的波段"),
    PATTERN_MAKING_SAMPLE_CROPPING_SWITCH("PATTERN_MAKING_SAMPLE_CROPPING_SWITCH","打版管理-样衣任务-裁剪页面是否显示"),
    HANG_TAG_INGREDIENT_WRAP("dpppcfxxmrhh","吊牌品牌成分信息默认换行"),
    HANG_TAG_WARM_TIPS_WRAP("dpppwxtsmrhh","吊牌品牌温馨提示默认换行"),
    ;
    private final String keyCode;

    private final String keyName;


    CcmBaseSettingEnum(String keyCode, String keyName) {
        this.keyCode = keyCode;
        this.keyName = keyName;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public String getKeyName() {
        return keyName;
    }
}
