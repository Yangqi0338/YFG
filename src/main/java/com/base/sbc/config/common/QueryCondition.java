package com.base.sbc.config.common;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.utils.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shenzhixiong
 * @version v1.0
 * @Description 查询条件构造器
 * @date 2017-3-8 上午9:38:11
 */
public final class QueryCondition extends BaseEntity {
    private static final long serialVersionUID = -2111154113320858338L;
    protected String orderByClause;
    protected String groupByClause;
    protected String countByClause;
    protected List<String> criteriaWithoutValue;
    protected List<Map<String, Object>> criteriaWithSingleValue;
    protected List<Map<String, Object>> criteriaWithListValue;
    protected List<Map<String, Object>> criteriaWithBetweenValue;
    protected Object t;

    public Object getT() {
        return t;
    }

    public void setT(Object t) {
        this.t = t;
    }

    public QueryCondition() {
        this.init();
    }

    /**
     * 可传入企业编码的构造器
     *
     * @param companyCode 企业编码和删除标记
     */
    public QueryCondition(String companyCode) {
        this.init();
        this.andEqualTo(BaseController.COMPANY_CODE, companyCode);
    }

    public void init() {
        this.criteriaWithoutValue = new ArrayList<>();
        this.criteriaWithSingleValue = new ArrayList<>();
        this.criteriaWithListValue = new ArrayList<>();
        this.criteriaWithBetweenValue = new ArrayList<>();
        this.orderByClause=null;
    }

    protected QueryCondition(QueryCondition example) {
        this.groupByClause = example.groupByClause;
        this.orderByClause = example.orderByClause;
        this.init();
    }

    /**
     * 清空当前构造器
     *
     * @return
     */
    public void clear() {
        this.init();
        this.groupByClause = StringUtils.EMPTY;
        this.orderByClause = StringUtils.EMPTY;
    }

    public String getGroupByClause() {
        return groupByClause;
    }

    /**
     * 放入分组条件   对应xml里面的分组
     *
     * @param groupByClause
     */
    public QueryCondition setGroupByClause(String groupByClause) {
        this.groupByClause = groupByClause;
        return this;
    }

    /**
     * 放入排序条件 对应xml的排序
     *
     * @param orderByClause
     */
    public QueryCondition setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
        return this;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public String getCountByClause() {
        return countByClause;
    }

    public QueryCondition setCountByClause(String countByClause) {
        this.countByClause = countByClause;
        return this;
    }


    public boolean isValid() {
        return criteriaWithoutValue.size() > 0
                || criteriaWithSingleValue.size() > 0
                || criteriaWithListValue.size() > 0
                || criteriaWithBetweenValue.size() > 0;
    }

    public List<String> getCriteriaWithoutValue() {
        return criteriaWithoutValue;
    }

    public List<Map<String, Object>> getCriteriaWithSingleValue() {
        return criteriaWithSingleValue;
    }

    public List<Map<String, Object>> getCriteriaWithListValue() {
        return criteriaWithListValue;
    }

    public List<Map<String, Object>> getCriteriaWithBetweenValue() {
        return criteriaWithBetweenValue;
    }

    protected void addCriterion(String condition) {
        if (condition == null) {
            throw new RuntimeException("Value for condition cannot be null");
        }
        criteriaWithoutValue.add(condition);
    }

    protected void addCriterion(String condition, Object value) {
        if (value == null) {
            throw new RuntimeException("Value for " + value + " cannot be null");
        }
        Map<String, Object> map = new HashMap<String, Object>(4);
        map.put("condition", condition);
        map.put("value", value);
        criteriaWithSingleValue.add(map);
    }

    protected void addCriterion(String condition, List<Object> values) {
        if (values == null || values.size() == 0) {
            throw new RuntimeException("Value list cannot be null or empty");
        }
        Map<String, Object> map = new HashMap<String, Object>(4);
        map.put("condition", condition);
        map.put("value", values);
        criteriaWithListValue.add(map);
    }

    protected void addCriterion(String condition, Object value1,
                                Object value2) {
        if (value1 == null || value2 == null) {
            throw new RuntimeException("Between values  cannot be null");
        }
        List<Object> list = new ArrayList<Object>();
        list.add(value1);
        list.add(value2);
        Map<String, Object> map = new HashMap<String, Object>(4);
        map.put("condition", condition);
        map.put("value", list);
        criteriaWithBetweenValue.add(map);
    }

    protected void addCriterionForJdbcDate(String condition, Date value) {
        addCriterion(condition, new java.sql.Date(value.getTime()));
    }

    protected void addCriterionForJdbcDate(String condition, List<Date> values) {
        if (values == null || values.size() == 0) {
            throw new RuntimeException("Value list cannot be null or empty");
        }
        List<Date> dateList = new ArrayList<Date>();
        Iterator<Date> iter = values.iterator();
        while (iter.hasNext()) {
            dateList.add(new java.sql.Date(((Date) iter.next()).getTime()));
        }
        addCriterion(condition, dateList);
    }

