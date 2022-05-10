package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.myapplication.adapter.CourseGridAdapter;
import com.example.myapplication.adapter.RecommendCourseListAdapter;
import com.example.myapplication.video.PlayCourseFullScreenActivity;

import java.util.ArrayList;
import java.util.List;

public class PlayCourseActivity extends AppCompatActivity {

    private VideoView vvCourseVideo;
    private TextView tvCourseName;
    private ListView lvRecommendCourse;
    private MediaController mController;
    private RelativeLayout relativeLayout;
    private Button btnFullPlay;
    private int position;
    private List<String> coursenameList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_course);
        coursenameList.add("Android程序设计");
        coursenameList.add("美术基础");
        coursenameList.add("数学基础");
        coursenameList.add("语文基础");
        coursenameList.add("Android开发");
        coursenameList.add("Java程序设计");

        Bundle bundle= getIntent().getExtras();
        position=bundle.getInt("position");
        vvCourseVideo=findViewById(R.id.vv_course_video);
        tvCourseName=findViewById(R.id.tv_course_name);
        tvCourseName.setText(coursenameList.get(position));

        mController=new MediaController(this);
        vvCourseVideo.setVideoPath("http://192.168.1.58:8081/MyServer/file/videosource/"+String.valueOf(position+1)+".mp4");
        vvCourseVideo.setMediaController(mController);
        //int screenHeight=getWindowManager().getDefaultDisplay().getHeight();
        //mController.setPadding(0, 0, 0, vvCourseVideo.getHeight());
        mController.setMediaPlayer(vvCourseVideo);
        vvCourseVideo.start();

        relativeLayout=findViewById(R.id.rl_vv_course);

        Log.d("相对布局宽度",relativeLayout.getWidth()+"");
        Log.d("视频宽度",vvCourseVideo.getWidth()+"");
        Log.d("视频路径","http://192.168.1.58:8081/MyServer/file/videosource/"+String.valueOf(position)+".mp4");

        lvRecommendCourse=findViewById(R.id.lv_course_recommend);
        lvRecommendCourse.setAdapter(new RecommendCourseListAdapter(PlayCourseActivity.this));
        lvRecommendCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(PlayCourseActivity.this,"i am "+i,Toast.LENGTH_SHORT);
            }
        });
        btnFullPlay=findViewById(R.id.btn_full_play);
        btnFullPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PlayCourseActivity.this, PlayCourseFullScreenActivity.class);
                intent.putExtra("courseUrl","http://192.168.1.58:8081/MyServer/file/videosource/"+String.valueOf(position+1)+".mp4" );
                startActivity(intent);
            }
        });

    }
}