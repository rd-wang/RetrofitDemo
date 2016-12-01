package com.text.rd.retrofitdemo.retrofit.taobaoConverter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by rd on 2016/11/22.
 */
final class TaoBaoGsonResponseBodyConverter<T> implements Converter<ResponseBody, String> {

    TaoBaoGsonResponseBodyConverter() {
    }

    @Override
    public String convert(ResponseBody value) throws IOException {
        return value.string();
    }
}
