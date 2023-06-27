package com.base.sbc.config.utils;

import com.github.pagehelper.PageInfo;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import java.util.List;

/**
 * Orika是一个简单、快速的JavaBean拷贝框架，Orika使用字节代码生成来创建具有最小开销的快速映射器。
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年6月21日
 */
public class CopyUtil {

	private static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

	/**
	 * 复制对象
	 * 
	 * @param <T>
	 * @param source
	 * @param target
	 * @return
	 */
	public static <T> T copy(Object source, Class<T> target) {
		return mapperFactory.getMapperFacade().map(source, target);
	}

	/**
	 * 给对象赋值相同属性
	 * 
	 * @param source 来源
	 * @param target 目前对象
	 */
	public static void copy(Object source, Object target) {
		mapperFactory.getMapperFacade().map(source, target);
	}

	/**
	 * 复制集合
	 * 
	 * @param <S>
	 * @param <D>
	 * @param source
	 * @param target
	 * @return
	 */
	public static <S, D> List<D> copy(Iterable<S> source, Class<D> target) {
		return mapperFactory.getMapperFacade().mapAsList(source, target);
	}

	/**
	 * 复制分页对象
	 * 
	 * @param <T>
	 * @param <E>
	 * @param page
	 * @param targetClazz
	 * @return
	 */
	public static <T, E> PageInfo<T> copy(PageInfo<?> page, Class<T> targetClazz) {
		return copy(page, page.getList(), targetClazz);
	}

	/**
	 * 复制分页对象
	 * 
	 * @param <T>
	 * @param <E>
	 * @param page
	 * @param sourceList
	 * @param targetClazz
	 * @return
	 */
	public static <T, E> PageInfo<T> copy(PageInfo<?> page, List<E> sourceList, Class<T> targetClazz) {
		PageInfo<T> pageResult = new PageInfo<T>();
		pageResult.setTotal(page.getTotal());
		pageResult.setPageNum(page.getPageNum());
		pageResult.setPageSize(page.getPageSize());
		List<T> records = copy(sourceList, targetClazz);
		pageResult.setList(records);
		return pageResult;
	}

	public static <A, B> ClassMapBuilder<A, B> classMap(Class<A> source, Class<B> target) {
		return mapperFactory.classMap(source, target);
	}
}
