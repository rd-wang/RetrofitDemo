package com.text.rd.retrofitdemo.retrofit;


import com.text.rd.retrofitdemo.bean.HomeBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by rd on 2016/11/22.
 */
public interface ApiService {
    @GET("home/list/v2")
    Call<List<HomeBean>> getHomeList();
}
