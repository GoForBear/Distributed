package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

public class stringDemo {

    private static JedisPool jedisPool = null;

    public void operate(String str){
        if(null == jedisPool){
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(50);
            jedisPoolConfig.setMaxIdle(50);
            jedisPoolConfig.setMaxWaitMillis(1000*10);
            jedisPoolConfig.setTestOnBorrow(true);
            jedisPool = new JedisPool(jedisPoolConfig,"127.0.0.1",6379,10000);
        }
        //写的什么东西。。上边是连接池，下边又是单独的连接。。要么保留上边，使用pool.getRource获取连接，要么只保留下边的单个连接
        Jedis jedis = new Jedis("localhost",6379);
        Map<String,Double> member = new HashMap<String,Double>();
        member.put("01",200.0);
        member.put("02",300.0);
        System.out.println(jedis.ping());
        jedis.set("key1",str);
        jedis.zadd("salary",member);
        System.out.println(jedis.get("key1"));
        System.out.println(jedis.zscore("salary","02"));

    }

    public static void main(String[] args) {
        stringDemo sd = new stringDemo();
        sd.operate("3333");
    }


}
