package com.text.rd.retrofitdemo.retrofit;

import com.google.gson.JsonElement;

/**
 * Created by rd on 2016/11/22.
 */
public class BaseResponse {
    private int code;
    private String msg;
    private JsonElement data;

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
