/******************************************************************************
 * Copyright (C) 2018 celizi.com 
 * All Rights Reserved.
 * 本软件为网址：celizi.com 开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.config.common.base;

import com.base.sbc.config.common.QueryCondition;
import com.google.common.collect.Lists;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;



/**
 * 类描述：DAO基类
 * @address com.celizi.base.common.base.BaseDao
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 创建时间：2018年8月1日17:19:39
 * @version 1.0
 */
@Transactional(readOnly = true)
public abstract class BaseDao <T> extends SqlSessionDaoSupport {

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Resource
	@Override
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}

	/***********************************************定义常量****************************************************/

	/** 主键字段：ID */
	protected final static String ID = "ID";

	/** 常量 ：点 */
	protected final static String POINT = ".";

	/** 新增  */
	protected final static String INSERT = "insert";

	/** 修改  */
	protected final static String UPDATE = "update";

	/** 修改   */
	protected final static String UPDATE_LIST = "updateList";

	/** 删除  */
	protected final static String DELETE = "delete";

	/** 查询  */
	protected final static String SELECT = "select";

	/** 汇总  */
	protected final static String SELECT_COUNT = "selectCount";


	/*************************************************命名空间***********************************************/
	/**
	 * 返回***Mapper.xml的namespace的字符串  两者一致 且全局唯一即可。
	 * @return 返回命名空间字符串
	 */
	protected abstract String getMapperNamespace();

	/**
	 * 将SqlMapping命名空间与给定的SqlMapping名组合在一起。
	 * 【为了通过命名空间和sql  找到xml里面的方法。】
	 * @param sqlName SqlMapping名
	 * @return 组合了SqlMapping命名空间后的完整SqlMapping名
	 */
	protected String getStatementName(String sqlName) {
		return sqlNameSpace + POINT + sqlName;
	}

	/**
	 * SqlMapping命名空间
	 */
	private String sqlNameSpace = getMapperNamespace();

	/**
	 * 获取SqlMapping命名空间
	 * @return SqlMapping命名空间
	 */
	public String getSqlNamespace() {
		return sqlNameSpace;
	}

	/**
	 * 设置SqlMapping命名空间。
	 * 此方法只用于注入SqlMapping命名空间，以改变默认的SqlMapping命名空间，
	 * @param sqlNamespace SqlMapping命名空间
	 */
	public void setSqlNamespace(String sqlNamespace) {
		this.sqlNameSpace = sqlNamespace;
	}
	/**************************************************命名空间*******************************************/


	/*************************************************基础方法：新增*******************************************/

	/**
	 * 新增 
	 * @param t
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	public int insert(T t) {
		try{
			return this.batchInsert(Lists.newArrayList(t));
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}

	/**
	 * 批量新增
	 * @param list
	 * @return
	 */
	@Transactional(readOnly = false)
	public int batchInsert(List<T> list){
		return this.batchInsert(INSERT,list);
	}

	/**
	 * 批量新增  用于导入等    大量数据同时进入系统
	 * @param list 对象的list集合  默认使用xml文件里的batchInsert
	 */
	@Transactional(readOnly = false)
	public int batchInsert(String statementName, List<T> list ){
		SqlSession batchSqlSession = null;
		int result = 1;
		try{
			batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
			// 每批commit的个数
			int batchCount = 1000;
			// 每批最后一个的下标
			int batchLastIndex = batchCount;
			for (int index = 0; index < list.size();) {
				if (batchLastIndex >= list.size()) {
					batchLastIndex = list.size();
					result = result * batchSqlSession.insert(getStatementName(statementName),list.subList(index, batchLastIndex));
					batchSqlSession.commit();
					// 数据插入完毕，退出循环
					break;
				} else {
					result = result * batchSqlSession.insert(getStatementName(statementName),list.subList(index, batchLastIndex));
					batchSqlSession.commit();
					// 设置下一批下标
					index = batchLastIndex;
					batchLastIndex = index + (batchCount - 1);
				}
			}
			batchSqlSession.commit();
			return batchLastIndex;
		}catch (Exception ex){
			batchSqlSession.rollback();
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		} finally {
			batchSqlSession.close();
		}
	}

