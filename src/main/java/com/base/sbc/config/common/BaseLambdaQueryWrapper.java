package com.base.sbc.config.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.segments.NormalSegmentList;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import cn.hutool.core.lang.Pair;
import org.bouncycastle.util.Arrays;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author 卞康
 * @date 2023/6/12 19:36:07
 * @mail 247967116@qq.com
 */
public class BaseLambdaQueryWrapper<T> extends LambdaQueryWrapper<T> {

    // 设置ThreadLocal,存储表对应的别名支持关联查询
    private InheritableThreadLocal<ConcurrentHashMap<Class<?>, Pair<BaseLambdaQueryWrapper<?>, String>>> joinAliasMap = new InheritableThreadLocal<>();
    private BaseLambdaQueryWrapper<?> mainQueryWrapper = null;

    public void setMainQueryWrapper(BaseLambdaQueryWrapper<?> mainQueryWrapper) {
        this.mainQueryWrapper = mainQueryWrapper;
    }

    public BaseLambdaQueryWrapper<T> notEmptyEq(SFunction<T, ?> column, String val) {
        this.eq(StrUtil.isNotBlank(val), column, val);
        return this;
    }

    public <R> BaseLambdaQueryWrapper<T> notNullEq(SFunction<T, R> column, R val) {
        this.eq(ObjectUtil.isNotEmpty(val), column, val);
        return this;
    }

    public <R> BaseLambdaQueryWrapper<T> notNullNe(SFunction<T, R> column, R val) {
        this.ne(ObjectUtil.isNotEmpty(val), column, val);
        return this;
    }


    public <R> BaseLambdaQueryWrapper<T> notEmptyIn(SFunction<T, R> column, Collection<R> coll) {
        this.in(CollUtil.isNotEmpty(coll), column, coll);

        return this;
    }

    public BaseLambdaQueryWrapper<T> notEmptyIn(SFunction<T, ?> column, String str) {
        this.in(StrUtil.isNotBlank(str), column, StrUtil.split(str, CharUtil.COMMA));
        return this;
    }

    public BaseLambdaQueryWrapper<T> notEmptyLike(SFunction<T, String> column, String val) {
        this.like(!StringUtils.isEmpty(val), column, val);
        return this;
    }

    public <R> BaseLambdaQueryWrapper<T> between(SFunction<T, R> column, String[] dates) {
        if (!Arrays.isNullOrEmpty(dates)) {
            this.and(i-> {
                String date = dates[0];
                i.ge(!StringUtils.isEmpty(date), column, date);
                if (dates.length > 1) {
                    i.le(!StringUtils.isEmpty(dates[1]), column, dates[1]);
                }
            });
        }
        return this;
    }

