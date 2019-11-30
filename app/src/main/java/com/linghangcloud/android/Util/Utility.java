package com.linghangcloud.android.Util;

import android.util.Log;

import com.google.gson.Gson;

import com.linghangcloud.android.GSON.FileList;
import com.linghangcloud.android.GSON.ReplyCommit;
import com.linghangcloud.android.GSON.Task;
import com.linghangcloud.android.GSON.TaskDetail;
import com.linghangcloud.android.GSON.Token;
import com.linghangcloud.android.R;
import com.linghangcloud.android.TaskDetail.Commit;
import com.linghangcloud.android.TaskDetail.HomeWork;

import com.linghangcloud.android.GSON.Tasks;
import com.linghangcloud.android.GSON.Token;
import com.linghangcloud.android.GSON.UserInfo;


import org.json.JSONObject;


import java.lang.reflect.Array;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class Utility {
    public static Token Handlogin(String response) {
        return new Gson().fromJson(response, Token.class);
    }

    public static TaskDetail handleTaskDetailResponse(String reponse){
        try {
            JSONObject jsonObject=new JSONObject(reponse);
            String task=jsonObject.getString("data").toString();
            Log.e("JSON解析 test: ",task);
//            JSONObject now =new JSONObject(task);
//            TaskDetail taskDetail =new TaskDetail();
//            taskDetail.setTaskid(Integer.valueOf(jsonObject.getString("taskid")));
//            taskDetail.setHeadline(jsonObject.getString("headline"));
//            taskDetail.setAdminaccount(jsonObject.);
            TaskDetail taskDetail=new Gson().fromJson(task,TaskDetail.class);

            return taskDetail;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }
    public static void handleReplyResponse(String reponse){
        try{
            JSONObject jsonObject=new JSONObject(reponse);
            String data=jsonObject.getString("data").toString();
            ReplyCommit replyCommit=new Gson().fromJson(data,ReplyCommit.class);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static com.linghangcloud.android.GSON.Commit heandleCommitResponse(String response){
        try {
            String task =response;
            Log.e("test ：评论", response);
            com.linghangcloud.android.GSON.Commit commit=new Gson().fromJson(task, com.linghangcloud.android.GSON.Commit.class);

            return commit;
        }catch (Exception e)
        {
            e.printStackTrace();
            Log.e("test ：评论", "shibai " );
        }
        return  null;
    }
    public static HomeWork handleFileResponse(String reponse){
        try {
            String task=reponse;
            Log.e("why test: ",task);
//            JSONObject now =new JSONObject(task);
//            TaskDetail taskDetail =new TaskDetail();
//            taskDetail.setTaskid(Integer.valueOf(jsonObject.getString("taskid")));
//            taskDetail.setHeadline(jsonObject.getString("headline"));
//            taskDetail.setAdminaccount(jsonObject.);

            FileList fileList =new Gson().fromJson(task,FileList.class);
            Log.e(" hhh test:",fileList.getNickname());
            HomeWork homeWork =HomeWork.Valueof(fileList);

            return homeWork;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }
//    public static  handleTaskDetailResponse(String reponse){
//        try {
//            JSONObject jsonObject=new JSONObject(reponse);
//            String task=jsonObject.getString("data").toString();
//            Log.e("JSON解析 test: ",task);
////            JSONObject now =new JSONObject(task);
////            TaskDetail taskDetail =new TaskDetail();
////            taskDetail.setTaskid(Integer.valueOf(jsonObject.getString("taskid")));
////            taskDetail.setHeadline(jsonObject.getString("headline"));
////            taskDetail.setAdminaccount(jsonObject.);
//            TaskDetail taskDetail=new Gson().fromJson(task,TaskDetail.class);
//
//            return taskDetail;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return  null;
//    }

    public static UserInfo HandUserInfo(String response) {
        return new Gson().fromJson(response, UserInfo.class);
    }

    public static Tasks HandTasks(String response) {
        return new Gson().fromJson(response, Tasks.class);
    }

    public static void SendHttp(String url, String token, okhttp3.Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("access_token", token)
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
