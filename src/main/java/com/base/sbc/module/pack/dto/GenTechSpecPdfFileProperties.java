package com.base.sbc.module.pack.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * {@code 描述：生成工艺单属性}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/11/29
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("gen.tech-spec")
public class GenTechSpecPdfFileProperties {

    public static String processFtlUrl;
    public static String processFobFtlUrl;

    public static String templateLinkType = "classes";
    public static String templateDirectoryBaseUrl;

    public void setProcessFtlUrl(String processFtlUrl) {
        GenTechSpecPdfFileProperties.processFtlUrl = processFtlUrl;
    }
    public void setProcessFobFtlUrl(String processFobFtlUrl) {
        GenTechSpecPdfFileProperties.processFobFtlUrl = processFobFtlUrl;
    }

    public void setTemplateLinkType(String templateLinkType) {
        GenTechSpecPdfFileProperties.templateLinkType = templateLinkType;
    }

    public void setTemplateDirectoryBaseUrl(String templateDirectoryBaseUrl) {
        GenTechSpecPdfFileProperties.templateDirectoryBaseUrl = templateDirectoryBaseUrl;
    }
}
