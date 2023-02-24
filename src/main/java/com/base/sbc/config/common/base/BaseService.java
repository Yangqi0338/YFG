package com.base.sbc.config.common.base;

import java.util.List;
import java.util.Map;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.base.sbc.config.i18n.LocaleMessages;
import com.base.sbc.config.utils.StringUtils;


/**
 * 类描述：
 * 
 * @address com.celizi.base.common.service.BaseService
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 创建时间：2018年8月1日17:19:49
 * @version 1.0
 * @param <T>
 */
@Transactional(readOnly = true)
public abstract class BaseService<T> {

	/** 国际化对象 */
	@Autowired
	private LocaleMessages localeMessages;
	
	protected static Logger logger = LoggerFactory.getLogger(BaseService.class);

	/**
	 * 在子类实现此函数,为下面的CRUD操作提供DAO.
	 * @return
	 */
	protected abstract BaseDao<T> getEntityDao();

	/***************************************** 基础方法：新增***************************************/

	/**
	 * 新增
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public int insert(T entity) {
		return this.getEntityDao().insert(entity);
	}


	/**
	 * 批量新增
	 * @param entitys 对象的list集合
	 */
	@Transactional(readOnly = false)
	public int batchInsert(final List<T> entitys) {
		return this.getEntityDao().batchInsert(entitys);
	}
	
	

	/*****************************************基础方法：删除***************************************/
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public int deleteById(String id) {
		return this.getEntityDao().deleteById(id);
	}

	/**
	 * 根据id删除,逻辑删除，将delFlag设置为1
	 * @param entity 当前需要修改的entity new entity()就可以
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public int deleteByIdDelFlag(BaseDataEntity entity,String id) {
		return this.getEntityDao().deleteByIdDelFlag(entity,id);
	}

	/**
	 * 根据id批量删除,逻辑删除，将delFlag设置为1
	 * @param entity 当前需要修改的entity new entity()就可以
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly = false)
	public int batchdeleteByIdDelFlag(BaseDataEntity entity,List<String> ids) {
		return this.getEntityDao().batchdeleteByIdDelFlag(entity,ids);
	}

	/**
	 * 按条件进行删除
	 * @param qc 条件
	 * @return
	 */
	@Transactional(readOnly = false)
	public int deleteByCondition(QueryCondition qc) {
		return this.getEntityDao().deleteByCondition(qc);
	}

	/*****************************************基础方法：修改***************************************/
	
	/**
	 * 完全更新  
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public int updateAll(T entity) {
		return this.getEntityDao().updateAll(entity);
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
		return this.getEntityDao().batchUpdateByCondition(qc);
	}


	/**
	 * 批量修改
	 * @param entitys
	 */
	@Transactional(readOnly = false)
	public int batchUpdate(final List<T> entitys) {
		return this.getEntityDao().batchUpdate(entitys);
	}
	/**
	 * 按条件批量更新  非空   qc.T为修改的实体  
	 * @param qc
	 * @return
	 */
	@Transactional(readOnly = false)
	public int batchUpdateByCondition(QueryCondition qc){
		return this.getEntityDao().batchUpdateByCondition(qc);
	}
	/***************************************** 基础方法：查询 ***************************************/

	/**
	 * 查询全部信息
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<T> findAll() {
		return this.getEntityDao().findByCondition(new QueryCondition());
	}

	/**
	 * 通过ID集合查询
	 *
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<T> getByIds(List<String> ids) {
		return this.getEntityDao().findByCondition(new QueryCondition().andIn(BaseGlobal.ID, ids));
	}

	/**
	 * 按条件查询
	 * @param qc
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<T> findByCondition(QueryCondition qc) {
		return this.getEntityDao().findByCondition(qc);
	}

	/**
	 * 按条件查询,只查询删除标记正常的对象
	 * @param qc 查询条件
	 * @return 单个实体类
	 */
	@Transactional(readOnly = true)
	public List<T> findByConditionAndDelNormal(QueryCondition qc) {
		return this.getEntityDao().findByConditionAndDelNormal(qc);
	}


	/**
	 * 分页查询，使用findByCondition
	 * @param qc
	 * @param page
	 * @return
	 */
	@Transactional(readOnly = true)
	public ApiResult findPageByCondition(QueryCondition qc, Page page) {
		if(StringUtils.isNoneBlank(page.getOrder())) {
			qc.setOrderByClause(page.getOrder());
		}
		com.github.pagehelper.Page<?> pages = PageHelper.startPage(page.getPageNum(), page.getPageSize());
		this.getEntityDao().findByCondition(qc);
		return pageSelectResult(pages.toPageInfo());
	}

