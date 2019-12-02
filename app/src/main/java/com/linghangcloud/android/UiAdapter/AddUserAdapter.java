package com.linghangcloud.android.UiAdapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linghangcloud.android.R;
import com.linghangcloud.android.UiComponent.TaskItemLayout;
import com.linghangcloud.android.db.User;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;

public class AddUserAdapter extends BaseAdapter {
    private int n;
    private List<EditText> data;
    private Context mContext;

    public AddUserAdapter(int n, Context mContext) {
        this.n = n;
        this.mContext = mContext;
        data = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return n + 1;
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
        AddUserAdapter.ViewHolder holder;
        if (position < n) {
            holder = new AddUserAdapter.ViewHolder();
            contentView = LayoutInflater.from(mContext).inflate(R.layout.adduser_item, parent, false);
            holder.user = (EditText) contentView.findViewById(R.id.user_count);
            data.add(holder.user);
            contentView.setTag(holder);
        } else {
            holder = new AddUserAdapter.ViewHolder();
            contentView = LayoutInflater.from(mContext).inflate(R.layout.add, parent, false);
            holder.imageView = contentView.findViewById(R.id.ima_add);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    n++;
                    notifyDataSetChanged();
                }
            });
        }
        return contentView;
    }

    private static class ViewHolder {
        EditText user;
        ImageView imageView;
    }

    public List<EditText> getData() {
        return data;
    }
}
