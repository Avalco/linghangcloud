package com.linghangcloud.android.TaskDetail;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linghangcloud.android.GSON.Commit;
import com.linghangcloud.android.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommitAdpat extends RecyclerView.Adapter<CommitAdpat.ViewHold> {
    private List<com.linghangcloud.android.GSON.Commit> commitList = new ArrayList<>();
    private Context context;
    private EditText contont;

    public CommitAdpat(List<com.linghangcloud.android.GSON.Commit> commitList, Context context, EditText editText) {
        this.commitList = commitList;
        this.context = context;
        contont = editText;
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commit, parent, false);
        final ViewHold viewHold = new ViewHold(view);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                StringBuilder builder = new StringBuilder(" @" + viewHold.id + " ");
                String contentoftext = contont.getText().toString();
                builder.append(contentoftext);
                contont.setText(new String(builder));
//                contont.append(" @"+viewHold.uaername.getText()+" ");
//                contont.setText("@"+viewHold.uaername.getText()+" ");
                return false;
            }
        });
        return viewHold;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {
        Commit commit = commitList.get(position);
        holder.id=commit.getCommentid();
        holder.detail.setText(commit.getDetails());
        holder.rename.setText(commit.getNickname());
        Glide.with(context).load(commit.getImgurl()).into(holder.circleImageView);
        holder.uaername.setText(commit.getNickname());
        holder.IsVisbale(commit.getParent()==-1, position + 1 < commitList.size() && !(commitList.get(position + 1).getParent()==-1));
        if (commit.getParent()==-1) {
            holder.to.setVisibility(View.GONE);

        }
        holder.to.setVisibility(View.GONE);
        //上面的语句无法
        holder.rename.setVisibility(View.GONE);
        holder.delete.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return commitList.size();
    }

    public void finshHeadList() {
        commitList.clear();
        notifyDataSetChanged();
    }


    public class ViewHold extends RecyclerView.ViewHolder {
        private CircleImageView circleImageView;
        private TextView uaername;
        private TextView rename;
        private TextView detail;
        private Button delete;
        private ImageView to;
        private LinearLayout headlayout;
        private RelativeLayout leftlayout;
        private int id;
        public ViewHold(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.commit_pic);
            uaername = itemView.findViewById(R.id.commit_user);
            rename = itemView.findViewById(R.id.commit_reuser);
            detail = itemView.findViewById(R.id.commit_detail);
            delete = itemView.findViewById(R.id.commit_delete);
            to = itemView.findViewById(R.id.commit_to);
            delete = itemView.findViewById(R.id.commit_delete);
            headlayout = itemView.findViewById(R.id.commit_headline);
            leftlayout = itemView.findViewById(R.id.commit_childline);
        }

        public void IsVisbale(boolean ischild, boolean thenext) {
            if (!ischild) {
//                if (!thenext){
//                    Log.e("test commit:", detail.getText().toString() );
//
//                }else {
//                    headlayout.setVisibility(View.GONE);
//                }
            } else {
                leftlayout.setVisibility(View.GONE);
            }
        }
    }
}
