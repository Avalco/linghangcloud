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
import android.widget.TextView;
import android.widget.Toast;

import com.linghangcloud.android.HomeActivity;
import com.linghangcloud.android.R;
import com.linghangcloud.android.TaskDetail.TaskDetailActivity;
import com.linghangcloud.android.UiComponent.TaskItemLayout;
import com.linghangcloud.android.db.Task;

import java.util.Date;
import java.util.List;

import androidx.annotation.RequiresApi;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class TaskAdapter extends BaseAdapter {
    private List<Task> data;
    private Context mContext;

    public TaskAdapter(List<Task> data, Context mContext) {
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
            contentView = LayoutInflater.from(mContext).inflate(R.layout.task_item, parent, false);
            holder.taskhead = (TextView) contentView.findViewById(R.id.task_head);
            holder.content = (TextView) contentView.findViewById(R.id.taskcontent);
            holder.createtime=(TextView)contentView.findViewById(R.id.createtime);
            holder.deadline=(TextView)contentView.findViewById(R.id.deadline);
            //  holder.toTop = (TextView) contentView.findViewById(R.id.to_top);
            holder.delete = (TextView) contentView.findViewById(R.id.delete);
            holder.modify=(TextView)contentView.findViewById(R.id.modify);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        Task myModel = (Task) getItem(position);
        holder.taskhead.setText(myModel.getHeadline());
        holder.content.setText(Html.fromHtml(myModel.getContent()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        long time = Long.parseLong(myModel.getCreatetime());
        Date date = new Date(time);
        Log.d(TAG, "getView: " + simpleDateFormat.format(date));
        holder.createtime.setText(simpleDateFormat.format(date) + "发布");
        time = Long.parseLong(myModel.getDeadline());
        date = new Date(time);
        holder.deadline.setText(simpleDateFormat.format(date) + "截止");
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
        TextView taskhead;
        TextView createtime;
        TextView deadline;
        TextView content;
       // TextView toTop;
        TextView delete;
        TextView modify;
    }

}
