package com.linghangcloud.android.TaskDetail;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyDownload {
    public static void  downLoadFromUrl(final String urlStr, final String fileName, final String savePath) throws IOException {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    URL url = null;
                    try {
                        url = new URL(urlStr);
                    } catch (MalformedURLException e) {
                        Log.e("test:", "run: " );
                        e.printStackTrace();
                    }
                    HttpURLConnection conn = null;
                    try {
                        conn = (HttpURLConnection)url.openConnection();
                    } catch (IOException e) {
                        Log.e("test:", "run: " );
                        e.printStackTrace();
                    }

                    //设置超时间为3秒
                    conn.setConnectTimeout(3*1000);
                    //防止屏蔽程序抓取而返回403错误
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                        Log.e("test:", savePath );

                    //得到输入流
                    InputStream inputStream = null;
                    try {
                        inputStream = conn.getInputStream();
                    } catch (IOException e) {
                        Log.e("test:", "run: " );
                        e.printStackTrace();
                    }

                    //文件保存位置
                    File saveDir = new File(savePath);
                    if (!saveDir.exists()){
                        saveDir.mkdirs();
                    }
                    File file = new File(saveDir+File.separator+fileName);
                    FileOutputStream fos = null;
                    try {
                        fos=new FileOutputStream(file);
                        int temp;
                        while((temp=inputStream.read())!=-1){
                            fos.write(temp);
                        }
                        fos.close();
                        inputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    MyZIp.ZipFileCreateTest.decompressing(file,savePath);
                    file.delete();
                    System.out.println("info:"+url+" download success");
                }
            }).start();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
