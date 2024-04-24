
package com.base.sbc.config.redis;


import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 *
 * @author 王赛超
 * 基于spring和redis的redisTemplate工具类
 * 针对所有的hash 都是以h开头的方法
 * 针对所有的Set 都是以s开头的方法                    不含通用方法
 * 针对所有的List 都是以l开头的方法
 */
@Data
public class RedisKeyBuilder {
	private RedisKeyBuilder() {
	}

	public static final String COMMA = ":";

	private static final String PREFIX = "businessData";
	private static final String CACHE = "CACHE";

	private List<String> keys = new ArrayList<>();
	private List<String> baseKeys = new ArrayList<>();
	private StringJoiner keyJoiner = new StringJoiner(COMMA);

	private static void clearJoiner(StringJoiner joiner){
        try {
			Field field = StringJoiner.class.getDeclaredField("value");
			field.setAccessible(true);
			field.set(joiner,null);
		} catch (Exception e) {
			e.printStackTrace();
        }
    }

	public static RedisKeyBuilder builder(){
		return new RedisKeyBuilder();
	}

	private static String build(RedisKeyBuilder builder){
		List<String> keys = builder.getKeys();
		StringJoiner keyJoiner = builder.getKeyJoiner();
		for (String key : keys) {
			keyJoiner.add(key);
		}
		return keyJoiner.toString();
	}

	public static RedisKeyBuilder init(String... keys){
		RedisKeyBuilder builder = new RedisKeyBuilder();
		List<String> keyList = CollUtil.toList(PREFIX);
		keyList.addAll(CollUtil.toList(keys));
		builder.setKeys(new ArrayList<>(keyList));
		builder.setBaseKeys(new ArrayList<>(keyList));
		return builder;
	}

	public RedisKeyBuilder add(String... keys){
		return add(false, keys);
	}

	public String addEnd(String... keys){
		return addEnd(false, keys);
	}

	public String addEnd(boolean cache, String... keys){
		return add(cache, keys).build();
	}

	public RedisKeyBuilder add(boolean cache, String... keys){
		String oldValue = this.cache(cache).build();
		List<String> newKeys = CollUtil.toList(oldValue);
		newKeys.addAll(CollUtil.toList(keys));
		this.setKeys(newKeys);
		this.setKeyJoiner(new StringJoiner(COMMA));
		return this;
	}

	public RedisKeyBuilder cache(boolean cache){
		if (cache) {
			this.keys.add(1, CACHE);
		}
		return this;
	}

	public String build() {
		String str = build(this);
		clearJoiner(this.keyJoiner);
		this.keys = new ArrayList<>(this.baseKeys);
		return str;
	}
}
