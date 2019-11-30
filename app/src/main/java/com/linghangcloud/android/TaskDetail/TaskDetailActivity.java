package com.linghangcloud.android.TaskDetail;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.EntityIterator;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import java.util.*;
import android.preference.PreferenceManager;
import android.text.Html;
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
import com.linghangcloud.android.GSON.Commit;
import com.linghangcloud.android.GSON.SendCommit;
import com.linghangcloud.android.GSON.TaskDetail;
import com.linghangcloud.android.R;
import com.linghangcloud.android.Util.Util;
import com.linghangcloud.android.Util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private Context context;
    private TextView punishgroup;
    private Button backbutton;
    private Button upButton;
    private TextView inputButton;
    private LinearLayout TestLayout;
    private LinearLayout MainDetailLayout;
    private NestedScrollView scrollView;
    private ProgressDialog progressDialog;
    private LinearLayout CommitTitle;
    private String UrlOfTask = "http://fjxtest.club:9090/task/findtask?taskid=";
    private String taskid = "38";
    private String account="android";
    private String[] groupName = {"后台组大神", "前端组大神", "安卓组大神"};

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        final Context context = getBaseContext();
        taskid = getIntent().getStringExtra("taskid");
        input = findViewById(R.id.input);
        title = findViewById(R.id.task_title);
        detail = findViewById(R.id.task_detail);
        punishName = findViewById(R.id.punnish_name);
        punishgroup = findViewById(R.id.punnish_group);
        inputButton = findViewById(R.id.task_input_button);
        backbutton = findViewById(R.id.task_back_button);
        menubutton = findViewById(R.id.task_menu_button);
        menudrawer = findViewById(R.id.task_menu_drawer);
        upButton = findViewById(R.id.task_upperButton);
        CommitTitle = findViewById(R.id.task_ThirldLinearLayout);
//        下方为吸顶的界面
        TestLayout = findViewById(R.id.task_test1);
//        下方为测量长度界面
        MainDetailLayout = findViewById(R.id.task_MainDetailLayout);
        scrollView = findViewById(R.id.task_scroll);
//        返回按钮功能声明
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        循环界面配置
        commitre = findViewById(R.id.task_commit_re);
        InitTestIndex();

        scrollView.fullScroll(View.FOCUS_UP);
//        返回顶部界面
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(0, 0);
            }
        });
//      打开抽屉
        menubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menudrawer.openDrawer(GravityCompat.END);
            }
        });
        if (TestLayout.getVisibility()==View.GONE){
            Log.e("test", "yes");
        }
//        输入按钮点击事件
        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(context);
                final String token=prefs.getString("token",null);
                String reply="http://fjxtest.club:9090/wordpress/reply";
                String send="http://fjxtest.club:9090/wordpress/postcomment";
                if (input.getText().toString().equals("")) {
                    Toast.makeText(context, "没词儿了吧，小伙", Toast.LENGTH_SHORT).show();
                } else {
//                    showProgressDialog();
                    String aite[];
                    String commiters[] = new String[20];
                    boolean isNull = false;
                    int j = 0;
                    StringBuilder contentoftalk = new StringBuilder("");
                    aite = input.getText().toString().split(" ");
                    for (String x : aite) {
                        if (x.startsWith("@")) {
                            commiters[j++] = x.substring(1);
                        } else {
                            contentoftalk.append(x);
                        }
                    }
                    Log.e("test input ：", new String(contentoftalk));
                    if (j == 0) {
                        j++;
                        isNull = true;
                    }
                    for (int i = 0; i < j; i++) {
                        Util util =new Util();
                      SendCommit commit = new SendCommit();
                        commit.setAccount(account);
                        commit.setDetail(new String(contentoftalk));
                        commit.setTaskid(taskid);
                        if (isNull) {
                            commiters[0] = " ";
                            isNull = false;
                            commit.setCommentid("null");
                            Util.posthttp("access_token", token, send, commit, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String reponse=response.body().string();
                                    Utility.handleReplyResponse(reponse);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context,"走你",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });

                            break;
                        }
                        commit.setCommentid(commiters[i]);
                        Util.posthttp("access_token", token, reply, commit, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String reponse=response.body().string();
                                Utility.handleReplyResponse(reponse);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context,"走你",Toast.LENGTH_SHORT).show();
                                        InitTestIndex();
                                    }
                                });
                            }
                        });
                    }

                    scrollView.fullScroll(View.FOCUS_DOWN);
