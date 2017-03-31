package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDAO;
import org.seckill.entity.SecKill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Description: RedisDAO的测试类
 *
 * @author: dong
 * @create: 2017-03-29-15:25
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
    private final long id = 1001;  // 代表的是商品编号的id.

    @Autowired
    private RedisDAO redisDAO;

    @Autowired
    private SeckillDAO seckillDAO;

    @Test
    public void testSeckill() throws Exception {
        // get and put
        SecKill secKill = redisDAO.getSeckill(id);
        if(secKill == null) {
            secKill = seckillDAO.queryById(id);
            // 调用Redis的方法将对象进行缓存
            String result = redisDAO.putSeckill(secKill);
            System.out.println(result);
            // 通过get方法从redis中进行查询
            secKill = redisDAO.getSeckill(id);
            System.out.println(secKill);
        }
    }
}
