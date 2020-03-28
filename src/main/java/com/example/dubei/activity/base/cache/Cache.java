package com.example.dubei.activity.base.cache;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存缓存
 * Created by yuyong.zhao on 2017-08-04 15:45.
 */
public class Cache {
    private final static Map<String, Entity> cache = new ConcurrentHashMap();
    //默认失效时间1h
    private final static int EXPIRE_TIME_1_H =  60 * 60 * 1000; // 1H

    public static Object getValue(String key) {
        if (key != null) {
            Entity entity = cache.get(key);
            if (entity == null) {
                return null;
            }
            long time = System.currentTimeMillis();
            long expirTime = entity.getExpireTime();
            if (time > expirTime && expirTime > 0) {
                cache.remove(key);
                return null;
            }
            return entity.getValue();
        }
        return null;
    }

    public static int put(String key, Object value) {
        return put(key, value, System.currentTimeMillis() + EXPIRE_TIME_1_H);
    }

    /**
     *
     * @param key
     * @param value
     * @param expireTime 超时时间，单位秒;0为永不超时
     * @return
     */
    public static int put(String key, Object value, long expireTime) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("参数异常.参数不能为null");
        }

        Entity entity = new Entity(key, value, expireTime); // 转化为毫秒
        cache.put(key, entity);
        return 1;
    }

    public static boolean contains(String key) {
        return getValue(key) !=null?true:false;
    }

    public static Object remove(Object key) {
        return cache.remove(key);
    }

    static class Entity {
        private String key;
        private Object value;
        //0代表永不失效。否则代表失效时刻
        private long expireTime;

        public Entity(String key, Object value, long expireTime) {
            this.key = key;
            this.value = value;
            this.expireTime = expireTime;
        }

        public String getKey() {
            return key;
        }

        public Entity setKey(String key) {
            this.key = key;
            return this;
        }

        public Object getValue() {
            return value;
        }

        public Entity setValue(Object value) {
            this.value = value;
            return this;
        }

        public long getExpireTime() {
            return expireTime;
        }

        public Entity setExpireTime(long expireTime) {
            this.expireTime = expireTime;
            return this;
        }
    }
}
