package com.qihoo.bumppic.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import com.qihoo.bumppic.SendActivity;
import com.qihoo.bumppic.entity.ImageEntity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by hacker on 16/8/31.
 */
public class FileUtils {

    public static void getAlbum(final Context context, final Handler mHandler) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            ToastUtils.showShort(context, "暂无外部存储,请插入SD卡。");
            return;
        }

        new Thread() {
            @Override
            public void run() {
                int mPicsSize = 0;
                File mImgDir;
                List<ImageEntity> mImageEntities = new ArrayList<ImageEntity>();
                int totalCount = 0;
                HashSet mDirPaths = new HashSet();
                String firstImage = null;
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = context.getContentResolver();
                Cursor mCursor = mContentResolver.query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE +
                                "=? or " + MediaStore.Images.Media.MIME_TYPE + "= ?", new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);
                while (mCursor.moveToNext()) {
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    if (firstImage == null) {
                        firstImage = path;
                    }
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null) {
                        continue;
                    }
                    String dirPath = parentFile.getAbsolutePath();
                    ImageEntity imageEntity = null;
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        imageEntity = new ImageEntity();
                        imageEntity.setDir(dirPath);
                        imageEntity.setFirstImagePath(path);
                    }
                    if (parentFile.list() == null)
                        continue;
                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File file, String s) {
                            if (s.endsWith(".jpg") || s.endsWith(".png") || s.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    }).length;
                    totalCount += picSize;
                    imageEntity.setCount(picSize);
                    mImageEntities.add(imageEntity);
                    if (picSize > mPicsSize) {
                        mPicsSize = picSize;
                        mImgDir = parentFile;
                    }
                }
                mCursor.close();
                mDirPaths = null;
                mHandler.sendEmptyMessage(0x110);
            }
        }.start();
    }

    public static FileObserver fileObsever(final  String parentpath,final Activity activity){
        FileObserver fileObserver = new FileObserver(parentpath) {
            @Override
            public void onEvent(int event, String path) {
                switch (event){
                    case FileObserver.MOVED_TO:
                        Log.i("test","得到了回调"+event+path);
                        if (activity instanceof SendActivity){
                            Message msg = ((SendActivity)activity).handler.obtainMessage(SendActivity.FILECHANGE_MSG);
                            msg.obj = parentpath + "/"+path;
                            msg.sendToTarget();
                            return;
                        }
                        break;
                }
            }
        };
        return fileObserver;
    }
    /**
     * 返回一个byte数组
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] getBytesFromFile(File file) {

        byte[] bytes = null;
        try {
            InputStream is = new FileInputStream(file);

            // 获取文件大小
            long length = file.length();

            if (length > Integer.MAX_VALUE) {

                // 文件太大，无法读取
                throw new IOException("File is to large " + file.getName());

            }

            // 创建一个数据来保存文件数据

            bytes = new byte[(int) length];

            // 读取数据到byte数组中

            int offset = 0;

            int numRead = 0;

            while (offset < bytes.length

                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {

                offset += numRead;

            }

            // 确保所有数据均被读取

            if (offset < bytes.length) {

                throw new IOException("Could not completely read file "
                        + file.getName());

            }

            // Close the input stream and return bytes

            is.close();
        } catch (Exception e) {

            e.printStackTrace();
        }

        return bytes;

    }

    /**
     * 把字节数组保存为一个文件
     *
     * @param b
     * @param outputFile
     * @return
     */
    public static File getFileFromBytes(byte[] b, String outputFile) {
        File ret = null;
        BufferedOutputStream stream = null;
        try {
            ret = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(ret);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            // log.error("helper:get file from byte process error!");
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // log.error("helper:get file from byte process error!");
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }
}
