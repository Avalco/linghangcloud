package com.linghangcloud.android.TaskDetail;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.linghangcloud.android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeWorkAdpat extends RecyclerView.Adapter<HomeWorkAdpat.ViewHolder> {
    private List<HomeWork> homeWorkList=new ArrayList<>();
    private Context context;
    private EditText editText;

    public HomeWorkAdpat(List<HomeWork> homeWorkList, Context context, EditText editText) {
        this.homeWorkList = homeWorkList;
                                                                                                                                                                                                        this.context = context;
                                                                                                                                                                                                    }

                                                                                                                                                                                    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homework,parent,false);
        final ViewHolder viewHolder =new ViewHolder(view);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("<------------>", "本次FileFragment页面长按操作完成" );
                return false;
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeWork homeWork = homeWorkList.get(position);
        holder.UserName.setText(homeWork.getUserName());
        holder.FileName.setText(homeWork.getFileName());
        Glide.with(context).load(homeWork.getFilePic()).into(holder.FilePic);
        holder.DownloadButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"暂时不提供下载服务",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return homeWorkList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView FilePic;
        private TextView FileName;
        private TextView UserName;
        private TextView DownloadButtom;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            FileName=itemView.findViewById(R.id.item_homework_filename);
            FilePic=itemView.findViewById(R.id.item_homework_file_pic);
            UserName=itemView.findViewById(R.id.item_homework_username);
            DownloadButtom =itemView.findViewById(R.id.file_download_button);
        }
    }
}
