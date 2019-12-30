package com.linghangcloud.android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;
import com.linghangcloud.android.GSON.ImageUrl;
import com.linghangcloud.android.GSON.Limit;
import com.linghangcloud.android.GSON.UserInfo;
import com.linghangcloud.android.GSON.Users;
import com.linghangcloud.android.UiAdapter.UsersAdapter;
import com.linghangcloud.android.UiComponent.AddUserDialog;
import com.linghangcloud.android.UiComponent.MyListView;
import com.linghangcloud.android.Util.Utility;
import com.linghangcloud.android.db.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    private String imagepath;
    private final static int FAIL_UPDATE_IMG = 2000;
    private final static int FAIL_UPDATE_INFO = 2001;
    private final static int SUCCESS_UPDATE_INFO = 2002;
    ImagePicker imagePicker;
    private SharedPreferences preferences;
    private Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FAIL_UPDATE_IMG:
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "头像上传失败", Toast.LENGTH_SHORT).show();
                    });
                    initui();
                    break;
                case FAIL_UPDATE_INFO:
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "个人信息修改失败", Toast.LENGTH_SHORT).show();
                    });
                    initui();
                    break;
                case SUCCESS_UPDATE_INFO:
                    initui();
                    Refresh();
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initui() {
        getActivity().runOnUiThread(() -> {
            imagepath = null;
            imageView.setVisibility(View.VISIBLE);
            circleImageView.setVisibility(View.GONE);
            hadmodify = false;
            nickname.setFocusable(false);
            nickname.setEnabled(false);
            nickname.setBackground(null);
            classn.setEnabled(false);
            classn.setFocusable(false);
            classn.setBackground(null);
            classn.setText(classname);
            nickname.setText(nickna);
            imamodi.setImageResource(R.drawable.modify);
        });
    }
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
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_menu,container,false);
        hadmodify = false;
        imagepath = null;
        imagePicker = new ImagePicker();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initView(view);
        SetListener();
        return view;
    }

    private void refreshui() {
        token = preferences.getString("token", "");
        count = preferences.getString("count", "");
        group = preferences.getString("group", "");
        nickna = preferences.getString("nickname", "");
        imageurl = preferences.getString("imageurl", "");
        classname = preferences.getString("classname", "");
        limit = preferences.getString("limit", "").equals("true");
        listView.setVisibility(View.GONE);
        nickname.setEnabled(false);
        classn.setEnabled(false);
        circleImageView.setVisibility(View.GONE);
        nickname.setText(nickna);
        classn.setText(classname);
        groupn.setText(group);
        acount.setText(count);
        if (!limit) {
            admin.setVisibility(View.GONE);
            adduser.setVisibility(View.GONE);
        }
        if (!imageurl.equals("")) Glide.with(getContext()).load(imageurl).into(imageView);
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
                                    editor.putString("classname", userInfo.getData().getClassName());
                                    editor.apply();
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            refreshui();
                                        }
                                    });
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void SetListener() {
        //修改个人信息
        circleImageView.setOnClickListener(v -> startChooser());
        modifyinfo.setOnClickListener(v -> {
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
                circleImageView.setImageResource(R.drawable.ic_add_circle_outline_black_24dp);
                circleImageView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
            } else {
                if (!nickna.equals(nickname.getText().toString()) || !classname.equals(classn.getText().toString()) || imagepath != null) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle("确认修改");
                    dialog.setMessage("是否上传修改信息");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", (dialog12, which) -> new Thread(() -> {
                        if (imagepath != null) {
                            File f = null;
                            Bitmap bitmap = getBitmap(imagepath);
                            try {
                                // create a file to write bitmap data
                                f = new File(getActivity().getCacheDir(), "file");
                                f.createNewFile();
                                // convert bitmap to byte array
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                                byte[] bitmapdata = bos.toByteArray();
                                // write the bytes in file
                                FileOutputStream fos = new FileOutputStream(f);
                                fos.write(bitmapdata);
                                fos.flush();
                                fos.close();
                            } catch (Exception e) {
                            }
                            RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), f);//将file转换成RequestBody文件
                            RequestBody imagerequestBody = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("img", imagepath,
                                            fileBody)
                                    .addFormDataPart("imagetype", "multipart/form-data")
                                    .build();
                            Utility.PostHttp("http://fjxtest.club:9090/upload/img", token, imagerequestBody, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Message message = mHandler.obtainMessage(FAIL_UPDATE_IMG);
                                    mHandler.sendMessage(message);
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String responsetext = response.body().string();
                                    ImageUrl imageUrl = Utility.HandImageurl(responsetext);
                                    if (imageUrl != null && imageUrl.code != null) {
                                        switch (imageUrl.code) {
                                            case "70001":
                                                imageurl = imageUrl.url;
                                                RequestBody requestBody = new FormBody.Builder()
                                                        .add("className", classn.getText().toString())
                                                        .add("nickname", nickname.getText().toString())
                                                        .add("oldpsd", preferences.getString("password", ""))
                                                        .add("userimg", imageurl)
                                                        .add("newpsd", "")
                                                        .build();
                                                Utility.PostHttp("http://fjxtest.club:9090/user/updateinfo", token, requestBody, new Callback() {
                                                    @Override
                                                    public void onFailure(Call call, IOException e) {
                                                        Message message = mHandler.obtainMessage(FAIL_UPDATE_INFO);
                                                        mHandler.sendMessage(message);
                                                    }

                                                    @Override
                                                    public void onResponse(Call call, Response response) throws IOException {
                                                        String responsetext = response.body().string();
                                                        ImageUrl imageUrl1 = Utility.HandImageurl(responsetext);

                                                        if (imageUrl1 != null && imageUrl1.code != null) {
                                                            switch (imageUrl1.code) {
                                                                case "20007":
                                                                    Message message = mHandler.obtainMessage(SUCCESS_UPDATE_INFO);
                                                                    mHandler.sendMessage(message);
                                                                    break;
                                                                default:
                                                                    Message mmessage = mHandler.obtainMessage(FAIL_UPDATE_INFO);
                                                                    mHandler.sendMessage(mmessage);
                                                            }
                                                        } else {
                                                            Message message = mHandler.obtainMessage(FAIL_UPDATE_INFO);
                                                            mHandler.sendMessage(message);
                                                        }

                                                    }
                                                });
                                                break;
                                            default:
                                                Message message = mHandler.obtainMessage(FAIL_UPDATE_IMG);
                                                mHandler.sendMessage(message);
                                        }
                                    } else {
                                        Message message = mHandler.obtainMessage(FAIL_UPDATE_IMG);
                                        mHandler.sendMessage(message);
                                    }
                                }
                            });
                        } else {
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("className", classn.getText().toString())
                                    .add("nickname", nickname.getText().toString())
                                    .add("oldpsd", preferences.getString("password", ""))
                                    .add("userimg", imageurl)
                                    .add("newpsd", "")
                                    .build();
                            Utility.PostHttp("http://fjxtest.club:9090/user/updateinfo", token, requestBody, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Message message = mHandler.obtainMessage(FAIL_UPDATE_INFO);
                                    mHandler.sendMessage(message);
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String responsetext = response.body().string();
                                    ImageUrl imageUrl1 = Utility.HandImageurl(responsetext);
                                    if (imageUrl1 != null && imageUrl1.code != null) {
                                        switch (imageUrl1.code) {
                                            case "20007":
                                                Message message = mHandler.obtainMessage(SUCCESS_UPDATE_INFO);
                                                mHandler.sendMessage(message);
                                                break;
                                            default:
                                                Message mmessage = mHandler.obtainMessage(FAIL_UPDATE_INFO);
                                                mHandler.sendMessage(mmessage);
                                        }
                                    } else {
                                        Message message = mHandler.obtainMessage(FAIL_UPDATE_INFO);
                                        mHandler.sendMessage(message);
                                    }

                                }
                            });
                        }

                    }).start());
                    dialog.setNegativeButton("取消", (dialog1, which) -> {
                        imagepath = null;
                        imageView.setVisibility(View.VISIBLE);
                        circleImageView.setVisibility(View.GONE);
                        hadmodify = false;
                        nickname.setFocusable(false);
                        nickname.setEnabled(false);
                        nickname.setBackground(null);
                        classn.setEnabled(false);
                        classn.setFocusable(false);
                        classn.setBackground(null);
                        classn.setText(classname);
                        nickname.setText(nickna);
                        imamodi.setImageResource(R.drawable.modify);
                    });
                    dialog.show();
                } else {
                    imagepath = null;
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
        //退出
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
        //展开成员列表
        vis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) listView.setVisibility(View.VISIBLE);
                else listView.setVisibility(View.GONE);
            }
        });
        //添加用户
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
        limit = preferences.getString("limit", "").equals("true");
        listView = view.findViewById(R.id.list_users);
        vis = view.findViewById(R.id.listvis);
        modifyinfo = view.findViewById(R.id.modifyinfo);
        nickname = view.findViewById(R.id.editnickname);
        classn = view.findViewById(R.id.editclass);
        imamodi = view.findViewById(R.id.modifyima);
        circleImageView = view.findViewById(R.id.addimage);
        imageView = view.findViewById(R.id.image);
        adduser = view.findViewById(R.id.adduser);
        groupn = view.findViewById(R.id.group);
        acount = view.findViewById(R.id.acount);
        admin = view.findViewById(R.id.admin);
        exit = view.findViewById(R.id.exit);
        refreshui();
        if (limit) {
            GetUsers();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
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

    private void startChooser() {
        // 启动图片选择器
        imagePicker.startChooser(this, new ImagePicker.Callback() {
            // 选择图片回调
            @Override
            public void onPickImage(Uri imageUri) {

            }

            // 裁剪图片回调
            @Override
            public void onCropImage(Uri imageUri) {
                imagepath = getFilePath(getActivity(), imageUri);
                circleImageView.setImageURI(imageUri);
            }

            // 自定义裁剪配置
            @Override
            public void cropConfig(CropImage.ActivityBuilder builder) {
                builder
                        // 是否启动多点触摸
                        .setMultiTouchEnabled(false)
                        // 设置网格显示模式
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        // 圆形/矩形
                        .setCropShape(CropImageView.CropShape.OVAL)
                        // 调整裁剪后的图片最终大小
                        .setRequestedSize(480, 480)
                        // 宽高比
                        .setAspectRatio(5, 5);
            }

            // 用户拒绝授权回调
            @Override
            public void onPermissionDenied(int requestCode, String[] permissions,
                                           int[] grantResults) {
            }
        });
    }

    public Bitmap getBitmap(String path) {
        File file = new File(path);
        Bitmap resizeBmp = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 数字越大读出的图片占用的heap越小 不然总是溢出
        opts.inSampleSize = 1;
        resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
        return resizeBmp;
    }

    public static String getFilePath(Context context, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {

            int sdkVersion = Build.VERSION.SDK_INT;
            if (sdkVersion >= 19) { // api >= 19
                return getRealPathFromUriAboveApi19(context, uri);
            } else { // api < 19
                return getRealPathFromUriBelowAPI19(context, uri);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
}
