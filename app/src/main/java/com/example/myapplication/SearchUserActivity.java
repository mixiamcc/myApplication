package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.data.UserData;

import org.litepal.LitePal;

import java.util.List;

public class SearchUserActivity extends AppCompatActivity {

    TextView searchUser;
    String searchUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        searchUser=findViewById(R.id.tv_search_btn);
        searchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchUserName=searchUser.getText().toString();
                List<UserData> users= LitePal.findAll(UserData.class);
                for (UserData user : users) {
                    if (user.getUsername().equals(searchUserName)) {
                        Intent intent=new Intent(SearchUserActivity.this,SearchAddFriendActivity.class);
                        intent.putExtra("searchUserName",searchUserName);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}