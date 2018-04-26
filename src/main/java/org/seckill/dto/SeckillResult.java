package org.seckill.dto;

/**
 * 响应结果集
 * 封装所有的ajax请求返回类型,方便返回json
 * @param <T>  T为data类型
 */
public class SeckillResult<T> {

    private boolean success;
    private T data;
    private String error;

    /**
     * 响应成功或出现系统允许异常（如重复秒杀等），传入数据
     * @param success
     * @param data
     */
    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    /**
     * 响应失败，传入失败的错误信息
     * @param success
     * @param error
     */
    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

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
