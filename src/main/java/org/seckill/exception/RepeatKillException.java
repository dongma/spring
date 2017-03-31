package org.seckill.exception;

/**
 * Description: 重复秒杀的异常,mysql只支持运行期异常的回滚操作.
 *
 * @author: dong
 * @create: 2017-03-16-16:39
 */
public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
