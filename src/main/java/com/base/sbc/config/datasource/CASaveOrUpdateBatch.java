package com.base.sbc.config.datasource;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.base.sbc.config.annotation.DuplicateSql;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.lang.reflect.Field;

/**
 * 批量插入更新方法实现
 */
public class CASaveOrUpdateBatch extends AbstractMethod {

    protected CASaveOrUpdateBatch() {
        super(CASaveOrUpdateBatch.class.getSimpleName());
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        final String sql = "<script>insert into %s %s values %s ON DUPLICATE KEY UPDATE %s</script>";
        final String tableName = tableInfo.getTableName();
        final String filedSql = prepareFieldSql(tableInfo);
        final String modelValuesSql = prepareModelValuesSql(tableInfo);
        final String modelKeySql = prepareDuplicateKeySql(modelClass);
        final String duplicateKeySql = prepareDuplicateKeySql(tableInfo);
        final String sqlResult = String.format(sql, tableName, filedSql, modelValuesSql, StringUtils.isBlank(modelKeySql) ? duplicateKeySql : modelKeySql);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sqlResult, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, this.methodName, sqlSource, new NoKeyGenerator(), null, null);
    }
 
    /**
     * 根据自定义字段更新
     *
     * @param modelClass
     * @return
     */
    private String prepareDuplicateKeySql(Class<?> modelClass) {
        final StringBuilder duplicateKeySql = new StringBuilder();
        // 获取所有的字段信息
        Field[] declaredFields = modelClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            DuplicateSql duplicateSql = declaredField.getAnnotation(DuplicateSql.class);

            if (ObjectUtil.isNotEmpty(duplicateSql)) {
                String columnName = duplicateSql.columnName();
                duplicateKeySql.append(columnName).append("=values(").append(columnName).append("),");
            }
        }
        if (StringUtils.isNotBlank(duplicateKeySql)) {
            duplicateKeySql.delete(duplicateKeySql.length() - 1, duplicateKeySql.length());
        }
        return duplicateKeySql.toString();
    }
 
    /**
     * 准备ON DUPLICATE KEY UPDATE sql
     * 根据全字段更新(不推荐使用)
     *
     * @param tableInfo
     * @return
     */
    private String prepareDuplicateKeySql(TableInfo tableInfo) {
        final StringBuilder duplicateKeySql = new StringBuilder();
        if (!StringUtils.isEmpty(tableInfo.getKeyColumn())) {
            duplicateKeySql.append(tableInfo.getKeyColumn()).append("=values(").append(tableInfo.getKeyColumn()).append("),");
        }
 
        tableInfo.getFieldList().forEach(x -> {
            duplicateKeySql.append(x.getColumn())
                .append("=values(")
                .append(x.getColumn())
                .append("),");
        });
        duplicateKeySql.delete(duplicateKeySql.length() - 1, duplicateKeySql.length());
        return duplicateKeySql.toString();
    }
 
    /**
     * 准备属性名
     *
     * @param tableInfo
     * @return
     */
    private String prepareFieldSql(TableInfo tableInfo) {
        StringBuilder fieldSql = new StringBuilder();
        fieldSql.append(tableInfo.getKeyColumn()).append(",");
        tableInfo.getFieldList().forEach(x -> {
            fieldSql.append(x.getColumn()).append(",");
        });
        fieldSql.delete(fieldSql.length() - 1, fieldSql.length());
        fieldSql.insert(0, "(");
        fieldSql.append(")");
        return fieldSql.toString();
    }
 
    private String prepareModelValuesSql(TableInfo tableInfo) {
        final StringBuilder valueSql = new StringBuilder();
        valueSql.append("<foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" separator=\"),(\" close=\")\">");
        if (!StringUtils.isEmpty(tableInfo.getKeyProperty())) {
            valueSql.append("#{item.").append(tableInfo.getKeyProperty()).append("},");
        }
        tableInfo.getFieldList().forEach(x -> valueSql.append("#{item.").append(x.getProperty()).append("},"));
        valueSql.delete(valueSql.length() - 1, valueSql.length());
        valueSql.append("</foreach>");
        return valueSql.toString();
    }
}