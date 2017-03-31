package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SecKillExecution;
import org.seckill.entity.SecKill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * Created by dong on 2017/3/16.
 * 业务接口:
 *  站在使用者的角度去思考问题,3个方面: 方法定义的粒度,方法的定义要非常清楚.
 *      2.参数，越简练越好 3.返回类型(return类型一定要友好/或者return异常,我们允许的异常)
 */
public interface SeckillService {


    /**
     * 返回查询全部的秒杀记录.
     * */
    public List<SecKill> getSeckillList();


    /**
     * 根据id编号查询秒杀记录.
     * */
    public SecKill getById(long seckillId);


    // 定义一些行为的重要接口
    /**
     *  在秒杀开始的时候输出秒杀的地址,否则输出系统时间和秒杀时间
     * */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀的操作,可能会失败,有可能的话,我们要抛出异常.
     * */
    SecKillExecution executeSeckill(long seckillId, long userPhone, String md5)
        throws SeckillException, RepeatKillException, SeckillCloseException;

    /**
     * 执行秒杀的操作,可能会失败,有可能的话,我们要抛出异常.
     * 通过调用存储过程来执行我们的操作.
     * */
    SecKillExecution executeSeckillByProcudure(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException;
}
