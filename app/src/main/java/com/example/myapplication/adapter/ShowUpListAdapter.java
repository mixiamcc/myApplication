package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.List;

public class ShowUpListAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private UserData user;
    private List<UserShow> userShows;

    public ShowUpListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ShowUpListAdapter(UserData user, Context mContext) {
        userShows= LitePal.findAll(UserShow.class);
        Log.d("usershows.size()",userShows.size()+"");
        this.user=user;
        //userShows=user.getShows();
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return userShows.size()+2;
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



        Bitmap header;
        Bitmap photo;
        int index= userShows.size() - position - 1;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
        String date=simpleDateFormat.format(userShows.get(index).getShowtime());
        if(index>=0) {

            holder.ivShowHeader.setImageBitmap(PhotoTools.Bytes2Bimap(userShows.get(index).getPhoto()));
            holder.ivShowPhoto.setImageBitmap(PhotoTools.Bytes2Bimap(userShows.get(index).getPhoto()));
            holder.tvShowName.setText(userShows.get(index).getText());
            holder.tvShowDate.setText(date);
            holder.tvShowContext.setText(userShows.get(index).getText());
            holder.tvShowContext.setTextColor(10);
        }
        if(position== userShows.size()) {
            header= BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/mipmap/default_header.png"));
            photo = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/mipmap/icon_man.png"));
            holder.ivShowHeader.setImageBitmap(header);
            holder.ivShowPhoto.setImageBitmap(photo);
            holder.tvShowName.setText("mcc");
            holder.tvShowDate.setText("2022.03.25");
            holder.tvShowContext.setText("保持快乐!");
        }

        if(position== userShows.size()+1) {
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