/*****************************************基础方法：删除***************************************/
	/**
	 * 删除数据操作,真实删除，  谨慎使用
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public int deleteById(String id) {
		QueryCondition qc = new QueryCondition();
		qc.andEqualTo(BaseGlobal.ID, id);
		return this.deleteByCondition(qc);
	}

	/**
	 * 删除数据操作，逻辑删除，del_flag变为1  谨慎使用
	 * @param entity 当前需要修改的entity new entity()就可以
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public int deleteByIdDelFlag(BaseDataEntity entity,String id) {
		QueryCondition qc = new QueryCondition();
		entity.setDelFlag(BaseGlobal.DEL_FLAG_DELETE);
		entity.preUpdate();
		qc.setT(entity);
		qc.andEqualTo(BaseGlobal.ID, id);
		return this.batchUpdateByCondition(qc);
	}

	/**
	 * 批量删除数据操作，逻辑删除，del_flag变为1  谨慎使用
	 * @param entity 当前需要修改的entity new entity()就可以
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly = false)
	public int batchdeleteByIdDelFlag(BaseDataEntity entity,List<String> ids) {
		QueryCondition qc = new QueryCondition();
		entity.setDelFlag(BaseGlobal.DEL_FLAG_DELETE);
		entity.preUpdate();
		qc.setT(entity);
		qc.andIn(BaseGlobal.ID, ids);
		return this.batchUpdateByCondition(qc);
	}

	/**
	 * 更新非空字段
	 * @param entity
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public int update(T entity, String id) {
		QueryCondition qc = new QueryCondition();
		qc.setT(entity);
		qc.andEqualTo(BaseGlobal.ID, id);
		return this.batchUpdateByCondition(qc);
	}

	/**
	 * 删除数据操作  谨慎使用
	 * @param qc
	 * @return
	 */
	@Transactional(readOnly = false)
	public int deleteByCondition(QueryCondition qc) {
		try{
			return this.getSqlSession().delete(getStatementName(DELETE), qc);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}

	/*****************************************基础方法：修改***************************************/

	/**
	 * 更新    字段为空也更新
	 * @param t
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	public int updateAll(T t) {
		return this.batchUpdate(Lists.newArrayList(t));
	}

	/**
	 * 场景 ：批量修改
	 * @param list
	 */
	@Transactional(readOnly = false)
	public int batchUpdate(List<T> list){
		return batchUpdate(UPDATE_LIST, list);
	}

	/**
	 * 场景 ：批量修改
	 * @param list
	 */
	@Transactional(readOnly = false)
	public int batchUpdate(String statementName, List<T> list){
		SqlSession batchSqlSession = null;
		int result = 1;
		try{
			batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
			// 每批commit的个数
			int batchCount = 1000;
			// 每批最后一个的下标
			int batchLastIndex = batchCount;
			for (int index = 0; index < list.size();) {
				if (batchLastIndex >= list.size()) {
					batchLastIndex = list.size();
					result = result * batchSqlSession.update(getStatementName(statementName),list.subList(index, batchLastIndex));
					batchSqlSession.commit();
					// 数据插入完毕，退出循环
					break;
				} else {
					result = result * batchSqlSession.update(getStatementName(statementName),list.subList(index, batchLastIndex));
					batchSqlSession.commit();
					// 设置下一批下标
					index = batchLastIndex;
					batchLastIndex = index + (batchCount - 1);
				}
			}
			batchSqlSession.commit();
			return batchLastIndex;
		}catch (Exception ex){
			batchSqlSession.rollback();
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		} finally {
			batchSqlSession.close();
		}
	}
	/**
	 * 批量修改  场景 ：知道 条件  批量修改状态 不需要查询。
	 * @param qc 条件构造器
	 */
	@Transactional(readOnly = false)
	public int batchUpdateByCondition(QueryCondition qc){
		return this.getSqlSession().update(getStatementName(UPDATE), qc);
	}


	/*****************************************基础方法：查询***************************************/
	/**
	 * 按条件查询
	 * @param qc
	 * @return
	 */
	public List<T> findByCondition(QueryCondition qc){
		try{
			return this.getSqlSession().selectList(getStatementName(SELECT),qc);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}

	/**
	 * 按条件查询删除标记为正常的
	 * @param qc
	 * @return
	 */
	public List<T> findByConditionAndDelNormal(QueryCondition qc){
		try{
			qc.andEqualTo(BaseController.DEL_FLAG,BaseGlobal.DEL_FLAG_NORMAL);
			return this.getSqlSession().selectList(getStatementName(SELECT),qc);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}
	/**
	 * 按条件查询单个对象
	 * @param qc
	 * @return
	 */
	public T getByCondition(QueryCondition qc){
		try{
			return this.getSqlSession().selectOne(getStatementName(SELECT),qc);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}

	/**
	 * 按条件查询单个对象，只查询删除标记正常的对象
	 * @param qc
	 * @return
	 */
	public T getByConditionAndDelNormal(QueryCondition qc){
		try{
			qc.andEqualTo(BaseController.DEL_FLAG,BaseGlobal.DEL_FLAG_NORMAL);
			return this.getSqlSession().selectOne(getStatementName(SELECT),qc);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}

	/**
	 * 按主键ID查询
	 * @param pk 主键
	 * @return
	 */
	public T getById(Object pk){
		try{
			QueryCondition qc = new QueryCondition();
			qc.andEqualTo(BaseGlobal.ID, pk);
			return this.getSqlSession().selectOne(getStatementName(SELECT),qc);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}

	/***
	 *
	 * @param qc
	 * @return
	 */
	public int countByCondition(QueryCondition qc){
		try{
			return this.getSqlSession().selectOne(getStatementName(SELECT_COUNT),qc);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}


	/*****************************************继承使用：新增，修改，删除***************************************/


	@Transactional(readOnly = false)
	public int insert(String statementName) {
		try{
			return this.getSqlSession().insert(getStatementName(statementName));
		}catch(Exception ex){
			logger.error("sql执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}


	@Transactional(readOnly = false)
	public int delete(String statementName) {
		try{
			return this.getSqlSession().delete(getStatementName(statementName));
		}catch(Exception ex){
			logger.error("sql执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}

	@Transactional(readOnly = false)
	public int update(String statementName) {
		try{
			return this.getSqlSession().update(getStatementName(statementName));
		}catch(Exception ex){
			logger.error("sql执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}


	@Transactional(readOnly = false)
	public int insertByEntity(String statementName, T t) {
		try{
			return this.getSqlSession().insert(getStatementName(statementName), t);
		}catch(Exception ex){
			logger.error("sql执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}


	@Transactional(readOnly = false)
	public int deleteByEntity(String statementName, T t) {
		try{
			return this.getSqlSession().delete(getStatementName(statementName), t);
		}catch(Exception ex){
			logger.error("sql执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}

	@Transactional(readOnly = false)
	public int updateByEntity(String statementName, T t) {
		try{
			return this.getSqlSession().update(getStatementName(statementName), t);
		}catch(Exception ex){
			logger.error("sql执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}


	@Transactional(readOnly = false)
	public int insertByMap(String statementName, Map<String,?> params) {
		try{
			return this.getSqlSession().insert(getStatementName(statementName), params);
		}catch(Exception ex){
			logger.error("sql执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}

	@Transactional(readOnly = false)
	public int deleteByMap(String statementName, Map<String,?> params) {
		try{
			return this.getSqlSession().delete(getStatementName(statementName), params);
		}catch(Exception ex){
			logger.error("sql执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}

	@Transactional(readOnly = false)
	public int updateByMap(String statementName, Map<String,?> params) {
		try{
			return this.getSqlSession().update(getStatementName(statementName), params);
		}catch(Exception ex){
			logger.error("sql执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}



	@Transactional(readOnly = false)
	public int insertByCondition(String statementName, QueryCondition qc) {
		try{
			return this.getSqlSession().insert(getStatementName(statementName), qc);
		}catch(Exception ex){
			logger.error("sql执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}

	@Transactional(readOnly = false)
	public int deleteByCondition(String statementName, QueryCondition qc) {
		try{
			return this.getSqlSession().delete(getStatementName(statementName), qc);
		}catch(Exception ex){
			logger.error("sql执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}

	@Transactional(readOnly = false)
	public int updateByCondition(String statementName, QueryCondition qc) {
		try{
			return this.getSqlSession().update(getStatementName(statementName), qc);
		}catch(Exception ex){
			logger.error("sql执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}




	/*****************************************继承使用：查询List<T>***************************************/
	/**
	 * 通过方法名查询
	 * @param statementName
	 * @return
	 */
	public List<T> selectList(String statementName) {
		try{
			return this.getSqlSession().selectList(getStatementName(statementName), null);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}

	/**
	 * 通过实体 附加参数查询
	 * @param statementName
	 * @param entity
	 * @return
	 */
	public List<T> selectList(String statementName, Object entity) {
		try{
			return this.getSqlSession().selectList(getStatementName(statementName), entity);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}
	/**
	 * 附加键值对参数进行查询
	 * @param statementName
	 * @param params
	 * @return
	 */
	public List<T> selectList(String statementName, Map<String,?> params) {
		try{
			return this.getSqlSession().selectList(getStatementName(statementName), params);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}
	/**
	 * 附加通用构造器的参数   进行查询
	 * @param statementName
	 * @param qc
	 * @return
	 */
	public List<T> selectList(String statementName, QueryCondition qc) {
		try{
			return this.getSqlSession().selectList(getStatementName(statementName), qc);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}
	/*****************************************继承使用：查询<E> List<E>***************************************/

	/**
	 * 查询
	 * @param statementName
	 * @return
	 */
	public <E> List<E> selectList2(String statementName) {
		try{
			return this.getSqlSession().selectList(getStatementName(statementName), null);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}
	/**
	 * 通过实体附加参数 进行查询
	 * @param statementName
	 * @param entity
	 * @return
	 */
	public <E> List<E> selectList2(String statementName, Object entity) {
		try{
			return this.getSqlSession().selectList(getStatementName(statementName), entity);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}
	/**
	 * 通过map 键值对附加参数  进行查询
	 * @param statementName
	 * @param params
	 * @return
	 */
	public <E> List<E> selectList2(String statementName, Map<String,?> params) {
		try{
			return this.getSqlSession().selectList(getStatementName(statementName), params);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}
	/**
	 * 通过查询条件构造器 附加参数进行查询
	 * @param statementName
	 * @param qc
	 * @return
	 */
	public <E> List<E> selectList2(String statementName, QueryCondition qc) {
		try{
			return this.getSqlSession().selectList(getStatementName(statementName), qc);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}
	/*****************************************继承使用：查询T 单个实体***************************************/
	@SuppressWarnings("unchecked")
	public T selectOne(String statementName) {
		try{
			return (T) this.getSqlSession().selectOne(getStatementName(statementName), null);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	public T selectOne(String statementName, Object entity) {
		try{
			return (T) this.getSqlSession().selectOne(getStatementName(statementName), entity);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}
	@SuppressWarnings("unchecked")
	public T selectOne(String statementName, Map<String,?> params) {
		try{
			return (T)(this.getSqlSession().selectOne( getStatementName(statementName), params));
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}
	@SuppressWarnings("unchecked")
	public T selectOne(String statementName,QueryCondition qc) {
		try{
			return (T)(this.getSqlSession().selectOne( getStatementName(statementName), qc));
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}
	public Object selectOneObject(String statementName,QueryCondition qc) {
		try{
			return this.getSqlSession().selectOne( getStatementName(statementName), qc);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}
	/*****************************************继承使用：查询<E> E***************************************/
	@SuppressWarnings("unchecked")
	public <E> E selectOne2(String statementName) {
		try{
			return (E) this.getSqlSession().selectOne(getStatementName(statementName), null);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}
	@SuppressWarnings("unchecked")
	public <E> E selectOne2(String statementName, Object entity) {
		try{
			return (E) this.getSqlSession().selectOne(getStatementName(statementName), entity);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	public <E> E selectOne2(String statementName, Map<String,?> params) {
		try{
			return (E)(this.getSqlSession().selectOne(getStatementName(statementName), params));
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	public <E> E selectOne2(String statementName, QueryCondition qc) {
		try{
			return (E)(this.getSqlSession().selectOne(getStatementName(statementName), qc));
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}
	/**
	 * 附加通用构造器的参数 进行查询
	 *
	 * @param statementName
	 * @param qc
	 * @return
	 */
	public List<String> selectList3(String statementName, QueryCondition qc) {
		try {
			return this.getSqlSession().selectList(getStatementName(statementName), qc);
		} catch (Exception ex) {
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}
}
