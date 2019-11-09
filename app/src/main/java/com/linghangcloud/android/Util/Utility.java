package com.linghangcloud.android.Util;

import com.google.gson.Gson;
import com.linghangcloud.android.GSON.Token;

import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    public static Token Handlogin(String response) {
        return new Gson().fromJson(response, Token.class);
    }
}
