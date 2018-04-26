package org.seckill.dto;

import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;

/**
 *封装秒杀执行结果
 */
public class SeckillExecution {
    //秒杀执行状态
    private int state;
    //秒杀执行详细信息
    private String seckillInfo;
    private long seckillId;
    //秒杀成功对象
    private SuccessKilled successKilled;

    public SeckillExecution(SeckillStateEnum seckillStateEnum, long seckillId) {
        this.state = seckillStateEnum.getState();
        this.seckillInfo = seckillStateEnum.getSeckillInfo();
        this.seckillId = seckillId;
    }

    public SeckillExecution(SeckillStateEnum seckillStateEnum, long seckillId, SuccessKilled successKilled) {
        this.state = seckillStateEnum.getState();
        this.seckillInfo = seckillStateEnum.getSeckillInfo();
        this.seckillId = seckillId;
        this.successKilled = successKilled;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSeckillInfo() {
        return seckillInfo;
    }

    public void setSeckillInfo(String seckillInfo) {
        this.seckillInfo = seckillInfo;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }

    @Override
    public String toString() {
        return "SeckillExecution{" +
                "state=" + state +
                ", seckillInfo='" + seckillInfo + '\'' +
                ", seckillId=" + seckillId +
                ", successKilled=" + successKilled +
                '}';
    }

}
