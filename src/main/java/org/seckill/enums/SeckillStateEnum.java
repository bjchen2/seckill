package org.seckill.enums;

/**
 * 使用枚举表述常量数据字段
 */
public enum SeckillStateEnum {
    SUCCESS(1,"秒杀成功"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1,"重复秒杀"),
    INNER_ERROR(-2,"系统异常"),
    DATA_REWRITE(-3,"数据被篡改");

    private int state;
    private String seckillInfo;

    SeckillStateEnum(int state, String seckillInfo) {
        this.state = state;
        this.seckillInfo = seckillInfo;
    }

    public String getSeckillInfo() {
        return seckillInfo;
    }

    public int getState() {
        return state;
    }

    public static SeckillStateEnum stateOf(int index){
        for (SeckillStateEnum state : values()){
            if (state.getState() == index){
                return state;
            }
        }
        return null;
    }
}
