package com.base.sbc.config.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.dto.QueryFieldDto;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.column.entity.ColumnDefine;
import com.base.sbc.module.column.service.ColumnDefineService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryGenerator {


    public static <T> boolean initQueryWrapperByMap(BaseQueryWrapper<T> qw, QueryFieldDto dto) {
        return initQueryWrapperByMap(qw, dto, true);
    }

    public static <T> boolean initQueryWrapperByMapNoDataPermission(BaseQueryWrapper<T> qw, QueryFieldDto dto) {
        return initQueryWrapperByMap(qw, dto, false);
    }

    /**
     * 特别注意，该方法中有查询  PageHelper 写到该方法下面
     *
     * @param qw
     * @param dto
     * @param <T>
     * @return
     */
    public static <T> boolean initQueryWrapperByMap(BaseQueryWrapper<T> qw, QueryFieldDto dto, boolean dataPermission) {
        if (StrUtil.isNotEmpty(dto.getTableCode()) && dataPermission) {
            //添加数据权限
            DataPermissionsService dataPermissionsService = SpringContextHolder.getBean(DataPermissionsService.class);
            dataPermissionsService.getDataPermissionsForQw(qw, dto.getTableCode());
        }
        if (StrUtil.isEmpty(dto.getTableCode()) || (MapUtil.isEmpty(dto.getFieldQueryMap()) && MapUtil.isEmpty(dto.getFieldOrderMap()))) {
            return false;
        }
        String columnHeard = dto.getColumnHeard();
        Map<String, String> fieldQueryMap = dto.getFieldQueryMap();
        Map<String, String> fieldOrderMap = dto.getFieldOrderMap();
        boolean isColumnHeard = false;

        ColumnDefineService columnDefineService = SpringContextHolder.getBean(ColumnDefineService.class);
        List<ColumnDefine> list = columnDefineService.getByTableCode(dto.getTableCode(), false);

        for (ColumnDefine columnDefine : list) {
            String columnCode = columnDefine.getColumnCode();
            String sqlCode = columnDefine.getSqlCode();
            String fieldValue = MapUtil.getStr(fieldQueryMap, columnCode);
            if (StrUtil.isNotBlank(fieldValue)) {
                String property = columnDefine.getProperty();
                if (StrUtil.isEmpty(property) && StrUtil.isNotEmpty(columnDefine.getColumnType()) && "date".equals(columnDefine.getColumnType())) {
                    property = "date";
                }
                if (StrUtil.isNotEmpty(columnHeard) && columnCode.equals(columnHeard)) {
                    //2 进行模糊匹配，列名不为空，并且值不为空不等于列名，模糊匹配标识等于列名，第二步，并且列头去重
                    if (StrUtil.isNotEmpty(property) && "insql".equals(property)) {
                        qw.inSql(sqlCode, columnDefine.getColumnFilter().replace("?", columnDefine.getColumnFilterExtent() + " like '%" + fieldValue + "%'"));
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
                        if (StrUtil.isNotEmpty(property) && ("insql".equals(property) || "date_insql".equals(property))) {
                            if ("insql".equals(property)) {
                                qw.notInSql(sqlCode, columnDefine.getColumnFilter().replace("?", " 1 = 1"))
                                        .or().inSql(sqlCode, columnDefine.getColumnFilter().replace("?", columnDefine.getColumnFilterExtent() + " is null or "
                                                + columnDefine.getColumnFilterExtent() + " = ''"));
                            } else {
                                qw.notInSql(sqlCode, columnDefine.getColumnFilter().replace("?", " 1 = 1"))
                                        .or().inSql(sqlCode, columnDefine.getColumnFilter().replace("?", columnDefine.getColumnFilterExtent() + " is null"));
                            }
                        } else if (StrUtil.isNotEmpty(property) && "date".equals(property)) {
                            qw.isNull(sqlCode);
                        } else {
                            qw.isNullStr(sqlCode);
                        }
                    } else if ("isNotNull".equals(fieldValue)) {
                        if (StrUtil.isNotEmpty(property) && ("insql".equals(property) || "date_insql".equals(property))) {
                            if ("insql".equals(property)) {
                                qw.inSql(sqlCode, columnDefine.getColumnFilter().replace("?", columnDefine.getColumnFilterExtent() + " is not null and "
                                        + columnDefine.getColumnFilterExtent() + " != ''"));
                            } else {
                                qw.inSql(sqlCode, columnDefine.getColumnFilter().replace("?", columnDefine.getColumnFilterExtent() + " is not null"));
                            }
                        } else if (StrUtil.isNotEmpty(property) && "date".equals(property)) {
                            qw.isNotNull(sqlCode);
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
                        qw.inSql(sqlCode, columnDefine.getColumnFilter().replace("?", columnDefine.getColumnFilterExtent() + " in (" + s + ")"));
                    } else if (StrUtil.isNotEmpty(property) && "date_insql".equals(property)) {
                        String[] dateArr = fieldValue.split(",");
                        if (StrUtil.isNotEmpty(dateArr[0]) && StrUtil.isNotEmpty(dateArr[1])) {
                            qw.inSql(sqlCode, columnDefine.getColumnFilter().replace("?", columnDefine.getColumnFilterExtent() + " >= '" + dateArr[0] + "' and '" + dateArr[1] + "' >= " + columnDefine.getColumnFilterExtent()));
                        }
                    } else {
                        //正常保留历史条件查询
                        List<String> collect = Arrays.stream(fieldValue.split(",")).map(o -> o.replace("@#", ",")).collect(Collectors.toList());
                        qw.in(sqlCode, collect);
                    }
                }
            }
            String fieldOrder = MapUtil.getStr(fieldOrderMap, columnCode);
            if (StrUtil.isNotBlank(fieldOrder)) {
                if (StrUtil.isBlank(sqlCode)) MapUtil.removeAny(fieldOrderMap, columnCode);
                else MapUtil.renameKey(fieldOrderMap, columnCode, sqlCode);
            }
            //记得排序
            if (StrUtil.isNotEmpty(dto.getOrderBy()) && dto.getOrderBy().equals(columnCode)) {
                //qw.order(sqlCode);
            }
        }
        dto.setColumnGroupSearch(isColumnHeard);
        return isColumnHeard;
    }

    /**
     * 报表参数验证
     *
     * @param bulkStyleNos 大货款号 （限制2000个）
     * @param year         年份
     * @param season       季节
     */
    public static void reportParamBulkStyleNosCheck(List<String> bulkStyleNos, String year, String season) {

        boolean yearIsNotBool = StrUtil.isNotEmpty(year) && StrUtil.isEmpty(season);
        boolean seasonIsNotBool = StrUtil.isEmpty(year) && StrUtil.isNotEmpty(season);
        boolean bulkStyleNosEmpty = CollUtil.isEmpty(bulkStyleNos);
        boolean yearEmpty = StrUtil.isEmpty(year);
        boolean seasonEmpty = StrUtil.isEmpty(season);
        if (!bulkStyleNosEmpty && bulkStyleNos.size() > 2000) {
            throw new OtherException("大货款号最多输入2000个！");
        }
        if (bulkStyleNosEmpty && (yearIsNotBool || seasonIsNotBool)) {
            throw new OtherException("当大货款号为空的时候,年份-季节必须一起绑定查询！");
        }
        if (bulkStyleNosEmpty && yearEmpty && seasonEmpty) {
            throw new OtherException("请输入大货款号或年份-季节参数查询！");
        }
    }

    /**
     * 报表参数验证
     *
     * @param materialNos 物料号 （限制2000个）
     * @param year        年份
     * @param season      季节
     */
    public static void reportParamMaterialsNoCheck(List<String> materialNos, String year, String season) {

        boolean yearIsNotBool = StrUtil.isNotEmpty(year) && StrUtil.isEmpty(season);
        boolean seasonIsNotBool = StrUtil.isEmpty(year) && StrUtil.isNotEmpty(season);
        boolean materialNosEmpty = CollUtil.isEmpty(materialNos);
        boolean yearEmpty = StrUtil.isEmpty(year);
        boolean seasonEmpty = StrUtil.isEmpty(season);
        if (!materialNosEmpty && materialNos.size() > 2000) {
            throw new OtherException("物料号最多输入2000个！");
        }
        if (materialNosEmpty && (yearIsNotBool || seasonIsNotBool)) {
            throw new OtherException("当物料号为空的时候,年份-季节必须一起绑定查询！");
        }
        if (materialNosEmpty && yearEmpty && seasonEmpty) {
            throw new OtherException("请输入物料号或年份-季节参数查询！");
        }
    }
}