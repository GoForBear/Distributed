package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import util.NIOConfig;

import java.util.ArrayList;
import java.util.List;

public class JedisPoolBuildPool {
    public static final int MAX_IDLE = 50;
    public static final int MAX_TOTAL = 50;
    private static JedisPool pool = null;

    static{
        buildPool();
        hotPool();
    }

    private static JedisPool buildPool(){
        if(null == pool){
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_TOTAL);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(1000 * 10);
            //对redis的连接做有效测试，保证都是有用的连接
            config.setTestOnBorrow(true);
            pool = new JedisPool(config, NIOConfig.REDIS_SERVER_IP,NIOConfig.REDIS_SERVER_PORT,10000);
        }
        return pool;
    }

    public static void hotPool(){
        List<Jedis> minIdleJedisList = new ArrayList<Jedis>(MAX_IDLE);
        Jedis jedis = null;
        for(int i = 0;i<MAX_IDLE;i++){
            try{
                jedis = pool.getResource();
                minIdleJedisList.add(jedis);
            }catch (Exception e){
                System.out.println("redis预热创建出现异常:" + e);
            }
        }
        for(int i = 0;i<MAX_IDLE;i++){
            try{
                jedis = minIdleJedisList.get(i);
                jedis.close();
            }catch (Exception e){
                System.out.println("redis预热关闭出现异常:"+e);
            }
        }
    }

    public static Jedis getJedis(){
        return pool.getResource();
    }

}
