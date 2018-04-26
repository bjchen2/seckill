package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created By Cx On 2018/4/20 0:25
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class RedisDaoTest {
    @Autowired
    RedisDao redisDao;
    @Autowired
    SeckillDao seckillDao;


    @Test
    public void getSeckill() {
        Seckill seckill = redisDao.getSeckill(1001);
        if (seckill == null){
            seckill = seckillDao.queryById(1001);
            if (seckill != null){
                System.out.println(redisDao.putSeckill(seckill));
                seckill = redisDao.getSeckill(1001);
            }
        }
        System.out.println(seckill);
    }
}