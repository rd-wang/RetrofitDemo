package com.text.rd.retrofitdemo.retrofit;

import com.text.rd.retrofitdemo.retrofit.taobaoConverter.TaobaoConverterFactory;

import retrofit2.Retrofit;

/**
 * Created by rd on 2016/11/23.
 */
public class RetrofitUtil {

    private static Retrofit instance;

    public static Retrofit getRetrofit(String url) {
        if (instance == null) {
            synchronized (RetrofitUtil.class) {
                if (instance == null) {
                    instance = new Retrofit.Builder().baseUrl(url)
                            .addConverterFactory(TaobaoConverterFactory.create()).build();
                }
            }
        }
        return instance;
    }

    public static <T> T getInstance(Class<T> targetClass, String url) {
        return getRetrofit(url).create(targetClass);
    }

}
