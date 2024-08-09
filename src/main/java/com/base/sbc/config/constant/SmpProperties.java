package com.base.sbc.config.constant;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;


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

    private static final Map<SystemModuleEnum, InterfaceConfig> urlMap =
            getSystemEnumList().stream().collect(Collectors.toMap(Function.identity(), InterfaceConfig::new));

    public void setUrlMap(Map<SystemModuleEnum, InterfaceConfig> urlMap) {
        urlMap.forEach((key, config) -> {
            Map.Entry<SystemModuleEnum, InterfaceConfig> entry = SmpProperties.urlMap.entrySet().stream().filter(it -> it.getKey() == key).findFirst().get();
            SystemModuleEnum systemEnum = entry.getKey();
            if (StrUtil.isBlank(config.getName())) {
                config.setName(systemEnum.getModuleName());
            }
            if (config.getRetryNum() == null) {
                config.setRetryNum(systemEnum.getRetryNum());
            }
            if (StrUtil.isBlank(config.getRetryTime())) {
                config.setRetryTime(systemEnum.getRetryTime());
            }
            SmpProperties.urlMap.put(systemEnum.decorate(config),config);
        });
    }

    public static List<SystemModuleEnum> getSystemEnumList() {
        return Arrays.stream(SystemModuleEnum.values()).filter(SystemModuleEnum::isSystem).collect(Collectors.toList());
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class InterfaceConfig extends UrlConfig {
        private Map<SystemModuleEnum, InterfaceConfig> modules;
        private Map<String, UrlConfig> urlConfigMap;

        public InterfaceConfig(SystemModuleEnum moduleEnum) {
            super(moduleEnum);
            this.setModules(
                    Arrays.stream(SystemModuleEnum.values()).filter(it-> it.parentModelEnum == moduleEnum)
                            .collect(Collectors.toMap(SystemModuleEnum::name, InterfaceConfig::new))
            );
        }

        public InterfaceConfig(UrlConfig urlConfig) {
            this.setUrl(urlConfig.getUrl());
            this.setExcludeErrorCodes(urlConfig.getExcludeErrorCodes());
            this.setName(urlConfig.getName());
            this.setRetryNum(urlConfig.getRetryNum());
            this.setRetryTime(urlConfig.getRetryTime());
        }

        public void setModules(Map<String, InterfaceConfig> modules) {
            this.modules = new HashMap<>();
            this.urlConfigMap = new HashMap<>();
            modules.forEach((modelEnumName, config) -> {
                if (MapUtil.isNotEmpty(config.getUrlConfigMap())) {
                    SystemModuleEnum moduleEnum = SystemModuleEnum.valueOf(modelEnumName);
                    moduleEnum.decorate(config);
                    if (StrUtil.isBlank(this.getName())) {
                        config.setName(moduleEnum.getModuleName());
                    }
                    if (this.getRetryNum() == null) {
                        config.setRetryNum(moduleEnum.getRetryNum());
                    }
                    if (StrUtil.isBlank(this.getRetryTime())) {
                        config.setRetryTime(moduleEnum.getRetryTime());
                    }
                    this.modules.put(moduleEnum, config);
                }else {
                    this.urlConfigMap.put(modelEnumName, config);
                }
            });
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    @Getter
    @AllArgsConstructor
    public enum SystemModuleEnum {
        /** 系统枚举 (不要相信这个URL, 只是默认值, 有可能通过Nacos配置进行了修改) */
        SCM(null,"http://10.8.250.100:1980/"),
        SMP(null,"http://10.98.250.31:7006/"),
        OA(null,"http://10.8.240.161:40002/"),
        SCM1(null,"http://10.98.250.71:9900/"),

        /** 模块枚举 (不要相信这个URL, 只是默认值, 有可能通过Nacos配置进行了修改) */
        APP_INFORMATION(SCM,"/escm-app/information/pdm/"),
        PDM(SMP,"/pdm/"),
        MPS_SAMPLE(OA,"/mps-interfaces/sample/"),
        NEW_MF_FAC(SCM,"/new-mf-fac/"),
        APP_BILL(SCM,"/escm-app/bill/"),
        ;
        private final SystemModuleEnum parentModelEnum;
        private String url;
        private String moduleName;
        private Integer retryNum = 3;
        private String retryTime = "180";

        SystemModuleEnum(SystemModuleEnum moduleEnum, String url) {
            this.url = url;
            this.moduleName = this.name();
            this.parentModelEnum = moduleEnum;
        }

        public boolean isSystem(){
            return this.parentModelEnum == null;
        }

        public SystemModuleEnum decorate(UrlConfig config){
            if (StrUtil.isBlank(this.url) && StrUtil.isNotBlank(config.getUrl())) {
                this.url = config.getUrl();
            }
            if (StrUtil.isBlank(this.moduleName) && StrUtil.isNotBlank(config.getName())) {
                this.moduleName = config.getName();
            }
            if (this.retryNum == null && config.getRetryNum() != null) {
                this.retryNum = config.getRetryNum();
            }
            if (StrUtil.isBlank(this.retryTime) && StrUtil.isNotBlank(config.getRetryTime())) {
                this.retryTime = config.getRetryTime();
            }
            return this;
        }

    }

    public static UrlConfig SCM_NEW_MF_FAC_PRODUCTION_IN_URL = UrlConfig.of("/v1/api/facPrdOrder/saveFacPrdOrder");
    public static UrlConfig SCM_NEW_MF_FAC_CANCEL_PRODUCTION_URL = UrlConfig.of("/v1/api/facPrdOrder/facPrdOrderUpCheck");
    public static UrlConfig SCM1_SPARE_URL = UrlConfig.of("/spare/trace/yflList");

    public static UrlConfig SCM_APP_BILL_PRODUCTION_BUDGET_LIST_URL = UrlConfig.of("/productionBudget/option/List");

    public static UrlConfig SMP_PDM_GOODS_URL = UrlConfig.of("/goods");
    public static UrlConfig SMP_PDM_MATERIALS_URL = UrlConfig.of("/materials");
    public static UrlConfig SMP_PDM_BOM_URL = UrlConfig.of("/bom");
    public static UrlConfig SMP_PDM_COLOR_URL = UrlConfig.of("/color");
    public static UrlConfig SMP_PDM_PROCESS_SHEET_URL = UrlConfig.of("/processSheet");

    @PostConstruct
    public void init(){
        // 获取所有静态变量 以_URL结尾的, 变量类型是UrlConfig
        List<Field> fieldList = Arrays.stream(SmpProperties.class.getDeclaredFields()).filter(it ->
                it.getName().endsWith("_URL") && Modifier.isStatic(it.getModifiers()) && UrlConfig.class.isAssignableFrom(it.getType())
        ).peek(it-> it.setAccessible(true)).collect(Collectors.toList());
        fieldList.forEach(field -> {
            for (Map.Entry<SystemModuleEnum, InterfaceConfig> entry : SmpProperties.urlMap.entrySet()) {
                if (setUrl(field, entry.getValue(), entry.getKey().toString() + "_")) break;
            }
        });
    }

    @SneakyThrows
    private static boolean setUrl(Field field, InterfaceConfig config, String preName){
        String fieldName = field.getName();
        if (config == null || !fieldName.startsWith(preName)) return false;

        Map<SystemModuleEnum, InterfaceConfig> modules = config.getModules();
        Map<String, UrlConfig> urlConfigMap = config.getUrlConfigMap();
        if (MapUtil.isNotEmpty(modules)) {
            for (Map.Entry<SystemModuleEnum, InterfaceConfig> entry : modules.entrySet()) {
                InterfaceConfig value = BeanUtil.copyProperties(entry.getValue(),InterfaceConfig.class);
                value.setUrl(StrUtil.removeSuffix(config.getUrl(),"/") + "/" + StrUtil.removePrefix( value.getUrl(), "/"));
                if (setUrl(field, value, preName + entry.getKey().toString() + "_")) return true;
            }
        }

        Object obj = null;
        UrlConfig urlConfig = (UrlConfig) field.get(obj);
        String name = StrUtil.removeSuffix(StrUtil.removePrefix(fieldName,preName),"_URL");
        if (MapUtil.isNotEmpty(urlConfigMap) && urlConfigMap.containsKey(name)) {
            urlConfig = urlConfigMap.get(name);
        }
        if (!urlConfig.toString().startsWith(config.toString())) {
            urlConfig.setUrl(StrUtil.removeSuffix(config.getUrl(),"/") + "/" + StrUtil.removePrefix( urlConfig.getUrl(), "/"));
        }
        urlConfig.setUrl(urlConfig.getUrl());
        field.set(obj,urlConfig);
        return true;
    }

}
