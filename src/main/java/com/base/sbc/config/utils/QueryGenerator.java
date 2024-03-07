package com.base.sbc.config.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.annotation.QueryField;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.dto.QueryFieldDto;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.module.column.entity.ColumnDefine;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class QueryGenerator {


    public static <T> boolean initQueryWrapper(BaseQueryWrapper<T> qw, QueryFieldDto dto) {
        String columnHeard = dto.getColumnHeard();

        Field[] declaredFields = dto.getClass().getDeclaredFields();

        boolean isColumnHeard = false;
        for (Field field : declaredFields) {
            //开启权限
            try {
                field.setAccessible(true);
                QueryField annotation = field.getAnnotation(QueryField.class);
                if (annotation != null && StrUtil.isNotEmpty(annotation.value())) {
                    String annotationValue = annotation.value();
                    Object o = field.get(dto);
                    //小漏斗筛选
                    if (ObjectUtil.isNotEmpty(o) && StrUtil.isNotEmpty(columnHeard) && field.getName().equals(columnHeard)) {
                        //2 进行模糊匹配，列名不为空，并且值不为空不等于列名，模糊匹配标识等于列名，第二步，并且列头去重
                        qw.select(" DISTINCT  IFNULL(" + annotation.value() + ", '') as " + field.getName());
                        qw.like(annotationValue, o);
                        isColumnHeard = true;
                    } else if (ObjectUtil.isNotEmpty(o) && field.getName().equals(o) && StrUtil.isEmpty(columnHeard)) {
                        //1 列头筛选，列名不为空，并且值等于列名，模糊匹配标识为空
                        qw.select(" DISTINCT  IFNULL(" + annotation.value() + ", '') as " + field.getName());
                        isColumnHeard = true;
                    } else if (ObjectUtil.isNotEmpty(o)) {
                        //3 选中数据查询，列名不为空，并且值不为空
                        //时间区间过滤
                        String property = annotation.property();
                        String s = String.valueOf(o);
                        if ("isNull".equals(s)) {
                            qw.isNullStr(annotation.value());
                        } else if ("isNotNull".equals(s)) {
                            qw.isNotNullStr(annotation.value());
                        } else if (StrUtil.isNotEmpty(property) && "date".equals(property)) {
                            String[] dateArr = s.split(",");
                            if (StrUtil.isNotEmpty(dateArr[0]) && StrUtil.isNotEmpty(dateArr[1])) {
                                dateArr[0] = dateArr[0] + " 00:00:00";
                                dateArr[1] = dateArr[1] + " 23:59:59";
                                qw.between(annotation.value(), dateArr);
                            }
                        } else if (StrUtil.isNotEmpty(property) && "replace".equals(property)) {
                            qw.in(annotation.value(), Arrays.asList(s.split(";")));
                        } else {
                            //正常保留历史条件查询
                            String[] lenStrArr = s.split(",");
                            if (lenStrArr.length > 0) {
                                qw.in(annotation.value(), lenStrArr);
                            }
                        }
                    }
                    //记得排序
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                field.setAccessible(false);
            }
        }
        return isColumnHeard;
    }

    public static <T> boolean initQueryWrapper1(BaseQueryWrapper<T> qw, QueryFieldDto dto) {
        String columnHeard = dto.getColumnHeard();
        Map<String, String> fieldQueryMap = dto.getFieldQueryMap();
        boolean isColumnHeard = false;

        RedisUtils redisUtils = SpringContextHolder.getBean("redisUtils");
        List<ColumnDefine> list = (List<ColumnDefine>) redisUtils.get(dto.getTableCode());

        for (ColumnDefine columnDefine : list) {
            String columnCode = columnDefine.getColumnCode();
            String sqlCode = columnDefine.getSqlCode();
            if (fieldQueryMap.containsKey(columnCode)) {
                String fieldValue = fieldQueryMap.get(columnCode);
                if (StrUtil.isNotEmpty(columnHeard) && columnCode.equals(columnHeard)) {
                    //2 进行模糊匹配，列名不为空，并且值不为空不等于列名，模糊匹配标识等于列名，第二步，并且列头去重
                    qw.select(" DISTINCT  IFNULL(" + sqlCode + ", '') as " + columnCode);
                    qw.like(sqlCode, fieldValue);
                    isColumnHeard = true;
                } else if (columnCode.equals(fieldValue) && StrUtil.isEmpty(columnHeard)) {
                    //1 列头筛选，列名不为空，并且值等于列名，模糊匹配标识为空
                    qw.select(" DISTINCT  IFNULL(" + sqlCode + ", '') as " + columnCode);
                    isColumnHeard = true;
                } else {
                    //3 选中数据查询，列名不为空，并且值不为空
                    //时间区间过滤
                    String property = columnDefine.getColumnFilter();
                    if ("isNull".equals(fieldValue)) {
                        qw.isNullStr(sqlCode);
                    } else if ("isNotNull".equals(fieldValue)) {
                        qw.isNotNullStr(sqlCode);
                    } else if (StrUtil.isNotEmpty(property) && "date".equals(property)) {
                        String[] dateArr = fieldValue.split(",");
                        if (StrUtil.isNotEmpty(dateArr[0]) && StrUtil.isNotEmpty(dateArr[1])) {
                            dateArr[0] = dateArr[0] + " 00:00:00";
                            dateArr[1] = dateArr[1] + " 23:59:59";
                            qw.between(sqlCode, dateArr);
                        }
                    } else if (StrUtil.isNotEmpty(property) && "replace".equals(property)) {
                        qw.in(sqlCode, Arrays.asList(fieldValue.split(";")));
                    } else {
                        //正常保留历史条件查询
                        qw.in(sqlCode, Arrays.asList(fieldValue.split(";")));
                    }
                }
                //记得排序
            }
        }
        return isColumnHeard;
    }

}