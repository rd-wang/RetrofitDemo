package com.text.rd.retrofitdemo.utils;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author yangxuanwen on 15/5/12.
 *
 * mod: rd 2016年06月08日11:18:14
 *          添加了过滤条件  --> *****
 *          新增分行显示 -->否则输出的json不全.
 */
public class LogCat {

    public static LogCat createInstance(Object obj) {
        return new LogCat(obj);
    }

    public static LogCat L = createInstance(LogCat.class);

    private LogCat(Object obj) {
        mTag = getTagName(obj);
    }

    private String mTag;

    private String extarTag = "*****";

    private boolean isRelease = false;

    public void setTag(Object o) {
        mTag = getTagName(o);
    }

    public void w(Object... m) {
        if (!isRelease) {
            line();
            int length=join(m).length();
            int times= (int) Math.ceil(length/1000.0);
            for(int i=0;i<times;i++){
                Log.w(extarTag + mTag,join(m).substring(i*1000,(i+1)*1000<length?(i+1)*1000:length), null);
            }
        }
    }

    public void d(Object... m) {
        if (!isRelease) {
            line();
            int length=join(m).length();
            int times= (int) Math.ceil(length/1000.0);
            for(int i=0;i<times;i++){
                Log.d(extarTag + mTag,join(m).substring(i*1000,(i+1)*1000<length?(i+1)*1000:length), null);
            }
        }
    }

    public void i(Object... m) {
        if (!isRelease) {
            line();
            int length=join(m).length();
            int times= (int) Math.ceil(length/1000.0);
            for(int i=0;i<times;i++){
                Log.i(extarTag + mTag,join(m).substring(i*1000,(i+1)*1000<length?(i+1)*1000:length), null);
            }
        }
    }

    private void subLine(Object[] m) {
        int length=join(m).length();
        int times= (int) Math.ceil(length/1000.0);
        for(int i=0;i<times;i++){
            Log.e(extarTag + mTag,join(m).substring(i*1000,(i+1)*1000<length?(i+1)*1000:length), null);
        }
    }
    private void subLine(String m) {
        int length=m.length();
        int times= (int) Math.ceil(length/1000.0);
        for(int i=0;i<times;i++){
            Log.e(extarTag + mTag,m.substring(i*1000,(i+1)*1000<length?(i+1)*1000:length), null);
        }
    }
    private void subLine(String m, Throwable t) {
        int length=m.length();
        int times= (int) Math.ceil(length/1000.0);
        for(int i=0;i<times;i++){
            Log.e(extarTag + mTag,m.substring(i*1000,(i+1)*1000<length?(i+1)*1000:length), t);
        }
    }

    public void e(String s){
        if (!isRelease) {
            line();
            subLine(s);
        }

    }
    public void e(Throwable t) {
        e("", t);
    }
    public void e(String m, Throwable t) {
        if (t != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            // sLogger.setError(sw.toString());
        }
        if (!isRelease) {
            line();
            subLine(m,t);
        }
    }

    public void e(Object... m) {
        if (!isRelease) {
            line();
            subLine(m);
            for (Object o: m) {
                if (o instanceof Exception) {
                    ((Exception) o).printStackTrace();
                }
            }
        }
    }

    private void line() {
        Log.d(mTag, "----------------------------------");
    }

    private String getTagName(Object o) {
        if (o instanceof Class<?>) {
            return ((Class<?>) o).getSimpleName();
        } else if (o instanceof CharSequence) {
            return (String) o;
        } else {
            return getTagName(o.getClass());
        }
    }

    private String join(Object[] arr) {
        if (arr == null || arr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object o: arr) {
            sb.append(String.valueOf(o));
        }
        return sb.toString();
    }
}
