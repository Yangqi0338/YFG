package com.base.sbc.config.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.annotation.QueryField;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.dto.QueryFieldDto;
import com.base.sbc.module.column.entity.ColumnDefine;
import com.base.sbc.module.column.service.ColumnDefineService;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class QueryGenerator {

    /**
     * 特别注意，该方法中有查询  PageHelper 写到该方法下面
     *
     * @param qw
     * @param dto
     * @param <T>
     * @return
     */
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

    /**
     * 特别注意，该方法中有查询  PageHelper 写到该方法下面
     *
     * @param qw
     * @param dto
     * @param <T>
     * @return
     */
    public static <T> boolean initQueryWrapperByMap(BaseQueryWrapper<T> qw, QueryFieldDto dto) {
        String columnHeard = dto.getColumnHeard();
        Map<String, String> fieldQueryMap = dto.getFieldQueryMap();
        boolean isColumnHeard = false;

        ColumnDefineService columnDefineService = SpringContextHolder.getBean(ColumnDefineService.class);
        List<ColumnDefine> list = columnDefineService.getByTableCode(dto.getTableCode(), false);

        for (ColumnDefine columnDefine : list) {
            String columnCode = columnDefine.getColumnCode();
            String sqlCode = columnDefine.getSqlCode();
            if (fieldQueryMap.containsKey(columnCode)) {
                String fieldValue = fieldQueryMap.get(columnCode);
                String property = columnDefine.getProperty();
                if (StrUtil.isNotEmpty(columnHeard) && columnCode.equals(columnHeard)) {
                    //2 进行模糊匹配，列名不为空，并且值不为空不等于列名，模糊匹配标识等于列名，第二步，并且列头去重
                    if (StrUtil.isNotEmpty(property) && "insql".equals(property)) {
                        qw.last(columnDefine.getColumnFilter().replace("?", columnDefine.getColumnFilterExtent() + " like '%" + fieldValue + "%'"));
                    } else {
                        qw.like(sqlCode, fieldValue);
                    }
                    qw.select(" DISTINCT  IFNULL(" + sqlCode + ", '') as " + columnCode);
                    isColumnHeard = true;
                } else if (columnCode.equals(fieldValue) && StrUtil.isEmpty(columnHeard)) {
                    //1 列头筛选，列名不为空，并且值等于列名，模糊匹配标识为空
                    qw.select(" DISTINCT  IFNULL(" + sqlCode + ", '') as " + columnCode);
                    isColumnHeard = true;
                } else {
                    //3 选中数据查询，列名不为空，并且值不为空
                    //时间区间过滤
                    if ("isNull".equals(fieldValue)) {
                        if (StrUtil.isNotEmpty(property) && "insql".equals(property)) {
                            qw.last(columnDefine.getColumnFilter().replace("?",
                                    "(" + columnDefine.getColumnFilterExtent() + " = '' or " + columnDefine.getColumnFilterExtent() + "is null)"));
                        } else {
                            qw.isNullStr(sqlCode);
                        }
                    } else if ("isNotNull".equals(fieldValue)) {
                        if (StrUtil.isNotEmpty(property) && "insql".equals(property)) {
                            qw.last(columnDefine.getColumnFilter().replace("?",
                                    "(" + columnDefine.getColumnFilterExtent() + " != '' or " + columnDefine.getColumnFilterExtent() + "is not null)"));
                        } else {
                            qw.isNotNullStr(sqlCode);
                        }
                    } else if (StrUtil.isNotEmpty(property) && "date".equals(property)) {
                        String[] dateArr = fieldValue.split(",");
                        if (StrUtil.isNotEmpty(dateArr[0]) && StrUtil.isNotEmpty(dateArr[1])) {
                            dateArr[0] = dateArr[0] + " 00:00:00";
                            dateArr[1] = dateArr[1] + " 23:59:59";
                            qw.between(sqlCode, dateArr);
                        }
                    } else if (StrUtil.isNotEmpty(property) && "replace".equals(property)) {
                        qw.in(sqlCode, Arrays.asList(fieldValue.split(";")));
                    } else if (StrUtil.isNotEmpty(property) && "insql".equals(property)) {
                        String s = "'" + String.join("','", fieldValue.split(";")) + "'";
                        qw.last(columnDefine.getColumnFilter().replace("?", columnDefine.getColumnFilterExtent() + " in (" + s + ")"));
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