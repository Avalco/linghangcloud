package com.linghangcloud.android.TaskDetail;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.linghangcloud.android.R;

import org.w3c.dom.Text;

import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileStore;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class FileFragment extends Fragment {
    private RecyclerView recyclerView;
    private EditText editText;
    private List<HomeWork> homeWorkList = new ArrayList<>();
    private TextView SubmitNum;
    private TextView SumNum;
    private CircleImageView submitButton;
    private int submit=5;
    private File z =null;
    private File apk=null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file,container,false);
        recyclerView=view.findViewById(R.id.file_re);
        SubmitNum=view.findViewById(R.id.fragment_file_submitnumber);
        SumNum = view.findViewById(R.id.fragment_file_sumnumber);
        editText=view.findViewById(R.id.input);
        submitButton=view.findViewById(R.id.outputfile_button);
        z =new File(getContext().getExternalCacheDir()+"//zip");
        apk=new File(getContext().getExternalCacheDir()+"//apk");
        InitList();
        SubmitNum.setText(""+submit);
        SumNum.setText(""+homeWorkList.size());

        z.mkdirs();
        apk.mkdirs();

//       提交按钮
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }else{
                    //通过intent打开目标压缩包或文件
//                    如果没有压缩包提示操作
                    OpenContentToHomeWork();
                }

            }
        });
//       配置循环界面
        HomeWorkAdpat homeWorkAdpat = new HomeWorkAdpat(homeWorkList,getContext(),editText);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(homeWorkAdpat);
        return view;
    }

    //打开文件管理器
    private void OpenContentToHomeWork() {
        Uri uri = null;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);

    }

    //有待改善 网络连接
    private void InitList() {
        for (int i=0;i<15;i++){
            HomeWork homeWork = new HomeWork();
            homeWork.setFileName("性感程序员，在线写BUG.avi");
            homeWork.setUserName("全Android组最帅的男人");
            homeWork.setSubmit(true);
            homeWork.setFilePic(R.drawable.testpic);
            homeWorkList.add(homeWork);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
//    回调函数
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = data.getData();
        Log.e("test：提取文件", "文件提取工作完成"+uri.toString() );
        String path=getPath(getActivity(),uri);
        File file =new File(path);

        if (file.exists()){
            Log.e("test：提取文件",file.getPath());
            MyZIp.ZipFileCreateTest zipFileCreateTest=new MyZIp.ZipFileCreateTest();
            try {
                Log.e("test:", "onActivityResult: "+getContext().getExternalCacheDir() );
                for (String x:file.getName().split(".")){
                    Log.e("test:", "onActivityResultssss: "+x );
                }
                //getContext().getExternalCacheDir().toString()
                zipFileCreateTest.zip(file.getName(),file,z.getPath());
                if (new File(getContext().getExternalCacheDir().toString(),file.getName()+".zip").exists()){
                    Log.e("test:", "onActivityResult: 存在" );
                }
                zipFileCreateTest.decompressing(new File(getContext().getExternalCacheDir().toString()+"//zip",file.getName()+".zip"),apk.getPath());

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("test:", "onActivityResult: 获取失败" );
            }
        }
    }

    //  提取文件的的工作
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, null, null, null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static void install(File file,Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}


