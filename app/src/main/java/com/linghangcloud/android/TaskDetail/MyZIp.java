package com.linghangcloud.android.TaskDetail;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class MyZIp {
    static class ZipFileCreateTest {
    public void zip(String FileName,File OrderFile,String Path)throws Exception{
        Log.e("test:", "zip: \"压缩中...\"" );
        try {
            //此处为名字
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(new File(Path,FileName+".zip")));
            zip(zipOutputStream, OrderFile,FileName);
            zipOutputStream.close();
            Log.e("test:","文件压缩完成+order:"+OrderFile.getPath());
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
    private void zip(ZipOutputStream out,File file,String base) throws FileNotFoundException {
        if (file.isDirectory()) {
            System.out.println("压缩文件目录"+base+"...");
            File[] fl=file.listFiles();
            if (base.length()!=0) {
                try {
                    out.putNextEntry(new ZipEntry(base+"\\"));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < fl.length; i++) {
//				System.out.println(fl.toString());
//				此处可以保证只取必要的一部分
                zip(out,fl[i],base+"\\"+fl[i].getName());
            }
        }else {

            try {
                Log.e("test:", "zip: 压缩文件"+file.getName()+"...\"");
                out.putNextEntry(new ZipEntry(base));
                FileInputStream fileInputStream =new FileInputStream(file);
                int b;
                while((b=fileInputStream.read())!=-1) {
                    out.write(b);
                }
                fileInputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }


    public void decompressing(File file,String orderpath) {
        ZipInputStream zin;
        try {
            System.out.println("开始解压");
            ZipFile zipFile = new ZipFile(file);
            zin=new ZipInputStream(new FileInputStream(file));
            ZipEntry entry =null;
		//	System.out.println(zin.getNextEntry().toString());

            while(((entry=zin.getNextEntry())!=null)&&!entry.isDirectory())
            {
                Log.e("test：","正在解压"+zipFile.getName()+"...");
                File tmpFile=new File(orderpath+"//"+entry.getName());
                if (!tmpFile.exists()) {
                    tmpFile.getParentFile().mkdirs();
                    tmpFile.createNewFile();
                    System.out.println(tmpFile+"解压开始");
                    OutputStream oStream=new FileOutputStream(tmpFile);

                    InputStream inputStream =zipFile.getInputStream(entry);

                    int count=0;
                    while((count=inputStream.read())!=-1) {
                        oStream.write(count);
                    }
                    oStream.close();
                    inputStream.close();
                }
                zin.closeEntry();
                if (tmpFile.exists()) {
                    System.out.println("sss");
                }
                System.out.println(tmpFile.getName()+"解压完成");
            }
            zin.close();
            System.out.println("解压完成");
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }
}
}
