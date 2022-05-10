package com.example.myapplication.Socket;

import android.util.Log;

import com.example.myapplication.data.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Login {

    public String returnCode;
    User user=new User();
    HashMap<String,String> resultmap=new HashMap<String,String>();
    public String getReturnCode()
    {
        return returnCode;
    }
    public User getUser()
    {
        return user;
    }
    public void initUser()
    {
        user.setUsername(resultmap.get("username"));
        user.setPassword(resultmap.get("userpassword"));
        user.setUserid(resultmap.get("Userid"));
        user.setCategory(resultmap.get("category"));
    }
    public void  LoginCon(String username, String password) {

        new Thread() {

            @Override
            public void run() {

                try {
                    // 建立连接到远程服务器的Socket
                    Socket socket = new Socket("192.168.1.58", 23000);  // ①
                    Log.d("连接服务器成功：", "192.168.1.58");

                    HashMap<String, String> request = new HashMap<>();
                    request.put("requestCode", "LOGIN");
                    request.put("username", username);
                    request.put("password", password);
                    System.out.println(request);
                    OutputStream os = socket.getOutputStream();

                    System.out.println("请求数据");
                    Gson gson = new Gson();

                    os.write(gson.toJson(request).getBytes(StandardCharsets.UTF_8));
                    //os.write(request.toString().getBytes(StandardCharsets.UTF_8));
                    System.out.println(request);
                    os.flush();
                    socket.shutdownOutput();


                    // 将Socket对应的输入流包装成BufferedReader
                    InputStream is = socket.getInputStream();
                    String resultStr = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining(System.lineSeparator()));

                    resultmap=gson.fromJson(resultStr,resultmap.getClass());

                    returnCode=resultmap.get("returnCode");
                    // 进行普通I/O操作
                    System.out.println(returnCode);
                    System.out.println(resultStr);
                    // 关闭输入流、socket
                    is.close();
                    os.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("服务器：", "失败");
                }
            }
        }.start();
    }

}
