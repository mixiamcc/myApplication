package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;

public class CategoryListAdapter extends BaseAdapter {
    List<String> categorylist;
    private Context mContext;


    public CategoryListAdapter(Context mContext) {
        this.mContext = mContext;
        categorylist.add("数学");
        categorylist.add("语文");
        categorylist.add("英语");
        categorylist.add("音乐");
        categorylist.add("美术");
        categorylist.add("编程");
        categorylist.add("心理");
    }


    @Override
    public int getCount() {
        return categorylist.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    static class ViewHolder {
        public TextView tvCategory;
    }//自定义类


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder = null;//形参
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_category_item, parent, false);
            holder = new ViewHolder();//创建空间，初始化
            holder.tvCategory = view.findViewById(R.id.tv_course_category);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        //给控件赋值
        holder.tvCategory.setText(categorylist.get(position));

        // Glide.with(mContext).load("https://iconfont.alicdn.com/t/a09a3b11-0234-4e10-af73-150088eebcf8.png").into(holder.imageView);
        return view;
    }
}
