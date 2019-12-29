package com.linghangcloud.android.UiAdapter;

import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;

import android.os.Build;
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
import com.linghangcloud.android.HomeActivity;
import com.linghangcloud.android.R;
import com.linghangcloud.android.TaskDetail.TaskDetailActivity;
import com.linghangcloud.android.UiComponent.TaskItemLayout;
import com.linghangcloud.android.db.Task;
import com.linghangcloud.android.db.User;

import java.util.Date;
import java.util.List;

import androidx.annotation.RequiresApi;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class UsersAdapter extends BaseAdapter {
    private List<User> data;
    private Context mContext;

    public UsersAdapter(List<User> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
    }

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
                data.remove(position);
                finalContentView.smoothCloseMenu();
                notifyDataSetChanged();
                Toast.makeText(mContext, "已删除", Toast.LENGTH_SHORT).show();
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
