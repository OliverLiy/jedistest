package com.sdxb;

import org.junit.Test;
import redis.clients.jedis.Jedis;

public class JedisTest {
    @Test
    public void testJedis(){
        //1.连接jedis
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        //2.操作jedis
        jedis.set("name","sdxb");
        String name = jedis.get("name");
        System.out.println(name);
        //3.关闭连接
        jedis.close();
    }
}
