package com.example.myapplication.viewpages;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import android.widget.TextView;


import com.example.myapplication.R;

import org.litepal.LitePal;

import com.example.myapplication.data.UserData;
import com.example.myapplication.widget.RoundImageView;
import com.example.myapplication.util.PhotoTools;

import java.util.List;

public class UpdateMineView {
    private String username;
    private View view;

    public UpdateMineView(View view,String username)
    {
        this.username=username;
        this.view=view;
    }

    public void updateMine()
    {

        TextView nickname=view.findViewById(R.id.tv_nickname);
        TextView category=view.findViewById(R.id.tv_category);
        RoundImageView userheader=view.findViewById(R.id.iv_userphoto);

        this.username= username;


        List<UserData> users= LitePal.findAll(UserData.class);
        for(UserData user :users)
        {
            if (user.getUsername().equals(username))
            {
                nickname.setText(user.getNickname());
                category.setText(user.getCategory());
                Bitmap bitmap;
                if(user.getHeader()==null)
                {
                    bitmap=BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/mipmap/default_header.png"));
                }
                else {
                    bitmap=PhotoTools.Bytes2Bimap(user.getHeader());
                }
                userheader.setImageBitmap(bitmap);
                user.setHeader(PhotoTools.bitmap2byte(bitmap));
            }
        }

    }

}
