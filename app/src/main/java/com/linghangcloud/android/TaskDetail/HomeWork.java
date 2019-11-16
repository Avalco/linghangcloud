package com.linghangcloud.android.TaskDetail;

import com.linghangcloud.android.GSON.FileList;

public class HomeWork {
    private String FilePic;
    private String FileName;
    private String UserName;
    private String download;
    private boolean IsSubmit;

    public String getFilePic() {
        return FilePic;
    }

    public void setFilePic(String filePic) {
        FilePic = filePic;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public boolean isSubmit() {
        return IsSubmit;
    }

    public void setSubmit(boolean submit) {
        IsSubmit = submit;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }
    public static HomeWork Valueof(FileList fileList){
        HomeWork homeWork=new HomeWork();
        homeWork.setFileName("性感程序员在线修bug");
        homeWork.setUserName(fileList.getNickname());
        homeWork.setDownload(fileList.getJoburl());
        homeWork.setFilePic(fileList.getImgurl());
        homeWork.setSubmit(true);
        return  homeWork;
    }
}
