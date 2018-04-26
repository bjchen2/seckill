package org.seckill.service.impl;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SeckillService的实现类
 * @Component：当不明确该类是什么类型时使用
 * @Service：Service注解
 * @Dao：Dao注解
 * @Controller：Controller注解
 */
@Service
public class SeckillServiceImpl implements SeckillService {
    /**
     * 盐值，用于md5的加密，字符串越复杂无规律越好
     * 所谓加Salt，就是加点“佐料”。当用户首次提供密码时（通常是注册时），由系统自动往这个密码里加一些“Salt值”，
     * 这个值是由系统随机生成的，并且只有系统知道。然后再散列。而当用户登录时，
     * 系统为用户提供的代码撒上同样的“Salt值”，然后散列，再比较散列值，已确定密码是否正确。 　　
     * 这样，即便两个用户使用了同一个密码，由于系统为它们生成的salt值不同，他们的散列值也是不同的。
     */
    private String salt = "1234u8eqjoDAQWEid;'123;l.xzXs'";
    //注入Service依赖，也可使用J2EE支持的@Resource、@Inject注解
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisDao redisDao;

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,5);
    }

    @Override
    public Seckill geSeckillById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //TODO 优化点：redis缓存优化
        //1,redis访问
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null){
            //2.访问数据库
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            }else {
                //3,放入redis
                redisDao.putSeckill(seckill);
            }
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if (startTime.getTime() > nowTime.getTime() || endTime.getTime() < nowTime.getTime()) {
            return new Exposer(false, seckillId, startTime.getTime(), endTime.getTime(), nowTime.getTime());
        }
        String md5 = getMD5(seckillId);
        return new Exposer(true, seckillId, md5);
    }

    private String getMD5(long seckillId){
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    //通过注解声明该方法需要使用事务管理
    //这个方法在使用了存储过程以后就没用了，是前期未优化的产物
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, String userPhone, String md5) throws SeckillException, SeckillCloseException, RepeatKillException {
        if (md5 == null || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite!!!");
        }
        //执行秒杀逻辑：减库存 + 记录购买行为
        try {
            //减库存
            int updateCount = seckillDao.reduceNumber(seckillId,new Date());
            if (updateCount <= 0){
                //没有更新到记录，秒杀结束
                throw new SeckillCloseException(("seckill is closed"));
            } else {
                //记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
                if (insertCount <= 0){
                    //重复购买
                    throw new RepeatKillException("seckill repeated");
                } else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(SeckillStateEnum.SUCCESS,seckillId,successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (SeckillException e3) {
            //所有编译期异常，转换成运行期异常，以便回滚
            logger.error(e3.getMessage(),e3);
            throw new SeckillException("seckill inner error" + e3.getMessage());
        }
    }

    @Override
    public SeckillExecution executeSeckillProcedure(long seckillId, String userPhone, String md5){
        if (md5 == null || !md5.equals(getMD5(seckillId))){
            return new SeckillExecution(SeckillStateEnum.DATA_REWRITE,seckillId);
        }
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("seckillId",seckillId);
        map.put("phone",userPhone);
        map.put("killTime",new Date());
        map.put("result",null);
        //执行存储过程，result被赋值。
        try {
            seckillDao.killByProcedure(map);
            //获取map的result值，若不存在，则给默认值-2
            int result = MapUtils.getIntValue(map,"result",-2);
            if (result == 1) {
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                return new SeckillExecution(SeckillStateEnum.SUCCESS, seckillId, successKilled);
            }else {
                return new SeckillExecution(SeckillStateEnum.stateOf(result),seckillId);
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new SeckillExecution(SeckillStateEnum.INNER_ERROR,seckillId);
        }
    }
}
