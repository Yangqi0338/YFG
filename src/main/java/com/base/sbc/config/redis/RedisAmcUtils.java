
package com.base.sbc.config.redis;


import org.springframework.stereotype.Component;


/**
 *
 * @author 王赛超
 * 基于spring和redis的redisTemplate工具类
 * 针对所有的hash 都是以h开头的方法
 * 针对所有的Set 都是以s开头的方法                    不含通用方法
 * 针对所有的List 都是以l开头的方法
 */
@Component
public class RedisAmcUtils extends RedisFunUtils {

	public RedisAmcUtils() {
		setRedisTemplate(1);
	}
}
