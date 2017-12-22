package com.xjcy.util;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author 41270
 *
 */
public class RedisUtils
{
	private static final Logger logger = Logger.getLogger(RedisUtils.class);

	private static JedisPool jedisPool;

	public static void create(String host, int port, String auth)
	{
		// 池基本配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(20); // 可创建的实例
		config.setMaxIdle(5); // 保持等待的实例
		config.setMaxWaitMillis(1000l); // 执行等待的时间
		config.setTestOnBorrow(false);
		jedisPool = new JedisPool(config, host, port, 3000, auth);
		if (logger.isDebugEnabled())
			logger.debug("初始化RedisPool完成");
	}

	/**
	 * 从Redis池中获取一个实例
	 * 
	 * @return
	 */
	private static Jedis getInstance()
	{
		try
		{
			return jedisPool.getResource();
		}
		catch (Exception e)
		{
			logger.error("获取Redis连接失败", e);
			return null;
		}
	}

	private static void close(Jedis jedis)
	{
		if (jedis != null)
		{
			jedis.close();
		}
		if (logger.isDebugEnabled())
			logger.debug("Pool, Active => " + jedisPool.getNumActive() + ", Idle => " + jedisPool.getNumIdle()
					+ ", Waiters => " + jedisPool.getNumWaiters());
	}

	public static String get(String cacheKey)
	{
		Jedis j = null;
		try
		{
			j = getInstance();
			String result = j.get(cacheKey);
			if (logger.isDebugEnabled())
			{
				logger.debug("获取缓存：" + cacheKey + " 结果：" + result);
			}
			return result;
		}
		catch (Exception e)
		{
			logger.error("获取缓存失败：" + cacheKey, e);
			return null;
		}
		finally
		{
			close(j);
		}
	}

	public static boolean set(String cacheKey, String cacheJSON, Integer seconds)
	{
		Jedis j = null;
		try
		{
			j = getInstance();
			String result = j.set(cacheKey, cacheJSON);
			if (logger.isDebugEnabled())
			{
				logger.debug("设置缓存：[key] => " + cacheKey + " 结果：" + result);
			}
			Long result2 = j.expire(cacheKey, seconds);
			if (logger.isDebugEnabled())
			{
				logger.debug("设置缓存失效时间：[key] => " + cacheKey + " 结果：" + result2);
			}
			return true;
		}
		catch (Exception e)
		{
			logger.error("写入缓存失败：" + cacheKey, e);
			return false;
		}
		finally
		{
			close(j);
		}
	}

	public static boolean delList(String key)
	{
		Jedis j = null;
		try
		{
			j = getInstance();
			// server_memberlist_153
			Set<String> values = j.keys(key + "*");
			for (String str : values)
			{
				j.del(str);
			}
			if (logger.isDebugEnabled())
			{
				logger.debug("[CACHE] 清除缓存列表成功" + key);
			}
			return true;
		}
		catch (Exception e)
		{
			logger.error("删除Redis的列表[" + key + "]失败：", e);
			return false;
		}
		finally
		{
			close(j);
		}
	}

	public static Boolean del(String key)
	{
		Jedis j = null;
		try
		{
			j = getInstance();
			Long result = j.del(key);
			if (logger.isDebugEnabled())
			{
				logger.debug("========删除缓存：[key] => " + key + " 结果：" + result);
			}
			return true;
		}
		catch (Exception e)
		{
			logger.error("删除key为：" + key + "的缓存失败", e);
			return false;
		}
		finally
		{
			close(j);
		}
	}

	public static void setList(String key, List<?> data, int seconds)
	{
		Jedis j = null;
		try
		{
			j = getInstance();
			j.del(key);
			Iterator<?> it = data.iterator();
			while (it.hasNext())
			{
				j.lpush(key, it.next().toString());
			}
			// 设置过期时间为30分钟
			j.expire(key, 1800);
		}
		catch (Exception e)
		{
			logger.error("写入缓存失败：" + key, e);
		}
		finally
		{
			close(j);
		}
	}

	public static boolean exists(String key)
	{
		Jedis j = null;
		try
		{
			j = getInstance();
			boolean result = j.exists(key);
			if (logger.isDebugEnabled())
			{
				logger.debug("检查缓存是否存在：[key] => " + key + " 结果：" + result);
			}
			return result;
		}
		catch (Exception e)
		{
			logger.error("检查缓存失败：[key] => " + key, e);
			return false;
		}
		finally
		{
			close(j);
		}
	}

	public static void destroy()
	{
		if (jedisPool != null)
			jedisPool.destroy();
		jedisPool = null;
	}
}
