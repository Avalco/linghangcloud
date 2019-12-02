package com.linghangcloud.android;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.linghangcloud.android.GSON.Token;
import com.linghangcloud.android.GSON.UserInfo;
import com.linghangcloud.android.Util.NetWorkUtil;
import com.linghangcloud.android.Util.Utility;

import java.io.IOException;
import java.net.NetPermission;
//登录界面
public class LoginActivity extends AppCompatActivity {
    private Button login;
    private EditText editcount;
    private EditText editpassword;
    private String count;
    private ProgressDialog progressDialog;
    private String password;
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        login = findViewById(R.id.login);
        editcount = findViewById(R.id.edit_count);
        editpassword = findViewById(R.id.edit_password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = editcount.getText().toString();
                password = editpassword.getText().toString();
                if (!count.equals("") && !password.equals("")) {
                    if (NetWorkUtil.isNetworkAvailable(LoginActivity.this)) {
                        showProgressDialog();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Callback callback = new Callback() {
                                    private String content = "err1";
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                closeProgressDialog();
                                                Toast.makeText(LoginActivity.this, "无法连接服务器", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String responsetext = null;
                                        if (response.body() != null) {
                                            responsetext = response.body().string();
                                        }
                                        final Token token = Utility.Handlogin(responsetext);
                                        if (token != null && token.getCode() != null) {
                                            Log.d(TAG, "onResponse: " + token.getToken());
                                            switch (token.getCode()) {
                                                case "20001":
                                                    content = "登录成功";
                                                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                                                    editor.putString("token", token.getToken());
                                                    editor.apply();
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            String url = "http://fjxtest.club:9090/user/showinfo?account=" + count;
                                                            Utility.SendHttp(url, token.getToken(), new Callback() {
                                                                @Override
                                                                public void onFailure(Call call, IOException e) {
                                                                    runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            closeProgressDialog();
                                                                            Toast.makeText(LoginActivity.this, "无法连接服务器", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                }

                                                                @Override
                                                                public void onResponse(Call call, Response response) throws IOException {
                                                                    String responsetext = null;
                                                                    if (response.body() != null)
                                                                        responsetext = response.body().string();
                                                                    UserInfo userInfo = Utility.HandUserInfo(responsetext);
                                                                    if (userInfo != null && userInfo.getCode() != null) {
                                                                        switch (userInfo.getCode()) {
                                                                            case "20009":
                                                                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                                                                                editor.putString("group", userInfo.getData().getGroup());
                                                                                editor.putString("count", userInfo.getData().getAccount());
                                                                                editor.putString("password", password);
                                                                                editor.putString("imageurl", userInfo.getData().getImageurl());
                                                                                editor.putString("nickname", userInfo.getData().getNickname());
                                                                                editor.putString("classname", userInfo.getData().getClassname());
                                                                                editor.apply();
                                                                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                                                closeProgressDialog();
                                                                                startActivity(intent);
                                                                                finish();
                                                                                break;
                                                                            case "20010":
                                                                                content = "登录失败";
                                                                                break;
                                                                        }
                                                                    }
                                                                    runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            closeProgressDialog();
                                                                            Toast.makeText(LoginActivity.this, content, Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        }
                                                    }).start();
                                                    break;
                                                case "20002":
                                                    content = "用户不存在";
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            closeProgressDialog();
                                                            Toast.makeText(LoginActivity.this, content, Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    break;
                                                case "20003":
                                                    content = "密码错误";
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            closeProgressDialog();
                                                            Toast.makeText(LoginActivity.this, content, Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    break;
                                            }
                                        }
                                    }
                                };
                                String url = "http://fjxtest.club:9090/user/login";
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("account", count)
                                        .add("psd", password)
                                        .build();
                                OkHttpClient okHttpClient = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url(url)
                                        .post(requestBody)
                                        .build();
                                okHttpClient.newCall(request).enqueue(callback);
                            }
                        }).start();
                    } else Toast.makeText(LoginActivity.this, "无法连接网络", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(LoginActivity.this, "账号和密码不能为空", Toast.LENGTH_SHORT).show();
            }
        });
        if (!preferences.getString("count", "").equals("") && !preferences.getString("password", "").equals("")) {
            editcount.setText(preferences.getString("count", ""));
            editpassword.setText(preferences.getString("password", ""));
            count = preferences.getString("count", "");
            password = preferences.getString("password", "");
            login.performClick();
        }
    }
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("登录中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog() {
        if (progressDialog != null) progressDialog.dismiss();
    }
}
