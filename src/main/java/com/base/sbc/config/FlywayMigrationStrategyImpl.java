package com.base.sbc.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.stereotype.Component;

/**
 * {@code 描述：}
 * @author KC
 * @since 2024/3/7
 * @CopyRight @ 广州尚捷科技有限公司
 */
@Component
public class FlywayMigrationStrategyImpl implements FlywayMigrationStrategy {
    @Override
    public void migrate(Flyway flyway) {
        flyway.setBaselineOnMigrate(true);
        flyway.setValidateOnMigrate(false);
//        flyway.setSchemas("flyway");
        flyway.setOutOfOrder(false);
        flyway.setEncoding("UTF-8");
        flyway.setBaselineVersion(MigrationVersion.fromVersion("1"));
//        clean-disabled: true
        flyway.setTable("t_schema_history");
//        flyway.setLocations();
        flyway.migrate();
    }
}
