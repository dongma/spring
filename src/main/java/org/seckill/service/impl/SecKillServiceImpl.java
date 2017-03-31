
package org.seckill.service.impl;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDAO;
import org.seckill.dao.SuccessKilledDAO;
import org.seckill.dao.cache.RedisDAO;
import org.seckill.dto.Exposer;
import org.seckill.dto.SecKillExecution;
import org.seckill.entity.SecKill;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sun.corba.se.impl.util.Utility.printStackTrace;

/**
 * Description: SeckillService业务的实现类
 * @author: dong
 * @create: 2017-03-16-16:47
 *
 * 修改记录:
 *  1. redis缓存优化 modify by dong 2017/03/29
 *  2. 事务优化减少rowLock的时间 modify by dong 2017/03/29
 */
@Service
public class SecKillServiceImpl implements SeckillService {

    // 用于向控制台输出日志的logger对象.
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 加入一个混淆的字符串(秒杀接口的)salt,为了避免猜出Md5的值,越复杂越好.
    private final String salt = "13njerj3jh4j3";

    // 因为与Mybatis整合,在Ioc容器中已经存在实例,故使用spring自动装配.
    @Autowired
    private SeckillDAO seckillDAO = null;

    @Autowired
    private SuccessKilledDAO successKilledDAO = null;

    // redis缓存优化 modify by dong 2017/3/29
    @Autowired
    private RedisDAO redisDAO;

    public SecKill getById(long seckillId) {
        return this.seckillDAO.queryById(seckillId);
    }

    public List<SecKill> getSeckillList() {
        return this.seckillDAO.queryByOffset(0, 100);
    }

    /**
     * 私有的方法,用于获取Seckill记录的MD5的值.
     * */
    private String getMd5(long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    public Exposer exportSeckillUrl(long seckillId) {
        // 使用缓存获取对象,进行查询的优化,建立在超时维护的基础上维护对象的一致性.
        /**
         * get from cache
         *  if not exist , get from db
         *  else return an object.
         *  execute logic operate;
         * */
      /*  SecKill seckill = this.seckillDAO.queryById(seckillId);
        if(null == seckill) {
            return new Exposer(false, seckillId);
        }*/
        // redis缓存优化 modify by dong 2017/3/29
        SecKill seckill = this.redisDAO.getSeckill(seckillId);
        if (null == seckill) {
            // 当缓存中不存在要查询的对象的时候,使用jdbc从数据库中进行查询.
            seckill = this.seckillDAO.queryById(seckillId);
            if (seckill == null) {
                // 说明数据库中也不存在该对象
                return new Exposer(false, seckillId);
            } else {
                // 否则将查询得到的结果放入到redis缓存中.
                this.redisDAO.putSeckill(seckill);
            }
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        // 系统当前的时间
        Date nowTime = new Date();
        // 当前now的秒杀时间如果在秒杀开始的时间之前或者在秒杀结束的时间之后,则是不能够参与秒杀活动的.
        if(nowTime.getTime() < startTime.getTime() ||
                nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, getMd5(seckillId), seckillId, nowTime.getTime(), startTime.getTime()
                    ,endTime.getTime());
        }
        return new Exposer(true, getMd5(seckillId), seckillId, nowTime.getTime(), startTime.getTime(),
                endTime.getTime());
    }

    /**
     * 使用注解基于声明式的事务处理;
     *  秒杀成功,减库存增减明细记录;秒杀失败,抛出异常,事务回滚;
     *  使用注解控制事务的优点:
     *  1.开发团队达成一致约定,明确标注事务方法的编程风格
     *  2.保证事务方法执行的时间尽可能的短,不要穿插其他的网络请求HTTP/RPC等请求操作.
     *  3.不是所有的方法都需要事务,如只有一条修改操作或者只读操作的时候不需要事务.
     * */
    @Transactional
    public SecKillExecution executeSeckill(long seckillId, long userPhone, String md5)
                throws SeckillException, RepeatKillException, SeckillCloseException {
        try {
            if (md5 == null || !md5.equals(getMd5(seckillId))) {
                throw new SeckillException("seckill data rewrite.");
            }
            // 执行秒杀的逻辑:减库存+记录购买的行为.
            Date now = new Date();
           /* // 减库存
            int updateCount = this.seckillDAO.reduceNumber(seckillId, now);
            if (updateCount <= 0) {  // 没有更新到记录,秒杀结束
                throw new SeckillCloseException("seckill is closed");
            } else {
                // 记录购买的行为
                int insertCount = successKilledDAO.insertSuccessKilled(seckillId, userPhone);
                // 唯一 seckillId, userPhone。
                if (insertCount <= 0) {
                    throw new RepeatKillException("seckill repeated");
                } else {
                    // 秒杀成功,返回执行结束之后的所有结果的信息.
                    SuccessKilled successKilled = successKilledDAO.queryByIdWithSecKill(seckillId, userPhone);
                    return new SecKillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }*/
            // 事务优化,在之前先是执行的reduceNumber()方法,
            // 1)会先获取一行记录的rowLock,当update完成之后释放rowLock;
            // 2)之后执行insert操作,总的时间是花费在GC和rowLock上面,优化的方案是缩短rowLock的时间.
            //      先执行insert的操作,之后再执行update Seckill的操作.

            // 事务优化减少rowLock的时间 modify by dong 2017/03/29;
            // 记录购买的行为
            int insertCount = successKilledDAO.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                throw new RepeatKillException("seckill repeated");
            } else {
                // 当insertCount大于0的时候再去减库存.
                int updateCount = this.seckillDAO.reduceNumber(seckillId, now);
                if (updateCount <= 0) {  // 没有更新到记录,秒杀结束
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    // 秒杀成功,返回执行结束之后的所有结果的信息.
                    SuccessKilled successKilled = successKilledDAO.queryByIdWithSecKill(seckillId, userPhone);
                    return new SecKillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 所有编译器的异常都转换为运行时的异常.
            throw new SeckillException("seckill innner error:" + e.getMessage());
        }
    }

    /**
     * 通过调用存储过程执行秒杀操作.
     * */
    public SecKillExecution executeSeckillByProcudure(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        // 首先判断md5的值
        if(md5 == null || !md5.equals(getMd5(seckillId))) {
            return new SecKillExecution(seckillId, SeckillStateEnum.DATE_REWRITE);
        }
        // 获取当前系统的时间
        Date date = new Date();
        Map<String, Object> params = new HashMap<String, Object>();
        // 将要调用存储过程的参数放入到map中
        params.put("seckillId", seckillId);
        params.put("phone", userPhone);
        params.put("killTime", date);
        params.put("result", null);
        try {
            // 执行秒杀的逻辑
            seckillDAO.killByProcedure(params);
            int result = MapUtils.getIntValue(params, "result", -2);
            if(result == 1) {
                SuccessKilled successKill = successKilledDAO.
                            queryByIdWithSecKill(seckillId, userPhone);
                return new SecKillExecution(seckillId, SeckillStateEnum.SUCCESS, successKill);
            } else {
                // 当调用秒杀失败时,也返回执行的结果
                return new SecKillExecution(seckillId, SeckillStateEnum.stateOf(result));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new SecKillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
        }
    }

}
