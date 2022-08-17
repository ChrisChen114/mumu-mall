package com.imooc.mall.common;

import com.imooc.mall.exception.ImoocMallExceptionEnum;

/**
 * 描述：      通用返回对象
 */
public class ApiRestResponse<T> {
    private Integer status;

    private String msg;

    //泛型
    private T data;

    private static final int OK_CODE=10000;
    private static final String OK_MSG = "SUCCESS";

    //构造函数
    public ApiRestResponse(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ApiRestResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    //无参的构造函数
    public ApiRestResponse( ) {
        this(OK_CODE,OK_MSG);
    }

    /**
     * 带着 OK_CODE和OK_MSG的返回值
     * @return
     * @param <T>
     */
    public static <T> ApiRestResponse<T> success(){
        return new ApiRestResponse<>();
    }

    public static <T>ApiRestResponse<T> success(T result){
        ApiRestResponse<T> response = new ApiRestResponse<>();
        response.setData(result);
        return response;
    }

    //错误处理
    public static <T>ApiRestResponse<T> error(Integer code,String msg){
        return new ApiRestResponse<>(code, msg);
    }

    //异常枚举的方法
    public static <T>ApiRestResponse<T> error(ImoocMallExceptionEnum ex){
        return new ApiRestResponse<>(ex.getCode(), ex.getMsg());
    }

    @Override
    public String toString() {
        return "ApiRestResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
