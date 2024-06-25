package com.base.sbc.config.constant;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.constant.SmpProperties.SystemModuleEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UrlConfig {
    private String url;
    private String name;
    private List<String> excludeErrorCodes;

    private Integer retryNum;
    private String retryTime;

    public static UrlConfig of(String url) {
        return UrlConfig.of(url, null);
    }

    public static UrlConfig of(String url, String name) {
        UrlConfig config = new UrlConfig();
        config.setUrl(url);
        config.setName(name);
        return config;
    }

    public UrlConfig(SystemModuleEnum moduleEnum) {
        this.setUrl(moduleEnum.getUrl());
        if (StrUtil.isBlank(this.getName())) {
            this.setName(moduleEnum.getModuleName());
        }
        if (this.getRetryNum() == null) {
            this.setRetryNum(moduleEnum.getRetryNum());
        }
        if (StrUtil.isBlank(this.getRetryTime())) {
            this.setRetryTime(moduleEnum.getRetryTime());
        }
    }

    public String getUrl() {
        return Opt.ofBlankAble(url).orElse("");
    }

    @Override
    public String toString() {
        return getUrl();
    }
}