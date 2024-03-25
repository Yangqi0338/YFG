package com.base.sbc.config.utils;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageInfo;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		PageInfo<T> pageResult = mapperFactory.getMapperFacade().map(page,PageInfo.class);
		List<T> records = copy(sourceList, targetClazz);
		pageResult.setList(records);
		return pageResult;
	}

	public static <T> PageInfo<T> copy(PageInfo<?> page, List<T> targetList) {
		PageInfo<T> pageResult = mapperFactory.getMapperFacade().map(page,PageInfo.class);
		pageResult.setList(targetList);
		return pageResult;
	}


	public static <A, B> ClassMapBuilder<A, B> classMap(Class<A> source, Class<B> target) {
		return mapperFactory.classMap(source, target);
	}

	/**
	 * 拷贝并转换为树形结构
	 *
	 * @param <S>
	 * @param <D>
	 * @param source     拷贝源
	 * @param target     目标class
	 * @param code       当前编码
	 * @param parentCode 上级编码
	 * @param children   存放的下级List名称
	 * @return
	 */
	public static <S, D> List<D> tree(Iterable<S> source, Class<D> target, String code, String parentCode,
									  String children) {
		List<D> list = mapperFactory.getMapperFacade().mapAsList(source, target);
		List<D> treeList = new ArrayList<>();
		Map<String, D> map = new HashMap<>(10);
		list.forEach(item -> map.put(String.valueOf(getValue(item, code)), item));
		list.forEach(item -> {
			D parent = map.get(String.valueOf(getValue(item, parentCode)));
			if (parent != null) {
				@SuppressWarnings("unchecked")
				List<D> items = (List<D>) getValue(parent, children);
				if (items == null) {
					items = new ArrayList<>();
				}
				items.add(item);
				setValue(parent, children, items);
			} else {
				treeList.add(item);
			}
		});
		return treeList;
	}

	/**
	 * 存值
	 *
	 * @param target
	 * @param field
	 * @param value
	 */
	private static void setValue(Object target, String field, Object value) {
		Field fd;
		try {
			fd = target.getClass().getDeclaredField(field);
			fd.setAccessible(true);
			fd.set(target, value);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取值
	 *
	 * @param target 对象
	 * @param field  属性
	 * @return
	 */
	private static Object getValue(Object target, String field) {
		Field fd;
		try {
			fd = target.getClass().getDeclaredField(field);
			fd.setAccessible(true);
			return fd.get(target);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
