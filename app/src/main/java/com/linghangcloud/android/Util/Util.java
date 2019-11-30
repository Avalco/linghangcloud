package com.linghangcloud.android.Util;

import com.google.gson.internal.LinkedHashTreeMap;
import com.linghangcloud.android.GSON.SendCommit;

import java.io.File;
import java.io.IOException;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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



    public static void LoadFile(String headname, String head, String address, String taskid, String filename, File file, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();

        RequestBody fileBody = RequestBody.create(MediaType.parse("zip"), file);//将file转换成RequestBody文件
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("job", file.getName(), fileBody)
                .addFormDataPart("taskid",taskid)
                .build();

        Request request = new Request.Builder().url(address)
                .addHeader(headname, head)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static  void posthttp (String headname, String head , String address , SendCommit sendCommit ,Callback callback){
        OkHttpClient ct=new OkHttpClient();
        RequestBody request=new MultipartBody.Builder()
                .addFormDataPart("taskid",sendCommit.getTaskid())
                .addFormDataPart("details",sendCommit.getDetail())
                .addFormDataPart("account",sendCommit.getAccount())
                .addFormDataPart("commentid",sendCommit.getCommentid())
                .build();
        Request requestBody=new Request.Builder()
                .url(address)
                .addHeader(headname,head)
                .post(request)
                .build();
        ct.newCall(requestBody).enqueue(callback);
    }
}
