package com.linghangcloud.android.Util;

import android.util.Log;

import com.google.gson.Gson;
import com.linghangcloud.android.GSON.FileList;
import com.linghangcloud.android.GSON.Task;
import com.linghangcloud.android.GSON.TaskDetail;
import com.linghangcloud.android.GSON.Token;
import com.linghangcloud.android.TaskDetail.Commit;
import com.linghangcloud.android.TaskDetail.HomeWork;

import org.json.JSONObject;

import java.lang.reflect.Array;

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
}
