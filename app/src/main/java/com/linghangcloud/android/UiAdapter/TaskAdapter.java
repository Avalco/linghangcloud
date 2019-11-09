package com.linghangcloud.android.UiAdapter;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;

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
        holder.content.setText(myModel.getContent());
        holder.createtime.setText(myModel.getCreatetime()+"发布");
        holder.deadline.setText(myModel.getDeadline()+"截止");
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
