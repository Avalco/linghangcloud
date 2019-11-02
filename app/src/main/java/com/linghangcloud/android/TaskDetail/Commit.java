package com.linghangcloud.android.TaskDetail;

public class Commit {
    private int pic;
    private int CommitId;
    private int TaskId;
    private String UserId;
    private String detail;
    private String commituser;
    private String reuser;


    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getTaskId() {
        return TaskId;
    }

    public void setTaskId(int taskId) {
        TaskId = taskId;
    }

    public int getCommitId() {
        return CommitId;
    }

    public void setCommitId(int commitId) {
        CommitId = commitId;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCommituser() {
        return commituser;
    }

    public void setCommituser(String commituser) {
        this.commituser = commituser;
    }

    public String getReuser() {
        return reuser;
    }

    public void setReuser(String reuser) {
        this.reuser = reuser;
    }


}
