package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.data.UserData;
import com.example.myapplication.data.UserShow;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import androidx.appcompat.app.ActionBar;

public class AddShowActivity extends AppCompatActivity {

    private String username;
    private UserData user;
    private ImageView ivShowBack;
    private Button btnUpShow;
    private EditText etUpShowSpeak;
    private ImageView ivUpShowPhoto;

    private PopupWindow popupWindow;
    private final int TAKE_PHOTO=1;
    private final int FROM_ALBUMS=2;
    private final String EXTRA_OUTPUT="3";

    List<UserShow> userShows=new ArrayList<>();
    UserShow userShow=new UserShow();
    private photoUtils photoUtils=new photoUtils();


    private String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_show);
        this.username = getIntent().getStringExtra("username");
        ivShowBack=findViewById(R.id.iv_show_back);
        btnUpShow=findViewById(R.id.btn_up_show);
        etUpShowSpeak=findViewById(R.id.et_up_show_speak);
        ivUpShowPhoto=findViewById(R.id.iv_up_show_photo);

        this.username = getIntent().getStringExtra("username");
        List<UserData> users = LitePal.findAll(UserData.class);
        for (UserData user : users) {
            if (user.getUsername().equals(username)) {
                this.user = user;
            }
        }

        ivUpShowPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_popup_windows();
            }
        });

        //如果点击back则结束活动
        ivShowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //设置监听器
        //保存数据
        btnUpShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //传回账号
                Date date = new Date();
