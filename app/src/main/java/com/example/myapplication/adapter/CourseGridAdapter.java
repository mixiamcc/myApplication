package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.data.UserData;
import com.glidebitmappool.GlideBitmapFactory;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class CourseGridAdapter extends BaseAdapter{

    private List<UserData> mFriends;
    private List<String> coursenameList=new ArrayList<>();
    private Context mContext;


    public CourseGridAdapter(Context mContext) {
        this.mContext = mContext;
        coursenameList.add("Android程序设计");
        coursenameList.add("美术基础");
        coursenameList.add("数学基础");
        coursenameList.add("语文基础");
        coursenameList.add("Android开发");
        coursenameList.add("Java程序设计");
    }

    public CourseGridAdapter(List<UserData> friends, Context mContext) {
        this.mFriends = friends;
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    static class ViewHolder{
        public ImageView ivCoursePhoto;
        //public VideoView vvCourseVideo;
        public TextView tvCourseName;
    }//自定义类


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder =null;//形参
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.layout_course_item, parent, false);
            holder=new ViewHolder();//创建空间，初始化
            holder.ivCoursePhoto=view.findViewById(R.id.iv_course_photo);
            //holder.vvCourseVideo=view.findViewById(R.id.vv_course_video);
            holder.tvCourseName=view.findViewById(R.id.tv_course_name);
            view.setTag(holder);

        }
        else
        {
            holder=(ViewHolder) view.getTag();
        }
        //给控件赋值


//        if(mFriends.get(position).getHeader()==null)
//        {
//            header= BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/mipmap/default_header.png"));
//
//        }
//        else {
//            header=PhotoTools.Bytes2Bimap(mFriends.get(position).getHeader());
//        }
       // holder.ivCoursePhoto.setImageURI(Uri.parse("http://192.168.1.58:8081/MyServer/file/videophoto/"+String.valueOf(position)+".png"));

        String imgUrl = "http://192.168.1.58:8081/MyServer/file/videophoto/"+String.valueOf(position+1)+".png";//此处为图片链接
//此处是加载网络链接图片资源

        Glide.with(mContext).load(imgUrl).into(holder.ivCoursePhoto);
//        Bitmap coursePhoto = BitmapFactory.decodeStream(getClass().getResourceAsStream("http://192.168.1.58:8081/MyServer/file/videophoto/"+String.valueOf(position+1)+".png"));
//            holder.ivCoursePhoto.setImageBitmap(coursePhoto);
           // holder.vvCourseVideo.setVideoPath("E:\\code\\MyApplication\\app\\src\\main\\res\\videos\\default_video.mp4");
              holder.tvCourseName.setText(coursenameList.get(position));


       // Glide.with(mContext).load("https://iconfont.alicdn.com/t/a09a3b11-0234-4e10-af73-150088eebcf8.png").into(holder.imageView);
        return view;
    }
}
