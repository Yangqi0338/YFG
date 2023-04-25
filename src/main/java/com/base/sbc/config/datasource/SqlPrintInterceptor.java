package com.base.sbc.config.datasource;

import com.base.sbc.config.common.annotation.DataIsolation;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.config.utils.SpringContextHolder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;

/**
 * MyBatis 将mybatis要执行的sql拦截打印出来
 *
 * @since 1.0.0
 */

@Component
@Intercepts({
        @Signature(
                type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class
        })
})
public class SqlPrintInterceptor implements Interceptor {

    private static Log logger = LogFactory.getLog(SqlPrintInterceptor.class);

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private HttpServletRequest httpServletRequest;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        //先拦截到RoutingStatementHandler，里面有个StatementHandler类型的delegate变量，其实现类是BaseStatementHandler，然后就到BaseStatementHandler的成员变量mappedStatement
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        //id为执行的mapper方法的全路径名，如com.uv.dao.UserMapper.insertUser
        String statementId = mappedStatement.getId();
        //sql语句类型 select、delete、insert、update
        String sqlCommandType = mappedStatement.getSqlCommandType().toString();
        BoundSql boundSql = statementHandler.getBoundSql();

        //获取到原始sql语句
        String sql = boundSql.getSql();
        String mSql = sql;

        try {
            //注解逻辑判断  添加注解了才拦截
            //1.获取目标类上的目标注解（可判断目标类是否存在该注解）
            String className = statementId.substring(0, statementId.lastIndexOf("."));
            String funName = statementId.substring(statementId.lastIndexOf(".")+1);
            Class classType =  Class.forName(className);
            DataIsolation dataIsolation = AnnotationUtils.findAnnotation(classType, DataIsolation.class);
            boolean isExecute = false;
            if (!Objects.isNull(dataIsolation) && dataIsolation.state() && sqlCommandType.equals("SELECT")) {
                isExecute = true;
                if(!ObjectUtils.isEmpty(dataIsolation.groups()) && ! arrSearch(dataIsolation.groups(),funName)){
                    isExecute = false;
                }
            }
            if(isExecute){
                //获取当前用户id
                String userId = httpServletRequest.getHeader("userId");
                String authorization = httpServletRequest.getHeader("Authorization");
                if(!StringUtils.isBlank(authorization) && !StringUtils.isBlank(userId)){
                    sql = sql.replaceAll("[\\s]+", " ").replaceAll("\\( ", "\\(").toLowerCase();
                    //当前查询语句的主表
                    Map sqlAnalyst = getTable(sql);
                    boolean isWhere = (boolean) sqlAnalyst.get("where");
                    String whereFlag = " where ";
                    String tableFlag = (String) sqlAnalyst.get("table");
                    String[] sqlArr = isWhere?sql.split(((String) sqlAnalyst.get("whereSql"))+"where"):null;
                    ManageGroupRoleImp manageGroupRole =  SpringContextHolder.getBean("manageGroupRoleImp");
                    RedisUtils redisUtils =  SpringContextHolder.getBean("redisUtils");
                    List<String> userList = null;
                    if(!redisUtils.hasKey("system_setting:user_isolation:"+userId)){
                        Map entity = (Map)manageGroupRole.userDataIsolation(authorization,null,dataIsolation.toString(),userId,className);
                        //默认开启角色的数据隔离
                        userList = (Boolean)entity.get("success")?(List<String>) entity.get("data"):null;
                        redisUtils.set("system_setting:user_isolation:"+userId,userList,60 * 3);//如果是新加入的人添加信息，数据的隔离3分钟后生效
                    }else{
                        userList = (List<String>) redisUtils.get("system_setting:user_isolation:"+userId);
                    }
                    if(!Objects.isNull(userList)){
                        whereFlag += tableFlag+"create_id in ("+StringUtils.join(userList, ",")+") and ";
                    }else{
                        whereFlag += tableFlag+"create_id = null and ";
                    }
                    mSql = sqlArr!=null? (sqlArr[0] +" "+(String) sqlAnalyst.get("whereSql") + whereFlag + sqlArr[1]):(sql+" "+whereFlag);
                    //通过反射修改sql语句
                    Field field = boundSql.getClass().getDeclaredField("sql");
                    field.setAccessible(true);
                    field.set(boundSql, mSql);
                }
            }
        }catch (Exception e){
            logger.error("  方法ID: " + statementId + "数据隔离时：");
            logger.error(e.getMessage());
        }

        Configuration configuration = mappedStatement.getConfiguration();
        Object parameterObject = null;
        if (invocation.getArgs().length > 1) {
            parameterObject = invocation.getArgs()[1];
        }
        String sql1 = getSql(boundSql, parameterObject, configuration);
        long end = System.currentTimeMillis();
        long timing = end - start;
        if(logger.isInfoEnabled()){
            logger.info("\n执行sql耗时:" + timing + " ms" + "  方法ID: " + statementId + "\nSQL语句:" + sql1);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }

    }

    @Override
    public void setProperties(Properties properties) {
    }
    private boolean arrSearch(String[] arr, String targetValue) {
        for(String s: arr){
            if(s.equals("'"+targetValue+"'")) {
                return true;
            }
        }
        return false;
    }

    private Map getTable(String sql){
        Map ret = new HashMap<>();
        String[] selectArr = sql.split("\\(select");
        String table = "";
        String fromStr = "";
        if(selectArr[0].indexOf("from") > -1){
            fromStr = (selectArr[0].split("from"))[1];
        }else{
            int fromSum = 0;
            String[] fromArr = null;
            for (int i = 1; i < selectArr.length; i++) {
                fromArr = selectArr[i].split("from");
                fromSum  += fromArr.length-1;
                if(fromSum == i+1){
                    fromStr = fromArr[fromArr.length-1];
                    break;
                }
            }
        }
        if(fromStr.indexOf("join")> -1){
            table = fromStr.split(" ")[2];
        }
        if(fromStr.indexOf("join")==-1 && !(fromStr.split(" ")[2].equals("where"))){
            table = fromStr.split(" ")[2];
        }
        if(table.equals("as")){
            table = fromStr.split(" ")[3];
        }
        boolean isWhere = fromStr.indexOf("where")> -1;
        ret.put("table",table!=""?table+".":"");
        ret.put("where",isWhere);
        ret.put("whereSql",isWhere?fromStr.split("where")[0]:"");
        return ret;
    }

    private String getSql(BoundSql boundSql, Object parameterObject, Configuration configuration) {
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        if (parameterMappings != null) {
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        MetaObject metaObject = configuration.newMetaObject(parameterObject);
                        value = metaObject.getValue(propertyName);
                    }
                    sql = replacePlaceholder(sql, value);
                }
            }
        }
        return sql;
    }

    private String replacePlaceholder(String sql, Object propertyValue) {
        String result;
        if (propertyValue != null) {
            if (propertyValue instanceof String) {
                result = "'" + propertyValue + "'";
            } else if (propertyValue instanceof Date) {
                result = "'" + DATE_FORMAT.format(propertyValue) + "'";
            } else {
                result = propertyValue.toString();
            }
        } else {
            result = "null";
        }
        return sql.replaceFirst("\\?", Matcher.quoteReplacement(result));
    }
}
