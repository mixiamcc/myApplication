package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.data.Friend;
import com.example.myapplication.data.UserData;

import org.litepal.LitePal;

import java.util.List;

public class SearchAddFriendActivity extends AppCompatActivity {
    String searchUserName;
    Button addFriend;
    Friend friend=new Friend();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_add_friend);
        List<UserData> users= LitePal.findAll(UserData.class);
        for (UserData user : users) {
            if (user.getUsername().equals(searchUserName)) {
            }
            }
        addFriend=findViewById(R.id.btn_search_add_friend);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Friend> friends= LitePal.findAll(Friend.class);
                friend.setNickname("mcc");
                friend.setCategory("志愿者");
                friends.add(friend);
                friend.save();
                finish();
            }
        });
    }
}