package com.text.rd.retrofitdemo.net.client;


import com.text.rd.retrofitdemo.bean.HomeBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by rd on 2016/11/14.
 */
public interface GroupClient {

    @GET("/group/home")
    Call<List<HomeBean>> contributors();
}