	/**
	 * 分页查询使用自定义statement
	 * @param qc
	 * @param page
	 * @param statement
	 * @return
	 */
	@Transactional(readOnly = true)
	public ApiResult findPageByCondition(QueryCondition qc, Page page,String statement) {
		if(StringUtils.isNoneBlank(page.getOrder())) {
			qc.setOrderByClause(page.getOrder());
		}
		com.github.pagehelper.Page<?> pages = PageHelper.startPage(page.getPageNum(), page.getPageSize());
		this.getEntityDao().selectList(statement,qc);
		return pageSelectResult(pages.toPageInfo());
	}

	/**
	 * 分页查询，使用findByCondition
	 * @param qc
	 * @param page
	 * @return
	 */
	@Transactional(readOnly = true)
	public ApiResult findPageByConditionAndDelNormal(QueryCondition qc, Page page) {
		if(StringUtils.isNoneBlank(page.getOrder())) {
			qc.setOrderByClause(page.getOrder());
		}
		com.github.pagehelper.Page<?> pages = PageHelper.startPage(page.getPageNum(), page.getPageSize());
		this.getEntityDao().findByConditionAndDelNormal(qc);
		return pageSelectResult(pages.toPageInfo());
	}

	private ApiResult pageSelectResult(PageInfo pageList){
		if (pageList.getList() != null && pageList.getList().size() > 0) {
			return ApiResult.success(getMessage(BaseErrorEnum.SUCCESS_SELECT.getErrorMessage()),pageList);
		}
		return ApiResult.error(getMessage(BaseErrorEnum.ERR_SELECT_NOT_FOUND.getErrorMessage()),BaseErrorEnum.valueOf(BaseErrorEnum.ERR_SELECT_NOT_FOUND.getErrorMessage()).getErrorCode());
	}

	/**
	 * 按条件查询单个对象
	 * @param qc 查询条件
	 * @return 单个实体类
	 */
	@Transactional(readOnly = true)
	public T getByCondition(QueryCondition qc) {
		return this.getEntityDao().getByCondition(qc);
	}

	/**
	 * 按条件查询单个对象,只查询删除标记正常的对象
	 * @param qc 查询条件
	 * @return 单个实体类
	 */
	@Transactional(readOnly = true)
	public T getByConditionAndDelNormal(QueryCondition qc) {
		return this.getEntityDao().getByConditionAndDelNormal(qc);
	}

	/**
	 * 通过主键查询单个实体
	 * @param pk 主键
	 * @return
	 */
	@Transactional(readOnly = true)
	public T getById(Object pk) {
		if (pk != null) {
			return this.getEntityDao().getById(pk);
		}
		return null;
	}

	public int countByCondition(QueryCondition qc){
		try{
			if(StringUtils.isBlank(qc.getCountByClause())){
				qc.setCountByClause(BaseGlobal.ID);
			}
			return this.getEntityDao().countByCondition(qc);
		}catch(Exception ex){
			logger.error("SQL执行错误：", ex);
			throw new DataAccessException(ex);
		}
	}

	
	
/*****************************************继承使用：新增，修改，删除***************************************/ 
	
	
	@Transactional(readOnly = false)
	public int insert(String statementName) {
    	return this.getEntityDao().insert(statementName);
    }
	
	@Transactional(readOnly = false)
	public int delete(String statementName) {
		return this.getEntityDao().delete(statementName);
    }
	
	@Transactional(readOnly = false)
	public int update(String statementName) {
		return this.getEntityDao().update(statementName);
    }
	

	
	@Transactional(readOnly = false)
	public int insertByEntity(String statementName, T t) {
		return this.getEntityDao().insertByEntity(statementName, t);
    }
	
	
	@Transactional(readOnly = false)
	public int deleteByEntity(String statementName, T t) {
		return this.getEntityDao().deleteByEntity(statementName, t);
    }
	
	@Transactional(readOnly = false)
	public int updateByEntity(String statementName, T t) {
		return this.getEntityDao().updateByEntity(statementName, t);
    }
	
	
	
	@Transactional(readOnly = false)
	public int insertByMap(String statementName, Map<String,?> params) {
		return this.getEntityDao().insertByMap(statementName, params);
    }
	
	@Transactional(readOnly = false)
	public int deleteByMap(String statementName, Map<String,?> params) {
		return this.getEntityDao().deleteByMap(statementName, params);
    }
	
	@Transactional(readOnly = false)
	public int updateByMap(String statementName, Map<String,?> params) {
		return this.getEntityDao().updateByMap(statementName, params);
    }
	
	
	
	@Transactional(readOnly = false)
	public int insertByCondition(String statementName, QueryCondition qc) {
		return this.getEntityDao().insertByCondition(statementName, qc);
    }
	
	@Transactional(readOnly = false)
	public int deleteByCondition(String statementName, QueryCondition qc) {
		return this.getEntityDao().deleteByCondition(statementName, qc);
    }
	
	@Transactional(readOnly = false)
	public int updateByCondition(String statementName, QueryCondition qc) {
		return this.getEntityDao().updateByCondition(statementName, qc);
    }
	
