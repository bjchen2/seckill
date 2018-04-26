package org.seckill.exception;

/**
 * 重复秒杀异常（运行期异常：Spring的事务管理只有在运行期异常时回滚）
 */
public class RepeatKillException extends SeckillException{

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepeatKillException(String message) {
        super(message);
    }
}
