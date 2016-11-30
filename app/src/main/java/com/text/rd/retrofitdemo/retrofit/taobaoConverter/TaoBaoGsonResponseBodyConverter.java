package com.text.rd.retrofitdemo.retrofit.taobaoConverter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by rd on 2016/11/22.
 */
final class TaoBaoGsonResponseBodyConverter<T> implements Converter<ResponseBody, String> {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    TaoBaoGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public String convert(ResponseBody value) throws IOException {
        String response = value.string();
        return response;
    }
}
