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
    DESIGN_BOM_TO_BIG_GOODS_IS_ONLY_ONCE_SWITCH("DESIGN_BOM_TO_BIG_GOODS_IS_ONLY_ONCE_SWITCH","设计bom转大货是否只转一次开关"),
    ADD_PLANNING_SEASON_DEFAULT_INSERT_TEAM_SWITCH("ADD_PLANNING_SEASON_DEFAULT_INSERT_TEAM_SWITCH","新增产品季默认添加团队开关"),
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
