package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口
 */
public interface SeckillService {

    /**
     * 查询所有秒杀产品记录
     * @return
     * 返回Seckill列表，表示所有秒杀产品记录
     */
    List<Seckill> getSeckillList();

    /**
     * 通过id查询秒杀记录
     * @param seckillId
     * @return
     * 返回Seckill对象，表示该id对应的秒杀记录
     */
    Seckill geSeckillById(long seckillId);

    /**
     * 秒杀开启时输出秒杀地址
     * 否则输出系统时间和秒杀开启时间
     * @param seckillId
     * @return
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作,这个方法在使用了存储过程以后就没用了，是前期未优化的产物
     * @return
     * 返回SeckillExecution对象，包含所有需要的秒杀结果信息
     */
    SeckillExecution executeSeckill(long seckillId, String userPhone, String md5)
            throws SeckillException,SeckillCloseException,RepeatKillException;

    /**
     * 执行秒杀操作 by 存储过程
     * @return
     * 返回SeckillExecution对象，包含所有需要的秒杀结果信息
     */
    SeckillExecution executeSeckillProcedure(long seckillId, String userPhone, String md5);
}
