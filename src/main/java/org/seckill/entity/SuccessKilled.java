package org.seckill.entity;

import java.util.Date;

/**
 * Description: 成功秒杀的实体类(一条成功秒杀的商品记录)
 *
 * @author: dong
 * @create: 2017-03-07-11:57
 */
public class SuccessKilled {
    private int seckillId;              // 秒杀商品的id编号
    private long userphone;              // 用户的联系方式
    private int state;                  // 表示的是用户当前的状态
    private Date createtime;            // 表示的是这条记录的创建时间
    // 多对一的关系,每一个商品记录都包含有一个seckill实体类.
    private SecKill seckill;

    // 私有属性的setter和getter方法.
    public int getSeckillId() {
        return seckillId;
    }
    public void setSeckillId(int seckillId) {
        this.seckillId = seckillId;
    }

    public long getUserphone() {
        return userphone;
    }

    public void setUserphone(long userphone) {
        this.userphone = userphone;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    // 生成SuccessKilled类对应的toString方法
    @Override
    public String toString() {
        return "SuccessKilled{" +
                "seckillId=" + seckillId +
                ", userphone=" + userphone +
                ", state=" + state +
                ", createtime=" + createtime +
                ", seckill=" + seckill +
                '}';
    }
}
