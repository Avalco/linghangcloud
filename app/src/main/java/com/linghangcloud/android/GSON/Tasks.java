package com.linghangcloud.android.GSON;

import com.linghangcloud.android.db.Task;

import java.util.List;

public class Tasks {
    private String code;
    private List<Task> data;

    public Tasks(String code, List<Task> data) {
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Task> getData() {
        return data;
    }

    public void setData(List<Task> data) {
        this.data = data;
    }
}
