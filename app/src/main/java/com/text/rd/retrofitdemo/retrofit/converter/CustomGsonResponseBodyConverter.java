package com.text.rd.retrofitdemo.retrofit.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.text.rd.retrofitdemo.retrofit.BaseResponse;
import com.text.rd.retrofitdemo.utils.ToastUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by rd on 2016/11/22.
 */
final class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private int errorCount;

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
        switch (baseResponse.getCode()) {
            case 0:
                // OK
                break;
            case 106:
                ToastUtil.showToast("登出");
                break;
            case 105:
                if (errorCount >= 3) {
                    ToastUtil.showToast("忘记密码？请点击左下角按钮找回密码");
                    errorCount = 0;
                } else {
                    ToastUtil.showToast("密码不正确呢");
                    errorCount++;
                }
                break;
            case 500:
                ToastUtil.showToast("服务器故障");
            default:
                ToastUtil.showToast(baseResponse.getMsg());
        }

        MediaType contentType = value.contentType();
        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
        InputStream inputStream = new ByteArrayInputStream(baseResponse.getData().toString().getBytes());
        Reader reader = new InputStreamReader(inputStream, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);

        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }


    }
}
