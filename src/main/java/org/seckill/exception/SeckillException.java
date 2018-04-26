package org.seckill.exception;

/**
 * 秒杀事务的所有异常
 */
public class SeckillException extends RuntimeException {

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeckillException(String message) {
        super(message);
    }
}
