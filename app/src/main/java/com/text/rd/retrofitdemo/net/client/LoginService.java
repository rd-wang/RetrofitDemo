package com.text.rd.retrofitdemo.net.client;


import com.text.rd.retrofitdemo.bean.User;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by rd on 2016/11/14.
 */
public interface LoginService {
    @POST("/login")
    Call<User> basicLogin();
}
