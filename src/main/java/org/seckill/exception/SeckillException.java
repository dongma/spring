package org.seckill.exception;

/**
 * Description: 秒杀相关的所有业务的异常。
 *
 * @author: dong
 * @create: 2017-03-16-16:40
 */
public class SeckillException extends RuntimeException {

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
