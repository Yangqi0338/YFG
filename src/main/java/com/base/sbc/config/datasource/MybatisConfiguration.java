package com.base.sbc.config.datasource;

import com.alibaba.druid.support.http.WebStatFilter;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.base.sbc.config.AutoFillFieldValueConfig;
import com.github.pagehelper.PageHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * mybatis的相关配置设置
 *
 * @author Jfei
 */
@Configuration
//@AutoConfigureAfter(DatasourceConfig.class)
@EnableTransactionManagement
public class MybatisConfiguration implements TransactionManagementConfigurer {

    private static Log logger = LogFactory.getLog(MybatisConfiguration.class);

    /**
     * 配置类型别名
     */
    @Value("${mybatis.typeAliasesPackage}")
    private String typeAliasesPackage;

    /**
     * 配置mapper的扫描，找到所有的mapper.xml映射文件
     */
    @Value("${mybatis.mapperLocations}")
    private String mapperLocations;

    /**
     * 加载全局的配置文件
     */
    @Value("${mybatis.configLocation}")
    private String configLocation;

    @Autowired
    private DataSource dataSource;

    @javax.annotation.Resource
    private AutoFillFieldValueConfig autoFillFieldValueConfig;

    /**
     * 提供SqlSeesion
     *
     * @return
     */
    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory() {
        try {
            MybatisSqlSessionFactoryBean sessionFactoryBean = new MybatisSqlSessionFactoryBean();
            sessionFactoryBean.setDataSource(dataSource);
            // 读取配置
            sessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);

            //设置mapper.xml文件所在位置
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(mapperLocations);
            sessionFactoryBean.setMapperLocations(resources);
            //设置mybatis-config.xml配置文件位置
            sessionFactoryBean.setConfigLocation(new DefaultResourceLoader().getResource(configLocation));
            //获取mybatis-plus全局配置

            GlobalConfig globalConfig = GlobalConfigUtils.defaults();
            //mybatis-plus全局配置设置元数据对象处理器为自己实现的那个
            globalConfig.setMetaObjectHandler(autoFillFieldValueConfig);
            globalConfig.setSqlInjector(sqlInjector());
            //mybatisSqlSessionFactoryBean关联设置全局配置
            sessionFactoryBean.setGlobalConfig(globalConfig);

            //添加分页插件、打印sql插件
            Interceptor[] plugins = new Interceptor[]{pageHelper(), sqlPrintInterceptor(), sqlVersionInterceptor()};
            sessionFactoryBean.setPlugins(plugins);

            return sessionFactoryBean.getObject();
        } catch (IOException e) {
            logger.error("mybatis resolver mapper*xml is error", e);
            return null;
        } catch (Exception e) {
            logger.error("mybatis sqlSessionFactoryBean create error", e);
            return null;
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 事务管理
     * @return
     */
    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 将要执行的sql进行日志打印(不想拦截，就把这方法注释掉)
     * @return
     */
    @Bean
    public SqlPrintInterceptor sqlPrintInterceptor() {
        return new SqlPrintInterceptor();
    }

    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        p.setProperty("returnPageInfo", "check");
        p.setProperty("params", "count=countSql");
        pageHelper.setProperties(p);
        return pageHelper;
    }

    @Bean
    public FilterRegistrationBean druidStatFilter() {

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        filterRegistrationBean.setName("druidWebStatFilter");
        //添加过滤规则.
        filterRegistrationBean.addUrlPatterns("/*");
        //添加忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

    @Bean
    public AbstractSqlInjector sqlInjector(){
        return new AbstractSqlInjector() {
            @Override
            public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
                List<AbstractMethod> methodList = new DefaultSqlInjector().getMethodList(mapperClass, tableInfo);
                methodList.add(new SaveOrUpdateBatch());
                methodList.add(new InsertBatchSomeColumn());
                return methodList;
            }
        };
    }

    /**
     * version 乐观锁插件
     * @return
     */
    @Bean
    public SqlVersionInterceptor sqlVersionInterceptor () {
        SqlVersionInterceptor interceptor = new SqlVersionInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
}
