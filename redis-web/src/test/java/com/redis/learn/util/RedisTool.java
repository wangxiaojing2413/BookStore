package com.redis.learn.util;  
  
import redis.clients.jedis.Jedis;  
import redis.clients.jedis.JedisPool;  
import redis.clients.jedis.JedisPoolConfig;  
  
public class RedisTool {  
  
    //Redis��������ַ  
    private static String address = "192.168.21.130";  
    //Redis�������˿ں�  
    private static int port = 6379;  
    //��������  
    private static String auth = null;  
    //��������ʵ���������Ŀ��Ĭ��ֵΪ8��  
    //�����ֵΪ-1�����ʾ�����ƣ����pool�Ѿ�������maxActive��jedisʵ�������ʱpool��״̬Ϊexhausted(�ľ�)��  
    //private static final int MAX_ACTIVE = 1024;  
      
    //����һ��pool����ж��ٸ�״̬Ϊidle(���е�)��jedisʵ����Ĭ��ֵҲ��8��  
    private static int MAX_IDLE = 10;  
    //�ȴ��������ӵ����ʱ�䣬��λ���룬Ĭ��ֵΪ-1����ʾ������ʱ����������ȴ�ʱ�䣬��ֱ���׳�JedisConnectionException��  
    private static int MAX_WAIT = 10000;  
    private static int TIMEOUT = 10000;  
    //��borrowһ��jedisʵ��ʱ���Ƿ���ǰ����validate���������Ϊtrue����õ���jedisʵ�����ǿ��õģ�  
    private static boolean TEST_ON_BORROW = true;  
    private static JedisPool jedisPool;  
      
    static {  
        JedisPoolConfig config = new JedisPoolConfig();  
        config.setMaxIdle(MAX_IDLE);  
        config.setMaxWait(MAX_WAIT);  
        config.setTestOnBorrow(TEST_ON_BORROW);  
        //config.setMaxTotal(MAX_ACTIVE);  
        jedisPool = new JedisPool(config, address, port, TIMEOUT);  
    }  
      
    /** 
     * ��ȡJedis�ͻ��� 
     * @return 
     */  
    public static Jedis getJedis() {  
        Jedis jedis = null;  
        if(null != jedisPool) {  
            jedis = jedisPool.getResource();  
        }  
        return jedis;  
    }  
      
    /** 
     * ������Դ 
     * @param jedis 
     */  
    public static void returnResource(Jedis jedis) {  
        jedisPool.returnBrokenResource(jedis);  
    }  
}  