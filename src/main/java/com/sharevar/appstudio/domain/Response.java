package com.sharevar.appstudio.domain;

public class Response<T> {
    String code="200";
    String msg="ok";
    T data;

    public Response(T data) {
        this.data = data;
    }

    public Response() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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