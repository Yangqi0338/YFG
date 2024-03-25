
package com.base.sbc.config.redis;


import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.BaseService;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author 王赛超
 * 基于spring和redis的redisTemplate工具类
 * 针对所有的hash 都是以h开头的方法
 * 针对所有的Set 都是以s开头的方法                    不含通用方法
 * 针对所有的List 都是以l开头的方法
 */
@Aspect
public class RedisEnhanceTemplate extends RedisTemplate<String, Object> {

	private BaseService<?> businessService;
	public String message;

	// 可以使用配置文件修改
	private final TimeUnit timeUnit = TimeUnit.SECONDS;

	public Boolean expire(String key,long time) {
		if(time>0){
			return this.expire(key, time, timeUnit);
		}
		return true;
	}

	@Override
	public Long getExpire(String key) {
		return super.getExpire(key, timeUnit);
	}

	public void delete(String... key) {
		if(key!=null && key.length>0){
			if(key.length==1){
				this.delete(key[0]);
			}else{
				this.delete(CollectionUtils.arrayToList(key));
			}
		}
	}

	@Override
	public Set<String> keys(String pattern) {
		// 使用scan
		return this.execute((RedisCallback<Set<String>>) connection -> {
			Set<String> keysTmp = new HashSet<>();
			try {
				Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder()
						.match(pattern)
						.count(10000).build());

				while (cursor.hasNext()) {
 					keysTmp.add(new String(cursor.next(), "Utf-8"));
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new RuntimeException(e);
			}
			return keysTmp;
		});
	}

	public void deletePattern(String pattern) {
		Set<String> keys = keys(pattern+(pattern.endsWith(":")?"":":")+"*");
		if (!keys.isEmpty()) {
			this.delete(keys);
		}
	}

	public Object hget(String key,String item){
		Object hashValue = this.opsForHash().get(key, item);
		if (hashValue == null) {
			if (businessService != null) {
				hashValue = businessService.findByCode(item);
				this.opsForHash().put(key,item, hashValue);
			}
		}
		if (hashValue == null && StrUtil.isNotBlank(message)) {
			throw new OtherException(message);
		}
		return hashValue;
	}

	public RedisEnhanceTemplate setBusinessService(BaseService<?> businessService) {
		this.businessService = businessService;
		return this;
	}

	public RedisEnhanceTemplate setMessage(String message) {
		this.message = message;
		return this;
	}
}
