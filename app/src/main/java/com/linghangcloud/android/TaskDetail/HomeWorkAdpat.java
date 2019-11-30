package com.linghangcloud.android.TaskDetail;

import android.content.Context;
import android.provider.DocumentsContract;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeWorkAdpat extends RecyclerView.Adapter<HomeWorkAdpat.ViewHolder> {
    private List<HomeWork> homeWorkList = new ArrayList<>();
    private Context context;
    private EditText editText;
    private File file;
    private String name;
    public HomeWorkAdpat(List<HomeWork> homeWorkList, Context context,String name ,EditText editText) {
        this.homeWorkList = homeWorkList;
        this.context = context;
        this.file = new File(context.getExternalCacheDir()+"//apk");
        this.name=name;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homework, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("<------------>", "本次FileFragment页面长按操作完成");
                return false;
            }
        });
        return viewHolder;
    }

    public void finshHeadList() {
        homeWorkList.clear();
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final HomeWork homeWork = homeWorkList.get(position);
        holder.UserName.setText(homeWork.getUserName());
        holder.FileName.setText(homeWork.getFileName());
        Glide.with(context).load(homeWork.getFilePic()).into(holder.FilePic);
        holder.DownloadButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MyDownload.downLoadFromUrl(homeWork.getDownload(),"The "+name+" "+position+".zip",file.getPath()+File.separator+name);
                    Toast.makeText(context,"下载完成",Toast.LENGTH_SHORT).show();
                    Log.e("test", "下载成功" );
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test：","下载失败" );
                }
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
            FileName = itemView.findViewById(R.id.item_homework_filename);
            FilePic = itemView.findViewById(R.id.item_homework_file_pic);
            UserName = itemView.findViewById(R.id.item_homework_username);
            DownloadButtom = itemView.findViewById(R.id.file_download_button);

        }
    }
}
