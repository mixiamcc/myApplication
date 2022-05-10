package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.UserData;

import org.litepal.LitePal;

import java.util.List;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        Button mBtnSignUP = findViewById(R.id.btn_signup2);
        mBtnSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = findViewById(R.id.et_username2);
                EditText password = findViewById(R.id.et_password2);
                EditText passwordAffirm = findViewById(R.id.et_passwordaffirm);
                String inputUsername = username.getText().toString();
                String inputPassword = password.getText().toString();
                String inputAffirm = passwordAffirm.getText().toString();
                UserData user = new UserData();
                if (inputAffirm.equals(inputPassword)) {
                    //存储账号密码
                    user.setUsername(inputUsername);
                    user.setPassword(inputPassword);
                    user.save();
                    System.out.println(user.getUsername()+user.getPassword());
                    Log.d("save","success");
                   // List<UserData> users = LitePal.findAll(UserData.class);

                    //传回账号
                    Intent intent = new Intent();
                    intent.putExtra("username", inputUsername);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}