    @SafeVarargs
    public final BaseLambdaQueryWrapper<T> andLike(String value, SFunction<T, String>... columns) {
        // 最好不用这个,容易导致索引失效
        // 推荐使用虚拟列+虚拟索引
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

    public <R> BaseLambdaQueryWrapper<T> notNull(SFunction<T, R> column) {
        this.isNotNull(column);
        this.ne(column, "");
        return this;
    }

    public <R> BaseLambdaQueryWrapper<T> isNull(SFunction<T, R> column) {
        this.and(qw -> qw.isNull(column).or(qw2 -> qw2.eq(column, "")));
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
    public <R> BaseLambdaQueryWrapper<T> findInSet(SFunction<T, R> column, String value) {
        if (StrUtil.isBlank(value)) {
            return this;
        }
        // 待完善
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
     * @param value
     * @return
     */
    public <R> BaseLambdaQueryWrapper<T> likeList(SFunction<T, R> columns, List<Object> value) {
        if (CollUtil.isEmpty(value)) {
            return this;
        }
        this.and(wrapper -> {
            wrapper.and(i -> {
                for (int j = 0; j < value.size(); j++) {
                    i.like(columns, value.get(j)).or(j < value.size() - 1);
                }
            });
        });
        return this;
    }

    /**
     * 模糊搜索一个字段为集合
     * @param columns
     * @param value
     * @return
     */
    public <R> BaseLambdaQueryWrapper<T> likeList(SFunction<T, R> columns, String value) {
        if (StrUtil.isBlank(value)) {
            return this;
        }
        List<String> split = StrUtil.split(value, CharUtil.COMMA);
        this.and(wrapper -> {
            wrapper.and(i -> {
                for (int j = 0; j < split.size(); j++) {
                    i.like(columns, split.get(j)).or(j < split.size() - 1);
                }
            });
        });
        return this;
    }

    @Override
    public String getCustomSqlSegment() {
        // 如果上下文为空,则未使用关联查询,直接返回父方法
        ConcurrentHashMap<Class<?>, Pair<BaseLambdaQueryWrapper<?>, String>> classPairConcurrentHashMap = joinAliasMap.get();
        if (classPairConcurrentHashMap == null || classPairConcurrentHashMap.isEmpty()) {
            return super.getCustomSqlSegment();
        }else {
            StringBuilder sqlSegment = new StringBuilder();
            for (Map.Entry<Class<?>, Pair<BaseLambdaQueryWrapper<?>, String>> entry : classPairConcurrentHashMap.entrySet()) {
                Class<?> clazz = entry.getKey();
                Pair<BaseLambdaQueryWrapper<?>, String> pair = entry.getValue();
                BaseLambdaQueryWrapper<?> queryWrapper = pair.getKey();
                String alias = pair.getValue();
                // 重写SqlSegment,以支持多表关联查询
                MergeSegments expression = queryWrapper.getExpression();
                if (Objects.nonNull(expression)) {
                    NormalSegmentList normal = expression.getNormal();
                    String joinSqlSegment = queryWrapper.getSqlSegment();
                    if (!isBlank(joinSqlSegment)) {
                        if (normal.isEmpty()) {
                            sqlSegment.append(joinSqlSegment);
                        }
                        sqlSegment.append("AND ").append(joinSqlSegment);
                    }
                }
            }
            return sqlSegment.toString();
        }
    }

    public static boolean isBlank(CharSequence cs) {
        if (cs != null) {
            int length = cs.length();

            for(int i = 0; i < length; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    public <A> BaseLambdaQueryWrapper<A> join(Class<A> clazz){
        ConcurrentHashMap<Class<?>, Pair<BaseLambdaQueryWrapper<?>, String>> joinAliasMap = this.joinAliasMap.get();
        // 存在上下文就直接返回queryWrapper
        if (joinAliasMap.containsKey(clazz)) {
            return (BaseLambdaQueryWrapper<A>) joinAliasMap.get(clazz).getKey();
        }else {
            // 不存在就构建一个新的上下文数据并返回queryWrapper
            BaseLambdaQueryWrapper<A> queryWrapper = new BaseLambdaQueryWrapper<>();
            queryWrapper.setMainQueryWrapper(this);
            joinAliasMap.put(clazz, Pair.of(queryWrapper,null));
            return queryWrapper;
        }
    }

    public <A> BaseLambdaQueryWrapper<A> join(Class<A> clazz, String alias){
        ConcurrentHashMap<Class<?>, Pair<BaseLambdaQueryWrapper<?>, String>> joinAliasMap = this.joinAliasMap.get();
        // 存在上下文就重置其中的别名,并返回queryWrapper
        if (joinAliasMap.containsKey(clazz)) {
            BaseLambdaQueryWrapper<?> queryWrapper = joinAliasMap.get(clazz).getKey();
            if (StrUtil.isNotBlank(alias)) {
                joinAliasMap.put(clazz,Pair.of(queryWrapper, alias));
            }
            return (BaseLambdaQueryWrapper<A>) queryWrapper;
        }else {
            // 不存在就构建一个新的上下文数据并返回queryWrapper
            BaseLambdaQueryWrapper<A> queryWrapper = new BaseLambdaQueryWrapper<>();
            queryWrapper.setMainQueryWrapper(this);
            joinAliasMap.put(clazz, Pair.of(queryWrapper,alias));
            return queryWrapper;
        }
    }

    // 返回到主查询器
    public BaseLambdaQueryWrapper<?> root(){
        if (this.mainQueryWrapper != null) {
            return mainQueryWrapper.root();
        }else {
            return this;
        }
    }

    public static <T> BaseLambdaQueryWrapper<T> wrap(LambdaQueryWrapper<T> queryWrapper) {
        return (BaseLambdaQueryWrapper<T>) queryWrapper;
    }

    public BaseQueryWrapper<T> unwrap() {
        return new BaseQueryWrapper<>(getEntity(), getEntityClass(), paramNameSeq, paramNameValuePairs,
                expression, paramAlias, lastSql, sqlComment, sqlFirst);
    }

}
