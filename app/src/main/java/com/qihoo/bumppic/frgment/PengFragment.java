package com.qihoo.bumppic.frgment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.qihoo.bumppic.R;
import com.qihoo.bumppic.SendPictureActivity;
import com.qihoo.bumppic.entity.ImageEntity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by hacker on 16/8/31.
 */
public class PengFragment extends Fragment{

    private View view;
    private FragmentActivity mContext;
    private ContentResolver mContentResolver;
    private ImageEntity imageEntity;
    private ImageLoader loader;
    private DisplayImageOptions options;
    private ImageEntity currentImageFolder;
    private static final int TAKE_PICTURE=201;
    private String cameraPath;
    private GridView gridView;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashMap<String, Integer> tmpDir = new HashMap<String, Integer>();
    private ArrayList<ImageEntity> mDirPaths = new ArrayList<ImageEntity>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.send_layout,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        mContentResolver = mContext.getContentResolver();
        initParamter();
        setView(view);
    }

    private void initParamter() {
        imageEntity = new ImageEntity();
        loader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.background_picture1)
                .showImageForEmptyUri(R.drawable.background_picture1).showImageOnFail(R.drawable.background_picture1)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).build();

        imageEntity.setDir("/BumpPic");
        currentImageFolder = imageEntity;
    }

    private void setView(View view) {
        gridView = (GridView)view.findViewById(R.id.send_gridView);
        gridView.setAdapter(new PictureAdapter());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position==0){
                    goCamare();
                }else {
                    gotoSendPicture(currentImageFolder.images.get(position-1));
                }
            }
        });
        getThumbnail();
    }
    private void gotoSendPicture(String str){
        Intent intent = new Intent(mContext, SendPictureActivity.class);
        intent.putExtra("path",str);
        startActivity(intent);
    }
    /**
     * 使用相机拍照
     *
     * @version 1.0
     * @author zyh
     */
    protected void goCamare() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = getOutputMediaFileUri();
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    /**
     * 用于拍照时获取输出的Uri
     */
    protected Uri getOutputMediaFileUri() {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Night");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        cameraPath = mediaFile.getAbsolutePath();
        return Uri.fromFile(mediaFile);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && cameraPath != null) {
            //这个跳到发送界面
            gotoSendPicture(cameraPath);
            // selectedPicture.add(cameraPath);
//            Intent data2 = new Intent();
//            data2.putExtra(INTENT_SELECTED_PICTURE, selectedPicture);
            // setResult(RESULT_OK, data2);
            //this.finish();
        }
    }

    /**
     * 得到缩略图
     */
    private void getThumbnail() {
        Cursor mCursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.ImageColumns.DATA }, "", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");
        Log.e("TAG", mCursor.getCount() + "");
        if (mCursor.moveToFirst()) {
            int _date = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            do {
                // 获取图片的路径
                String path = mCursor.getString(_date);
                Log.e("TAG", path);
                imageEntity.images.add(new String(path));
                // 获取该图片的父路径名
                File parentFile = new File(path).getParentFile();
                if (parentFile == null) {
                    continue;
                }
                ImageEntity imageFloder = null;
                String dirPath = parentFile.getAbsolutePath();
                if (!tmpDir.containsKey(dirPath)) {
                    // 初始化imageFloder
                    imageFloder = new ImageEntity();
                    imageFloder.setDir(dirPath);
                    imageFloder.setFirstImagePath(path);
                    mDirPaths.add(imageFloder);
                    tmpDir.put(dirPath, mDirPaths.indexOf(imageFloder));
                } else {
                    imageFloder = mDirPaths.get(tmpDir.get(dirPath));
                }
                imageFloder.images.add(new String(path));
            } while (mCursor.moveToNext());
        }
        mCursor.close();
//        for (int i = 0; i < mDirPaths.size(); i++) {
//            ImageEntity f = mDirPaths.get(i);
//            Log.d("zyh", i + "-----" + f.getName() + "---" + f.images.size());
//        }
        tmpDir = null;
    }

    class PictureAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return currentImageFolder.images.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return currentImageFolder.images.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.send_item_layout, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) convertView.findViewById(R.id.send_item_ig);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            float density = dm.density;
            int item_width = (int)((width-10*density*4)/3);
            int item_height = (int)(item_width/0.658);
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                    item_width,item_height);//传入自己需要的宽高
            holder.iv.setLayoutParams(param);
            if (position == 0) {
                holder.iv.setImageResource(R.drawable.take_photo);
            } else {
                position = position - 1;
                final String item = currentImageFolder.images.get(position);
                loader.displayImage("file://" + item, holder.iv, options);
               // boolean isSelected = selectedPicture.contains(item.path);
            }
            return convertView;
        }
    }

    class ViewHolder {
        ImageView iv;
    }
    public void back(View v) {
        mContext.onBackPressed();
    }
}
