/**
 *
 */
package com.jswhzl.common.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jswhzl.common.config.Constants;
import com.jswhzl.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis缓存基类
 *
 * @param <T>
 * @author Jinjichao
 */
@Component
public class BaseRedisCache<T> {

    /**
     *
     */
    private static Logger logger = LoggerFactory.getLogger(BaseRedisCache.class.getName());

    /**
     *
     */
    private String loggerName = "【缓存管理】";

    /**
     * 命名空间分隔符
     */
    public final String SEPARATOR = ":";

    /**
     * 键名通配符
     */
    private final String WILDCARD = "*";

    /**
     * 默认有效期
     */
    private final Long TIMEOUT = Constants.DEFAULT_EXPIRE_SECOND;

    /**
     * 默认空KEY名称
     */
    private final String EMPTY_KEY = "EMPTYKEY";

    /**
     * 日志开关
     */
    private final boolean LOG_SWITCH = false;

    /**
     *
     */
    @Resource
    protected RedisTemplate<String, T> redisTemplate;

    /**
     *
     */
    protected String prefix;

    /**
     *
     */
    public BaseRedisCache() {

    }

    /**
     * @param redisTemplate
     * @param prefix
     */
    public BaseRedisCache(RedisTemplate<String, T> redisTemplate, String prefix) {
        this.redisTemplate = redisTemplate;
        this.prefix = prefix;
    }

    /**
     * 插入永久object
     *
     * @param id
     * @param object
     */
    public void insert(Serializable id, T object) {
        if (id != null && object != null) {
            insert(id.toString(), object);
        }
    }

    /**
     * 插入永久object
     *
     * @param id
     * @param object
     */
    public void insert(String id, T object) {
        if (StringUtil.isNotEmpty(id) && object != null) {
            redisTemplate.opsForValue().set(getKey(id), object);
            this.logInfo("set " + getKey(id) + " " + object);
        }
    }

    /**
     * 插入永久object
     *
     * @param object
     * @return
     */
    public void insertObj(T object) {
        if (object != null) {
            redisTemplate.opsForValue().set(getKey(), object);
            this.logInfo("set " + getKey() + " " + object);
        }
    }

    /**
     * 插入定时object
     *
     * @param id
     * @param object
     * @param timeout
     */
    public void insert(String id, T object, Long timeout) {
        if (StringUtil.isNotEmpty(id) && object != null) {
            timeout = timeout == null ? TIMEOUT : timeout;
            redisTemplate.opsForValue().set(getKey(id), object, timeout, TimeUnit.SECONDS);
            this.logInfo("set " + getKey(id) + " " + timeout + " " + object);
        }
    }


    /**
     * 插入永久List
     *
     * @param list
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    @SuppressWarnings("unchecked")
    public void insertList(List<T> list) throws IllegalArgumentException, IllegalAccessException {
        if (StringUtil.isNotEmpty(list)) {

            Map<String, T> map = new HashMap<String, T>();
            Class<T> clazz = (Class<T>) list.get(0).getClass();
            Field[] fields = clazz.getDeclaredFields();

            //将已逻辑删除的记录移除
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getAnnotation(TableLogic.class) != null) {
                    list = list.stream()
                            .filter(object -> {
                                Integer isEnable = null;
                                try {
                                    isEnable = (Integer) field.get(object);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                if (isEnable != null && isEnable == 0) {
                                    return false;
                                }
                                return true;
                            })
                            .collect(Collectors.toList());
                }
            }

            //提取entity主键，再将entity存入map中
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getAnnotation(TableId.class) != null) {
                    for (T object : list) {
                        String id = field.get(object).toString();
                        if (StringUtil.isNotEmpty(id) && object != null) {
                            map.put(id, object);
                        }
                    }
                    this.insertMap(map);
                    break;
                }
            }
        }
    }

    /**
     * 插入定时List
     *
     * @param list
     * @param timeout
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void insertList(List<T> list, Long timeout) throws Exception {
        if (StringUtil.isNotEmpty(list)) {
            Map<String, T> map = new HashMap<String, T>();
            Class<T> clazz = (Class<T>) list.get(0).getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getAnnotation(TableId.class) != null) {
                    for (T object : list) {
                        String id = field.get(object).toString();
                        if (StringUtil.isNotEmpty(id) && object != null) {
                            map.put(id, object);
                        }
                    }
                    timeout = timeout == null ? TIMEOUT : timeout;
                    this.insertMap(map, timeout);
                    break;
                }
            }
        }
    }

    /**
     * 插入永久map
     *
     * @param map
     */
    public void insertMap(Map<String, T> map) {
        Map<String, T> keyMap = new HashMap<String, T>();
        for (String id : map.keySet()) {
            if (StringUtil.isNotEmpty(id) && map.get(id) != null) {
                keyMap.put(getKey(id), map.get(id));
            }
        }
        redisTemplate.opsForValue().multiSet(keyMap);
        this.logInfo("mset " + keyMap);
    }

