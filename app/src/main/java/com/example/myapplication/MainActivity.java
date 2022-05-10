package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Socket.Login;
import com.example.myapplication.data.User;
import com.example.myapplication.data.UserData;

import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.example.myapplication.Socket.Login;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    EditText username;
    EditText password;
    Button mBtnSignUp;
    Button mBtnSignIn;
    int count = 0;
    String returnCode="";
    HashMap<String,String> resultmap=new HashMap<String,String>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);//更换早晚
        imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
//                    textView.setText("录");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
//                    textView.setText("录");
                    count = 0;
                }
            }

            public void onSwipeLeft() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                   // textView.setText("Night");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                  //  textView.setText("Morning");
                    count = 0;
                }
            }

            public void onSwipeBottom() {
            }

        });

        //创建数据库
        Connector.getDatabase();

        //注册跳转
        mBtnSignUp=findViewById(R.id.btn_signup);
        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("jump","跳转注册页面");
                Intent intent=new Intent(MainActivity.this,SignupActivity.class);
                //startActivity(intent);
                startActivityForResult(intent,1);
            }
        });


        //登录
        mBtnSignIn=findViewById(R.id.btn_signin);
        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<UserData> users = LitePal.findAll(UserData.class);
                username = findViewById(R.id.et_username);
                password = findViewById(R.id.et_password);
                int i=0;
                for(UserData user:users) {
                    Log.d("i",i+"");
                    i++;
                    System.out.println(user.getUsername()+user.getPassword());
                    if (user.getUsername().equals(username.getText().toString())) {
                        if (user.getPassword().equals(password.getText().toString())) {
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.putExtra("username", username.getText().toString());
                            startActivity(intent);
                            //finish();
                            onDestroy();
                        } else {
                            Toast.makeText(MainActivity.this, "账号或密码错误！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                Toast.makeText(MainActivity.this, "账号未注册！", Toast.LENGTH_SHORT).show();

//                Log.d("连接服务器0：", "192.168.1.58");
//
//                new Thread() {
//
//                    @Override
//                    public void run() {
//
//                        try {
//                            // 建立连接到远程服务器的Socket
//                            Socket socket = new Socket("192.168.1.58", 23000);  // ①
//                            Log.d("连接服务器成功：", "192.168.1.58");
//
//                            HashMap<String, String> request = new HashMap<>();
//                            request.put("requestCode", "LOGIN");
//                            String user_name = username.getText().toString();
//                            String pass_word = password.getText().toString();
//                            request.put("username", user_name);
//                            ;
//                            request.put("password", pass_word);
//                            //System.out.println(request);
//                            OutputStream os = socket.getOutputStream();
//
//                            System.out.println("请求数据");
//                            //System.out.println(request.get("requestCode"));
//                            Gson gson = new Gson();
//
//                            String requestStr = gson.toJson(request);
//
//                            //request=JSON.parseObject(requestStr);
//
//                            os.write(requestStr.getBytes());
//                            //os.write(request.toString().getBytes());
//                            System.out.println(request);
//                            os.flush();
//                            socket.shutdownOutput();
//
//
//                            // 将Socket对应的输入流包装成BufferedReader
//                            InputStream is = socket.getInputStream();
//                            String resultStr = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining(System.lineSeparator()));
//                            //HashMap<String,String> resultmap=new HashMap<String,String>();
//                            resultmap = gson.fromJson(resultStr, new TypeToken<HashMap<String, String>>() {
//                            }.getType());
//
//                            //String returnCode;
//                            returnCode = resultmap.get("returnCode");
//                            // 进行普通I/O操作
//                            //System.out.println(returnCode);
//                            System.out.println(resultStr);
//                            // 关闭输入流、socket
//                            is.close();
//                            os.close();
//                            socket.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            Log.d("服务器：", "失败");
//                        }
//                    }
//                }.start();
//
//
//                returnCode="access";
//                while (true) {
//
//                    if(returnCode!="") {
//                        System.out.println(returnCode);
//                        if (returnCode.equals("access")) {
//                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                            intent.putExtra("username", username.getText().toString());
//                            startActivity(intent);
//                            //finish();
//                            onDestroy();
//                        } else {
//                            Toast.makeText(MainActivity.this, "账号或密码错误！", Toast.LENGTH_SHORT).show();
//                        }
//                        Toast.makeText(MainActivity.this, "账号未注册！", Toast.LENGTH_SHORT).show();
//
//                    }
//                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //接受传回来的账号
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    final EditText loginUsername = findViewById(R.id.et_username);
                    String returnUsername = data.getStringExtra("username");
                    loginUsername.setText(returnUsername);
                    loginUsername.setSelection(returnUsername.length());
                }
                break;
            default:
        }
    }
}
