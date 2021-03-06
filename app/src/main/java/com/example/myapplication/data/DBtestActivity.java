package com.example.myapplication.data;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class DBtestActivity extends Activity {

    private Button btn_get_data;
    private TextView tv_data;
    private static final int TEST_USER_SELECT=1;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.d("结果：",(String)msg.obj);
            switch (msg.what){
                case 0x11:
                    String s = (String) msg.obj;
                    Log.d("结果：",s);
                    tv_data.setText(s);
                    break;
                case 0x12:
                    String ss = (String) msg.obj;
                    tv_data.setText(ss);
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);

        // 控件的初始化
        btn_get_data = findViewById(R.id.btn_get_data);
        tv_data = findViewById(R.id.tv_data);
        Log.d("test2","跳转数据库测试");
        setListener();
    }

    /**
     * 设置监听
     */
    private void setListener() {

        // 按钮点击事件
        btn_get_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 创建一个线程来连接数据库并获取数据库中对应表的数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 调用数据库工具类DBUtils的getInfoByName方法获取数据库表中数据
                        HashMap<String, Object> map = DBUtils.getInfoByName("test");
                        Message message = handler.obtainMessage();
                        if(map != null){
                            String s = "";
                            for (String key : map.keySet()){
                                s += key + ":" + map.get(key) + "\n";
                            }
                            //消息的定位标志
                            message.what =0x11;
                            message.obj = s;
                        }else {
                            message.what = 0x12;
                            message.obj = "查询结果为空";
                        }
                        // 发消息通知主线程更新UI
                        handler.sendMessage(message);
                    }
                }).start();


            }
        });

    }
}