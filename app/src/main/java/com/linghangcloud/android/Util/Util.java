package com.linghangcloud.android.Util;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Util {
    public void getwithokhttp(String url,okhttp3.Callback callback)  throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

}
