package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.adapter.MsgAdapter;
import com.example.myapplication.data.Msg;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener{
    private List<Msg> msgList = new ArrayList<>();
    private EditText inputText;
    private Button send;
    private Button back;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    private Socket socketSend;
    private String ip="192.168.1.58";
    private String port="6666";
    DataInputStream dis;
    DataOutputStream dos;
    boolean isRunning = false;
    private TextView myName;
    private String recMsg;
    private boolean isSend=false;
    private String name;
    private Handler handler = new Handler(Looper.myLooper()){//获取当前进程的Looper对象传给handler
        @Override
        public void handleMessage(@NonNull Message msg){//?
            if(!recMsg.isEmpty()){
                addNewMessage(recMsg,Msg.TYPE_RECEIVED);//添加新数据
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Intent intent =getIntent();
        name=intent.getStringExtra("name");
        inputText = findViewById(R.id.input_text);
        send=findViewById(R.id.send);
        send.setOnClickListener(this);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder dialog= new AlertDialog.Builder(ChatRoomActivity.this);
                dialog.setTitle("退出");
                dialog.setMessage("退出聊天？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();//finish()是在程序执行的过程中使用它来将对象销毁,finish（）方法用于结束一个Activity的生命周期
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();//让返回键开始启动
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager layoutManager = new LinearLayoutManager(ChatRoomActivity.this);
                msgRecyclerView= findViewById(R.id.msg_recycler_view);
                msgRecyclerView.setLayoutManager(layoutManager);
                adapter = new MsgAdapter(msgList);
                msgRecyclerView.setAdapter(adapter);
            }
        });
        new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    if((socketSend = new Socket(ip,Integer.parseInt(port)))==null){
                        Log.d("ttw","发送了一条消息1");
                    }
                    else{
                        isRunning = true;
                        Log.d("ttw","发送了一条消息2");
                        dis = new DataInputStream(socketSend.getInputStream());
                        dos = new DataOutputStream(socketSend.getOutputStream());
                        new Thread(new receive(),"接收线程").start();
                        new Thread(new Send(),"发送线程").start();
                    }
                }catch(Exception e){
                    isRunning = false;
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(ChatRoomActivity.this, "连接服务器失败！！！", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    try{
                        socketSend.close();
                    }catch(IOException e1){
                        e1.printStackTrace();
                    }
                    finish();
                }
            }
        }).start();
    }
    public void addNewMessage(String msg,int type){
        Msg message = new Msg(msg,type);
        msgList.add(message);
        adapter.notifyItemInserted(msgList.size()-1);
        msgRecyclerView.scrollToPosition(msgList.size()-1);
    }
    class receive implements Runnable{
        public void run(){
            recMsg = "";
            while(isRunning){
                try{
                    recMsg = dis.readUTF();
                    Log.d("ttw","收到了一条消息"+"recMsg: "+ recMsg);
                }catch(Exception e){
                    e.printStackTrace();
                }
                if(!TextUtils.isEmpty(recMsg)){
                    Log.d("ttw","inputStream:"+dis);
                    Message message = new Message();
                    message.obj=recMsg;
                    handler.sendMessage(message);
                }
            }
        }
    }
    @Override
    public void onClick(View view){
        String content = inputText.getText().toString();
        @SuppressLint("SimpleDateFormat")
        String date = new SimpleDateFormat("hh:mm:ss").format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append(content).append("\n\n"+date);
        content = sb.toString();
        if(!"".equals(content)){
            Msg msg = new Msg(content,Msg.TYPE_SENT);
            msgList.add(msg);
            adapter.notifyItemInserted(msgList.size()-1);
            msgRecyclerView.scrollToPosition(msgList.size()-1);
            isSend = true;
        }
        sb.delete(0,sb.length());
    }
    class Send implements Runnable{
        @Override
        public void run(){
            while(isRunning){
                String content = inputText.getText().toString();
                Log.d("ttw","发了一条消息");
                if(!"".equals(content)&&isSend){
                    @SuppressLint("SimpleDateFormat")
                    String date = new SimpleDateFormat("hh:mm:ss").format(new Date());
                    StringBuilder sb = new StringBuilder();
                    sb.append(content).append("\n\n来自：").append(name).append("\n"+date);
                    content = sb.toString();
                    try{
                        dos.writeUTF(content);
                        sb.delete(0,sb.length());
                        Log.d("ttw","发送了一条消息");
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    isSend = false;
                    inputText.setText("");
                }
            }
        }
    }
}
