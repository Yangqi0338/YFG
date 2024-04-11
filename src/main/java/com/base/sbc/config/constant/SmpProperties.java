package com.base.sbc.config.constant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.redis.RedisKeyBuilder;
import com.base.sbc.config.redis.RedisKeyConstant;
import com.base.sbc.config.redis.RedisStaticFunUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.standard.entity.StandardColumn;
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
@ConfigurationProperties("interface")
public class SmpProperties {
    private static String DELIMITER = "/";

    public static String SCM_BASE_URL = SystemEnums.SCM.baseUrl;
    public static String SMP_BASE_URL = SystemEnums.SMP.baseUrl;
    public static String OA_BASE_URL = SystemEnums.OA.baseUrl;

    public void setScmBaseUrl(String scmBaseUrl) {
        SCM_BASE_URL = scmBaseUrl;
    }
    public void setScmSmpUrl(String scmSmpUrl) {
        SMP_BASE_URL = scmSmpUrl;
    }
    public void setScmOaUrl(String scmOaUrl) {
        OA_BASE_URL = scmOaUrl;
    }

    public static String SCM_URL = buildUrl(SCM_BASE_URL, "/escm-app/information/pdm/");
    public static String SMP_URL = buildUrl(SMP_BASE_URL, "/pdm/");
    public static String OA_URL = buildUrl(OA_BASE_URL, "/mps-interfaces/sample/");
    public static String SCM_MF_URL = buildUrl(SCM_BASE_URL, "/new-mf-fac/");

    public void setScmUrl(String scmUrl) {
        SCM_URL = buildUrl(SCM_BASE_URL, scmUrl);
    }
    public void setSmpUrl(String smpUrl) {
        SMP_URL = buildUrl(SMP_BASE_URL, smpUrl);
    }
    public void setOaUrl(String oaUrl) {
        OA_URL = buildUrl(OA_BASE_URL, oaUrl);
    }
    public void setScmMfUrl(String scmMfUrl) {
        SCM_MF_URL = buildUrl(SCM_BASE_URL, scmMfUrl);
    }

    public static String SCM_MF_PRODUCTION_IN_URL = "/v1/api/facPrdOrder/facPrdOrderUpCheck";
    public static String SCM_MF_CANCEL_PRODUCTION_URL = "/v1/api/facPrdOrder/saveFacPrdOrder";

    public void setScmMfProductionInUrl(String scmMfProductionInUrl) {
        SCM_MF_PRODUCTION_IN_URL = buildUrl(SCM_MF_URL, scmMfProductionInUrl);
    }
    public void setScmMfCancelProductionUrl(String scmMfCancelProductionUrl) {
        SCM_MF_CANCEL_PRODUCTION_URL = buildUrl(SCM_MF_URL, scmMfCancelProductionUrl);
    }

    @Getter
    public enum SystemEnums {
        SCM("http://10.8.250.100:1980/", "20","3"),
        SMP("http://10.98.250.31:7006/", "20","3"),
        OA("http://10.8.240.161:40002/", "20","3"),
        ;
        /** 基础连接 */
        private final String baseUrl;
        private final String maxWaitTime;
        private final String retryNum;
        SystemEnums(String baseUrl, String maxWaitTime, String retryNum) {
            this.baseUrl = baseUrl;
            this.maxWaitTime = maxWaitTime;
            this.retryNum = retryNum;
        }
    }

    public static String buildUrl(String... urls){
        StrJoiner urlJoiner = CommonUtils.strJoin(DELIMITER);
        for (String url : urls) {
            urlJoiner.append(StrUtil.removeSuffix(StrUtil.removePrefix(url, DELIMITER), DELIMITER));
        }
        return urlJoiner.toString();
    }

}
