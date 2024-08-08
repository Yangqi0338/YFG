package com.base.sbc.config;

import com.base.sbc.config.constant.DbSyncProperties;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

import static com.base.sbc.config.constant.Constants.COMMA;

/**
 * {@code 描述：生成工艺单属性}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/11/29
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = DbSyncProperties.KEY, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties
@DependsOn({DbSyncProperties.KEY})
public class DbSyncConfig {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(prefix = DbSyncProperties.KEY, name = "name", havingValue = FlywayEnableConfiguration.KEY)
    public static class FlywayEnableConfiguration {
        public final static String KEY = "flyway";

        @Bean
        public FlywayMigrationStrategy flywayMigrationStrategy(){
            return flyway -> {
                ClassicConfiguration configuration = (ClassicConfiguration) flyway.getConfiguration();
                configuration.setTable(DbSyncProperties.table);
                configuration.setLocationsAsStrings(DbSyncProperties.locations.split(COMMA));
                if (configuration.isValidateOnMigrate()) {
                    try {
                        flyway.validate();
                        flyway.migrate();
                    }catch (FlywayException e) {
                        flyway.repair();
                    }
                }
            };
        }
    }

    @Bean
    public FlywayMigrationInitializer flywayInitializer(Flyway flyway, ObjectProvider<FlywayMigrationStrategy> migrationStrategy) {
        return new FlywayMigrationInitializer(flyway,migrationStrategy.getIfAvailable()){
            @Override
            public void afterPropertiesSet() throws Exception {
                if (DbSyncProperties.name.equals(FlywayEnableConfiguration.KEY)) {
                    super.afterPropertiesSet();
                }
            }
        };
    }

}
