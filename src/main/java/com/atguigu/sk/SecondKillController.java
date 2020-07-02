package com.atguigu.sk;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

@RestController//相当于Controller+ResponseBody
public class SecondKillController {

    @PostMapping(value = "/sk/doSecondKill",produces = "text/html;charset=UTF-8")
    public String doSecondKill(Integer id){
        //随机生成用户id
        Integer usrid = (int) (10000 * Math.random());

        //秒杀商品id
        Integer pid = id;
        //拼接商品库存的key和用户列表集合的key
        String qtKey = "sk:"+pid+":qt";
        String usrsKey = "sk:"+pid+":usr";

        Jedis jedis = new Jedis("192.168.194.130", 6379);
        //1.判断用户是否秒杀过
        if(jedis.sismember(usrsKey, usrid+"")){
            System.err.println("该用户已经秒杀过了，请勿重复秒杀"+usrid);
            return "该用户已经秒杀过了，请勿重复秒杀";
        }

        //2.获取redis中的库存，判断是否足够
        jedis.watch(qtKey);
        String qtStr = jedis.get(qtKey);
        if(StringUtils.isEmpty(qtStr)){
            System.err.println("秒杀尚未开始");
            return "秒杀尚未开始";
        }
        int qtNum = Integer.parseInt(qtStr);
        if(qtNum<=0){
            System.err.println("库存不足");
            return "库存不足";
        }
        //3.库存足够，秒杀业务
        //减库存
        Transaction multi = jedis.multi();
        multi.decr(qtKey);
        //将用户加入到秒杀成功的列表
        multi.sadd(usrsKey, usrid+"");
        System.out.println("秒杀成功："+usrid);
        multi.exec();
        jedis.close();
        return "ok";
    }
}
