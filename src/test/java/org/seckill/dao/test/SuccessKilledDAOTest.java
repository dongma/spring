package org.seckill.dao.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SuccessKilledDAO;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Description: SuccessKilledDAO类的测试类
 *
 * @autor: dong
 * @create: 2017-03-23-15:10
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉Junit,Spring配置文件的位置
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDAOTest {

    @Resource
    private SuccessKilledDAO successKilledDAO;

    @Test
    public void testInsertSuccessKilled() {
        /**
         * effect:0,在SuccessKilledDAO.xml中存在ignore关键字,当存在主键冲突的时候不会报告重复的错误.
         * */
        long id = 1000L;
        long phone = 15289374792L;
        // 将记录插入到数据表中.
        int effect = this.successKilledDAO.insertSuccessKilled(id, phone);
        System.out.println("effect:" + effect);
    }

    @Test
    public void testQueryByIdWithSecKill() {
        /**
         * Caused by: org.apache.ibatis.binding.BindingException:
         * Parameter 'userPhone' not found.
         * Available parameters are [seckillId, userphone, param1, param2]
         *
         * 2.Caused by: com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException:
         * You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near 'innner join seckill s
         on sk.seckill_id = s.seckill_id
         where sk' at line 13
         *
         * 3.Caused by: com.mysql.jdbc.exceptions.jdbc4.MySQLDataException: '1.5289374792E10' in column '2' is outside valid range for the datatype INTEGER.
         at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
         at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
         at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
         at java.lang.reflect.Constructor.newInstance(Constructor.java:423)
         * */
        long id = 1000L;
        long userPhone = 15289374792l;
        // 通过Id与电话号码查询唯一的记录.
        SuccessKilled successKilled = this.successKilledDAO.
                queryByIdWithSecKill(id, userPhone);
        System.out.println(successKilled);
    }
}
