package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 配置spring和junit的整合
 * 使junit启动时加载SpringIOC容器，获取可依赖的SeckillDao实现类
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit Spring的配置文件位置
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    /**
     * 注入Dao实现类依赖
     * 当看到@Resource注解时，会自动去SpringIOC容器中查找可注入的实现类
     */
    @Resource
    private SeckillDao seckillDao;


    @Test
    public void insertSeckill(){
        Seckill seckill = new Seckill();
        seckill.setName("ha2ole");
        seckill.setNumber(12);
        System.out.println(seckill.getSeckillId());
        System.out.println(seckillDao.insertSeckill(seckill,8));
    }

    @Test
    public void reduceNumber() {
        int s = seckillDao.reduceNumber(1004,new Date());
        System.out.println(s);
    }

    @Test
    public void queryById() {
        int id = 1002;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void queryAll() {
        /**
         * Parameter 'offset' not found. Available parameters are [0, 1, param1, param2]
         * java没有保留形参的名字，他是以agr0,agr1来表示，
         * 即List<Seckill> queryAll(int offset,int limit)编译后-->List<Seckill> queryAll(int arg0,int arg1);
         * 在mybatis中如果要表示多个参数的传进来，要用注解@Param来指定参数名，如果是一个参数，那不用指定。
         */
        List<Seckill> seckillList = seckillDao.queryAll(1,2);
        System.out.println(seckillList.get(0));
    }
}