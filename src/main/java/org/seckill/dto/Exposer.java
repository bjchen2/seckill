package org.seckill.dto;

/**
 * 暴露秒杀地址DTO
 * exposer：曝光器
 */
public class Exposer {
    //是否开启秒杀
    private boolean exposed;
    //id
    private long seckillId;
    //通过加密操作后的地址
    private String md5;
    //系统当前时间（毫秒）
    private long now;
    //秒杀开启时间
    private long start;
    //秒杀结束时间
    private long end;

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
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

    @Override
    public String toString() {
        return "Exposer{" +
                "exposed=" + exposed +
                ", seckillId=" + seckillId +
                ", md5='" + md5 + '\'' +
                ", now=" + now +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

    //成功时返回的实例
    public Exposer(boolean exposed, long seckillId, String md5) {

        this.exposed = exposed;
        this.seckillId = seckillId;
        this.md5 = md5;
    }
    //对象存在但时间未到或超出时返回的实例
    public Exposer(boolean exposed, long seckillId, long now, long start, long end) {
        this.seckillId = seckillId;
        this.exposed = exposed;
        this.now = now;
        this.start = start;
        this.end = end;
    }
    //对象不存在时返回的实例
    public Exposer(boolean exposed, long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }
}
