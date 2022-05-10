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
import com.example.myapplication.data.UserData;
import com.example.myapplication.data.UserShow;
import com.example.myapplication.util.PhotoTools;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class ShowListAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public ShowListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ShowListAdapter(UserData user, Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return 2;
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
        public ImageView ivShowHeader,ivShowPhoto;
        public TextView tvShowName,tvShowContext,tvShowDate;
    }//自定义类

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder =null;//形参
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.layout_show_item, parent, false);
            //view=mLayoutInflater.inflate(R.layout.layout_chat_item,null);
            holder=new ViewHolder();//创建空间，初始化
            holder.ivShowHeader=view.findViewById(R.id.iv_show_header);
            holder.ivShowPhoto=view.findViewById(R.id.iv_show_photo);
            holder.tvShowName=view.findViewById(R.id.tv_show_name);
            holder.tvShowContext=view.findViewById(R.id.tv_show_context);
            holder.tvShowDate=view.findViewById(R.id.tv_show_date);
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
        Bitmap header;
        Bitmap photo;
        if(position==0) {
            header= BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/mipmap/default_header.png"));
            photo = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/mipmap/icon_man.png"));
            holder.ivShowHeader.setImageBitmap(header);
            holder.ivShowPhoto.setImageBitmap(photo);
            holder.tvShowName.setText("mcc");
            holder.tvShowDate.setText("2022.03.25");
            holder.tvShowContext.setText("保持快乐!");
        }

        if(position==1) {
            header = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/mipmap/littlecat.png"));
            photo = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/mipmap/happy.png"));
            holder.ivShowHeader.setImageBitmap(header);
            holder.ivShowPhoto.setImageBitmap(photo);
            holder.tvShowName.setText("lucky");
            holder.tvShowDate.setText("2022.03.25");
            holder.tvShowContext.setText("行动派!");
        }



       // Glide.with(mContext).load("https://iconfont.alicdn.com/t/a09a3b11-0234-4e10-af73-150088eebcf8.png").into(holder.imageView);
        return view;
    }
}
