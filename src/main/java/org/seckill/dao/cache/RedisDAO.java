package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.SecKill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Description: Redis访问数据库的对象
 *
 * @author: dong
 * @create: 2017-03-29-14:25
 */
public class RedisDAO {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;

    // RuntimeSchema相当于一个类的描述,从class文件中进行获取.
    private RuntimeSchema<SecKill> schema = RuntimeSchema.createFrom(SecKill.class);

    public RedisDAO(String ip, int port) {
        // 用于初始化Redis连接池.
        this.jedisPool = new JedisPool(ip, port);
    }

    public SecKill getSeckill(long seckillId) {
        // redis操作的逻辑
        try {
            // 获取一个jedis的连接对象.
            Jedis jedis = jedisPool.getResource();
            try {
                // 在Redis内部并没有实现序列化操作.
                String key = "seckill:" + seckillId;
                // get -> byte[] -> 反序列化 -> Object(seckill)
                // 采用自定义的序列化, protostuff.pojo;
                byte[] bytes = jedis.get(key.getBytes());
                // 缓存重新获取得到
                if(null != bytes) {
                    // 创建一个空对象
                    SecKill secKill = schema.newMessage();
                    // 使用ProtoStuff从缓存中反序列化对象.
                    ProtostuffIOUtil.mergeFrom(bytes, secKill, schema);
                    return secKill;
                }
            } finally {
                jedis.close();  // 当执行缓存操作结束之后,释放连接的对象.
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将一个Seckill对象放入到Redis缓存中
     * */
    public String putSeckill(SecKill secKill) {
        // set Object(SecKill) --> 序列化 --> byte[]
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + secKill.getSeckillId();
                // 将seckill对象进行序列化
                byte[] bytes = ProtostuffIOUtil.toByteArray(secKill, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                // 超时缓存
                int timeout = 60 * 60;  // 1小时
                // 当缓存成功之后,会返回“ok”的String字符串.
                String result = jedis.setex(key.getBytes(), timeout, bytes);
                return result;
            } finally {
                jedis.close();  // 当执行完缓存操作之后,关闭jedis.
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
