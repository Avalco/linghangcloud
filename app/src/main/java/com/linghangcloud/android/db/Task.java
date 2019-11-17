package com.linghangcloud.android.db;

import com.google.gson.annotations.SerializedName;

public class Task {
    private String headline; // 任务标题
    private  String group; // 组别
    @SerializedName("details")
    private  String content; // 任务内容
    private String taskid; // 任务id
    private String createtime; // 创建时间
    private  String deadline; // 截止时间
    private  String adminaccount; // 任务发布者账号

    public Task(String headline, String group, String content, String taskid, String createtime, String deadline, String adminaccount) {
        this.headline = headline;
        this.group = group;
        this.content = content;
        this.taskid = taskid;
        this.createtime = createtime;
        this.deadline = deadline;
        this.adminaccount = adminaccount;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getAdminaccount() {
        return adminaccount;
    }

    public void setAdminaccount(String adminaccount) {
        this.adminaccount = adminaccount;
    }
}
