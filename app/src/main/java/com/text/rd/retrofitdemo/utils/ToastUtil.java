package com.text.rd.retrofitdemo.utils;

import android.content.Context;
import android.widget.Toast;

import com.text.rd.retrofitdemo.Application.MyApplication;


/**
 * Created by rd on 2016/11/22.
 */
public class ToastUtil {
    private static Context mContext = MyApplication.getAppContext();

    public static void showToast(CharSequence string){
        Toast.makeText(mContext, string,Toast.LENGTH_SHORT).show();
    }
}
