//package com.base.sbc.config.datasource;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import javax.sql.DataSource;
//import java.sql.SQLException;
///**
// * @author Fred
// * @data 创建时间:2020/2/3
// */
//@Configuration
//public class DatasourceConfig {
//	private Logger logger = LoggerFactory.getLogger(DatasourceConfig.class);
//
//    @Value("${spring.datasource.type}")
//    private String dbType;
//
//    @Value("${spring.datasource.initialSize}")
//    private int initialSize;
//
//    @Value("${spring.datasource.minIdle}")
//    private int minIdle;
//
//    @Value("${spring.datasource.maxActive}")
//    private int maxActive;
//
//    @Value("${spring.datasource.maxWait}")
//    private int maxWait;
//
//    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
//    private int timeBetweenEvictionRunsMillis;
//
//    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
//    private int minEvictableIdleTimeMillis;
//
//    @Value("${spring.datasource.validationQuery}")
//    private String validationQuery;
//
//    @Value("${spring.datasource.testWhileIdle}")
//    private boolean testWhileIdle;
//
//    @Value("${spring.datasource.testOnBorrow}")
//    private boolean testOnBorrow;
//
//    @Value("${spring.datasource.testOnReturn}")
//    private boolean testOnReturn;
//
//    @Value("${spring.datasource.poolPreparedStatements}")
//    private boolean poolPreparedStatements;
//
//    @Value("${spring.datasource.filters}")
//    private String filters;
//
//
//
//    @Bean(name="dataSource")
//    @Primary
//    public DataSource dataSource(DataSource dataSource){
//        DruidDataSource datasource = (DruidDataSource) dataSource;
//        try {
//	        datasource.setDbType(dbType);
//	        datasource.setInitialSize(initialSize);
//	        datasource.setMinIdle(minIdle);
//	        datasource.setMaxActive(maxActive);
//	        datasource.setMaxWait(maxWait);
//	        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
//	        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
//	        datasource.setValidationQuery(validationQuery);
//	        datasource.setTestWhileIdle(testWhileIdle);
//	        datasource.setTestOnBorrow(testOnBorrow);
//	        datasource.setTestOnReturn(testOnReturn);
//	        datasource.setPoolPreparedStatements(poolPreparedStatements);
//            datasource.setFilters(filters);
//        } catch (SQLException e) {
//            logger.error("druid configuration initialization filter", e);
//        }
//        return datasource;
//    }
//}