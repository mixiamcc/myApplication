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
import com.example.myapplication.data.Friend;
import com.example.myapplication.data.UserData;
import com.example.myapplication.util.PhotoTools;

import java.util.ArrayList;
import java.util.List;

public class FriendListAdapter extends BaseAdapter{

    private List<Friend> mFriends;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public FriendListAdapter(List<Friend> friends, Context mContext) {
        this.mFriends = friends;
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return mFriends.size();
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
        public ImageView imageView;
        public TextView tvFriendName;
    }//自定义类


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder =null;//形参
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.layout_friend_item, parent, false);
            //view=mLayoutInflater.inflate(R.layout.layout_chat_item,null);
            holder=new ViewHolder();//创建空间，初始化
            holder.imageView=view.findViewById(R.id.iv_friend_header);
            holder.tvFriendName=view.findViewById(R.id.friend_name);
            view.setTag(holder);

        }
        else
        {
            holder=(ViewHolder) view.getTag();
        }
        //给控件赋值

        Bitmap bitmap;
        holder.tvFriendName.setText(mFriends.get(position).getNickname());
        if(mFriends.get(position).getHeader()==null)
        {
            bitmap= BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/mipmap/default_header.png"));
        }
        else {
            bitmap=PhotoTools.Bytes2Bimap(mFriends.get(position).getHeader());
        }
        holder.imageView.setImageBitmap(bitmap);

       // Glide.with(mContext).load("https://iconfont.alicdn.com/t/a09a3b11-0234-4e10-af73-150088eebcf8.png").into(holder.imageView);
        return view;
    }
}
