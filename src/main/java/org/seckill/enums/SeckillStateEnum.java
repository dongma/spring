package org.seckill.enums;

/**
 * Created by dong on 2017/3/16.
 */
public enum SeckillStateEnum {
    SUCCESS(1, "秒杀成功"),
    END(0, "秒杀结束"),
    REPEAT_KILL(-1, "重复秒杀"),
    INNER_ERROR(-2, "系统错误"),
    DATE_REWRITE(-3, "数据篡改");

    // 秒杀的状态
    private int state;

    // 信息表示.
    private String stateInfo;

    SeckillStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    // 根据下标的值获取得到Enum
    public static SeckillStateEnum stateOf(int index) {
        for(SeckillStateEnum en : values()) {
            if(en.getState() == index) {
                return en;
            }
        }
        return null;
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

    // toString
    @Override
    public String toString() {
        return "SeckillStateEnum{" +
                "state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                '}';
    }
}
