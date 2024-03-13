package com.base.sbc.config.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.dto.QueryFieldDto;
import com.base.sbc.module.column.entity.ColumnDefine;
import com.base.sbc.module.column.service.ColumnDefineService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryGenerator {

    /**
     * 特别注意，该方法中有查询  PageHelper 写到该方法下面
     *
     * @param qw
     * @param dto
     * @param <T>
     * @return
     */
    public static <T> boolean initQueryWrapperByMap(BaseQueryWrapper<T> qw, QueryFieldDto dto) {
        if (StrUtil.isEmpty(dto.getTableCode()) || MapUtil.isEmpty(dto.getFieldQueryMap())) {
            return false;
        }
        String columnHeard = dto.getColumnHeard();
        Map<String, String> fieldQueryMap = dto.getFieldQueryMap();
        boolean isColumnHeard = false;

        ColumnDefineService columnDefineService = SpringContextHolder.getBean(ColumnDefineService.class);
        List<ColumnDefine> list = columnDefineService.getByTableCode(dto.getTableCode(), false);

        for (ColumnDefine columnDefine : list) {
            String columnCode = columnDefine.getColumnCode();
            String sqlCode = columnDefine.getSqlCode();
            if (fieldQueryMap.containsKey(columnCode) && StrUtil.isNotEmpty(fieldQueryMap.get(columnCode))) {
                String fieldValue = fieldQueryMap.get(columnCode);
                String property = columnDefine.getProperty();
                if (StrUtil.isNotEmpty(columnHeard) && columnCode.equals(columnHeard)) {
                    //2 进行模糊匹配，列名不为空，并且值不为空不等于列名，模糊匹配标识等于列名，第二步，并且列头去重
                    if (StrUtil.isNotEmpty(property) && "insql".equals(property)) {
                        qw.last(columnDefine.getColumnFilter().replace("?", columnDefine.getColumnFilterExtent() + " like '%" + fieldValue + "%'"));
                        qw.select(sqlCode, "count(" + sqlCode + ") as groupCount");
                        qw.groupBy(sqlCode);
                        qw.orderByAsc(sqlCode);
                        dto.setQueryFieldColumn(columnCode);
                    } else {
                        qw.like(sqlCode, fieldValue);
                        qw.select(sqlCode + " as " + columnCode, "count(" + sqlCode + ") as groupCount");
                        qw.groupBy(sqlCode);
                        qw.orderByAsc(sqlCode);
                    }
                    isColumnHeard = true;
                } else if (columnCode.equals(fieldValue) && StrUtil.isEmpty(columnHeard)) {
                    //1 列头筛选，列名不为空，并且值等于列名，模糊匹配标识为空
                    if (StrUtil.isNotEmpty(property) && "insql".equals(property)) {
                        qw.select(sqlCode, "count(" + sqlCode + ") as groupCount");
                        qw.groupBy(sqlCode);
                        qw.orderByAsc(sqlCode);
                        dto.setQueryFieldColumn(columnCode);
                    } else {
                        qw.select(sqlCode + " as " + columnCode, "count(" + sqlCode + ") as groupCount");
                        qw.groupBy(sqlCode);
                        qw.orderByAsc(sqlCode);
                    }
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
                        String s = "'" + String.join("','", fieldValue.split(",")) + "'";
                        qw.last(columnDefine.getColumnFilter().replace("?", columnDefine.getColumnFilterExtent() + " in (" + s + ")"));
                    } else if (StrUtil.isNotEmpty(property) && "date_insql".equals(property)) {
                        String[] dateArr = fieldValue.split(",");
                        if (StrUtil.isNotEmpty(dateArr[0]) && StrUtil.isNotEmpty(dateArr[1])) {
                            qw.last(columnDefine.getColumnFilter().replace("?", columnDefine.getColumnFilterExtent() + " >= '" + dateArr[0] + "' and '" + dateArr[1] + "' >= " + columnDefine.getColumnFilterExtent()));
                        }
                    } else {
                        //正常保留历史条件查询
                        List<String> collect = Arrays.stream(fieldValue.split(",")).map(o -> o.replace("@#", ",")).collect(Collectors.toList());
                        qw.in(sqlCode, collect);
                    }
                }
            }
            //记得排序
            if (StrUtil.isNotEmpty(dto.getOrderBy()) && dto.getOrderBy().equals(columnCode)) {
                //qw.order(sqlCode);
            }
        }
        return isColumnHeard;
    }

}