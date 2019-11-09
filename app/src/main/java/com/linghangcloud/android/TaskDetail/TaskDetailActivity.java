package com.linghangcloud.android.TaskDetail;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.linghangcloud.android.GSON.TaskDetail;
import com.linghangcloud.android.R;
import com.linghangcloud.android.Util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskDetailActivity extends AppCompatActivity {
//    属性声明界面
    private Button menubutton;
    private DrawerLayout menudrawer;
    private RecyclerView commitre;
    private EditText input;
    private TextView title;
    private TextView detail;
    private TextView punishName;
    private TextView punishgroup;
    private Button backbutton;
    private Button upButton;
    private TextView inputButton;
    private LinearLayout TestLayout;
    private LinearLayout MainDetailLayout;
    private ScrollView scrollView;
    private ProgressDialog progressDialog;
    private LinearLayout CommitTitle ;
    private List<Commit> commitList=new ArrayList<>();
    private String UrlOfTask="http://fjxtest.club:9090/task/findtask?taskid=";
    private String taskid="34";
    private String[] groupName={"后台组大神","前端组大神","安卓组大神"};
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        final Context context=getBaseContext();
        input=findViewById(R.id.input);
        title=findViewById(R.id.task_title);
        detail=findViewById(R.id.task_detail);
        punishName=findViewById(R.id.punnish_name);
        punishgroup=findViewById(R.id.punnish_group);
        inputButton=findViewById(R.id.task_input_button);
        backbutton=findViewById(R.id.task_back_button);
        menubutton=findViewById(R.id.task_menu_button);
        menudrawer=findViewById(R.id.task_menu_drawer);
        upButton=findViewById(R.id.task_upperButton);
        CommitTitle=findViewById(R.id.task_ThirldLinearLayout);
//        下方为吸顶的界面
        TestLayout=findViewById(R.id.task_test1);
//        下方为测量长度界面
        MainDetailLayout=findViewById(R.id.task_MainDetailLayout);

        scrollView=findViewById(R.id.task_scroll);
        TestLayout.setVisibility(View.GONE);

//        返回按钮功能声明
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        循环界面配置
        commitre=findViewById(R.id.task_commit_re);
        InitTestIndex();
        RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(this);
        commitre.setLayoutManager(layoutManager);
        CommitAdpat adpat =new CommitAdpat(commitList,context,input);
        commitre.setAdapter(adpat);

//        返回顶部界面
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(0,0);
            }
        });
//      打开抽屉
        menubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menudrawer.openDrawer(GravityCompat.END);
            }
        });
//        输入按钮点击事件
        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input.getText().toString().equals( "")){
                    Toast.makeText(context,"没词儿了吧，小伙",Toast.LENGTH_SHORT).show();
                }else {
//                    showProgressDialog();
                    String aite[];
                    String commiters[]=new String[20];
                    boolean isNull=false;
                    int j=0;
                    StringBuilder contentoftalk=new StringBuilder("");
                    aite=input.getText().toString().split(" ");
                    for (String x:aite) {
                        if (x.startsWith("@")){
                            commiters[j++]=x.substring(1).trim();
                        }else {
                            contentoftalk.append(x);
                        }
                    }
                    Log.e("test input ：", new String(contentoftalk));
                    if (j==0){ j++; isNull=true;}
                    for (int i=0;i<j;i++){
                        Commit commit=new Commit();
                        commit.setPic(R.drawable.testpicsecond);
                        if (isNull){
                            commiters[0]=" ";
                            isNull=false;
                        }
                        commit.setReuser(commiters[i]);
                        commit.setCommituser("领航太上皇");

                        commit.setDetail(new String(contentoftalk));
                        commitList.add(commit);
                    }
                    scrollView.fullScroll(View.FOCUS_DOWN);
//                    closeProgressDialog();
                    input.setText("");
                    Toast.makeText(context,"走你",Toast.LENGTH_SHORT).show();
                }

            }
        });

//        吸顶操作
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > MainDetailLayout.getHeight()){
                    TestLayout.setVisibility(View.VISIBLE);
                    CommitTitle.setVisibility(View.GONE);
                }else {
                    if (CommitTitle.getVisibility()==View.GONE)
                    {
                        CommitTitle.setVisibility(View.VISIBLE);
                    }
                    if (TestLayout.getVisibility()==View.VISIBLE){
                        TestLayout.setVisibility(View.GONE);
                    }
                }
            }
        });
//        吸顶结束
        InitTaskDetail();

    }

    private void InitTaskDetail() {
        String url = UrlOfTask+taskid;
        try {
            new Util().getwithokhttp(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(final Call call, Response response) throws IOException {
                    String  responseBody=response.body().string();
                    Gson gson=new Gson();
                    Log.e("服务器数据 TaskDetail：", responseBody);
                    final TaskDetail taskDetail=gson.fromJson(responseBody,new TypeToken<TaskDetail>(){}.getType());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            title.setText(taskDetail.getHeadline());
                            detail.setText(taskDetail.getDetails());
                            punishName.setText(taskDetail.getNickname());
                            punishgroup.setText(groupName[taskDetail.getGroup()]);
                        }
                    });

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void InitTestIndex() {
        for (int i=0;i<5;i++){
            Commit commit =new Commit();
            commit.setCommituser("HansomeCoder");
            commit.setDetail("这个文件真是太赞了");
            commit.setReuser(" ");
            commit.setPic(R.drawable.testpic);
            commitList.add(commit);
            if (i%3==0){
                Commit commitx =new Commit();
                commitx.setCommituser("SkyCoder");
                commitx.setDetail("+1");
                commitx.setReuser("HansomeCoder");
                commitx.setPic(R.drawable.testpicsecond);
                commitList.add(commitx);

            }

        }
    }

    //关闭加载动画
    private void closeProgressDialog() {
        if (progressDialog!=null) {
            progressDialog.dismiss();
        }
    }
    //打开加载动画
    private void showProgressDialog() {
        if (progressDialog==null){
            progressDialog = new ProgressDialog(getBaseContext());
            progressDialog.setMessage("网络拼命加载中");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
}
