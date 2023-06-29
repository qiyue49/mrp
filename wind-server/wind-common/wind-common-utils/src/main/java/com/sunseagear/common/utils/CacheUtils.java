package com.sunseagear.common.utils;

import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @title: CacheUtils.java
 * @package com.sunseagear.core.utils
 * @description: Cache工具类  * @date:   2017年6月27日 下午10:41:03
 * @copyright: 2017 www.sunseagear.com Inc. All rights reserved.
 */
public class CacheUtils {

    private static RedisTemplate redisTemplate;
    private static final String SYS_CACHE = "sys_cache";

    static {
        redisTemplate = SpringContextHolder.getBean("redisTemplate");
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public static <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 删除基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     */
    public static void deleteCacheObject(final String key) {
        redisTemplate.delete(key);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public static <T> void setCacheObject(final String key, final T value, final long timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }


    /**
     * 缓存基本的对象到数据集，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public static <T> void addCacheSet(final String key, final T value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * 从数据集删除基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     */
    public static void removeCacheSet(final String key, String value) {
        redisTemplate.opsForSet().remove(key, value);
    }


    /**
     * 设置有效时间，默认单位为秒
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public static <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除对象
     *
     * @param cacheName
     * @return
     */
    public static boolean clear(final String cacheName) {
        Set<String> keys = redisTemplate.keys(cacheName + "*");
        long count = redisTemplate.delete(keys);
        return keys.size() == count;
    }

    public static Set<String> keys(final String cacheName){
        return redisTemplate.keys(cacheName + "*");
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public static <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public static <T> Map<String, T> getCacheMap(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 缓存SET对象，名称为SYS_CACHE
     *
     * @param key
     * @return
     */
    public static void put(String key, Object value) {
        put(SYS_CACHE, key, value);
    }

    /**
     * 缓存SET对象
     *
     * @param cacheName
     * @param key
     * @param value
     */
    public static void put(String cacheName, String key, Object value) {
        getCache(cacheName).put(key, value);
    }

    /**
     * 获取SET对象，名称为SYS_CACHE
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        return get(SYS_CACHE, key);
    }

    /**
     * 获取SET对象
     *
     * @param cacheName 缓存名称
     * @param key       键名称
     * @return value
     */
    public static Object get(String cacheName, String key) {
        return getCache(cacheName).get(key);
    }

    /**
     * 获得缓存的set的全部成员
     *
     * @param key
     * @return
     */
    public static <T> Set<T> getCacheSet(final String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 从SYS_CACHE缓存中移除SET对象，名称为SYS_CACHE
     *
     * @param key
     * @return
     */
    public static void remove(String key) {
        remove(SYS_CACHE, key);
    }

    /**
     * 从缓存中移除SET对象
     *
     * @param cacheName
     * @param key
     */
    public static void remove(String cacheName, String key) {
        getCache(cacheName).delete(key);
    }

    /**
     * 获得一个Cache，没有则创建一个。
     *
     * @param cacheName
     * @return
     */
    public static BoundHashOperations getCache(String cacheName) {
        return redisTemplate.boundHashOps(cacheName);
    }

}
