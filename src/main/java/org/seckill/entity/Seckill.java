package org.seckill.entity;

import java.util.Date;

/**
 * 对应数据库中的秒杀库存表
 */
public class Seckill {
    private long seckillId;
    private int number;
    private String name;
    private Date startTime;
    private Date endTime;
    private Date createTime;

    @Override
    public String toString() {
        return "Seckill{" +
                "seckillId=" + seckillId +
                ", number=" + number +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createTime=" + createTime +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getNumber() {

        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getSeckillId() {

        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }
}
