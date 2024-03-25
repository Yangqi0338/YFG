package com.base.sbc.config.constant;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
@ConfigurationProperties("business")
public class BusinessProperties {

    public static String tablePrefix = "t_";


    public void setTablePrefix(String tablePrefix) {
        BusinessProperties.tablePrefix = tablePrefix;
    }

}
