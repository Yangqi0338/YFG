
package com.base.sbc.config.redis;


import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.base.sbc.config.redis.RedisKeyBuilder.*;


/**
 *
 * @author 王赛超
 * 基于spring和redis的redisTemplate工具类
 * 针对所有的hash 都是以h开头的方法
 * 针对所有的Set 都是以s开头的方法                    不含通用方法
 * 针对所有的List 都是以l开头的方法
 */
public interface RedisKeyConstant {
	String STANDARD_COLUMN_CACHE_TOPIC = "standardColumn";
	RedisKeyBuilder STANDARD_COLUMN_LIST = init("standard_column_list");
	RedisKeyBuilder COUNTRY_LANGUAGE = init("country_language");
	RedisKeyBuilder STANDARD_COLUMN_COUNTRY_RELATION = init("standard_column_country_relation");
	RedisKeyBuilder HANG_TAG_COUNTRY = init("hang_tag_country");
	RedisKeyBuilder JOB_THREAD_ID = RedisKeyBuilder.builder().add("jobThreadId");

}
