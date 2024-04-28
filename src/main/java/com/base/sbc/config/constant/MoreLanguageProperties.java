package com.base.sbc.config.constant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.RFIDType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisKeyBuilder;
import com.base.sbc.config.redis.RedisKeyConstant;
import com.base.sbc.config.redis.RedisStaticFunUtils;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.INCORRECT_STANDARD_CODE;

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
    public static String saftyStandardCode = "DP02";
    public static String multiSeparator = "\n";
    public static String specialMultiSeparator = ";";
    public static String specialStandardColumnCode = "DP16,XM01";
    public static String fieldValueSeparator = ":";
    public static String showInfoLanguageSeparator = "；";
    public static String checkItemSeparator = "/";
    public static String checkMsgItemSeparator = "、";
    public static String checkMergedSeparator = "（%s）";
    public static String checkMultiItemSeparator = "+";
    public static Integer styleCountryStatusImportMaxSize = 60;
    public static Integer importExecutePoolSize = 12;
    public static Pair<Integer,Integer> styleCountryStatusImportCountRange = Pair.of(20,200);
    public static String countryLanguageCodePrefix = "GY";
    public static Integer countryLanguageCodeZeroFill = 10;
    public static String languageCodePrefix = "Y";
    public static Integer languageCodeZeroFill = 10;
    public static String excelTranslateContentField = "content";
    public static String modelStandardColumnCode = "DP06";
    public static String excelTitle = "温馨提示：请您按照导入规测进行导入，否测可能影响到导入成功，请注意。\n" +
            "1、如若需要删除内容信息，请删除整行信息。\n" +
            "2、请不要删除表头信息。\n" +
            "3、请不要删除翻译语言内自带信息。\n";

    public static String hangTagMainDbAlias = "tsd.";
    public static String noDecorateCategory1stCode = "A05";

    public static Integer excelDataRowNum = 2;

    public static String notCheckStandardColumnCode = "DP14,XM02,XM03,XM04,XM05,XM08,XM09,XM10,XM11";
    public static Map<String,String> msgEnumMap = CollUtil.list(false,MoreLanguageMsgEnum.values())
            .stream().collect(Collectors.toMap(MoreLanguageMsgEnum::name,MoreLanguageMsgEnum::getText));

    public static Boolean isInternalLanguageCode(String languageCode) {
        return MoreLanguageProperties.internalLanguageCode.equals(languageCode);
    }

    public static String getMsg(MoreLanguageMsgEnum msgEnum, Object... param) {
        return String.format(msgEnumMap.getOrDefault(msgEnum.name(), ""), param);
    }

    public static String getMsg(MoreLanguageMsgEnum... param) {
        return getMsg(param[0], Arrays.stream(param).skip(1).map(MoreLanguageMsgEnum::getText).toArray());
    }

    public static Integer calculateImportCount() {
        return calculateImportCount(importExecutePoolSize);
    }

    public static Integer calculateImportCount(Integer poolSize) {
        if (poolSize <= 0) return 1;
        double count = (double) styleCountryStatusImportMaxSize / poolSize;
        Integer minCount = styleCountryStatusImportCountRange.getKey();
        Integer maxCount = styleCountryStatusImportCountRange.getValue();
        if (count < minCount) {
            return calculateImportCount(--poolSize);
        }else if (count > maxCount) {
            return calculateImportCount(++poolSize);
        }
        return poolSize;
    }

    public static String getCountryZeroFill(Integer count) {
        return MoreLanguageProperties.calculateZeroFill(count, MoreLanguageProperties.countryLanguageCodeZeroFill) + count;
    }

    public static String getLanguageZeroFill(Integer count) {
        return MoreLanguageProperties.calculateZeroFill(count, MoreLanguageProperties.languageCodeZeroFill) + count;
    }

    public static String calculateZeroFill(Integer count, Integer zeroFill) {
        int square = (int) Math.log10(zeroFill);
        int countSquare = (int) Math.log10(count);
        StringBuilder fill = new StringBuilder();
        for (int i = 0; i < square - countSquare; i++) {
            fill.append("0");
        }
        return fill.toString();
    }

    public static StandardColumn getStandardColumn(BaseService<?> businessService, CountryLanguageType type, String standardColumnCode) {
        // 从redis标准列数据,若没有查询DB,若没有报设置的Message
        RedisStaticFunUtils.setBusinessService(businessService).setMessage(MoreLanguageProperties.getMsg(INCORRECT_STANDARD_CODE));
        return (StandardColumn) RedisStaticFunUtils.hget(RedisKeyConstant.STANDARD_COLUMN_LIST.build(), type.getCode() + RedisKeyBuilder.COMMA + standardColumnCode);
    }

    public static Boolean checkInternal(String languageCode) {
        return !MoreLanguageProperties.internalLanguageCode.equals(languageCode) || MoreLanguageProperties.internalCheck;
    }

    public void setInternalLanguageCode(String internalLanguageCode) {
        MoreLanguageProperties.internalLanguageCode = internalLanguageCode;
    }

    public void setInternalCheck(Boolean internalCheck) {
        MoreLanguageProperties.internalCheck = internalCheck;
    }

    public void setLanguageDictCode(String languageDictCode) {
        MoreLanguageProperties.languageDictCode = languageDictCode;
    }

    public void setSaftyStandardCode(String saftyStandardCode) {
        MoreLanguageProperties.saftyStandardCode = saftyStandardCode;
    }

    public void setMultiSeparator(String multiSeparator) {
        MoreLanguageProperties.multiSeparator = multiSeparator;
    }

    public static String getMultiSeparator(String standardColumnCode) {
        return MoreLanguageProperties.specialStandardColumnCode.contains(standardColumnCode)
                ? MoreLanguageProperties.specialMultiSeparator :
                MoreLanguageProperties.multiSeparator;
    }

    public void setSpecialMultiSeparator(String specialMultiSeparator) {
        MoreLanguageProperties.specialMultiSeparator = specialMultiSeparator;
    }

    public void setSpecialStandardColumnCode(String specialStandardColumnCode) {
        MoreLanguageProperties.specialStandardColumnCode = specialStandardColumnCode;
    }

    public void setFieldValueSeparator(String fieldValueSeparator) {
        MoreLanguageProperties.fieldValueSeparator = fieldValueSeparator;
    }

    public void setShowInfoLanguageSeparator(String showInfoLanguageSeparator) {
        MoreLanguageProperties.showInfoLanguageSeparator = showInfoLanguageSeparator;
    }

    public void setCheckItemSeparator(String checkItemSeparator) {
        MoreLanguageProperties.checkItemSeparator = checkItemSeparator;
    }

    public void setCheckMsgItemSeparator(String checkMsgItemSeparator) {
        MoreLanguageProperties.checkMsgItemSeparator = checkMsgItemSeparator;
    }

    public void setCheckMultiItemSeparator(String checkMultiItemSeparator) {
        MoreLanguageProperties.checkMultiItemSeparator = checkMultiItemSeparator;
    }

    public void setCheckMergedSeparator(String checkMergedSeparator) {
        MoreLanguageProperties.checkMergedSeparator = checkMergedSeparator;
    }

    public void setNoDecorateCategory1stCode(String noDecorateCategory1stCode) {
        MoreLanguageProperties.noDecorateCategory1stCode = noDecorateCategory1stCode;
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

    public void setHangTagMainDbAlias(String hangTagMainDbAlias) {
        MoreLanguageProperties.hangTagMainDbAlias = hangTagMainDbAlias;
    }

    public void setMsgEnumMap(List<MoreLanguageMsgEnum> msgEnumList) {
        if (CollUtil.isNotEmpty(msgEnumList)) {
            msgEnumList.forEach(msgEnum-> {
                MoreLanguageProperties.msgEnumMap.putIfAbsent(msgEnum.name(),msgEnum.getText());
            });
        }
    }

    public void setExcelDataRowNum(Integer excelDataRowNum) {
        MoreLanguageProperties.excelDataRowNum = excelDataRowNum;
    }

    public void setImportExecutePoolSize(Integer importExecutePoolSize) {
        MoreLanguageProperties.importExecutePoolSize = importExecutePoolSize;
    }

    public void setStyleCountryStatusImportCountRange(Pair<Integer, Integer> styleCountryStatusImportCountRange) {
        MoreLanguageProperties.styleCountryStatusImportCountRange = styleCountryStatusImportCountRange;
    }

    public void setNotCheckStandardColumnCode(String notCheckStandardColumnCode) {
        MoreLanguageProperties.notCheckStandardColumnCode = notCheckStandardColumnCode;
    }

    public void setModelStandardColumnCode(String modelStandardColumnCode) {
        MoreLanguageProperties.modelStandardColumnCode = modelStandardColumnCode;
    }

    @Getter
    public enum MoreLanguageMsgEnum {
        NOT_INSERT("PDM未创建对应的语言号型数据"),
        HAVEN_T_TAG("不存在%s的吊牌信息"),
        HAVEN_T_LANGUAGE("%s不存在该语种"),
        HAVEN_T_CONTENT("%s未翻译"),
        HAVEN_T_AUDIT("%s未审核"),
        FIELD("字段"),
        CONTENT("内容"),
        STATUS("状态"),
        TIME("时间"),
        TRANSLATE("翻译"),
        CONTENT_FORMAT("%s%s%s"),
        HAVEN_T_COUNTRY_LANGUAGE("未查询到模板标准"),
        SUCCESS_IMPORT("您的吊牌信息已经导入成功. %s"),
        CHECK_REIMPORT("%s, 请问是否需要导入?"),
        FAILURE_IMPORT("导入失败, 请你根据导入规则进行导入\n%s"),
        EXCESS_STATUS_IMPORT("仅能导入%s条数据,后续款号不执行"),
        INCORRECT_COUNTRY_LANGUAGE("模板标准对应不上,请清理缓存"),
        EXIST_COUNTRY_LANGUAGE("已存在对应模板标准"),
        INCORRECT_STANDARD_CODE("非法标准列code"),
        NOT_FOUND_COUNTRY_LANGUAGE("未找到对应的模板标准数据"),
        NOT_EXIST_STANDARD_CODE("未设置表头,请找开发协助"),
        WARN_EXAMINE_STATUS("吊牌状态必须为待翻译确认之后"),
        NOT_EXIST_BULK_STATUS("失败,没有找到对应款号"),
        ERROR_STATUS("成功,审核流程仅进行到%s"),
        EXIST_STANDARD("已存在相同的标准表"),
        INCORRECT_IMPORT_MAPPING_KEY("%s隐藏列的关键映射值被修改,请重新导入"),
        HAVEN_T_IMPORT_FIRST_ROW("请勿删除导出模板%s的首行数据,请重新导出一份"),
        HAVEN_T_OPERA_LOG_NAME("缺少日志类型"),
        NOT_EXIST_CONTENT("【提示：%s为空】"),
        NOT_EXIST_HANG_TAG_TYPE("吊牌类型不能为空"),
        FILE_DOWNLOAD_FAILED("文件下载失败"),
        THE_FILE_DOES_NOT_EXIST("文件不存在"),
        FILE_EXPORT_FAILED("文件导出失败"),
        ;
        /** 文本 */
        private final String text;
        MoreLanguageMsgEnum(String text) {
            this.text = text;
        }
    }

}
