package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.myapplication.adapter.ChatListAdapter;
import com.example.myapplication.adapter.MsgListAdapter;
import com.example.myapplication.adapter.ShowListAdapter;
import com.example.myapplication.data.Msg;

import java.util.List;

public class ChatActivity extends AppCompatActivity {


    private List<Msg> mMsgList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



//        Msg msg1=new Msg("hello",Msg.TYPE_RECEIVED);
//        Msg msg2=new Msg("hello",Msg.TYPE_SENT);
//        mMsgList.add(msg1);
//        mMsgList.add(msg2);

//        LinearLayoutManager layoutManager=new LinearLayoutManager(ChatActivity.this);
//        msgRecyclerView.setLayoutManager(layoutManager);
//        MsgListAdapter msgListAdapter=new MsgListAdapter(mMsgList);
//        msgRecyclerView.setAdapter(msgListAdapter);

    }
}