package com.linghangcloud.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.linghangcloud.android.UiAdapter.TaskAdapter;
import com.linghangcloud.android.UiComponent.MyListView;
import com.linghangcloud.android.db.Task;

import java.util.ArrayList;
import java.util.List;

import com.linghangcloud.android.GSON.TaskDetail;
import com.linghangcloud.android.TaskDetail.TaskDetailActivity;

public class HomeActivity extends AppCompatActivity {
private Button menu;
private MyListView item;
private DrawerLayout drawerLayout;
private Button release;
    private long exitTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        menu=findViewById(R.id.homemenu);
        item=findViewById(R.id.tasklist);
        release=findViewById(R.id.release);
        drawerLayout=findViewById(R.id.drawerlayout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        List<Task> list=new ArrayList<>();
        for (int i=0;i<30;i++)
        {
            list.add(new Task("任务"+i,"安卓","1111111111111111111四大发送到发四大发送到发发 撒旦法师手动阀手动阀asd速度手动阀阿斯顿发阿达东风发"," 11 ","2019-9-1","2019-9-2","1111"));
        }
        item.setAdapter(new TaskAdapter(list,HomeActivity.this));
        release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(HomeActivity.this,ReleaseActivity.class);
                startActivity(intent);
            }
        });
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
