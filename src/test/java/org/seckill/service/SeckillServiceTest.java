package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> seckillList = seckillService.getSeckillList();
        logger.info("seckillList = {}", seckillList);
    }

    @Test
    public void geSeckillById() {
        long id = 1002;
        Seckill seckill = seckillService.geSeckillById(id);
        logger.info("seckill = {}", seckill);
    }

    //测试org.seckill.service.SeckillServiceImpl的exportSeckillUrl+executeSeckill集成方法
    //即测试秒杀的整个流程
    @Test
    public void exportSeckillLogic() {
        long id = 1001;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()) {
            //秒杀开启
            logger.info("exposer = {}", exposer);
            String userPhone = "12345678905";
            String md5 = exposer.getMd5();
            try {
                //数据库秒杀信息更新
                SeckillExecution seckillExecution = seckillService.executeSeckill(id, userPhone, md5);
                logger.info("seckillExecution = {}", seckillExecution);
            } catch (SeckillCloseException e) {
                //秒杀关闭异常（可以接受的异常，不终止程序，测试可以通过，但要进行事务回滚），仅用日志记录
                logger.error(e.getMessage());
            } catch (RepeatKillException e) {
                //重复秒杀异常
                logger.error(e.getMessage());
            }
        } else {
            //秒杀未开启
            logger.warn("exposer = {}", exposer);
        }
    }

    @Test
    public void executeSeckillProcedure(){
        long id = 1001;
        String phone = "12345678921";
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()){
            String md5 = exposer.getMd5();
            SeckillExecution seckillExecution = seckillService.executeSeckillProcedure(id,phone,md5);
            System.out.println(seckillExecution.getSeckillInfo());
        }
    }
}