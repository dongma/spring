package org.seckill.dto;

import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;

/**
 * Description: 封装执行秒杀结束后的结果
 *
 * @author: dong
 * @create: 2017-03-16-16:24
 */
public class SecKillExecution {

    private long scekillId;

    // 执行秒杀后的状态
    private int state;

    // 状态的标识
    private String stateInfo;

    // 当秒杀成功之后,需要传递秒查成功的对象.
    private SuccessKilled successKilled;

    public SecKillExecution(long scekillId, SeckillStateEnum enums, SuccessKilled successKilled) {
        this.scekillId = scekillId;
        this.state = enums.getState();
        this.stateInfo = enums.getStateInfo();
        this.successKilled = successKilled;
    }

    public SecKillExecution(long scekillId, SeckillStateEnum enums) {
        this.scekillId = scekillId;
        this.state = enums.getState();
        this.stateInfo = enums.getStateInfo();
    }

    public long getScekillId() {
        return scekillId;
    }

    public void setScekillId(long scekillId) {
        this.scekillId = scekillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }

    // toString method.
    @Override
    public String toString() {
        return "SecKillExecution{" +
                "scekillId=" + scekillId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", successKilled=" + successKilled +
                '}';
    }
}
