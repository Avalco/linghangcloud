package com.linghangcloud.android.UiAdapter;

import android.content.Context;
import android.content.Intent;

import java.io.IOException;
import java.text.SimpleDateFormat;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.linghangcloud.android.GSON.ImageUrl;
import com.linghangcloud.android.HomeActivity;
import com.linghangcloud.android.R;
import com.linghangcloud.android.TaskDetail.TaskDetailActivity;
import com.linghangcloud.android.UiComponent.TaskItemLayout;
import com.linghangcloud.android.Util.Utility;
import com.linghangcloud.android.db.Task;
import com.linghangcloud.android.db.User;

import java.util.Date;
import java.util.List;

import androidx.annotation.RequiresApi;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class UsersAdapter extends BaseAdapter {
    private List<User> data;
    private Context mContext;
    private String token;

    public UsersAdapter(List<User> data, Context mContext, String token) {
        this.data = data;
        this.mContext = mContext;
        this.token = token;
    }

    private Handler myhandler = new Handler() {
        /**
         * Subclasses must implement this to receive messages.
         *
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    data.remove(msg.arg1);
                    TaskItemLayout taskItemLayout = (TaskItemLayout) msg.obj;
                    taskItemLayout.smoothCloseMenu();
                    notifyDataSetChanged();
                    Toast.makeText(mContext, "已删除", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    TaskItemLayout taskItemLayout1 = (TaskItemLayout) msg.obj;
                    taskItemLayout1.smoothCloseMenu();
                    Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (data != null) {
            return data.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(final int position, View contentView, ViewGroup parent) {
        ViewHolder holder;
        if (contentView == null) {
            holder = new ViewHolder();
            contentView = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
            holder.image = (ImageView) contentView.findViewById(R.id.image);
            holder.nickname = (TextView) contentView.findViewById(R.id.nickname);
            holder.count = (TextView) contentView.findViewById(R.id.account);
            holder.classn = (TextView) contentView.findViewById(R.id.classn);
            //  holder.toTop = (TextView) contentView.findViewById(R.id.to_top);
            holder.delete = (TextView) contentView.findViewById(R.id.delete);
            holder.modify = (TextView) contentView.findViewById(R.id.modify);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        User myModel = (User) getItem(position);
        holder.nickname.setText(myModel.getNickname());
        holder.classn.setText(myModel.getClassname());
        holder.count.setText(myModel.getAccount());
        if (!myModel.getImageurl().equals(""))Glide.with(mContext).load(myModel.getImageurl()).into(holder.image);
//        holder.content.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(mContext, TaskDetailActivity.class);
//                mContext.startActivity(intent);
//            }
//        });
        final TaskItemLayout finalContentView = (TaskItemLayout) contentView;
//        holder.toTop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, "已置顶", Toast.LENGTH_SHORT).show();
//                finalContentView.smoothCloseMenu();
//            }
//        });
        holder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "修改", Toast.LENGTH_SHORT).show();
                finalContentView.smoothCloseMenu();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RequestBody requestBody = new FormBody.Builder()
                                .add("account", data.get(position).getAccount())
                                .build();
                        Utility.PostHttp("http://fjxtest.club:9090/user/deleteuser", token, requestBody, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Message message = myhandler.obtainMessage();
                                message.what = 1;
                                message.obj = finalContentView;
                                myhandler.sendMessage(message);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responsetext = response.body().string();
                                ImageUrl imageUrl = Utility.HandImageurl(responsetext);
                                if (imageUrl != null && imageUrl.code != null) {
                                    switch (imageUrl.code) {
                                        case "20013":
                                            Message message = myhandler.obtainMessage();
                                            message.arg1 = position;
                                            message.obj = finalContentView;
                                            message.what = 2;
                                            myhandler.sendMessage(message);
                                            break;
                                        default:
                                            Message mmessage = myhandler.obtainMessage();
                                            mmessage.what = 1;
                                            mmessage.obj = finalContentView;
                                            myhandler.sendMessage(mmessage);
                                    }
                                } else {
                                    Message message = myhandler.obtainMessage();
                                    message.what = 1;
                                    message.obj = finalContentView;
                                    myhandler.sendMessage(message);
                                }
                            }
                        });
                    }
                }).start();
            }
        });
        return contentView;
    }
    private static class ViewHolder {
        ImageView image;
        TextView count;
        TextView nickname;
        TextView classn;
        TextView delete;
        TextView modify;
    }

}