    /**
     * 插入定时map
     *
     * @param map
     * @param timeout
     */
    public void insertMap(Map<String, T> map, Long timeout) {
        for (String id : map.keySet()) {
            if (StringUtil.isNotEmpty(id) && map.get(id) != null) {
                timeout = timeout == null ? TIMEOUT : timeout;
                redisTemplate.opsForValue().set(getKey(id), map.get(id), timeout, TimeUnit.SECONDS);
                this.logInfo("setex " + getKey(id) + " " + timeout + " " + map.get(id));
            }
        }

    }

    /**
     * 查询对象所有记录返回List
     *
     * @param key
     * @return
     */
    public List<T> selectAll() {
        List<T> list = new ArrayList<T>();
        Set<String> keys = redisTemplate.keys(prefix + SEPARATOR + WILDCARD);
        keys.forEach(key -> {
            T object = redisTemplate.opsForValue().get(key);
            this.logInfo("get " + key + " " + object);
            if (object != null) {
                list.add(object);
            }
        });
        return list;
    }

    /**
     * 查询对象所有记录返回Map
     *
     * @param key
     * @return
     */
    public Map<String, T> selectAllMap() {
        Map<String, T> map = new HashMap<String, T>();
        Set<String> keys = redisTemplate.keys(prefix + SEPARATOR + WILDCARD);
        keys.forEach(key -> {
            T object = redisTemplate.opsForValue().get(key);
            this.logInfo("get " + key + " " + object);
            if (object != null) {
                map.put(getId(key), object);
            }
        });
        return map;
    }

    /**
     * 查询object
     *
     * @param id
     * @return
     */
    public T selectById(Serializable id) {
        if (id != null) {
            return selectById(id.toString());
        }
        return null;
    }

    /**
     * 查询object
     *
     * @param id
     * @return
     */
    public T selectById(String id) {
        if (StringUtil.isNotEmpty(id)) {
            T object = redisTemplate.opsForValue().get(getKey(id));
            this.logInfo("get " + getKey(id) + " " + object);
            return object;
        }
        return null;
    }

    /**
     * 查询object
     *
     * @return
     */
    public T select() {
        T object = redisTemplate.opsForValue().get(getKey());
        this.logInfo("get " + getKey() + " " + object);
        return object;
    }

    /**
     * 批量查询
     *
     * @param list
     * @return
     */
    public List<T> selectListByIds(List<String> ids) {
        return redisTemplate.opsForValue().multiGet(getKeys(ids));
    }

    /**
     * 批量查询
     *
     * @param list
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<T> selectByList(List<T> list) throws Exception {
        if (StringUtil.isNotEmpty(list)) {
            List<String> ids = new ArrayList<String>();
            Class<T> clazz = (Class<T>) list.get(0).getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getAnnotation(TableId.class) != null) {
                    for (T object : list) {
                        String id = field.get(object).toString();
                        if (StringUtil.isNotEmpty(id) && object != null) {
                            ids.add(id);
                        }
                    }
                    break;
                }
            }
            return selectListByIds(ids);
        }
        return list;
    }

    /**
     * 删除Object
     *
     * @param id
     */
    public boolean deleteById(Serializable id) {
        if (id != null) {
            return deleteById(id.toString());
        }
        return false;
    }

