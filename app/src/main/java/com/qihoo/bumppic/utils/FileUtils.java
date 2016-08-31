package com.qihoo.bumppic.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import com.qihoo.bumppic.entity.ImageEntity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by hacker on 16/8/31.
 */
public class FileUtils {

    public static void getAlbum(final Context context,final Handler mHandler){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            ToastUtils.showShort(context,"暂无外部存储,请插入SD卡。");
            return;
        }

        new Thread(){
            @Override
            public void run() {
                int mPicsSize=0;
                File mImgDir;
                List<ImageEntity>mImageEntities=new ArrayList<ImageEntity>();
                int totalCount = 0;
                HashSet mDirPaths = new HashSet();
                String firstImage = null;
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = context.getContentResolver();
                Cursor mCursor = mContentResolver.query(mImageUri,null,MediaStore.Images.Media.MIME_TYPE+
                "=? or "+MediaStore.Images.Media.MIME_TYPE+"= ?",new String[]{"image/jpeg","image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);
                while (mCursor.moveToNext()){
                    String path =  mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    if (firstImage==null){
                        firstImage = path;
                    }
                    File parentFile = new File(path).getParentFile();
                    if (parentFile==null){
                        continue;
                    }
                    String dirPath = parentFile.getAbsolutePath();
                    ImageEntity imageEntity = null;
                    if (mDirPaths.contains(dirPath)){
                        continue;
                    }else {
                        mDirPaths.add(dirPath);
                        imageEntity = new ImageEntity();
                        imageEntity.setDir(dirPath);
                        imageEntity.setFirstImagePath(path);
                    }
                    if (parentFile.list() == null )
                        continue;
                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File file, String s) {
                            if (s.endsWith(".jpg")|| s.endsWith(".png") || s.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    }).length;
                    totalCount +=picSize;
                    imageEntity.setCount(picSize);
                    mImageEntities.add(imageEntity);
                    if (picSize>mPicsSize){
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
}
