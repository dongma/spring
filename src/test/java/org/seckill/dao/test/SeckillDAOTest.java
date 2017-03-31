package org.seckill.dao.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDAO;
import org.seckill.entity.SecKill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Description: SeckillDAO的单元测试类
 *  配置Spring与JUnit的整合,JUnit在启动的时候加载Spring Ioc容器
 * @author: dong
 * @create: 2017-03-09-10:42
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉JUnit,Spring的配置文件位置.
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDAOTest {

    @Resource
    private SeckillDAO seckillDAO;

    @Test
    public void testQueryById() throws Exception {
        /**
         * 1000元秒杀iphone6s; 通过调用mybatis查询的方法是成功的.
         * */
        long id = 1000;
        SecKill secKill = seckillDAO.queryById(id);
        System.out.println(secKill.getName());
        System.out.println(secKill);
    }

    @Test
    public void testReduceNumber() throws Exception {
        /**
         *  将id编号为1000的 iphone6s数量-1
         * */
        long id = 1000;
        // 返回的是受影响的行数.
        int effect = seckillDAO.reduceNumber(id, new Date());
        System.out.println("effect行数:" + effect);
    }

    @Test
    public void testQueryAll() {
        /**
         * 调用mybatis中的sql进行查询.
         * error:
         * Caused by: org.apache.ibatis.binding.BindingException: Parameter 'offset' not found.
         * List<SecKill> queryByOffset(int offset, int limit);
         * Java并没有保存形参的记录,参数offset是不能找到的
         * queryByOffset(int offset, int limit); --> queryByOffset(arg0, arg1)
         * */
        List<SecKill> listKill = this.seckillDAO.queryByOffset(0, 100);
        for(SecKill sec : listKill) {
            System.out.println(sec);
        }
    }

}
