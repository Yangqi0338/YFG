package com.base.sbc.config.common;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.segments.NormalSegmentList;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.base.sbc.config.common.base.BaseGlobal;
import com.google.common.collect.Sets;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 卞康
 * @date 2023/6/12 19:36:07
 * @mail 247967116@qq.com
 */
@Data
@NoArgsConstructor
public class BaseQueryWrapper<T> extends QueryWrapper<T> {
    public BaseQueryWrapper(T entity, Class<T> entityClass, AtomicInteger paramNameSeq,
                         Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString paramAlias,
                         SharedString lastSql, SharedString sqlComment, SharedString sqlFirst) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.paramAlias = paramAlias;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
    }

    public QueryWrapper<T> notEmptyEq(String column, Object val) {
        return this.eq(!StringUtils.isEmpty(val), column, val);
    }

    public QueryWrapper<T> notNullEq(String column, Object val) {
        this.eq(ObjectUtil.isNotEmpty(val), column, val);
        return this;
    }

    public QueryWrapper<T> notEmptyIn(String column, Collection<?> coll) {
        if (coll == null) {
            return this;
        }
        return this.in(CollUtil.isNotEmpty(coll), column, coll);
    }

    public QueryWrapper<T> notEmptyIn(String column, String str) {
        if (StrUtil.isBlank(str)) {
            return this;
        }
        return this.in(StrUtil.isNotBlank(str), column, StrUtil.split(str, CharUtil.COMMA));
    }

    public QueryWrapper<T> notEmptyLike(String column, Object val) {
        return this.like(!StringUtils.isEmpty(val), column, val);
    }

    public QueryWrapper<T> notEmptyLikeOrIsNull(String column, Object val) {
        if(!StringUtils.isEmpty(val) && BaseGlobal.NULL_KEY.equals(val)){
            return this.isNullStr(column);
        }else{
            return this.like(!StringUtils.isEmpty(val), column, val);
        }
    }

    public QueryWrapper<T> notEmptyEqOrIsNull(String column, Object val) {
        if(!StringUtils.isEmpty(val) && BaseGlobal.NULL_KEY.equals(val)){
            return this.isNullStr(column);
        }else{
            return this.eq(!StringUtils.isEmpty(val), column, val);
        }
    }

    public QueryWrapper<T> between(String column, String[] strings) {
        if (strings != null && strings.length > 0) {
            this.ge(!StringUtils.isEmpty(strings[0]), column, strings[0]);
            if (strings.length > 1) {
                return this.and(i -> i.le(!StringUtils.isEmpty(strings[1]), column, strings[1]));
            }else {
                return this.and(i -> i.le(!StringUtils.isEmpty(strings[0]), column, strings[0]));
            }
        }
        return this;
    }

    public QueryWrapper<T> andLike(String value, String... columns) {
        if (StrUtil.isBlank(value)) {
            return this;
        }
        this.and(wrapper -> {
            wrapper.and(i -> {
                for (int j = 0; j < columns.length; j++) {
                    i.like(columns[j], value).or(j < columns.length - 1);
                }
            });
        });
        return this;
    }

    public QueryWrapper<T> notNull(String column) {
        this.isNotNull(column);
        this.ne(column, "");
        return this;
    }

    public QueryWrapper<T> isNullStr(String column) {
        this.and(qw -> qw.isNull(column).or(qw2 -> qw2.eq(column, "")));
        return this;
    }

    public QueryWrapper<T> isNullStrEq(String column,String value) {
        this.and(qw -> qw.isNull(column).or(qw2 -> qw2.eq(column, value)));
        return this;
    }

    public QueryWrapper<T> isNotNullStr(String column) {
        this.and(qw -> qw.isNotNull(column).and(qw2 -> qw2.ne(column, "")));
        return this;
    }

    /**
     * findInSet
     * 转为
     * and ( FIND_IN_SET('1',column) or FIND_IN_SET('2',column) )
     *
     * @param column 字典
     * @param value  1,2
     * @return
     */
    public QueryWrapper<T> findInSet(String column, String value) {
        if (StrUtil.isBlank(value)) {
            return this;
        }
        List<String> split = StrUtil.split(value, CharUtil.COMMA);
        this.and(wrapper -> {

            wrapper.and(i -> {
                for (int k = 0; k < split.size(); k++) {
                    i.apply("FIND_IN_SET({0}," + column + ")", split.get(k)).or(k < split.size() - 1);
                }
            });
        });
        return this;
    }

    /**
     * 模糊搜索一个字段为集合
     * @param columns
     * @param val
     * @return
     */
    public QueryWrapper<T> likeList(String columns, List<String> val) {
        if (CollUtil.isEmpty(val)) {
            return this;
        }
        this.and(wrapper -> {
            wrapper.and(i -> {
                for (int j = 0; j < val.size(); j++) {
                    String value = val.get(j);
                    i.like(StrUtil.isNotBlank(value),columns, value).or(j < val.size() - 1);
                }
            });
        });
        return this;
    }

    /**
     * 模糊搜索一个字段为集合
     * @param columns
     * @param val
     * @return
     */
    public QueryWrapper<T> likeList(String columns, String val) {
       if (StringUtils.isEmpty(val)){
           return this;
       }
       return this.likeList(columns, StrUtil.split(val, CharUtil.COMMA));
    }

    /**
     * 判断时间区间 或者时间是否为空
     * @param columns
     * @param val
     * @param flag
     * @return
     */
    public QueryWrapper<T> dateBetweenOrIsNull(String columns, String val, String flag) {
        if (!StringUtils.isEmpty(val)) {
            return this.between(columns, val.split(","));
        }
        if(StrUtil.equals(flag, BaseGlobal.IN)){
            return this.isNull(columns);
        }
        if(StrUtil.equals(flag,BaseGlobal.YES)){
            return this.isNotNull(columns);
        }
        return this;
    }

    /**
     * 模糊搜索一个字段为集合
     * @param columns
     * @param val
     * @return
     */
    public QueryWrapper<T> likeList(boolean b,String columns, List<String> val) {
        if(!b){
            return this;
        }
        if (CollUtil.isEmpty(val)) {
            return this;
        }
        this.and(wrapper -> {
            wrapper.and(i -> {
                for (int j = 0; j < val.size(); j++) {
                    i.like(columns, val.get(j)).or(j < val.size() - 1);
                }
            });
        });
        return this;
    }

    private Map<String,String> groupSqlSegment = new HashMap<>();

    /** 适配count */
    public Map<String,String> getGroupSqlSegment(){
        if (MapUtil.isNotEmpty(groupSqlSegment)) {
            return groupSqlSegment;
        }
        Map<String,StringJoiner> map = new HashMap<>();
        MergeSegments expression = getExpression();
        List<ISqlSegment> linkKeyword = Arrays.asList(SqlKeyword.AND, SqlKeyword.OR);
        if (Objects.nonNull(expression)) {
            NormalSegmentList normal = expression.getNormal();
            // 遍历
            StringJoiner joiner = new StringJoiner(StringPool.SPACE);
            AtomicInteger handlerNum = new AtomicInteger(0);
            for (int i = 0, normalSize = normal.size() - 1; i <= normalSize; i++) {
                boolean lastElement = i == normalSize;
                int num = handlerNum.get();
                boolean nonFinish = IntStream.of(1, 2).anyMatch(it -> it == num);
                boolean isFinish = num > 2;
                ISqlSegment iSqlSegment = normal.get(i);
                String sqlSegment = iSqlSegment.getSqlSegment();
                // 支持嵌套 TODO
                if (iSqlSegment instanceof QueryWrapper) {
                    continue;
                }
                if (linkKeyword.contains(iSqlSegment) && isFinish) {
                    if (iSqlSegment == SqlKeyword.AND && (!lastElement) && !normal.get(i+1).getSqlSegment().contains("REGEXP")) {
                        // 若下一个的前缀和之前一
                        if ((i+1) != normalSize) {
                            String[] split = normal.get(i+1).getSqlSegment().split(Pattern.quote(StringPool.DOT));
                            // 有前缀
                            String key = split[0];
                            if (split.length > 1 && map.containsKey(key)) {
                                map.get(key).add(sqlSegment);
                                handlerNum.set(0);
                            }
                        }
                        continue;
                    }
                    // 支持OR TODO
//                    if (iSqlSegment == SqlKeyword.OR)
                }
                // 找 ColumnSegment, 直到下一个为止
                if (!nonFinish && !lastElement && normal.get(i+1) instanceof SqlKeyword && !linkKeyword.contains(iSqlSegment)) {
                    String[] split = sqlSegment.split("[^a-zA-Z0-9._]");
                    Set<String> keySet = Sets.newHashSet();
                    // 有前缀
                    for (String mayKey : split) {
                        String[] keySplit = mayKey.split(Pattern.quote(StringPool.DOT));
                        if (keySplit.length > 1) {
                            String newKey = keySplit[0];
                            keySet.add(newKey);

                        }
                    }
                    //是权限列表有多个别名
                    String key = "";
                    if (keySet.size() > 1){
                        key = "permission";
                    }else {
                        if (CollUtil.isNotEmpty(keySet)){
                            key = keySet.iterator().next();
                        }
                    }
                    joiner = map.getOrDefault(key.trim(), new StringJoiner(StringPool.SPACE));
                    joiner.add(sqlSegment);
                    map.put(key, joiner);
                    handlerNum.set(1);
                } else {

                   if (sqlSegment.contains("1=0")){
                       joiner.add(SqlKeyword.AND.name()+ " " + "1=0");
                       continue;
                   }

                   if (!linkKeyword.contains(iSqlSegment) && lastElement && !sqlSegment.contains("ew.")){
                       Set<String> keySet = getPrefixKey(sqlSegment);
                       //有前缀
                       if (CollUtil.isNotEmpty(keySet)){
                           //是权限列表有多个别名
                           String key = "";
                           if (keySet.size() > 1){
                               key = "permission";
                           }else {
                               key = keySet.iterator().next();
                           }
                           joiner = map.getOrDefault(key.trim(), new StringJoiner(StringPool.SPACE));
                           joiner.add(sqlSegment);
                           map.put(key, joiner);
                           handlerNum.set(1);
                           continue;
                       }
                   }
                   if (linkKeyword.contains(iSqlSegment) && (joiner.toString().endsWith(SqlKeyword.AND.name()) || joiner.toString().endsWith(SqlKeyword.OR.name()))){
                       handlerNum.incrementAndGet();
                       continue;
                    }
                    joiner.add(sqlSegment);
                    handlerNum.incrementAndGet();
                }
            }

        }
        map.remove("");
        Map<String, String> result = MapUtil.map(map, (key, value) -> {
            if (value.length() <= 2){
                return null;
            }
            String str = value.toString();
            if (str.endsWith(SqlKeyword.AND.name())){
                return str.substring(0,str.length() -3);
            }
            if (str.endsWith(SqlKeyword.OR.name())){
                return str.substring(0,str.length() -2);
            }

            return value.toString();
        });
        groupSqlSegment.putAll(MapUtil.removeNullValue(result));
        return groupSqlSegment;
    }

    public boolean hasAlias(String alias){
        return getGroupSqlSegment().containsKey(alias);
    }

    public String getSqlByAlias(String alias){
        return getGroupSqlSegment().getOrDefault(alias, "1 = 1");
    }

    private Set<String> getPrefixKey(String sqlSegment) {
        Set<String> keySet = Sets.newHashSet();
        String[] split = sqlSegment.split("[^a-zA-Z0-9._]");
        // 有前缀
        for (String mayKey : split) {
            String[] keySplit = mayKey.split(Pattern.quote(StringPool.DOT));
            if (keySplit.length > 1) {
                String newKey = keySplit[0];
                keySet.add(newKey);
            }
        }
        return keySet;
    }

    public static void main(String[] args) {
        String s = "concat(ts.year_name,\" \",ts.season_name,\" \",ts.brand_name)";
        String[] tokens = s.split("[^a-zA-Z0-9._]");

        for (String token : tokens) {
            System.out.println(token);
        }
        System.out.println();
    }

}
