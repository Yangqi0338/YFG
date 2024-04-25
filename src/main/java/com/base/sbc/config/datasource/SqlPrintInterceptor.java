package com.base.sbc.config.datasource;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.config.common.annotation.DataIsolation;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.SqlProperties;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.config.utils.SpringContextHolder;
import com.base.sbc.module.httplog.entity.HttpLog;
import com.base.sbc.module.pushrecords.entity.PushRecords;
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
import java.lang.reflect.Method;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

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

    private static final Log logger = LogFactory.getLog(SqlPrintInterceptor.class);

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private HttpServletRequest httpServletRequest;

    @Resource
    private SqlProperties sqlProperties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        //先拦截到RoutingStatementHandler，里面有个StatementHandler类型的delegate变量，其实现类是BaseStatementHandler，然后就到BaseStatementHandler的成员变量mappedStatement
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        String statementId = mappedStatement.getId();
        BoundSql boundSql = statementHandler.getBoundSql();

        //获取到原始sql语句
        String sql = boundSql.getSql();
        try {
            String userId = httpServletRequest.getHeader("userId");
            String usercompany = httpServletRequest.getHeader("Usercompany");
            String authorization = httpServletRequest.getHeader("Authorization");
            String sqlCommandType = mappedStatement.getSqlCommandType().toString();
            if (!StringUtils.isBlank(usercompany) && !StringUtils.isBlank(userId) && !StringUtils.isBlank(authorization) && !"INSERT".equals(sqlCommandType)){
                getAuthoritySql( boundSql, statementId, mappedStatement, sql,sqlCommandType);
            }
        }catch (Exception e){
            logger.error("无法数据隔离");
        }


        Configuration configuration = mappedStatement.getConfiguration();

        Object parameterObject = boundSql.getParameterObject();
        String sql1 = getSql(boundSql, parameterObject, configuration);
        Object proceed = invocation.proceed();
        if (sqlProperties.getExcludePrintList().contains(sql1)) {
            return proceed;
        }
        // 如果是一些记录类的,如请求日志,推送日志等就不记录了
        if (StrUtil.contains(sql1,"t_http_log") || StrUtil.contains(sql1,"t_push_record")) {
            return proceed;
        }

        long end = System.currentTimeMillis();
        long timing = end - start;
        if (logger.isInfoEnabled()) {
            logger.info("\n执行sql耗时:" + timing + " ms" + "  方法ID: " + statementId + "\nSQL语句:" + sql1);
        }
        //记录sql信息
        UserCompany userCompany = companyUserInfo.get();
        if (userCompany != null) {
            HttpLog httpLog = userCompany.getHttpLog();

            JSONArray jsonArray = new JSONArray();
            if (httpLog.getSqlLog() != null) {
                jsonArray = JSON.parseArray(httpLog.getSqlLog());
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sql", sql1);
            jsonObject.put("time", timing);
            jsonObject.put("statementId", statementId);

            jsonArray.add(jsonObject);
            httpLog.setSqlLog(jsonArray.toJSONString());
        }


        return proceed;
    }
    private void getAuthoritySql(BoundSql boundSql, String statementId, MappedStatement mappedStatement, String sql,String sqlCommandType){
        try {
            //注解逻辑判断  添加注解了才拦截
            //1.获取目标类上的目标注解（可判断目标类是否存在该注解）
            String funName = statementId.substring(statementId.lastIndexOf(".") + 1);
            DataIsolation dataIsolation = getDataIsolationAnnotation(mappedStatement);
            boolean isExecute = false;
            if (!Objects.isNull(dataIsolation) && dataIsolation.state() && StringUtils.isNotBlank(dataIsolation.authority())) {
                isExecute = ObjectUtils.isEmpty(dataIsolation.groups()) || arrSearch(dataIsolation.groups(), funName);
            }
            if (isExecute) {
                //获取当前用户id
                String userId = httpServletRequest.getHeader("userId");
                String usercompany = httpServletRequest.getHeader("Usercompany");
                //当前查询语句的主表
                Map sqlAnalyst = getTable(sql,sqlCommandType);
                //是否有where
                boolean isWhere = (boolean) sqlAnalyst.get("where");
                String tablePre = (String) sqlAnalyst.get("table");
//              nageGroupRoleImp manageGroupRole = SpringContextHolder.getBean("manageGroupRoleImp");

                List<String>  sqlArr = (List<String>) sqlAnalyst.get("sqlArr");
                RedisUtils redisUtils = SpringContextHolder.getBean("redisUtils");

                //sql语句类型 select、delete、insert、update
                String operateType= "SELECT".equals(sqlCommandType)?"read":"write";
                DataPermissionsService dataPermissionsService = SpringContextHolder.getBean("dataPermissionsService");
                Map<String,Object> entity=dataPermissionsService.getDataPermissionsForQw(usercompany,userId,dataIsolation.authority(),operateType,tablePre,dataIsolation.authorityFields(),dataIsolation.isAssignFields());

                String authorityField = entity.containsKey("authorityField")?(String)entity.get("authorityField"):null;
                boolean authorityState=entity.containsKey("authorityState")?(Boolean)entity.get("authorityState"):false;
                String whereFlag = " ";
                if (authorityState && StringUtils.isNotBlank(authorityField)) {
                    whereFlag +=  authorityField + " and ";
                }
                if(!authorityState){
                    whereFlag += " 1=0 and ";
                }
                if(StringUtils.isNotBlank(whereFlag)){
                    whereFlag=" where "+whereFlag;
                    String mSql = isWhere ? (sqlArr.get(0) + " " + whereFlag + " " + sqlArr.get(1)) : (sql + " " + whereFlag);
                    //通过反射修改sql语句
                    Field field = boundSql.getClass().getDeclaredField("sql");
                    field.setAccessible(true);
                    field.set(boundSql, mSql);
                }

            }
        } catch (Exception e) {
            logger.error("  方法ID: " + statementId + "数据隔离时：");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * 获取方法上的DataIsolation注解
     *
     * @param mappedStatement MappedStatement
     * @return EncryptResultFieldAnnotation注解
     */
    private DataIsolation getDataIsolationAnnotation(MappedStatement mappedStatement) {
        DataIsolation annotation = null;
        try {
            String id = mappedStatement.getId();
            String className = id.substring(0, id.lastIndexOf("."));
            String methodName = id.substring(id.lastIndexOf(".") + 1);
            methodName= methodName.contains("_") ?methodName.substring(0,methodName.indexOf("_")):methodName;
            Class<?> classType = Class.forName(className);
            final Method[] method = classType.getMethods();
            for (Method me : method) {
                if (me.getName().equals(methodName) && me.isAnnotationPresent(DataIsolation.class)) {
                    return me.getAnnotation(DataIsolation.class);
                }
            }
            annotation=AnnotationUtils.findAnnotation(classType, DataIsolation.class);
        } catch (Exception ex) {
            logger.error("", ex);
        }
        return annotation;
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
        for (String s : arr) {
            if (s.equals("'" + targetValue + "'")) {
                return true;
            }
        }
        return false;
    }

    private Map getTable(String sql, String sqlCommandType) {
        sql = sql.replaceAll(" {2,}", " ").replaceAll("\\( ", "\\(").replaceAll("\\) ", "\\)").replaceAll("select ","SELECT ").replaceAll(" from "," FROM ").replaceAll(" join "," JOIN ").replaceAll(" as "," AS ").replaceAll(" where "," WHERE ");
        sql=disposeSql(sql,true);
        Map<String,Object> ret = new HashMap<>(10);
        String table = "";
        String fromStr;
        String fromSuf="";
        if("SELECT".equalsIgnoreCase(sqlCommandType)){
            int fromCount=searchStrBycount(sql," FROM ");
            if(fromCount>0){
                int start;
                int end;
                int index=8;
                fromfor:for (int i = 0; i < fromCount; i++) {
                    fromStr=sql.substring(0,sql.indexOf(" FROM ",index));
                    start=searchStrBycount(fromStr,"\\(");
                    end=searchStrBycount(fromStr,"\\)");
                    if(start==end){
                        fromSuf=sql.substring(sql.indexOf(" FROM ",index)+6);
                        String[] arrAs=fromSuf.split(" ");
                        for (int j = 1; j <arrAs.length-1; j++) {
                            if("AS".equals(arrAs[j])){
                                table=arrAs[j+1];
                                break fromfor;
                            }else if("WHERE".equals(arrAs[j+1]) || "JOIN".equals(arrAs[j+1]) || (j+2<arrAs.length && "JOIN".equals(arrAs[j+2]))){
                                table=arrAs[j];
                                break fromfor;
                            }
                        }
                        break;
                    }
                    index=fromStr.length()+1;
                }
                ret=disposeWhere(fromSuf,sql);
            }

        }else {
            sql=sql.replaceAll("update ","UPDATE ").replaceAll("delete ","DELETE ").replaceAll("insert ","INSERT ");
            ret=disposeWhere(sql,sql);
        }
        ret.put("table", StringUtils.isNotBlank(table) ? table + "." : "");
        return ret;
    }
    private Map<String,Object> disposeWhere(String fromSuf, String sql){
        Map ret = new HashMap<>(10);
        List<String> sqlArr=new ArrayList<>();
        boolean isWhere = false;
        int start;
        int end;
        int index=0;
        String whereStr;
        int whereCount=searchStrBycount(fromSuf," WHERE ");
        if(whereCount>0){
            wherefor:for (int i = 0; i < whereCount; i++) {
                whereStr=fromSuf.substring(0,fromSuf.indexOf(" WHERE ",index));
                start=searchStrBycount(whereStr,"\\(");
                end=searchStrBycount(whereStr,"\\)");
                if(start==end){
                    String whereSuf=sql.substring(sql.indexOf(" WHERE ",index));
                    sqlArr.add(disposeSql(sql.split(whereSuf)[0],false));
                    sqlArr.add(disposeSql(whereSuf.substring(7),false));
                    break wherefor;
                }
                index=whereStr.length()+1;
            }
            isWhere=true;
        }
        ret.put("where", isWhere);
        ret.put("sqlArr",sqlArr);
        return ret;
    }
    private String disposeSql(String sql, Boolean type){
        if(type){
            sql=sql.replaceAll("\\?"," ::;,11,;:: ")
                    .replaceAll("\\+"," ::;,22,;:: ")
                    .replaceAll("\\|"," ::;,33,;:: ")
                    .replaceAll("\\."," ::;,44,;:: ")
                    .replaceAll("\\*"," ::;,55,;:: ")
                    .replaceAll("\\$"," ::;,66,;:: ")
                    .replaceAll("\\("," ::;,77,;:: ")
                    .replaceAll("\\)"," ::;,88,;:: ");
        }else {
            sql=sql.replaceAll(" ::;,11,;:: ","\\?")
                    .replaceAll(" ::;,22,;:: ","\\+")
                    .replaceAll(" ::;,33,;:: ","\\|")
                    .replaceAll(" ::;,44,;:: ","\\.")
                    .replaceAll(" ::;,55,;:: ","\\*")
                    .replaceAll(" ::;,66,;::","\\$")
                    .replaceAll(" ::;,77,;:: ","\\(")
                    .replaceAll(" ::;,88,;::","\\) ");
        }
        return sql;
    }
    //查询str中有几个searchStr
    private int searchStrBycount(String str, String searchStr){
        Pattern pattern = Pattern.compile(searchStr);
        Matcher matcher = pattern.matcher(str);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
    private String getSql(BoundSql boundSql, Object parameterObject, Configuration configuration) {
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        if (parameterMappings != null) {
            for (ParameterMapping parameterMapping : parameterMappings) {
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
