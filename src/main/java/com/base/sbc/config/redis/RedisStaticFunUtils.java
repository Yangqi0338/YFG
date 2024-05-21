
package com.base.sbc.config.redis;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.base.sbc.module.common.service.BaseService;
import org.bouncycastle.util.Arrays;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author 王赛超
 * 基于spring和redis的redisTemplate工具类
 * 针对所有的hash 都是以h开头的方法
 * 针对所有的Set 都是以s开头的方法                    不含通用方法
 * 针对所有的List 都是以l开头的方法
 */
@Component
public class RedisStaticFunUtils {

	@Resource(name = "redisTemplateAmc")
	public void redisTemplateAmc(RedisTemplate<String, Object> redisTemplateAmc){
		RedisStaticFunUtils.redisTemplateAmc = redisTemplateAmc;
	}

	@Resource(name = "redisTemplate")
	public void redisTemplate(RedisTemplate<String, Object> redisTemplate) {
		RedisStaticFunUtils.redisTemplateDefault = redisTemplate;
	}

	private static RedisTemplate<String, Object> redisTemplateAmc;
	private static RedisTemplate<String, Object> redisTemplateDefault;

	private static final ThreadLocal<RedisEnhanceTemplate> currentRedisTemplate = new TransmittableThreadLocal<>();

	public static RedisEnhanceTemplate setRedisTemplate(boolean amc) {
		RedisEnhanceTemplate template = BeanUtil.copyProperties(amc ? redisTemplateAmc : redisTemplateDefault, RedisEnhanceTemplate.class);
		template.afterPropertiesSet();
		currentRedisTemplate.set(template);
		return template;
	}

	private static RedisEnhanceTemplate getRedisTemplate() {
		RedisEnhanceTemplate redisEnhanceTemplate = currentRedisTemplate.get();
		if (redisEnhanceTemplate == null) {
			redisEnhanceTemplate = setRedisTemplate(false);
		}
		return redisEnhanceTemplate;
	}

	public static void clear() {
		RedisEnhanceTemplate redisEnhanceTemplate = currentRedisTemplate.get();
		if (redisEnhanceTemplate != null) {
			currentRedisTemplate.remove();
		}
	}

	public static RedisEnhanceTemplate setBusinessService(BaseService<?> businessService) {
		clear();
		return getRedisTemplate().setBusinessService(businessService);
	}

	public static RedisEnhanceTemplate setMessage(String message) {
		return getRedisTemplate().setMessage(message);
	}

	/**
	 * 默认过期时长，单位：秒
	 */
	public static final long DEFAULT_EXPIRE = 60 * 60 * 24;

