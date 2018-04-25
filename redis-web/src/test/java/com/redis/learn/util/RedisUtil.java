
package com.redis.learn.util;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

/**
 * Redis��ز���
 * @author xianglj
 */
public class RedisUtil {
	
	private static final Charset UTF_8 = Charset.forName("utf-8");

	/**
	 * ��redis�����ַ�����ֵ��
	 * @param key
	 * @param value
	 */
	public static boolean setString(String key, String value) {
		if(null == key || value == null ) {
			return false;
		}
		
		return setBytes(key.getBytes(UTF_8), value.getBytes(UTF_8));
	}
	
	/**
	 * ��Redis�д����ֵ�Ե�byte���飬����ܳ���1GB���ֽ�
	 * @param key ��
	 * @param value ֵ
	 * @return
	 */
	public static boolean setBytes(byte[] key, byte[] value) {
		if(null == key || null == value) {
			return false;
		}
		
		Jedis jedis = RedisTool.getJedis();
		String statusCode = jedis.set(key, value);
		System.out.println("״̬��:(" + statusCode + ")");
		RedisTool.returnResource(jedis);
		return true;
	}
	
	/**
	 * ��ȡString���͵�ֵ
	 * @param key ����ֵ
	 * @return
	 */
	public static String getString(String key) {
		if(null == key) {
			return null;
		}
		
		byte[] val = getBytes(key.getBytes(UTF_8));
		if(val == null) {
			return null;
		}
		return new String(val, UTF_8);
	}
	
	/**
	 * ��ȡRedis�еĻ���ֵ
	 * @param key
	 * @return
	 */
	public static byte[] getBytes(byte[] key) {
		if(null == key) {
			return null;
		}
		
		Jedis jedis = RedisTool.getJedis();
		byte[] val = jedis.get(key);
		RedisTool.returnResource(jedis);
		return val;
	}
	
	/**
	 * ɾ��ĳ�������������ɾ�����ٴ�������ͬ��ʱ������null
	 * @param key
	 */
	private static boolean del(byte[] key) {
		if(null == key) {
			return true;
		}
		
		Jedis jedis = RedisTool.getJedis();
		jedis.del(key);
		return true;
	}
	
	/**
	 * �����ַ�������(String),ɾ����
	 * @param key
	 * @return
	 */
	public static boolean delString(String key) {
		if(null == key) {
			return true;
		}
		
		byte[] k = key.getBytes(UTF_8);
		return del(k);
	}
	
	/**
	 * �������뻺��:<br>
	 * key,value,key,value<br>
	 * ����<br>
	 * name��johnny,age,12<br>
	 * �������name=johnny,age=12�Ļ��棬����ڻ������Ѿ�������ͬ�Ļ��棬����������¡�
	 * @param keyValues
	 * @return
	 */
	public static boolean fetchSet(String ... keyValues) {
		if(keyValues == null) {
			return false;
		}
		
		Jedis jedis = RedisTool.getJedis();
		jedis.mset(keyValues);
		RedisTool.returnResource(jedis);
		return true;
	}
	
	/**
	 * ����һ�������͵�Map
	 * @param key
	 * @param map
	 */
	public static void addMap(String key, Map<String, String> map) {
		if(null == key || null == map) {
			return;
		}
		
		Jedis jedis = RedisTool.getJedis();
		jedis.hmset(key, map);
		RedisTool.returnResource(jedis);
	}
	
	public static void addMapVal(String key, String field, String value) {
		if(null == key || field == null || null == value) {
			return;
		}
		Jedis jedis = RedisTool.getJedis();
		jedis.hsetnx(key, field, value);
		RedisTool.returnResource(jedis);
	}
	
	public static void addMapVal(byte[] key, byte[] field, byte[] value) {
		if(null == key || field == null || null == value) {
			return;
		}
		Jedis jedis = RedisTool.getJedis();
		jedis.hsetnx(key, field, value);
		RedisTool.returnResource(jedis);
	}
	
	/**
	 * ��Redis�в���һ��Map��ֵ
	 * @param key
	 * @param mapByte
	 */
	public static void addMap(byte[] key, Map<byte[], byte[]> mapByte) {
		if(null == key || null == mapByte) {
			return;
		}
		
		Jedis jedis = RedisTool.getJedis();
		//���ǻ᷵��OK,������ִ��ʧ��
		String status = jedis.hmset(key, mapByte);
		System.out.println("ִ��״̬:" + status);
		RedisTool.returnResource(jedis);
	}
	
	/**
	 * ��ȡMap�е�ֵ��ֻ�ܹ�
	 * @param key
	 * @return
	 */
	public static List<String> getMapVal(String key, String ... fields) {
		if(null == key) {
			return null;
		}
		
		Jedis jedis = RedisTool.getJedis();
		
		List<String> rtnList = null;
		if(null == fields || fields.length == 0) {
			rtnList = jedis.hvals(key);
		} else {
			rtnList = jedis.hmget(key, fields);
		}
		
		RedisTool.returnResource(jedis);
		return rtnList;
	}
	
	/**
	 * ��ȡMap�е�ֵ
	 * @param key
	 * @param fields
	 * @return
	 */
	public static List<byte[]> getMapVal(byte[] key, byte[] ... fields) {
		if(null == key) {
			return null;
		}
		Jedis jedis = RedisTool.getJedis();
		
		if(!jedis.exists(key)) {
			return null;
		}
		List<byte[]> rtnList = null;
		if(null == fields || fields.length == 0) {
			rtnList = jedis.hvals(key);
		} else {
			rtnList = jedis.hmget(key, fields);
		}
		
		return rtnList;
	}
	
