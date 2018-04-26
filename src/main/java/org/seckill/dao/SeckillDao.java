package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 关于Seckill实体类的Dao接口
 */
public interface SeckillDao {

    /**
     * 减少库存
     * @param seckillId
     * @param killTime
     * @return
     * 返回影响行数，若>=1则成功，0则失败
     */
    int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);

    /**
     * 根据id查询秒杀实例
     * @param seckillId
     * @return
     * 返回查询的实例
     */
    Seckill queryById(long seckillId);

    /**
     * 用于分页，根据偏移量查询秒杀对象列表
     * @param offset
     * @param limit
     * @return
     * 返回查询到的秒杀列表
     * 特别的：java没有保留形参的名字，他是以agr0,agr1来表示，
     * 所以传多个参数时要用注解@Param来指定参数名，如果只有一个参数，不用指定。
     * 即List<Seckill> queryAll(int offset,int limit)编译后-->List<Seckill> queryAll(int arg0,int arg1);
     */
    List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);

    int insertSeckill(@Param("seckill")Seckill seckill,@Param("id")int id);

    /**
     * TODO
     * 使用存储过程执行秒杀
     * 最后会修改传入map的值，然后直接调用Map即可
     * @param paramMap
     */
    void killByProcedure(Map<String, Object> paramMap);
}
