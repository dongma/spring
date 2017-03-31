package org.seckill.dto;

/**
 * Description: 暴露秒查的地址
 *
 * @author: dong
 * @create: 2017-03-16-16:17
 */
public class Exposer {

    // 是否开启秒杀
    private boolean isExposed;

    // 加密的措施
    private String md5;

    // 秒杀记录的id编号
    private long seckillId;

    // 系统当前的时间(毫秒)
    private long now;

    // 秒杀开始的时间
    private long start;

    // 秒杀结束的时间.
    private long end;

    public Exposer(boolean isExposed, String md5, long seckillId) {
        this.isExposed = isExposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public Exposer(boolean isExposed, String md5, long seckillId, long now, long start, long end) {
        this.isExposed = isExposed;
        this.md5 = md5;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public Exposer(boolean isExposed, long seckillId) {
        this.isExposed = isExposed;
        this.seckillId = seckillId;
    }

    // setter and getter
    public boolean isExposed() {
        return isExposed;
    }

    public void setExposed(boolean exposed) {
        isExposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    // toString
    @Override
    public String toString() {
        return "Exposer{" +
                "isExposed=" + isExposed +
                ", md5='" + md5 + '\'' +
                ", seckillId=" + seckillId +
                ", now=" + now +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
