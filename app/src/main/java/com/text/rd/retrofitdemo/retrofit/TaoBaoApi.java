package com.text.rd.retrofitdemo.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by rd on 2016/11/23.
 */
public interface TaoBaoApi {
    @GET("https://xianba.tmall.com/i/asynSearch.htm?_ksTS=1479958530428_363&callback=jsonp364&mid=w-14986613211-0&wid=14986613211&path=/search.htm&&search=y&spm=a1z10.1-b-s.w7219108-14986613109.2.pkptia")
    Call<String> getXianBa();

    @GET
    Call<String> getUrlDetail(@Url String url);

    @GET("https://uitox.tmall.hk/category-1129809897.htm?spm=a1z10.5-b-s.w4010-14465380856.43.it8OGU&search=y&catName=%CD%E6%BE%DF%2F%C4%A3%CD%E6#bd")
    Call<String> getRiXi();

    @GET("https://auldeywj.tmall.com/i/asynSearch.htm?_ksTS=1480128319534_127&callback=jsonp128&mid=w-14621824277-0&wid=14621824277&path=/category.htm&&spm=a1z10.3-b-s.w4011-14621824277.119.fmpJgA&search=y&scene=taobao_shop&pageNo=1")
    Call<String> getFeiXia();

    @GET("https://lianshangseoul.taobao.com/category-1224889825.htm?spm=a1z10.4-c-s.0.0.uWXzAW&search=y&parentCatId=969763178&parentCatName=%CD%E6%BE%DF&catName=%BB%AC%CC%DD%C7%EF%C7%A7%D3%CE%C0%D6%B3%A1")
    Call<String> getYaYa();
}
