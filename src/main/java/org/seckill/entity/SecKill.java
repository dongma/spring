package org.seckill.entity;

import java.util.Date;

/**
 * Description: 秒杀数据表对应的实体类(商品对象)
 *
 * @author: dong
 * @create: 2017-03-07-11:50
 */
public class SecKill {

    private int seckillId;         // 秒杀的id编号
    private String name;            // 秒杀系统的商品名称
    private int number;             // 表示的是商品的库存
    private Date startTime;        // 秒杀的开始时间
    private Date endTime;          // 秒杀活动的结束时间
    private Date createTime;       // 秒杀活动的创建时间

    // 私有属性的setter和getter方法
    public int getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(int seckillId) {
        this.seckillId = seckillId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    // 生成当前类对应的toString方法.
    @Override
    public String toString() {
        return "SecKill{" +
                "seckillId=" + seckillId +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createTime=" + createTime +
                '}';
    }
}
