package com.base.sbc.config.constant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.base.sbc.config.enums.business.RFIDType;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * {@code 描述：生成工艺单属性}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/11/29
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("moreLanguage")
public class MoreLanguageProperties {

    public static String internalLanguageCode = "ZH";
    public static Boolean internalCheck = false;
    public static String languageDictCode = "language";
    public static Map removeConditionMap = MapUtil.ofEntries(MapUtil.entry("10","DP02"));
    public static String multiSeparator = "\n";
    public static String fieldValueSeparator = ":";
    public static Integer styleCountryStatusImportMaxSize = 60;
    public static String countryLanguageCodePrefix = "GY";
    public static Integer countryLanguageCodeZeroFill = 10;
    public static String languageCodePrefix = "Y";
    public static Integer languageCodeZeroFill = 10;
    public static String excelTranslateContentField = "content";
    public static String excelTitle = "温馨提示：请您按照导入规测进行导入，否测可能影响到导入成功，请注意。\n" +
            "1、如若需要删除内容信息，请删除整行信息。\n" +
            "2、请不要删除表头信息。\n" +
            "3、请不要删除翻译语言内自带信息。\n";

    public static String styleCountryStatusMainDbAlias = "tsd.";
    public static List<MoreLanguageMsgEnum> msgEnumList = CollUtil.list(false,MoreLanguageMsgEnum.values());

    public void setInternalLanguageCode(String internalLanguageCode) {
        MoreLanguageProperties.internalLanguageCode = internalLanguageCode;
    }

    public void setInternalCheck(Boolean internalCheck) {
        MoreLanguageProperties.internalCheck = internalCheck;
    }

    public void setLanguageDictCode(String languageDictCode) {
        MoreLanguageProperties.languageDictCode = languageDictCode;
    }

    public void setRemoveConditionMap(Map removeConditionMap) {
        MoreLanguageProperties.removeConditionMap = removeConditionMap;
    }

    public void setMultiSeparator(String multiSeparator) {
        MoreLanguageProperties.multiSeparator = multiSeparator;
    }

    public void setFieldValueSeparator(String fieldValueSeparator) {
        MoreLanguageProperties.fieldValueSeparator = fieldValueSeparator;
    }

    public void setStyleCountryStatusImportMaxSize(Integer styleCountryStatusImportMaxSize) {
        MoreLanguageProperties.styleCountryStatusImportMaxSize = styleCountryStatusImportMaxSize;
    }

    public void setCountryLanguageCodePrefix(String countryLanguageCodePrefix) {
        MoreLanguageProperties.countryLanguageCodePrefix = countryLanguageCodePrefix;
    }

    public void setCountryLanguageCodeZeroFill(Integer countryLanguageCodeZeroFill) {
        MoreLanguageProperties.countryLanguageCodeZeroFill = countryLanguageCodeZeroFill;
    }

    public void setLanguageCodePrefix(String languageCodePrefix) {
        MoreLanguageProperties.languageCodePrefix = languageCodePrefix;
    }

    public void setLanguageCodeZeroFill(Integer languageCodeZeroFill) {
        MoreLanguageProperties.languageCodeZeroFill = languageCodeZeroFill;
    }

    public void setExcelTranslateContentField(String excelTranslateContentField) {
        MoreLanguageProperties.excelTranslateContentField = excelTranslateContentField;
    }

    public void setExcelTitle(String excelTitle) {
        MoreLanguageProperties.excelTitle = excelTitle;
    }

    public void setStyleCountryStatusMainDbAlias(String styleCountryStatusMainDbAlias) {
        MoreLanguageProperties.styleCountryStatusMainDbAlias = styleCountryStatusMainDbAlias;
    }

    public void setMsgEnumList(List<MoreLanguageMsgEnum> msgEnumList) {
        MoreLanguageProperties.msgEnumList = msgEnumList;
    }

    public enum MoreLanguageMsgEnum {
        NOT_INSERT("PDM未创建%s国家语言翻译"),
        HAVEN_T_TAG("不存在%s的吊牌信息"),
        HAVEN_T_LANGUAGE("%s不存在该语种"),
        HAVEN_T_CONTENT("%s未翻译"),
        FIELD("字段"),
        CONTENT("内容"),
        FIELD_FORMAT("%s:%s %s"),
        CONTENT_FORMAT("%s%s%s%s"),
        HAVEN_T_COUNTRY_LANGUAGE("未查询到国家语言"),
        HAVEN_T_BULK_TAG("大货款号: 不存在%s的吊牌信息"),
        SUCCESS_IMPORT("您的吊牌信息已经导入成功. %s"),
        CHECK_REIMPORT("%s, 请问是否需要导入?"),
        FAILURE_IMPORT("导入失败, 请你根据导入规则进行导入\n"),
        EXCESS_STATUS_IMPORT("仅能导入%s条数据,后续款号不执行"),
        INCORRECT_COUNTRY_LANGUAGE("国家对应不上,请清理缓存"),
        EXIST_COUNTRY_LANGUAGE("已存在对应国家"),
        NOT_COMMIT("非法标准列code"),
        NOT_FOUND_COUNTRY_LANGUAGE("未找到对应的国家语言数据"),
        WARN_COUNTRY_LANGUAGE("无效的国家或语言"),
        WARN_STANDARD_CODE("无效的标准列,请重新刷新页面"),
        NOT_EXIST_STANDARD_CODE("未设置表头,请找开发协助"),
        WARN_EXAMINE_STATUS("吊牌状态必须为待翻译确认或已审核"),
        NOT_EXIST_STATUS_BULK("失败,没有找到对应款号"),
        WARN_STATUS("成功,审核流程仅进行到%s"),
        ;
        /** 编码 */
        private final String code;
        /** 文本 */
        private final String text;
        MoreLanguageMsgEnum(String text) {
            String code = this.name().toLowerCase();
            this.code = StrUtil.toCamelCase(code);
            this.text = text;
        }
    }

}
