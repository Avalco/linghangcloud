package com.linghangcloud.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.linghangcloud.android.GSON.Limit;
import com.linghangcloud.android.GSON.Tasks;
import com.linghangcloud.android.UiAdapter.TaskAdapter;
import com.linghangcloud.android.UiComponent.MyListView;
import com.linghangcloud.android.Util.Utility;
import com.linghangcloud.android.db.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.linghangcloud.android.GSON.TaskDetail;
import com.linghangcloud.android.TaskDetail.TaskDetailActivity;

public class HomeActivity extends AppCompatActivity {
private Button menu;
private MyListView item;
private DrawerLayout drawerLayout;
private Button release;
private Boolean Mlimit;
    private TextView title;
    private List<Task> list;

    private enum group {
        后台组(0), 前端组(1), 安卓组(2);
        private int value = 0;

        group(int value) {
            this.value = value;
        }

        public int getint() {
            return value;
        }
    }
    private long exitTime = 0;
    private static final String TAG = "HomeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://fjxtest.club:9090/user/limit?account=" + preferences.getString("count", "");
                Utility.SendHttp(url, preferences.getString("token", ""), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(HomeActivity.this, "无法连接服务器", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responsetext = null;
                        if (response.body() != null) responsetext = response.body().string();
                        Limit limit = Utility.HandLimit(responsetext);
                        Log.d(TAG, "onResponse: " + limit.getIsadmin());
                        if (limit != null) {
                            Mlimit=limit.getIsadmin().equals("true");
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).edit();
                            editor.putString("limit", limit.getIsadmin());
                            editor.apply();
                        }
                    }
                });
            }
        }).start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        item=findViewById(R.id.tasklist);
        menu=findViewById(R.id.homemenu);
        release=findViewById(R.id.release);
        drawerLayout=findViewById(R.id.drawerlayout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        if (!Mlimit)
        {
            release.setVisibility(View.GONE);
            item.setScrollable(false);
        }
        final group group = HomeActivity.group.valueOf(preferences.getString("group", ""));
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://fjxtest.club:9090/task/findalltask?group=" + group.getint();
                Utility.SendHttp(url, preferences.getString("token", ""), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(HomeActivity.this, "无法连接服务器", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responsetext = null;
                        if (response.body() != null) responsetext = response.body().string();
                        Tasks tasks = Utility.HandTasks(responsetext);
                        if (tasks != null && tasks.getCode() != null) {
                            switch (tasks.getCode()) {
                                case "30010":
                                    list = tasks.getData();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            item.setAdapter(new TaskAdapter(list, HomeActivity.this));
                                            item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    Intent intent = new Intent(HomeActivity.this, TaskDetailActivity.class);
                                                    intent.putExtra("taskid", list.get(position).getTaskid());
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    });
                            }
                        }
                    }
                });
            }
        }).start();

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(HomeActivity.this,ReleaseActivity.class);
                startActivity(intent);
            }
        });
        title = findViewById(R.id.title_home);
        title.setText("任务中心(" + preferences.getString("group", "") + ")");
        Log.d(TAG, "onCreate: " + preferences.getString("token", null));
    }
          @Override
           public boolean onKeyDown(int keyCode, KeyEvent event) {
            if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
                     if((System.currentTimeMillis()-exitTime) > 2000){
                             Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                            exitTime = System.currentTimeMillis();
                        } else {
                             finish();
                             System.exit(0);
                        }
                    return true;
                 }
             return super.onKeyDown(keyCode, event);
         }

}