	//=============================common============================
	/**
	 * 指定缓存失效时间
	 * @param key 键
	 * @param time 时间(秒)
	 * @return
	 */
	public static boolean expire(String key,long time){
		try {
			return getRedisTemplate().expire(key, time);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据key 获取过期时间
	 * @param key 键 不能为null
	 * @return 时间(秒) 返回0代表为永久有效
	 */
	public static long getExpire(String key){
		return getRedisTemplate().getExpire(key);
	}

	/**
	 * 判断key是否存在
	 * @param key 键
	 * @return true 存在 false不存在
	 */
	public static boolean hasKey(String key){
		try {
			return getRedisTemplate().hasKey(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除缓存
	 * @param key 可以传一个值 或多个
	 */
	@SuppressWarnings("unchecked")
	public static void del(String ... key){
		getRedisTemplate().delete(key);
	}

	/**
	 * 批量删除key
	 * @param pattern
	 */
	public static void removePattern(String pattern) {
		getRedisTemplate().deletePattern(pattern);
	}
	/**
	 * 批量删除key 根据前后缀删除
	 * @param pattern
	 */
	public static void removePatternAndIndexOf(String pattern,String[] suffix) {
		if (suffix.length > 0) {
			for(String indexOf : suffix)
			{
				removePatternAndIndexOf(pattern,indexOf);
			}
		}
	}

	public static String decorateKey(String key){
		return decorateKey(key, RedisKeyBuilder.COMMA);
	}

	public static String decorateKey(String key, String separator){
		return key + (key.endsWith(separator) ? "" : separator);
	}

	/**
	 * 扫描
	 * @param key
	 */
	public static Set<String> keys(String key,String... indexList) {
		StringBuilder pattern = new StringBuilder(decorateKey(key));
		if (!Arrays.isNullOrEmpty(indexList)) {
			for (String index : indexList) {
				pattern.append(decorateKey(index));
			}
		}

		return getRedisTemplate().keys(decorateKey(pattern.toString(),"*"));
	}

	/**
	 * 取string的长度
	 * @param key
	 */
	public static Long size(String key) {
		return getRedisTemplate().opsForList().size(key);
	}
	/**
	 * 批量删除key 根据前缀与中间值删除
	 * @param pattern
	 */
	public static void removePatternAndIndexOf(String pattern,String indexOf) {
		
		Set<String> keys = getRedisTemplate().keys(pattern+(pattern.endsWith(":")?"":":")+"*:"+indexOf+":*");
		if (keys.size() > 0) {
			for(String key : keys)
			{
				getRedisTemplate().delete(key);
			}
		}
	}
	//============================String=============================
	/**
	 * 普通缓存获取
	 * @param key 键
	 * @return 值
	 */
	public static Object get(String key){
		
		return key==null?null:getRedisTemplate().opsForValue().get(key);
	}

	/**
	 * 普通缓存获取并删除
	 * @param key 键
	 * @return 值
	 */
	public static Object sPop(String key){
		// 使用Lua脚本保证原子性 TODO
		if (StrUtil.isBlank(key)) return null;
		Object value = getRedisTemplate().opsForValue().get(key);
		del(key);
		return value;
	}


	/**
	 * 普通缓存放入
	 * @param key 键
	 * @param value 值
	 * @return true成功 false失败
	 */
	public static boolean set(String key,Object value) {
		try {
			
			getRedisTemplate().opsForValue().set(key, value);
			expire(key, RedisStaticFunUtils.DEFAULT_EXPIRE);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 普通缓存放入并设置时间
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
	 */
	public static boolean set(String key,Object value,long time){
		try {
			
			if(time>0){
				getRedisTemplate().opsForValue().set(key, value, time, TimeUnit.SECONDS);
			}else{
				getRedisTemplate().opsForValue().set(key, value);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 递增
	 * @param key 键
	 * @param by 要增加几(大于0)
	 * @return
	 */
	public static long incr(String key, long delta){
		if(delta<0){
			throw new RuntimeException("递增因子必须大于0");
		}

		return getRedisTemplate().opsForValue().increment(key, delta);
	}

	/**
	 * 递减
	 * @param key 键
	 * @param by 要减少几(小于0)
	 * @return
	 */
	public static long decr(String key, long delta){
		if(delta<0){
			throw new RuntimeException("递减因子必须大于0");
		}
		
		return getRedisTemplate().opsForValue().increment(key, -delta);
	}

	//================================Map=================================
	/**
	 * HashGet
	 * @param key 键 不能为null
	 * @param item 项 不能为null
	 * @return 值
	 */
	public static Object hget(String key,String item){
		return getRedisTemplate().hget(key, item);
	}

	/**
	 * 获取hashKey对应的所有键值
	 * @param key 键
	 * @return 对应的多个键值
	 */
	public static Map<Object,Object> hmget(String key){
		
		return getRedisTemplate().opsForHash().entries(key);
	}

	/**
	 * HashSet
	 * @param key 键
	 * @param map 对应多个键值
	 * @return true 成功 false 失败
	 */
	public static boolean hmset(String key, Map<String,Object> map){
		try {
			
			getRedisTemplate().opsForHash().putAll(key, map);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * HashSet 并设置时间
	 * @param key 键
	 * @param map 对应多个键值
	 * @param time 时间(秒)
	 * @return true成功 false失败
	 */
	public static boolean hmset(String key, Map<String,Object> map, long time){
		try {
			
			getRedisTemplate().opsForHash().putAll(key, map);
			if(time>0){
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 * @param key 键
	 * @param item 项
	 * @param value 值
	 * @return true 成功 false失败
	 */
	public static boolean hset(String key,String item,Object value) {
		try {
			
			getRedisTemplate().opsForHash().put(key, item, value);
			expire(key, RedisStaticFunUtils.DEFAULT_EXPIRE);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 * @param key 键
	 * @param item 项
	 * @param value 值
	 * @param time 时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
	 * @return true 成功 false失败
	 */
	public static boolean hset(String key,String item,Object value,long time) {
		try {
			
			getRedisTemplate().opsForHash().put(key, item, value);
			if(time>0){
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除hash表中的值
	 * @param key 键 不能为null
	 * @param item 项 可以使多个 不能为null
	 */
	public static void hdel(String key, Object... item){
		
		getRedisTemplate().opsForHash().delete(key,item);
	}

	/**
	 * 判断hash表中是否有该项的值
	 * @param key 键 不能为null
	 * @param item 项 不能为null
	 * @return true 存在 false不存在
	 */
	public static boolean hHasKey(String key, String item){
		
		return getRedisTemplate().opsForHash().hasKey(key, item);
	}

	/**
	 * hash递增 如果不存在,就会创建一个 并把新增后的值返回
	 * @param key 键
	 * @param item 项
	 * @param by 要增加几(大于0)
	 * @return
	 */
	public static double hincr(String key, String item,double by){
		
		return getRedisTemplate().opsForHash().increment(key, item, by);
	}

	/**
	 * hash递减
	 * @param key 键
	 * @param item 项
	 * @param by 要减少记(小于0)
	 * @return
	 */
	public static double hdecr(String key, String item,double by){
		
		return getRedisTemplate().opsForHash().increment(key, item,-by);
	}

	//============================set=============================
	/**
	 * 根据key获取Set中的所有值
	 * @param key 键
	 * @return
	 */
	public static Set<Object> sGet(String key){
		try {
			
			return getRedisTemplate().opsForSet().members(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据value从一个set中查询,是否存在
	 * @param key 键
	 * @param value 值
	 * @return true 存在 false不存在
	 */
	public static boolean sHasKey(String key,Object value){
		try {
			
			return getRedisTemplate().opsForSet().isMember(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将数据放入set缓存
	 * @param key 键
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public static long sSet(String key, List<Object> values) {
		return sSet(key, values.toArray(new Object[]{}));
	}

	/**
	 * 将数据放入set缓存
	 * @param key 键
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public static long sSet(String key, Object...values) {
		try {
			
			expire(key, RedisStaticFunUtils.DEFAULT_EXPIRE);
			SetOperations<String, Object> opsForSet = getRedisTemplate().opsForSet();
			for (Object value : values) {
				opsForSet.add(key, value);
			}
			return values.length;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 将set数据放入缓存
	 * @param key 键
	 * @param time 时间(秒)
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public static long sSetAndTime(String key,long time,Object...values) {
		try {
			
			Long count = getRedisTemplate().opsForSet().add(key, values);
			if(time>0) {
				expire(key, time);
			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取set缓存的长度
	 * @param key 键
	 * @return
	 */
	public static long sGetSetSize(String key){
		try {
			
			return getRedisTemplate().opsForSet().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 移除值为value的
	 * @param key 键
	 * @param values 值 可以是多个
	 * @return 移除的个数
	 */
	public static long setRemove(String key, Object ...values) {
		try {
			
			Long count = getRedisTemplate().opsForSet().remove(key, values);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	//===============================list=================================

	public static List<Object> lGet(String key){
		return lGet(key, 0, -1);
	}

	/**
	 * 获取list缓存的内容
	 * @param key 键
	 * @param start 开始
	 * @param end 结束  0 到 -1代表所有值
	 * @return
	 */
	public static List<Object> lGet(String key,long start, long end){
		try {
			
			return getRedisTemplate().opsForList().range(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取list缓存的长度
	 * @param key 键
	 * @return
	 */
	public static long lGetListSize(String key){
		try {
			
			return getRedisTemplate().opsForList().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 通过索引 获取list中的值
	 * @param key 键
	 * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
	 * @return
	 */
	public static Object lGetIndex(String key,long index){
		try {
			
			return getRedisTemplate().opsForList().index(key, index);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将list放入缓存
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒)
	 * @return
	 */
	public static boolean lSet(String key, Object value) {
		try {
			
			getRedisTemplate().opsForList().rightPush(key, value);
			expire(key, RedisStaticFunUtils.DEFAULT_EXPIRE);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒)
	 * @return
	 */
	public static boolean lSet(String key, Object value, long time) {
		try {
			
			getRedisTemplate().opsForList().rightPush(key, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒)
	 * @return
	 */
	public static boolean lSet(String key, List<Object> value) {
		try {

			getRedisTemplate().opsForList().rightPushAll(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒)
	 * @return
	 */
	public static boolean lSet(String key, List<Object> value, long time) {
		try {

			getRedisTemplate().opsForList().rightPushAll(key, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据索引修改list中的某条数据
	 * @param key 键
	 * @param index 索引
	 * @param value 值
	 * @return
	 */
	public static boolean lUpdateIndex(String key, long index,Object value) {
		try {
			
			getRedisTemplate().opsForList().set(key, index, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 移除N个值为value
	 * @param key 键
	 * @param count 移除多少个
	 * @param value 值
	 * @return 移除的个数
	 */
	public static long lRemove(String key,long count,Object value) {
		try {
			
			Long remove = getRedisTemplate().opsForList().remove(key, count, value);
			return remove;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
