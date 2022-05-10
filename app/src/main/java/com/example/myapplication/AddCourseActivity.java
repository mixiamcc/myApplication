package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.myapplication.data.Course;
import com.example.myapplication.data.UserData;
import com.example.myapplication.util.PhotoTools;
import com.example.myapplication.util.ToastUtil;

import androidx.appcompat.app.AlertDialog;

import Bean.MediaExtraBean;
import Bean.MediaUtils;
import org.litepal.LitePal;

import java.util.List;

public class AddCourseActivity extends AppCompatActivity {
    Button btnSelectCategory;
    Button btnOnclickUpVideo;//选择视频
    Button btnUpCourse;//发布
    //VideoView upVideoView;
    ImageView ivUpCoursePhoto;
    EditText etCourseName;
    String username;

    //UserData user;

    Course course=new Course();
    final String[] category = new String[]{"数学", "语文","英语","音乐","美术","编程","心理"};
    boolean[] isSelected=new boolean[]{false,false,false,false,false,false,false};

    int PHOTO_VIDEO= 103; //视频
    String videoTimeStr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        //upVideoView=findViewById(R.id.vv_up_course_video);

        etCourseName=findViewById(R.id.et_course_name);
        username=getIntent().getStringExtra("username");

//        List<UserData> users = LitePal.findAll(UserData.class);
//        for (UserData user : users) {
//            if (user.getUsername().equals(username)) {
//                this.user = user;
//            }
//        }


        ivUpCoursePhoto=findViewById(R.id.iv_add_course_photo);
        btnOnclickUpVideo=findViewById(R.id.btn_onclick_up_course);
        btnOnclickUpVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLocalVideo();
            }
        });
        btnSelectCategory=findViewById(R.id.btn_select_category);
        btnSelectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder4 = new AlertDialog.Builder(AddCourseActivity.this);
                builder4.setTitle("选择课程类别").setMultiChoiceItems(category, isSelected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                        course.setCourseCategory(category[i]);
                        ToastUtil.showMsg(AddCourseActivity.this,category[i]+":"+isChecked);
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //

                    }
                }).show();
            }
        });
        btnUpCourse=findViewById(R.id.btn_up_course);
        btnUpCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //user.getCourseList().add(course);
                //user.save();
                course.save();
                finish();
            }
        });
    }

    private void openLocalVideo(){

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,"video/*");
        startActivityForResult(intent,PHOTO_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("YM","本地视频获取的数据:requestCode->"+requestCode+"-->resultCode:"+resultCode);
        if (requestCode == PHOTO_VIDEO){
            Uri videoUri = data.getData();
            //upVideoView.setVideoURI(videoUri);
            MediaUtils mediaUtils=new MediaUtils(this);
            MediaExtraBean mediaExtraBean=mediaUtils.getRingDuring(videoUri);
            ivUpCoursePhoto.setImageURI(mediaExtraBean.getThumbPath());


            course.setAuthorId(username);
            course.setCourseUrl(mediaExtraBean.getLocalPath());
            course.setCourseName(etCourseName.getText().toString());

            course.setCoursePhoto(mediaExtraBean.getThumbPath());
            //
            //Log.d("photopath",mediaExtraBean.getImagePath());
//            Bitmap bitmap= BitmapFactory.decodeStream(getClass().getResourceAsStream(mediaExtraBean.getImagePath()));
//            course.setCoursePhoto(PhotoTools.bitmap2byte(bitmap));

            //upVideoView.setVideoPath(mediaExtraBean.getLocalPath());
            //ivUpCoursePhoto.findViewById()

        }
    }



//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,Intent data1) {
//        super.onActivityResult(requestCode, resultCode, data1);
//        Log.e("uploadFile", "" + requestCode);
//        try {
//            if (data1 != null) {
//                //获取文件路径，工具类代码在下方
//                String realPathFromUri = UriUtils.getPath(this, data1.getData());
//                ;
//                Log.e("uploadFile", realPathFromUri);
////                if (requestCode == PICK_PHOTO) {
////                    Toast.makeText(this, "图片上传中，请稍等", Toast.LENGTH_SHORT).show();
////                    upload(BitmapBase64.imageToBase64(realPathFromUri));
////                } else
//                   if (requestCode == PHOTO_VIDEO) {
//                    File file = new File(realPathFromUri);
//                    MediaPlayer player = new MediaPlayer();
//                    try {
//                        player.setDataSource(realPathFromUri); //recordingFilePath（）为音频文件的路径
//                        player.prepare();
//                    } catch (IOException e){
//                        e.printStackTrace();
//                    } catch(java.lang.Exception e){
//                        e.printStackTrace();
//                    }
//                    Double duration = Double.valueOf(player.getDuration()); //获取音频的时间
//                    int videoTime = (int)(duration/1000);
//                    Log.d("uploadFile", "### duration: $videoTime  ");
//                    videoTimeStr = String.valueOf(videoTime);
//                    player.release(); //记得释放资源
//                    Toast.makeText(this, "视频上传中，请稍等", Toast.LENGTH_SHORT).show();
//                    //uploadFile(file);
//                }
//
//            } else {
//                Log.d("uploadFile", "文件损坏，请重新选择");
//                Toast.makeText(this, "文件损坏，请重新选择", Toast.LENGTH_SHORT).show();
//            }
//
//        } catch (java.lang.Exception e) {
//            e.printStackTrace();
//            Log.e("uploadFile", e.toString());
//        }
//
//    }



//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 1){
//            if (data != null) {
//                final String filepath = data.getExtras().getString("path");
//                //final VideoView videoView = new VideoView(AddCourseActivity.this);
//                Uri uri = Uri.parse(filepath);
//                RelativeLayout.LayoutParams layoutParams =
//                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                upVideoView.setLayoutParams(layoutParams);
//                upVideoView=findViewById(R.id.vv_up_course_video);
//                upVideoView.setVideoURI(uri);
//                upVideoView.setMediaController(new MediaController(this));
//                upVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mp) {
////                     videoView.start();
//                    }
//                });
////                ll_pics.addView(videoView,ll_pics.getChildCount() - 1);//此处自己用一个LinearLayout控件来进行装载就ok了
//                Log.e( "onActivityResult: " ,filepath);
//                File file = new File(filepath);
//                Log.e("filename----",file.getName());
////       receiveHandler.sendEmptyMessage(HANDLE_UPLOAD_IMG);//实现上传方法
//            }
//        }
//    }

}