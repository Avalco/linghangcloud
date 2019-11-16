package com.linghangcloud.android.GSON;

import com.google.gson.annotations.SerializedName;

public class Token {
    private String code;

    @SerializedName("data")
    private String token;

    public Token(String code, String token) {
        this.code = code;
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
