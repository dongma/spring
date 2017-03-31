package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SecKill;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description: 秒杀实体类的底层数据处理
 *
 * @author: dong
 * @create: 2017-03-07-12:16
 */
public interface SeckillDAO {

    /**
     * seckillId 秒杀商品的id编号
     * killTime 秒杀商品的时间,在对商品的数量进行递减的时候,同时也需要在秒杀明细表中将处理的时间放入.
     * */
    int reduceNumber(@Param("seckillId")long seckillId, @Param("killTime") Date killTime);


    /**
     * seckillId 秒杀商品的id编号
     * queryById是通过秒杀的id获取得到SecKill对象.
     * */
    SecKill queryById(long seckillId);


    /**
     * 通过偏移量查询秒杀商品列表,limit表示的是偏移量.
     * */
    List<SecKill> queryByOffset(@Param("offset") int offset, @Param("limit") int limit);

    /**
     *  通过存储过程执行秒杀的逻辑.
     * */
    void killByProcedure(Map params);
}
