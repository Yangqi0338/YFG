package com.base.sbc.config.constant;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConfigurationProperties(DbSyncProperties.KEY)
public class DbSyncProperties {
    public final static String KEY = "spring.db-sync";

    public static String name = "flyway";
    public static String locations = "classpath:db/migration";
    public static String table = "t_schema_history";

    public void setName(String name) {
        DbSyncProperties.name = name;
    }

    public void setLocations(String locations) {
        DbSyncProperties.locations = locations;
    }

    public void setTable(String table) {
        DbSyncProperties.table = table;
    }
}