//        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
//        System.out.println(dateFormat.format(date));
                userShow.setShowtime(date);
                userShows.add(userShow);

                //user.setShows(userShows);
                //user.getShows().add(userShow);
                //user.save();
                userShow.save();


                Intent intent = new Intent();
                intent.putExtra("result","success!");
                setResult(3, intent);
                finish();
            }
        });

    }
    private void show_popup_windows(){
        /**
         * 在这里首先说明一下popupWindow 和AlertDialog 的区别
         * AlertDialog 不能显示在指定的位置 只能显示在屏幕的中间
         * popupWindow 可以显示在任意的位置
         */
        RelativeLayout layout_photo_selected = (RelativeLayout) getLayoutInflater().inflate(R.layout.photo_select,null);
        if(popupWindow==null){
            popupWindow = new PopupWindow(layout_photo_selected, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        }
        //显示popupwindows
        popupWindow.showAtLocation(layout_photo_selected, Gravity.CENTER, 0, 0);
        //设置监听器
        TextView take_photo =  (TextView) layout_photo_selected.findViewById(R.id.take_photo);
        TextView from_albums = (TextView)  layout_photo_selected.findViewById(R.id.from_albums);
        LinearLayout cancel = (LinearLayout) layout_photo_selected.findViewById(R.id.cancel);
        //拍照按钮监听
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow != null && popupWindow.isShowing()) {
                    //调用相机，拍摄结果会存到imageUri也就是outputImage中
                    if(ContextCompat.checkSelfPermission( AddShowActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(AddShowActivity.this, new String[]{Manifest.permission.CAMERA},
                                1);
                    }
                    else {
                        //Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //intent.putExtra(EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, TAKE_PHOTO);
                    }
                    //去除选择框
                    popupWindow.dismiss();
                }
            }
        });
        //相册按钮监听
        from_albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //申请权限
                if(ContextCompat.checkSelfPermission(AddShowActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AddShowActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    //打开相册
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent, FROM_ALBUMS);
                }
                //去除选择框
                popupWindow.dismiss();
            }
        });
        //取消按钮监听
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
    }



    //处理拍摄照片回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        switch (requestCode){
            //拍照得到图片
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    //将拍摄的图片展示并更新数据库
                    Bundle bundle = data.getExtras();
                    //intent跳转携带参数返回，data转化成Bitmap，获得的是个略缩图
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    //Bitmap bitmap = BitmapFactory.decodeStream((getContentResolver().openInputStream(imageUri)));
                    userShow.setPhoto(photoUtils.bitmap2byte(bitmap));
                    ivUpShowPhoto.setImageBitmap(bitmap);
                }
                break;
            //从相册中选择图片
            case FROM_ALBUMS:
                if(resultCode == RESULT_OK){
                    //判断手机版本号
                    if(Build.VERSION.SDK_INT >= 19){
                        photoUtils.handleImageOnKitKat( data);
                    }else {
                        photoUtils.handleImageBeforeKitKat( data);
                    }
                }
                if(imagePath != null){
                    //将拍摄的图片展示并更新数据库
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    ivUpShowPhoto.setImageBitmap(bitmap);
                    userShow.setPhoto(photoUtils.bitmap2byte(bitmap));
                }else{
                    Log.d("food","没有找到图片");
                }
                break;
            default:
                break;
        }


    }
    public class photoUtils {

        private byte[] bitmap2byte(Bitmap bm)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            return data;
        }
        public Bitmap Bytes2Bimap(byte[] b) {
            if (b.length != 0) {
                return BitmapFactory.decodeByteArray(b, 0, b.length);
            } else {
                return null;
            }
        }

        //获取权限
        private void getPermission() {
            //如果没有权限，Android6.0之后，必须动态申请权限（记住这个套路）
            if (ContextCompat.checkSelfPermission(AddShowActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //如果用户已经拒绝过一次权限申请，该方法返回true
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        AddShowActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //提示用户这一权限的重要性
                    Toast.makeText(AddShowActivity.this, "读取SD卡功能是本应用的核心"
                                    + "功能，如果不授予权限，程序是无法正常工作！",
                            Toast.LENGTH_SHORT).show();
                }
                //请求权限
                ActivityCompat.requestPermissions(AddShowActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else { //权限已被授予，打开相册
                openAlbum();
            }
        }

        //弹出一个权限申请的对话框，并且用户做出选择后，该方法自动回调
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
                case 1:
                    if (grantResults.length > 0 && grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED) {
                        //权限已授予，打开相册
                        openAlbum();
                    } else {
                        //权限未授予
                        Toast.makeText(AddShowActivity.this, "未授予权限的情况下，程序无法正常工作",Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }

        private void openAlbum() {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        }

        //回调
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            if (requestCode == 1) {
                // 判断手机版本
                if (Build.VERSION.SDK_INT >= 19) {
                    // 4.4及以上系统使用这个方法处理图片
                    handleImageOnKitKat(data);
                } else {
                    // 4.4以下系统使用这个方法处理图片
                    handleImageBeforeKitKat(data);
                }
            }

        }


        @TargetApi(19)
        private void handleImageOnKitKat(Intent data) {
            String imagePath = null;
            Uri uri = data.getData();
            Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
            Log.d("TAG", "handleImageOnKitKat : " + uri.getAuthority());
            Log.d("TAG", "handleImageOnKitKat urlPath: " + uri.getScheme());
            if (DocumentsContract.isDocumentUri(AddShowActivity.this, uri)) {
                // 如果是document类型的Uri，则通过document id处理
                String docId = DocumentsContract.getDocumentId(uri);
                Log.d("TAG", "handleImageOnKitKat: docId" + docId);

                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1]; // 解析出数字格式的id
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
                    imagePath = getImagePath(contentUri, null);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                // 如果是content类型的Uri，则使用普通方式处理
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                // 如果是file类型的Uri，直接获取图片路径即可
                imagePath = uri.getPath();
            }
            displayImage(imagePath); // 根据图片路径显示图片
        }

        private void handleImageBeforeKitKat(Intent data) {
            Uri uri = data.getData();
            String imagePath = getImagePath(uri, null);
            displayImage(imagePath);
        }

        private String getImagePath(Uri uri, String selection) {
            String path = null;
            // 通过Uri和selection来获取真实的图片路径
            Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int i=cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    path=cursor.getString(i);
                }
                cursor.close();
            }
            Log.e("TAG", "getImagePath: " + path);
            return path;
        }

        private void displayImage(String ip) {
            imagePath = ip;
            if (imagePath != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                ivUpShowPhoto.setImageBitmap(bitmap);
            } else {
                Toast.makeText(AddShowActivity.this, "failed to get image", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
