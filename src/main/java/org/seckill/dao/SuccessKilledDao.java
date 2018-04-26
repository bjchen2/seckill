package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;

/**
 * SuccessKilled实体类的Dao接口规范
 */
public interface SuccessKilledDao {

    /**
     * 插入商品购买信息
     * @param seckillId
     * @param userPhone
     * @return
     * 返回插入的行数，若为0则表示失败
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") String userPhone);

    /**
     * 根据id和phone查询带有商品对象的购买信息对象
     * @param seckillId
     * @param userPhone
     * @return
     * 返回带有商品对象的秒杀信息对象
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") String userPhone);
}
