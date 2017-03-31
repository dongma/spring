package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SecKillExecution;
import org.seckill.entity.SecKill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Description: Service实现类的Test测试
 *
 * @author: dong
 * @create: 2017-03-16-18:06
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit Spring的配置文件
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceImplTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void testGetSeckillList() {
        List<SecKill> list = this.seckillService.getSeckillList();
        // System.out.println(list);
        logger.info("list={}", list);
    }

    @Test
    public void testById() {
        long id = 1000;
        SecKill secKill = this.seckillService.getById(id);
//        System.out.println(secKill);
        logger.info("seckill={}", secKill);
    }

    @Test
    public void testExportSeckillUrl() {
        /**
         Exposer{isExposed=true,
            md5='1fc5572ac5f63d19e2654d8e578fad61',
            seckillId=1000,
            now=1490257279708,
            start=1490252493000,
            end=1496073600000}
         * */
        long id = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(id);
//      System.out.println(exposer);
        logger.info("exposer={}", exposer);
    }

    @Test
    public void testExecuteSeckill() throws Exception {
        long id = 1000;
        long phone = 18191533864l;
        String md5 = "1fc5572ac5f63d19e2654d8e578fad61";
        SecKillExecution execution = this.seckillService.executeSeckill(id, phone, md5);
        System.out.println("execution:" + execution);
    }

    /**
     * 用于测试使用存储过程procedure执行秒杀.
     * */
    @Test
    public void testProcedureKill() {
        long seckillId = 1001;
        long phone = 17302190118l;
        // 用于暴露秒杀的地址
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if(null != exposer) {
            String md5 = exposer.getMd5();
            // InteLLi IDE Ctrl + ALt + V自动补全返回的类型.
            SecKillExecution execution = seckillService.
                    executeSeckillByProcudure(seckillId, phone, md5);
            logger.info(execution.getStateInfo() + "  " + execution.getState());
        }
    }
}
