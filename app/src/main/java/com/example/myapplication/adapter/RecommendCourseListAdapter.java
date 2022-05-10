package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class RecommendCourseListAdapter extends BaseAdapter{

    private Context mContext;

    public RecommendCourseListAdapter( Context mContext) {

        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return 3;
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
        public TextView tvCourseName,tvAuthor;
    }//自定义类


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder =null;//形参
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.layout_recommend_course_item, parent, false);
            //view=mLayoutInflater.inflate(R.layout.layout_chat_item,null);
            holder=new ViewHolder();//创建空间，初始化
            holder.ivCoursePhoto=view.findViewById(R.id.play_recommend_course_photo);
            holder.tvCourseName=view.findViewById(R.id.play_recommend_course_name);
            holder.tvAuthor=view.findViewById(R.id.tv_play_recommend_course_author);
            view.setTag(holder);

        }
        else
        {
            holder=(ViewHolder) view.getTag();
        }
        //给控件赋值

        Bitmap bitmap;
        holder.tvCourseName.setText("android");
        List<String> text=new ArrayList<>();
        text.add("小明");
        text.add("秀秀");
        text.add("洛杉矶不是客人");
        holder.tvAuthor.setText(text.get(position));

        bitmap= BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/mipmap/default_video.png"));
        holder.ivCoursePhoto.setImageBitmap(bitmap);

       // Glide.with(mContext).load("https://iconfont.alicdn.com/t/a09a3b11-0234-4e10-af73-150088eebcf8.png").into(holder.imageView);
        return view;
    }
}
