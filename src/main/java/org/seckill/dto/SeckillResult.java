package org.seckill.dto;

/**
 * Description: 将所有的Ajax请求全部封装成json数据
 *
 * @author: dong
 * @create: 2017-03-19-19:01
 */
public class SeckillResult<T> {

    // 是否请求成功
    private boolean success;

    // 请求的数据
    private T data;

    // 用于保存请求中错误的信息
    private String error;

    public SeckillResult(boolean isSuccess, T data) {
        this.success = isSuccess;
        this.data = data;
    }

    public SeckillResult(boolean isSuccess, String error) {
        this.error = error;
        this.success = isSuccess;
    }

    // setter and getter
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