    protected void addCriterionForJdbcDate(String condition, Date value1,
                                           Date value2) {
        if (value1 == null || value2 == null) {
            throw new RuntimeException("Between values cannot be null");
        }
        addCriterion(condition, new java.sql.Date(value1.getTime()),
                new java.sql.Date(value2.getTime()));
    }

    /**
     * 字段为空
     *
     * @param column
     * @return
     */
    public QueryCondition andIsNull(String column) {
        addCriterion(column + " is null");
        return this;
    }

    /**
     * 字段不为空
     *
     * @param column 字段
     * @return
     */
    public QueryCondition andIsNotNull(String column) {
        addCriterion(column + " is not null");
        return this;
    }

    /**
     * 是否含有sql注入，返回true表示含有
     *
     * @param obj
     * @return
     */
    public static boolean containsSqlInjection(Object obj) {
        Pattern pattern = Pattern.compile("" +
                "\\b(exec|insert|drop|grant|alter|delete|update|chr|mid|master|truncate|char|declare)\\b|(\\*|;|\\+)");
//			Pattern pattern= Pattern.compile("" +
//					"\\b(and|exec|insert|select|drop|grant|alter|delete|update|count|chr|mid|master|truncate|char|declare|or)\\b|(\\*|;|\\+|'|%)");
        Matcher matcher = pattern.matcher(obj.toString());
        return matcher.find();
    }

    /**
     * 拼接sql 没有自带 外括号
     *
     * @param conditionSql 条件sql
     * @return
     */
    public QueryCondition andConditionSql(String conditionSql) {
        if (!containsSqlInjection(conditionSql)) {
            addCriterion(StringEscapeUtils.unescapeHtml4(conditionSql));
        } else {
            throw new RuntimeException("Your SQL does not comply with the security rules");
        }
        return this;
    }

    /**
     * 字段 等于 值
     *
     * @param column 字段
     * @param value  值
     * @return
     */
    public QueryCondition andEqualTo(String column, Object value) {
        addCriterion(column + " =", value);
        return this;
    }

    /**
     * 字段不等于值
     *
     * @param column 字段
     * @param value  值
     * @return
     */
    public QueryCondition andNotEqualTo(String column, Object value) {
        addCriterion(column + " !=", value);
        return this;
    }

    /**
     * 字段 大于 值
     *
     * @param column 字段
     * @param value  值
     * @return
     */
    public QueryCondition andGreaterThan(String column, Object value) {
        addCriterion(column + " >", value);
        return this;
    }

    /**
     * 字段 大于等于 值
     *
     * @param column 字段
     * @param value  值
     * @return
     */
    public QueryCondition andGreaterThanOrEqualTo(String column, Object value) {
        addCriterion(column + " >=", value);
        return this;
    }

    /**
     * 字段 小于 值
     *
     * @param column 字段
     * @param value  值
     * @return
     */
    public QueryCondition andLessThan(String column, Object value) {
        addCriterion(column + " <", value);
        return this;
    }

    /**
     * 字段 小于等于 值
     *
     * @param column 字段
     * @param value  值
     * @return
     */
    public QueryCondition andLessThanOrEqualTo(String column, Object value) {
        addCriterion(column + " <=", value);
        return this;
    }

    /**
     * 字段  在list集合中
     *
     * @param column
     * @param values
     * @return
     */
    @SuppressWarnings("unchecked")
    public QueryCondition andIn(String column, @SuppressWarnings("rawtypes") List values) {
        if (values != null && values.size() > 0) {
            if (values.size() == 1 && values.get(0) != null) {
                return andEqualTo(column, values.get(0));
            }
            addCriterion(column + " in", values);
        }
        return this;
    }

    /**
     * 字段  不在list集合中
     *
     * @param column
     * @param values
     * @return
     */
    @SuppressWarnings("unchecked")
    public QueryCondition andNotIn(String column, @SuppressWarnings("rawtypes") List values) {
        addCriterion(column + " not in", values);
        return this;
    }

    /**
     * 字段在 之间   between value1 and value2
     *
     * @param column
     * @param value1
     * @param value2
     * @return
     */
    public QueryCondition andBetween(String column, Object value1, Object value2) {
        addCriterion(column + " between", value1, value2);
        return this;
    }

    /**
     * 字段不在 之间  not between value1 and value2
     *
     * @param column
     * @param value1
     * @param value2
     * @return
     */
    public QueryCondition andNotBetween(String column, Object value1, Object value2) {
        addCriterion(column + " not between", value1, value2);
        return this;
    }

