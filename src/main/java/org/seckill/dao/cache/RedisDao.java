package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created By Cx On 2018/4/19 23:38
 */
public class RedisDao {
    private Logger logger = LoggerFactory.getLogger(RedisDao.class);
    private final JedisPool jedisPool;
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public RedisDao(String ip, int port) {
        this.jedisPool = new JedisPool(ip, port);
    }

    public Seckill getSeckill(long seckillId) {
        //redis获取操作
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckillId;
                //没有实现内部序列化操作
                //get->byte[]->反序列化 -> Object(Seckill)
                //采用自定义序列化(使用protostuff)，比java提供的序列化操作快数十倍
                // protostuff要求:只能序列化pojo（有get/set方法的普通bean而不能是String等基础类型）
                byte[] bytes = jedis.get(key.getBytes());
                if (bytes != null) {
                    //创建一个空对象（所有属性值都没有被赋值过。因为mergeFrom第二个参数必须接收一个空对象）
                    Seckill seckill = schema.newMessage();
                    //seckill反序列化，通过bytes（key）获取对应的字节数组，通过schema映射到对应的seckill的各个属性中
                    ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                    return seckill;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public String putSeckill(Seckill seckill) {
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckill.getSeckillId();
                //第三个参数是缓存器，若当前对象过大有一个缓冲过程
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                int timeout = 60 * 60;
                //置入key-value,正确返回OK，错误返回错误信息。第二个参数是缓存时长（单位：s）
                String result = jedis.setex(key.getBytes(), timeout, bytes);
                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}