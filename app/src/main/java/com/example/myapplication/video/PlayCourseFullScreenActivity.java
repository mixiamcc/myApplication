package com.example.myapplication.video;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.myapplication.R;

public class PlayCourseFullScreenActivity extends AppCompatActivity {

    private VideoView vvFullCourseVideo;
    private MediaController mFullController;
    private String courseUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_course_full_screen);
        courseUrl=getIntent().getStringExtra("courseUrl");
        vvFullCourseVideo=findViewById(R.id.vv_full_course_video);
        mFullController=new MediaController(this);
        vvFullCourseVideo.setVideoPath(courseUrl);
        vvFullCourseVideo.setMediaController(mFullController);
        mFullController.setMediaPlayer(vvFullCourseVideo);
        vvFullCourseVideo.start();

    }
}