//                    closeProgressDialog();
                    input.setText("");
                    Toast.makeText(context, "走你", Toast.LENGTH_SHORT).show();
                }

            }
        });

//        吸顶操作
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > MainDetailLayout.getHeight()) {
                    TestLayout.setVisibility(View.VISIBLE);
                    CommitTitle.setVisibility(View.GONE);
                } else {
                    if (CommitTitle.getVisibility() == View.GONE) {
                        CommitTitle.setVisibility(View.VISIBLE);
                    }
                    if (TestLayout.getVisibility() == View.VISIBLE) {
                        TestLayout.setVisibility(View.GONE);
                    }
                }
            }
        });
//        吸顶结束
        InitTaskDetail();

    }

    @Override
    protected void onStart() {
        super.onStart();
        TestLayout.setVisibility(View.GONE);
        scrollView.scrollTo(0,0);
    }

    private void InitTaskDetail() {
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
        String url = UrlOfTask + taskid;
        final String token=prefs.getString("token",null);
        final String headname="access_token";
        try {
            new Util().getwithokhttphead(url,token,headname,new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(final Call call, Response response) throws IOException {
                    String responseBody = response.body().string();

                    Log.e("服务器数据 TaskDetail：", responseBody);
                    Log.e("服务器数据 TOKEN：", token);
                    final TaskDetail taskDetail = Utility.handleTaskDetailResponse(responseBody);
                    Log.e("JSON ：test：", taskDetail.getHeadline());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            title.setText(taskDetail.getHeadline());
                            detail.setText(Html.fromHtml(taskDetail.getDetails()));
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String token=prefs.getString("token","null");
        String headname="access_token";
        String url="http://fjxtest.club:9090/wordpress/getcomment?taskid="+taskid;
        Log.e("test ：评论", token);
        final List<com.linghangcloud.android.GSON.Commit> commitList = new ArrayList<>();
        Util util =new Util() ;
        try {
            util.getwithokhttphead(url, token, headname, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.e("test ：评论", "评论获取失败" );
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String respon = response.body().string();
                    Log.e("test ：评论", respon );
                    JSONObject jsonObject= null;
                    try {
                        jsonObject = new JSONObject(respon);
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++){
                            commitList.add(Utility.heandleCommitResponse(jsonArray.get(i).toString()));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("test ：评论","size = "+commitList.size());
                                sortCommit(commitList);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                                commitre.setLayoutManager(layoutManager);
                                CommitAdpat adpat = new CommitAdpat(commitList,getBaseContext(), input);
                                commitre.setAdapter(adpat);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("test ：评论", "评论解析失败" );
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test ：评论", "网路" );
        }

    }

    //关闭加载动画
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    //打开加载动画
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getBaseContext());
            progressDialog.setMessage("网络拼命加载中");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void sortCommit(List<Commit> commits){
        List<comunity> comunities=new ArrayList<>();
        List<Commit> others=new ArrayList<>();
        Iterator iterator=commits.iterator();
        while(iterator.hasNext()){
            Commit commit=(Commit)iterator.next();
            if(commit.getParent()==-1){
                List<Commit> commit1=new ArrayList();
                commit1.add(commit);
                comunities.add(new comunity(commit1));
            }else{
                others.add(commit);
            }
        }
        for(int j=0;j<others.size();j++){
            Commit commit=others.get(j);
            boolean flag =false;
            for(int k=0;k<comunities.size();k++){
               if(flag) break;
                List<Commit> commitList=comunities.get(k).getList();
                for(int f=0;f<commitList.size();f++){
                    if(commitList.get(f).getCommentid()==commit.getParent())
                    {
                        comunities.get(k).getList().add(f+1,commit);
                        flag=true;
                        break;
                    }

                }
            }
        }
        commits.clear();
        for (int i=0;i<comunities.size();i++){
            List<Commit> commitList=comunities.get(i).getList();
            for(int j=0;j<commitList.size();j++){
                commits.add(commitList.get(j));
                Log.e("test：sort ", ""+commitList.get(j).getCommentid()+" xx"+commitList.get(j).getParent());
            }
        }
    }
}