	/**
	 * 批量新增
	 * @param statementName
	 * @param entitys
	 * @return
	 */
	@Transactional(readOnly = false)
	public int batchInsert(String statementName, final List<T> entitys) {
		return this.getEntityDao().batchInsert(statementName, entitys);
	}
	
	@Transactional(readOnly = false)
	public int batchUpdate(String statementName, final List<T> entitys) {
		return this.getEntityDao().batchUpdate(statementName, entitys);
	}
	
	/***************************************** 继承使用：查询T 单个实体 ***************************************/
	/**
	 * 根据xml 查询语句的id查询单个实体
	 * @param statementName
	 * @return
	 */
	@Transactional(readOnly = true)
	public T selectOne(String statementName) {
		return this.getEntityDao().selectOne(statementName);
	}
	/**
	 * 通过实体 附加参数查询
	 * @param statementName
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = true)
	public T selectOne(String statementName, Object entity) {
		return this.getEntityDao().selectOne(statementName, entity);
	}
	/**
	 * 通过map 附加参数查询
	 * @param statementName
	 * @param params
	 * @return
	 */
	@Transactional(readOnly = true)
	public T selectOne(String statementName, Map<String, ?> params) {
		return this.getEntityDao().selectOne(statementName, params);
	}
	/**
	 * 通过查询条件 附加参数查询
	 * @param statementName
	 * @param qc
	 * @return
	 */
	@Transactional(readOnly = true)
	public T selectOne(String statementName, QueryCondition qc) {
		return this.getEntityDao().selectOne(statementName, qc);
	}
	/**
	 * 通过查询条件 附加参数查询
	 * @param statementName
	 * @param qc
	 * @return
	 */
	@Transactional(readOnly = true)
	public Object selectOneResult(String statementName, QueryCondition qc) {
		return this.getEntityDao().selectOne(statementName, qc);
	}
	/***************************************** 继承使用：查询List<T>***************************************/
	/**
	 * 通过方法名查询
	 * @param statementName
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<T> selectList(String statementName) {
		return this.getEntityDao().selectList(statementName);
	}

	/**
	 * 通过实体 附加参数查询
	 * @param statementName
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<T> selectList(String statementName, Object entity) {
		return this.getEntityDao().selectList(statementName, entity);
	}

	/**
	 * 附加键值对参数进行查询
	 * @param statementName
	 * @param params
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<T> selectList(String statementName, Map<String, ?> params) {
		return this.getEntityDao().selectList(statementName, params);
	}

	/**
	 * 附加通用构造器的参数 进行查询
	 * @param statementName
	 * @param qc
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<T> selectList(String statementName, QueryCondition qc) {
		return this.getEntityDao().selectList(statementName, qc);
	}

	/***************************************** 继承使用：查询<E> List<E>***************************************/
	@Transactional(readOnly = true)
	public <E> List<E> selectList2(String statementName) {
		return this.getEntityDao().selectList2(statementName);
	}

	@Transactional(readOnly = true)
	public <E> List<E> selectList2(String statementName, Object entity) {
		return this.getEntityDao().selectList2(statementName, entity);
	}
	
	@Transactional(readOnly = true)
	public <E> List<E> selectList2(String statementName, Map<String, ?> params) {
		return this.getEntityDao().selectList2(statementName, params);
	}

	@Transactional(readOnly = true)
	public <E> List<E> selectList2(String statementName, QueryCondition qc) {
		return this.getEntityDao().selectList2(statementName, qc);
	}

	/***************************************** 继承使用：查询<E> E***************************************/

	@Transactional(readOnly = true)
	public <E> E selectOne2(String statementName) {
		return this.getEntityDao().selectOne2(statementName);
	}

	@Transactional(readOnly = true)
	public <E> E selectOne2(String statementName, Object entity) {
		return this.getEntityDao().selectOne2(statementName, entity);
	}

	@Transactional(readOnly = true)
	public <E> E selectOne2(String statementName, Map<String, ?> params) {
		return this.getEntityDao().selectOne2(statementName, params);
	}

	@Transactional(readOnly = true)
	public <E> E selectOne2(String statementName, QueryCondition qc) {
		return this.getEntityDao().selectOne2(statementName, qc);
	}
	/**
	 * 附加通用构造器的参数 进行查询
	 * @param statementName
	 * @param qc
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<String> selectList3(String statementName, QueryCondition qc) {
		return this.getEntityDao().selectList3(statementName, qc);
	}
	
	/**
	 * 获取国际化信息
	 * @param messageCcode  国际化编码 key
	 * @return
	 */
	protected String getMessage(String messageCcode) {
		String msg = localeMessages.getMessage(messageCcode);
		if (StringUtils.isNotBlank(msg)) {
			return msg;
		}
		return messageCcode;
	}

}