    /**
     * 字段 包含 value  已拼接CONCAT(CONCAT('%','" + value + "'),'%')
     *
     * @param column 字段
     * @param value  值
     * @return
     */
    public QueryCondition andLike(String column, Object value) {
        addCriterion(column + " like concat(concat('%','" + value + "'),'%') ");
        return this;
    }

    /**
     * 字段 以值开头
     *
     * @param column 字段
     * @param value  值
     * @return
     */
    public QueryCondition andLikeStart(String column, Object value) {
        addCriterion(column + " like concat('" + value + "','%') ");
        return this;
    }

    /**
     * 字段 以值结束
     *
     * @param column 字段
     * @param value  值
     * @return
     */
    public QueryCondition andLikeEnd(String column, Object value) {
        addCriterion(column + " like concat('%','" + value + "') ");
        return this;
    }

    /**
     * 一个值    同时多个字段模糊匹配   效率低    少用
     *
     * @param value   值
     * @param columns
     * @return
     */
    public QueryCondition andLikeOr(Object value, String... columns) {
        StringBuilder sBuffer = new StringBuilder("(");
        for (int i = 0; i < columns.length; i++) {
            sBuffer.append(columns[i]).append(" like concat(concat('%','").append(value).append("'),'%')");
            //如果不是最后一个
            if (i != columns.length - 1) {
                sBuffer.append(" or ");
            }
        }
        sBuffer.append(")");
        addCriterion(sBuffer.toString());
        return this;
    }

    /**
     * 字段 不 包含 value  已拼接CONCAT(CONCAT('%','" + value + "'),'%')
     *
     * @param column 字段
     * @param value  值
     * @return
     */
    public QueryCondition andNotLike(String column, Object value) {
        addCriterion(column + " not like concat(concat('%','" + value + "'),'%')");
        return this;
    }

    /**
     * 等于 该时间
     *
     * @param column 字段
     * @param value  时间
     * @return
     */
    public QueryCondition andDateEqualTo(String column, Date value) {
        addCriterionForJdbcDate(column + " =", value);
        return this;
    }

    /**
     * 不等于 该时间
     *
     * @param column 字段
     * @param value  时间
     * @return
     */
    public QueryCondition andDateNotEqualTo(String column, Date value) {
        addCriterionForJdbcDate(column + " <>", value);
        return this;
    }

    /**
     * 大于该时间
     *
     * @param column 字段
     * @param value  时间
     * @return
     */
    public QueryCondition andDateGreaterThan(String column, Date value) {
        addCriterionForJdbcDate(column + " >", value);
        return this;
    }

    /**
     * 大于等于该时间
     *
     * @param column 字段
     * @param value  时间
     * @return
     */
    public QueryCondition andDateGreaterThanOrEqualTo(String column, Date value) {
        addCriterionForJdbcDate(column + " >=", value);
        return this;
    }

    /**
     * 小于该时间
     *
     * @param column 字段
     * @param value  时间
     * @return
     */
    public QueryCondition andDateLessThan(String column, Date value) {
        addCriterionForJdbcDate(column + " <", value);
        return this;
    }

    /**
     * 小于等于该时间
     *
     * @param column 字段
     * @param value  时间
     * @return
     */
    public QueryCondition andDateLessThanOrEqualTo(String column, Date value) {
        addCriterionForJdbcDate(column + " <=", value);
        return this;
    }

    /**
     * 字段在时间集合中
     *
     * @param column   字段
     * @param dateList 时间集合
     * @return
     */
    public QueryCondition andDateIn(String column, List<Date> dateList) {
        addCriterionForJdbcDate(column + " in", dateList);
        return this;
    }

    /**
     * 字段不在时间集合中
     *
     * @param column   字段
     * @param dateList 时间集合
     * @return
     */
    public QueryCondition andDateNotIn(String column, List<Date> dateList) {
        addCriterionForJdbcDate(column + " not in", dateList);
        return this;
    }

    /**
     * 时间在起始时间内
     *
     * @param column    字段
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    public QueryCondition andDateBetween(String column, Date beginDate, Date endDate) {
        addCriterionForJdbcDate(column + " between", beginDate, endDate);
        return this;
    }

    /**
     * 时间不在起始时间内
     *
     * @param column    字段
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    public QueryCondition andDateNotBetween(String column, Date beginDate, Date endDate) {
        addCriterionForJdbcDate(column + " not between", beginDate, endDate);
        return this;
    }


//	}

    public static void main(String[] args) {
        List<Map<String, Object>> criteriaWithSingleValue = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("condaon", "con");
        params.put("value", 3);
        criteriaWithSingleValue.add(params);
        System.out.println(criteriaWithSingleValue.size());
        Map<String, Object> params1 = new HashMap<>();
        params1.put("condaon", "con");
        params1.put("value", 3);
        criteriaWithSingleValue.remove(params1);
        System.out.println(criteriaWithSingleValue.size());
    }

    @Override
    public void preInsert() {

    }

    @Override
    public void preUpdate() {

    }
}