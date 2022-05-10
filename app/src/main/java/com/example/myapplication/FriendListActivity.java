package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.myapplication.adapter.FriendListAdapter;
import com.example.myapplication.adapter.ShowListAdapter;
import com.example.myapplication.data.Friend;
import com.example.myapplication.data.UserShow;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        listView=findViewById(R.id.lv_friends);
        FriendListAdapter friendListAdapter;
        List<Friend> friends= LitePal.findAll(Friend.class);

        friendListAdapter=new FriendListAdapter(friends,FriendListActivity.this);
        listView.setAdapter(friendListAdapter);
    }
}