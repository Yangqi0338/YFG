package com.base.sbc.config.constant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.redis.RedisKeyBuilder;
import com.base.sbc.config.redis.RedisKeyConstant;
import com.base.sbc.config.redis.RedisStaticFunUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.standard.entity.StandardColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.INCORRECT_STANDARD_CODE;
import static com.base.sbc.config.constant.SmpProperties.SystemEnums.*;

/**
 * {@code 描述：生成工艺单属性}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/11/29
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(SmpProperties.PREFIX)
public class SmpProperties {
    public static final String PREFIX = "interface";

    private static String DELIMITER = "/";

    public static void setDelimiter(String delimiter) {
        SmpProperties.DELIMITER = delimiter;
    }

    private static Map<SystemEnums, InterfaceConfig> urlMap =
            Arrays.stream(values()).collect(Collectors.toMap(Function.identity(), (it)-> new InterfaceConfig()));

    public void setUrlMap(Map<SystemEnums, InterfaceConfig> urlMap) {
        SmpProperties.urlMap = urlMap;
        urlMap.forEach((systemEnums, interfaceConfig) -> {
            UrlConfig config = interfaceConfig.getConfig();
            if (StrUtil.isNotBlank(config.getUrl())) {
                systemEnums.baseUrl = config.getUrl();
            }
            if (StrUtil.isNotBlank(config.getName())) {
                systemEnums.name = config.getName();
            }
            if (config.getRetryNum() != null) {
                systemEnums.retryNum = config.getRetryNum();
            }
            if (StrUtil.isNotBlank(config.getRetryTime())) {
                systemEnums.retryTime = config.getRetryTime();
            }
        });
    }

    @Data
    @NoArgsConstructor
    public static class InterfaceConfig {
        @NestedConfigurationProperty
        private UrlConfig config = new UrlConfig();
        private Map<SystemModuleEnums, Config> modules = Arrays.stream(SystemModuleEnums.values()).collect(Collectors.toMap(Function.identity(), (it)-> new Config()));

        public void setModules(Map<SystemModuleEnums, Config> modules) {
            this.modules = modules;
            modules.forEach((systemModuleEnums, config) -> {
                if (StrUtil.isNotBlank(config.getUrl())) {
                    systemModuleEnums.baseUrl = config.getUrl();
                }
                if (StrUtil.isNotBlank(config.getName())) {
                    systemModuleEnums.name = config.getName();
                }
                if (config.getRetryNum() != null) {
                    systemModuleEnums.retryNum = config.getRetryNum();
                }
                if (StrUtil.isNotBlank(config.getRetryTime())) {
                    systemModuleEnums.retryTime = config.getRetryTime();
                }
//                buildUrl(systemModuleEnums, config.getUrls());
            });
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Config extends UrlConfig {
        private List<UrlConfig> urls;
    }

    @Data
    public static class UrlConfig {
        private String url;
        private String name;
        private List<String> excludeErrorCodes;

        private Integer retryNum = 3;
        private String retryTime = "180";
    }

    @Getter
    @NoArgsConstructor
    public enum SystemEnums {
        /** 系统枚举 */
        SCM("http://10.8.250.100:1980/"),
        SMP("http://10.98.250.31:7006/"),
        OA("http://10.8.240.161:40002/"),
        ;
        private String baseUrl;
        private String name;
        private Integer retryNum = 3;
        private String retryTime = "180";

        SystemEnums(String baseUrl) {
            this.baseUrl = baseUrl;
            this.name = this.name().toLowerCase();
        }
    }

    @Getter
    public enum SystemModuleEnums {
        /** 模块枚举 */
        APP_INFORMATION(SCM,"/escm-app/information/pdm/"),
        PDM(SMP,"/pdm/"),
        MPS_SAMPLE(OA,"/mps-interfaces/sample/"),
        NEW_MF_FAC(SCM,"/new-mf-fac/"),
        APP_BILL(SCM,"/escm-app/bill/"),
        ;
        private final SystemEnums systemEnums;
        private String baseUrl;
        private String name;
        private Integer retryNum;
        private String retryTime;

        SystemModuleEnums(SystemEnums systemEnums, String baseUrl) {
            this.systemEnums = systemEnums;
            this.baseUrl = baseUrl;
            this.name = this.name().toLowerCase();
        }
    }

    public static String SCM_APP_INFORMATION_URL = buildUrl(SCM.baseUrl, SystemModuleEnums.APP_INFORMATION.baseUrl);
    public static String SMP_PDM_URL = buildUrl(SMP.baseUrl, SystemModuleEnums.PDM.baseUrl);
    public static String OA_MPS_SAMPLE_URL = buildUrl(OA.baseUrl, SystemModuleEnums.MPS_SAMPLE.baseUrl);
    public static String SCM_NEW_MF_FAC_URL = buildUrl(SCM.baseUrl, SystemModuleEnums.NEW_MF_FAC.baseUrl);
    public static String SCM_APP_BILL_URL = buildUrl(SCM.baseUrl, SystemModuleEnums.APP_BILL.baseUrl);

    public static String SCM_NEW_MF_FAC_PRODUCTION_IN_URL = buildUrl(SCM_NEW_MF_FAC_URL, "/v1/api/facPrdOrder/saveFacPrdOrder");
    public static String SCM_NEW_MF_FAC_CANCEL_PRODUCTION_URL = buildUrl(SCM_NEW_MF_FAC_URL, "/v1/api/facPrdOrder/facPrdOrderUpCheck");

    public static String SCM_APP_BILL_PRODUCTION_BUDGET_LIST_URL = buildUrl(SCM_APP_BILL_URL, "/productionBudget/option/List");

    public static String SMP_PDM_GOODS_URL = buildUrl(SMP_PDM_URL, "/goods");
    public static String SMP_PDM_MATERIALS_URL = buildUrl(SMP_PDM_URL, "/materials");
    public static String SMP_PDM_BOM_URL = buildUrl(SMP_PDM_URL, "/bom");
    public static String SMP_PDM_COLOR_URL = buildUrl(SMP_PDM_URL, "/color");
    public static String SMP_PDM_PROCESS_SHEET_URL = buildUrl(SMP_PDM_URL, "/processSheet");

    public static String buildUrl(String... urls){
        StrJoiner urlJoiner = CommonUtils.strJoin(DELIMITER);
        for (String url : urls) {
            urlJoiner.append(StrUtil.removeSuffix(StrUtil.removePrefix(url, DELIMITER), DELIMITER));
        }
        return urlJoiner.toString();
    }

    public static void buildUrl(SystemModuleEnums systemModuleEnums, UrlConfig... urlConfigs){

    }

}
