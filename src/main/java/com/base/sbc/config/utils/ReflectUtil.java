package com.base.sbc.config.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author TMT
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class ReflectUtil {

	private static Logger logger = LoggerFactory.getLogger(ReflectUtil.class);
	
	public static void setFieldValue(Object target, String fname, Class ftype,
            Object fvalue) {
        boolean flag = target == null
                || fname == null
                || "".equals(fname)
                || (fvalue != null && !ftype
                .isAssignableFrom(fvalue.getClass()));
        if (flag) {
            return;
        }
		Class clazz = target.getClass();
        try {
            Method method = clazz.getDeclaredMethod("set"
                    + Character.toUpperCase(fname.charAt(0))
                    + fname.substring(1), ftype);
            if (!Modifier.isPublic(method.getModifiers())) {
                method.setAccessible(true);
            }
            method.invoke(target, fvalue);

        } catch (Exception me) {
            if (logger.isDebugEnabled()) {
            	logger.debug("", me);
            }
            try {
                Field field = clazz.getDeclaredField(fname);
                if (!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                }
                field.set(target, fvalue);
            } catch (Exception fe) {
                if (logger.isDebugEnabled()) {
                    logger.debug("", fe);
                }
            }
        }
    }
    
    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     */
    public static Object getFieldValue(final Object object, final String fieldName) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
        	throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }

        makeAccessible(field);

        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常{}", e);
        }
        return result;
    }

    /**
     * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
     */
    public static void setFieldValue(final Object object, final String fieldName, final Object value) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
        	throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
        makeAccessible(field);

        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常:{}", e);
        }
    }

    /**
     * 直接调用对象方法,无视private/protected修饰符.
     */
    public static Object invokeMethod(final Object object, final String methodName, final Class<?>[] parameterTypes,
            final Object[] parameters) throws InvocationTargetException {
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        if (method == null) {
        	throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
        }

        method.setAccessible(true);

        try {
            return method.invoke(object, parameters);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常:{}", e);
        }

        return null;
    }

    /**
     * 循环向上转型,获取对象的DeclaredField.
     */
    public static Field getDeclaredField(final Object object, final String fieldName) {
        Assert.notNull(object, "object不能为空");
        Assert.hasText(fieldName, "fieldName");
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 循环向上转型,获取对象的DeclaredField.
     */
    protected static void makeAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    /**
     * 循环向上转型,获取对象的DeclaredMethod.
     */
    protected static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {
        Assert.notNull(object, "object不能为空");

        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                // Method不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 通过反射,获得Class定义中声明的父类的泛型参数的类型.
     * eg.
     * public UserDao extends HibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be determined
     */
    public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射,获得定义Class时声明的父类的泛型参数的类型.
     * 
     * 如public UserDao extends HibernateDao<User,Long>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be determined
     */
    public static Class getSuperClassGenricType(final Class clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }

        return (Class) params[index];
    }
    
    /**
     * 得到属性的实际泛型类型
     * @param clazz
     * @param method
     * @return
     */
    public static Class getFieldGenricType(final Class clazz,String method){
    	try {
    		ParameterizedType pt = (ParameterizedType)clazz.getDeclaredField(method).getGenericType();
			return (Class)pt.getActualTypeArguments()[0];
		}catch (NoSuchFieldException e) {
			logger.error(clazz.getName()+"没有"+method+"方法");
		}
    	return Object.class;
    }

    /**
     * 提取集合中的对象的属性(通过getter函数),组合成List.
     * 
     * @param collection 来源集合.
     * @param propertyName 要提取的属性名.
     */
   /* public static List fetchElementPropertyToList(final Collection collection, final String propertyName) {
        List list = new ArrayList();

        try {
            for (Object obj : collection) {
                list.add(PropertyUtils.getProperty(obj, propertyName));
            }
        } catch (Exception e) {
            convertToUncheckedException(e);
        }

        return list;
    }*/

    /**
     * 提取集合中的对象的属性(通过getter函数),组合成由分割符分隔的字符串.
     * 
     * @param collection 来源集合.
     * @param propertyName 要提取的属性名.
     * @param separator 分隔符.
     */
   /* public static String fetchElementPropertyToString(final Collection collection, final String propertyName,
            final String separator) {
        List list = fetchElementPropertyToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }
*/

    /**
     * 将反射时的checked exception转换为unchecked exception.
     */
    public static IllegalArgumentException convertToUncheckedException(Exception e) {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException) {
        	return new IllegalArgumentException("Refelction Exception.", e);
        }
        else {
        	return new IllegalArgumentException(e);
        }
    }
    
    /**
     * 通过反射，将Bean转换为Map对象
     * @param obj
     * @author liliang
     */
	/*public static Map<String,Object> bean2Map(Object bean){
    	Map<String, Object> map;
		try {
			map = PropertyUtils.describe(bean);
		} catch (IllegalAccessException e) {
			throw convertToUncheckedException(e);
		} catch (InvocationTargetException e) {
			throw convertToUncheckedException(e);
		} catch (NoSuchMethodException e) {
			throw convertToUncheckedException(e);
		}
    	return map;
    }*/
    
    /** 
     * 判断一个类型是否是基本类型
     * @Title: isSimpleType 
     * @Description: 
     * @return boolean    返回类型 
     */
    public static boolean isSimpleType(Class<?> clazz){
    	return clazz.equals(String.class)||clazz.equals(Integer.class)||clazz.equals(Long.class)
    			|| clazz.equals(Short.class)
    			||clazz.equals(BigDecimal.class)||clazz.equals(java.util.Date.class)||
    			clazz.equals(java.sql.Date.class)||clazz.equals(Timestamp.class)||
    			clazz.equals(int.class)||clazz.equals(long.class)||clazz.isEnum();
    }
    
    /** 
     * 取得一个对象的全部属性名称，并通过列表返回
     * @Title: getAllFieldNames 
     * @Description: 
     * @return List<String>    返回类型 
     */
    public static List<String> getAllFieldNames(Class<?> clazz){
    	List<String> lstFieldNames = new ArrayList<>();
    	Field[] fields = clazz.getDeclaredFields();
    	for(Field field:fields){
    		lstFieldNames.add(field.getName());
    	}
    	return lstFieldNames;
    }
    
    /** 
     * @Title: getPropertyType 
     * @Description: 得到某个类的某个属性的类型 
     * @return Class<?>    返回类型 
     */
    public static Class<?> getPropertyType(Class<?> clazz,String property){
    	Field field;
		try {
			field = clazz.getDeclaredField(property);
			return field.getType();
		} catch (SecurityException e) {
			logger.error("访问安全异常", e);
		} catch (NoSuchFieldException e) {
			logger.error("无此方法", e);
		}
		return Object.class;
    }
    
    /** 
     * @Title: getPropertyType 
     * @Description: 得到某个类的某个属性的类型 
     * @return Class<?>    返回类型 
     */
    public static Class<?> getPropertyType(String className,String property){
    	try {
			return getPropertyType(Class.forName(className),property);
		} catch (ClassNotFoundException e) {
			logger.error("找不到对应的类型", e);
		}
    	return Object.class;
    }
    
    /** 
     * @Title: getPropertyDateType 
     * @Description: 判断一个类型是日期类型、数字类型还是字符串类型
     * @return DataType    返回类型 
     */
    public static DataType getPropertyDateType(Class<?> clazz){
    	if(clazz.equals(int.class)||clazz.equals(Integer.class)||clazz.equals(BigDecimal.class)
    			||clazz.equals(short.class)||clazz.equals(Short.class)
    			||clazz.equals(Long.class)||clazz.equals(long.class)||clazz.equals(double.class)){
    		return DataType.NUMBER;
    	}
    	if(clazz.equals(java.util.Date.class)||clazz.equals(java.sql.Date.class)||clazz.equals(Timestamp.class)){
    		return DataType.DATE;
    	}
    	return DataType.STRING;
    }
    
    /**对象数组初始化方法
     * @param obj
     * @param cla
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static void initArray(Object[] obj,Class cla){
         for(int i=0;i<obj.length;i++){
             try {
                obj[i]=cla.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
         }
     }
    
    /**
     * BeanUtils.describe(obj)，只是去掉了异常
     * @param obj
     * @return
     */
   /* public static Map descBean(Object obj){
    	Map mapReportData = new HashMap();
		try {
			mapReportData = BeanUtils.describe(obj);
		} catch (IllegalAccessException e) {
			logger.error("计算报错",e);
		} catch (InvocationTargetException e) {
			logger.error("计算报错",e);
		} catch (NoSuchMethodException e) {
			logger.error("计算报错",e);
		}
		return mapReportData;
    }*/
    
    /**
     * BeanUtils.populate(obj)，只是去掉了异常
     * @param bean
     * @param properties
     */
   /* public static void populate(Object bean,Map properties){
    	try {
			BeanUtils.populate(bean, properties);
		} catch (IllegalAccessException e) {
			logger.error("计算报错",e);
		} catch (InvocationTargetException e) {
			logger.error("计算报错",e);
		}
    }*/
    /**
     * 根据信息返回
     * @author xiong
     *
     */
    public enum DataType {
    	//类型
    	STRING("string"),
    	//数字
    	NUMBER("number"),
    	//时间
    	DATE("date");
    	public String getType() {
    		return type;
    	}
    	private String type;
    	DataType(String type){
    		this.type=type;
    	}
    }
}