	/**
	 * ��Redis�����set����
	 * @param key
	 * @param values
	 */
	public static void addSet(String key, String ... values) {
		if(null == key || values == null) {
			return;
		}
		Jedis jedis = RedisTool.getJedis();
		jedis.sadd(key, values);
	}
	
	public static void delSetVal(String key, String ... fields) {
		if(null == key) {
			return;
		}
		
		if(fields == null || fields.length == 0) {
			del(key.getBytes(UTF_8));
			return;
		}
		
		Jedis jedis = RedisTool.getJedis();
		jedis.srem(key, fields);
		RedisTool.returnResource(jedis);
	}
	
	public static void addSetBytes(byte[] key, byte[]...values) {
		if(null == key || values == null) {
			return;
		}
		
		Jedis jedis = RedisTool.getJedis();
		jedis.sadd(key, values);
		RedisTool.returnResource(jedis);
	}
	
	public static void delSetVal(byte[] key, byte[]...values) {
		if(null == key) {
			return;
		}
		if(values == null || values.length == 0) {
			del(key);
			return;
		}
		Jedis jedis = RedisTool.getJedis();
		jedis.srem(key, values);
		RedisTool.returnResource(jedis);
	}
	
	/**
	 * ��ȡ���е�ֵ
	 * @param key
	 */
	public static Set<byte[]> getSetVals(byte[] key) {
		if(null == key) {
			return null;
		}
		Jedis jedis = RedisTool.getJedis();
		Set<byte[]> rtnList = jedis.smembers(key);
		return rtnList;
	}
	
	public static Set<String> getSetVals(String key) {
		if(null == key) {
			return null;
		}
		Jedis jedis = RedisTool.getJedis();
		Set<String> rtnSet = jedis.smembers(key);
		RedisTool.returnResource(jedis);
		return rtnSet;
	}
	
	/**
	 * �ж��Ƿ�Set�����а���Ԫ��
	 * @param key
	 * @param field
	 * @return
	 */
	public static boolean isSetContain(String key, String field) {
		if(null == key || field == null) {
			return false;
		}
		Jedis jedis = RedisTool.getJedis();
		boolean isContain = jedis.sismember(key, field);
		RedisTool.returnResource(jedis);
		return isContain;
	}
	
	public static boolean isSetContain(byte[] key, byte[] field) {
		if(null == key || field == null) {
			return false;
		}
		Jedis jedis = RedisTool.getJedis();
		boolean isSuccess = jedis.sismember(key, field);
		RedisTool.returnResource(jedis);
		return isSuccess;
	}
	
	/**
	 * ����Set�����е�Ԫ�ظ���
	 * @param key
	 * @return
	 */
	public static Long getSetLength(String key) {
		if(null == key) {
			return 0L;
		}
		Jedis jedis = RedisTool.getJedis();
		Long length = jedis.scard(key);
		return length;
	}
	
	public static Long getSetLength(byte[] key) {
		if(null == key) {
			return 0L;
		}
		Jedis jedis = RedisTool.getJedis();
		Long length = jedis.scard(key);
		RedisTool.returnResource(jedis);
		return length;
	}
	
	/**
	 * ��list���������Ԫ��
	 * @param key
	 * @param values
	 */
	public static void addList(String key, String ...values) {
		if(null == key || values == null) {
			return;
		}
		
		Jedis jedis = RedisTool.getJedis();
		jedis.rpush(key, values);
		RedisTool.returnResource(jedis);
	}
	
	/**
	 * ��list���������Ԫ��
	 * @param key
	 * @param values
	 */
	public static void addList(byte[] key, byte[] ...values) {
		if(null == key || values == null) {
			return;
		}
		
		Jedis jedis = RedisTool.getJedis();
		jedis.rpush(key, values);
		RedisTool.returnResource(jedis);
	}
	
	/**
	 * ��ȡstart��end��Χ��ֵ������list�ķ�Χ�������׳��쳣
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<String> getListVals(String key, int start, int end) {
		if(null == key) {
			return null;
		}
		
		Jedis jedis = RedisTool.getJedis();
		List<String> rtnList = jedis.lrange(key, start, end);
		RedisTool.returnResource(jedis);
		return rtnList;
	}
	
	/**
	 * ��ȡstart��end��Χ��ֵ������list�ķ�Χ�������׳��쳣
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<byte[]> getListVals(byte[] key, int start, int end) {
		if(null == key) {
			return null;
		}
		
		Jedis jedis = RedisTool.getJedis();
		List<byte[]> rtnList = jedis.lrange(key, start, end);
		RedisTool.returnResource(jedis);
		return rtnList;
	}
	
	public static List<String> getListAll(String key) {
		if(null == key) {
			return null;
		}
		return getListVals(key, 0, -1);
	}
	
	public static List<byte[]> getListAll(byte[] key) {
		if(null == key) {
			return null;
		}
		return getListVals(key, 0, -1);
	}
	
	public static String popList(String key) {
		if(null == key) {
			return null;
		}
		Jedis jedis = RedisTool.getJedis();
		return jedis.lpop(key);
	}
	public static byte[] popList(byte[] key) {
		if(null == key) {
			return null;
		}
		Jedis jedis = RedisTool.getJedis();
		return jedis.lpop(key);
	}
}

