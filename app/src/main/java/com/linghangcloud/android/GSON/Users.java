package com.linghangcloud.android.GSON;

import com.linghangcloud.android.db.User;

import java.util.List;

public class Users {
    private List<User> data;
   private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Users(List<User> data, String code) {
        this.data = data;
        this.code=code;
    }

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }
}
