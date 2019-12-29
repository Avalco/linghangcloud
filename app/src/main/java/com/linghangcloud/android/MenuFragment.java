package com.linghangcloud.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.linghangcloud.android.GSON.Limit;
import com.linghangcloud.android.GSON.UserInfo;
import com.linghangcloud.android.GSON.Users;
import com.linghangcloud.android.UiAdapter.UsersAdapter;
import com.linghangcloud.android.UiComponent.AddUserDialog;
import com.linghangcloud.android.UiComponent.MyListView;
import com.linghangcloud.android.Util.Utility;
import com.linghangcloud.android.db.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class MenuFragment extends Fragment{
    private MyListView listView;
    private LinearLayout exit;
    private LinearLayout adduser;
    private EditText nickname;
    private EditText acount;
    private EditText groupn;
    private CircleImageView circleImageView;
    private CircleImageView imageView;
    private ImageView imamodi;
    private EditText classn;
    private RelativeLayout admin;
    private CheckBox vis;
    private LinearLayout modifyinfo;
    private boolean hadmodify;
    private String token;
    private String count;
    private String classname;
    private String nickna;
    private String imageurl;
    private String group;
    private boolean limit;
    private String content;
    private SharedPreferences preferences;
    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     *
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_menu,container,false);
        hadmodify = false;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initView(view);
        SetListener();
        return view;
    }
    private void Refresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://fjxtest.club:9090/user/showinfo?account=" + count;
                Utility.SendHttp(url, token, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "无法连接服务器", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responsetext = null;
                        if (response.body() != null)
                            responsetext = response.body().string();
                        UserInfo userInfo = Utility.HandUserInfo(responsetext);
                        if (userInfo != null && userInfo.getCode() != null) {
                            switch (userInfo.getCode()) {
                                case "20009":
                                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                                    editor.putString("group", userInfo.getData().getGroup());
                                    editor.putString("count", userInfo.getData().getAccount());
                                    editor.putString("imageurl", userInfo.getData().getImageurl());
                                    editor.putString("nickname", userInfo.getData().getNickname());
                                    editor.putString("classname", userInfo.getData().getClassname());
                                    editor.apply();
                                    break;
                                case "20010":
                                    content = "获取信息失败";
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                            }
                        }

                    }
                });
            }
        }).start();
    }
    private void SetListener() {
        modifyinfo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (!hadmodify) {
                    hadmodify = true;
                    nickname.setFocusable(true);
                    nickname.requestFocus();
                    nickname.setFocusableInTouchMode(true);
                    nickname.setEnabled(true);
                    nickname.setBackgroundResource(R.drawable.back_edit);
                    classn.setEnabled(true);
                    classn.setFocusable(true);
                    classn.setFocusableInTouchMode(true);
                    classn.setBackgroundResource(R.drawable.back_edit);
                    imamodi.setImageResource(R.drawable.ic_done_black_24dp);
                    circleImageView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    circleImageView.setVisibility(View.GONE);
                    hadmodify = false;
                    nickname.setFocusable(false);
                    nickname.setEnabled(false);
                    nickname.setBackground(null);
                    classn.setEnabled(false);
                    classn.setFocusable(false);
                    classn.setBackground(null);
                    imamodi.setImageResource(R.drawable.modify);
                }
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                editor.putString("count", "");
                editor.putString("password", "");
                editor.apply();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        vis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) listView.setVisibility(View.VISIBLE);
                else listView.setVisibility(View.GONE);
            }
        });
        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AddUserDialog dialog = new AddUserDialog(getContext());
                dialog.
                        setOnClickBottomListener(new AddUserDialog.OnClickBottomListener() {
                            @Override
                            public void onPositiveClick() {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "xxxx", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onNegtiveClick() {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }
    private void initView(View view) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        token = preferences.getString("token", "");
        count = preferences.getString("count", "");
        group = preferences.getString("group", "");
        nickna = preferences.getString("nickname", "");
        imageurl = preferences.getString("imageurl", "");
        classname = preferences.getString("classname", "");
        limit = preferences.getString("limit", "").equals("true");
        Log.d(TAG, "onCreateView: classname" + classname);
        Log.d(TAG, "onCreateView: imageurl" + imageurl);
        Log.d(TAG, "onCreateView: limit " + limit);
        listView = view.findViewById(R.id.list_users);
        listView.setVisibility(View.GONE);
        vis = view.findViewById(R.id.listvis);
        modifyinfo = view.findViewById(R.id.modifyinfo);
        nickname = view.findViewById(R.id.editnickname);
        nickname.setEnabled(false);
        classn = view.findViewById(R.id.editclass);
        classn.setEnabled(false);
        imamodi = view.findViewById(R.id.modifyima);
        circleImageView = view.findViewById(R.id.addimage);
        circleImageView.setVisibility(View.GONE);
        imageView = view.findViewById(R.id.image);
        adduser = view.findViewById(R.id.adduser);
        nickname.setText(nickna);
        classn.setText(classname);
        groupn = view.findViewById(R.id.group);
        groupn.setText(group);
        acount = view.findViewById(R.id.acount);
        acount.setText(count);
        admin = view.findViewById(R.id.admin);
        if (!limit) {
            admin.setVisibility(View.GONE);
            adduser.setVisibility(View.GONE);
        }
        if (!imageurl.equals("")) Glide.with(getContext()).load(imageurl).into(imageView);
        if (limit) {
            GetUsers();
        }
        exit = view.findViewById(R.id.exit);
    }

    private void GetUsers() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://fjxtest.club:9090/user/findallmember";
                Utility.SendHttp(url, preferences.getString("token", ""), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "无法连接服务器", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responsetext = null;
                        if (response.body() != null) responsetext = response.body().string();
                        Users users = Utility.HandUsers(responsetext);
                        if (users != null&&users.getCode()!=null) {
                            switch (users.getCode())
                            {
                                case "20011":
                                    final List<User> userList =users.getData();
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            listView.setAdapter(new UsersAdapter(userList, getContext()));
                                        }
                                    });
                                    break;
                            }
                        }
                    }
                });
            }
        }).start();
    }
}
