package org.seckill.exception;

/**
 * Description: 秒杀关闭的异常,当秒杀结束时用户还要进行秒杀就会抛出这个异常
 *
 * @author: dong
 * @create: 2017-03-16-16:43
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
