package com.linghangcloud.android.Util;

import com.google.gson.internal.LinkedHashTreeMap;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Util {
    public void getwithokhttp(String url, okhttp3.Callback callback) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
    public void getwithokhttphead(String url, String head,String headname, Callback callback)throws  IOException{
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .addHeader(headname,head)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

}
