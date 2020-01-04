package com.sdxb;

import com.sdxb.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;

public class Service {
    //请求模拟
    public void call(){
        System.out.println("调用服务");
    }
    //用户限制模拟,传入用户id
    public void limitcall(String id){
        Jedis jedis=JedisUtil.getJedis();
       // Jedis jedis = new Jedis("127.0.0.1", 6379);
        String value = jedis.get("user" + id);
        //第一步，查看该值是否存在
        try {
            if (value==null){
                //如果不存在，创建值，设置生命周期为20s
                jedis.setex("user"+id,20,Long.MAX_VALUE-10+"");
            }else{
                //如果存在，则加1，直到超过最大值抛出异常
                jedis.incr("user"+id);
                call();
            }
        }catch (JedisDataException e){
            //超过最大值（即每20s访问超过10次）,执行异常
            System.out.println("达到请求上限，稍后再试");
            return;
        }finally {
            jedis.close();
        }

    }
}
class MyThread extends Thread{
    Service service=new Service();
    @Override
    public void run() {
        while (true){
            service.limitcall("用户A");
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MyThread myThread=new MyThread();
        myThread.run();
    }
}

