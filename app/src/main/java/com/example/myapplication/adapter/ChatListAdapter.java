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

//import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.data.UserData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.example.myapplication.util.PhotoTools;

public class ChatListAdapter extends BaseAdapter{

    private List<UserData> mFriends;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public ChatListAdapter(List<UserData> friends, Context mContext) {
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
        public TextView tvFriendName,tvChatContext;
    }//自定义类


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder =null;//形参
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.layout_chat_item, parent, false);
            //view=mLayoutInflater.inflate(R.layout.layout_chat_item,null);
            holder=new ViewHolder();//创建空间，初始化
            holder.imageView=view.findViewById(R.id.iv_friend_header);
            holder.tvFriendName=view.findViewById(R.id.friend_name);
            holder.tvChatContext=view.findViewById(R.id.chat_context);
            view.setTag(holder);

        }
        else
        {
            holder=(ViewHolder) view.getTag();
        }
        //给控件赋值

        Bitmap bitmap;
        holder.tvFriendName.setText(mFriends.get(position).getNickname());
        List<String> text=new ArrayList<>();
        text.add("");
        text.add("");
        text.add("");
        text.add("");
        text.add("你现在好吗");
        holder.tvChatContext.setText(text.get(position));

        if(mFriends.get(position).getHeader()==null)
        {
            bitmap= BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/mipmap/littlecat.png"));
        }
        else {
            bitmap=PhotoTools.Bytes2Bimap(mFriends.get(position).getHeader());
        }
        holder.imageView.setImageBitmap(bitmap);

       // Glide.with(mContext).load("https://iconfont.alicdn.com/t/a09a3b11-0234-4e10-af73-150088eebcf8.png").into(holder.imageView);
        return view;
    }
}