    /**
     * 删除Object
     *
     * @param id
     */
    public boolean deleteById(String id) {
        if (StringUtil.isEmpty(id)) {
            return false;
        } else {
            redisTemplate.delete(getKey(id));
            this.logInfo("del " + getKey(id));
            return true;
        }
    }

    /**
     * 删除对象所有记录
     */
    public void deleteAll() {
        if (StringUtil.isNotEmpty(prefix)) {
            Set<String> keys = redisTemplate.keys(prefix + SEPARATOR + WILDCARD);
            redisTemplate.delete(keys);
            this.logInfo("del " + keys);
        }
    }

    /**
     * 初始化对象列表
     *
     * @param list
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public List<T> initList(List<T> list) throws IllegalArgumentException, IllegalAccessException {
        this.deleteAll();
        this.insertList(list);
        return list;
    }

    /**
     * 判断key是否存在
     *
     * @param id
     * @return
     */
    public boolean exists(String id) {
        if (StringUtil.isNotEmpty(id)) {
            return redisTemplate.hasKey(getKey(id));
        }
        return false;
    }

    /**
     * 模糊查询KEY的记录数
     *
     * @param pattern
     * @return
     */
    public int count(String pattern) {
        return redisTemplate.keys(pattern).size();
    }

    /**
     * 设置过期时间
     *
     * @param id
     * @param timeout
     * @return
     */
    public boolean setExpire(String id) {
        return setExpire(id, null);
    }

    /**
     * 设置过期时间
     *
     * @param id
     * @param timeout
     * @return
     */
    public boolean setExpire(String id, Long timeout) {
        if (StringUtil.isEmpty(id)) {
            return false;
        } else {
            timeout = timeout == null ? TIMEOUT : timeout;
            return redisTemplate.expire(getKey(id), timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * 获取过期时间
     *
     * @param id
     * @param timeout
     * @return
     */
    public Long getExpire(String id) {
        if (StringUtil.isNotEmpty(id)) {
            return redisTemplate.getExpire(getKey(id), TimeUnit.SECONDS);
        }
        return null;
    }

    /**
     * 生成Key
     *
     * @return
     */
    private String getKey() {
        if (!StringUtil.isEmpty(prefix)) {
            return prefix;
        }
        return EMPTY_KEY;
    }

    /**
     * 生成Key
     *
     * @param id
     * @return
     */
    private String getKey(String id) {
        if (!StringUtil.isEmpty(prefix, id)) {
            String key = prefix + SEPARATOR + id;
            return key.trim().replace(" ", "");
        }
        return EMPTY_KEY;
    }

    /**
     * 批量生成Key
     *
     * @param ids
     * @return
     */
    private List<String> getKeys(List<String> ids) {
        List<String> keys = new ArrayList<String>();
        if (StringUtil.isNotEmpty(prefix)) {
            ids.stream()
                    .filter(id -> StringUtil.isNotEmpty(id))
                    .forEach(id -> keys.add((prefix + SEPARATOR + id).trim().replace(" ", "")));
        }
        return keys;
    }

    /**
     * 生成id
     *
     * @param key
     * @return
     */
    private String getId(String key) {
        if (!StringUtil.isEmpty(prefix, key) && key.startsWith(prefix + SEPARATOR)) {
            String id = key.substring((prefix + SEPARATOR).length());
            return id.trim().replace(" ", "");
        }
        return EMPTY_KEY;
    }

    /**
     * 日志打印
     *
     * @param logString
     */
    private void logInfo(String logString) {
        if (LOG_SWITCH) {
            logger.info(loggerName + logString);
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setRedisTemplate(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String toString() {
        return "RedisClientUtil [redisTemplate=" + redisTemplate + ", prefix=" + prefix + "]";
    }

}
