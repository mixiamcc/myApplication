package com.example.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.litepal.LitePal;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.myapplication.data.UserData;
import com.example.myapplication.util.ToastUtil;
import com.example.myapplication.widget.ItemGroup;
import com.example.myapplication.widget.RoundImageView;
import com.example.myapplication.widget.TitleLayout;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditInfoActivity extends AppCompatActivity {

    private String username;
    private RoundImageView userheader;
    private TextView tvEditHeader;
    private ItemGroup igUsername,igEditNickname,igEditPassword,igEditGender,igEditCategory,igEditBirthday;

    private TitleLayout titleLayout;
    private UserData user;
    private String imagePath;

    private PopupWindow popupWindow;
    private final int TAKE_PHOTO=1;
    private final int FROM_ALBUMS=2;
    private final String EXTRA_OUTPUT="3";

    private photoUtils photoUtils=new photoUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        this.username = getIntent().getStringExtra("username");
        List<UserData> users = LitePal.findAll(UserData.class);
        for (UserData user : users) {
            if (user.getUsername().equals(username)) {
                this.user = user;
            }
        }
        userheader=findViewById(R.id.ri_header);
        igUsername=findViewById(R.id.ig_username);
        tvEditHeader = findViewById(R.id.edit_header);
        igEditNickname = findViewById(R.id.ig_edit_nickname);
        igEditPassword=findViewById(R.id.ig_edit_password);
        igEditCategory = findViewById(R.id.ig_edit_category);
        igEditGender = findViewById(R.id.ig_edit_gender);
        igEditBirthday=findViewById(R.id.ig_edit_birthday);

        titleLayout=findViewById(R.id.tl_title);
        TextView tvSave = (TextView) titleLayout.findViewById(R.id.tv_save);


        //刷新页面数据
        flashInfoView();

        OnClick onClick = new OnClick();
        tvEditHeader.setOnClickListener(onClick);
        igEditNickname.setOnClickListener(onClick);
        igEditPassword.setOnClickListener(onClick);
        igEditCategory.setOnClickListener(onClick);
        igEditGender.setOnClickListener(onClick);
        igEditBirthday.setOnClickListener(onClick);


        tvSave.setOnClickListener(onClick);

    }


        class OnClick implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.edit_header:
                        show_popup_windows();
                        break;
                    case R.id.ig_edit_nickname:
                        AlertDialog.Builder builder2=new AlertDialog.Builder(EditInfoActivity.this);
                        View view2= LayoutInflater.from(EditInfoActivity.this).inflate(R.layout.layout_dialog_nickname,null);
                        EditText etNickName = view2.findViewById(R.id.et_nickname);
                        Button btnAffirm=view2.findViewById(R.id.btn_nick_affirm);
                        btnAffirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String nickname=etNickName.getText().toString();
                                user.setNickname(nickname);
                                //更新昵称显示
                                igEditNickname.flashContext(nickname);
                                ToastUtil.showMsg(EditInfoActivity.this,"修改成功");

                            }
                        });
                        builder2.setTitle("设置昵称").setView(view2).show();
                        break;
                    case R.id.ig_edit_password:
                        AlertDialog.Builder builder3=new AlertDialog.Builder(EditInfoActivity.this);
                        View view3= LayoutInflater.from(EditInfoActivity.this).inflate(R.layout.layout_dialog_password,null);
                        EditText etOldPassword=view3.findViewById(R.id.et_old_password);
                        EditText etPassword1= view3.findViewById(R.id.et_re_password1);
                        EditText etPassword2=view3.findViewById(R.id.et_re_password2);
                        Button btnAffirm3=view3.findViewById(R.id.btn_password_affirm);
                        btnAffirm3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String oldp=etOldPassword.getText().toString();
                                String p1=etPassword1.getText().toString();
                                String p2=etPassword2.getText().toString();
                                Log.d("oldp:",oldp);Log.d("p1:",p1);Log.d("p2:",p2);

                                if(oldp.equals(user.getPassword()))
                                {//旧密码正确
                                    if(p1.equals(p2)) {//两次密码一致
                                        user.setPassword(p1);
                                        ToastUtil.showMsg(EditInfoActivity.this,"密码修改成功");
                                    }
                                    else{//密码不一致
                                    ToastUtil.showMsg(EditInfoActivity.this,"两次密码不一致，请重新输入");
                                    etOldPassword.setText("");
                                    etPassword1.setText("");
                                    etPassword2.setText("");
                                    }
                                }
                                else//旧密码不正确
                                {
                                    ToastUtil.showMsg(EditInfoActivity.this,"密码输入错误");

                                }

                            }
                        });
                        builder3.setTitle("设置密码").setView(view3).show();
                        break;
                    case R.id.ig_edit_category:
                        final String[] array3 = new String[]{"志愿者", "学生"};
                        AlertDialog.Builder builder4=new AlertDialog.Builder(EditInfoActivity.this);
                        builder4.setTitle("选择身份").setSingleChoiceItems(array3, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                user.setCategory(array3[i]);
                                ToastUtil.showMsg(EditInfoActivity.this, array3[i]);
                                igEditCategory.flashContext(array3[i]);
                                dialogInterface.dismiss();//选择完自动消失
                            }
                        }).show();
                        break;
                    case R.id.ig_edit_gender:
                        final String[] array4 = new String[]{"男", "女","保密"};
                        AlertDialog.Builder builder5=new AlertDialog.Builder(EditInfoActivity.this);
                        builder5.setTitle("选择性别").setSingleChoiceItems(array4, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                user.setGender(array4[i]);
                                ToastUtil.showMsg(EditInfoActivity.this, array4[i]);
                                igEditGender.flashContext(array4[i]);
                                dialogInterface.dismiss();//选择完自动消失
                            }
                        }).show();
                        break;
                    //点击修改生日逻辑
                    case R.id.ig_edit_birthday:
                        //时间选择器
                        //修改打开的默认时间，如果选择过则是选择过的时间，否则是系统默认时间
                        Calendar selectedDate = Calendar.getInstance();
                        if (user.getBirthday() != null){
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                                selectedDate.setTime(sdf.parse(user.getBirthday()));
                            }catch (ParseException e){
                                e.printStackTrace();
                            }
                        }
                        //初始化picker并show
                        TimePickerView pvTime = new TimePickerBuilder(EditInfoActivity.this, new OnTimeSelectListener() {
                            @Override
                            public void onTimeSelect(Date date, View v) {
                                //选择了则显示并暂存user，退出时保存至数据库
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                                igEditBirthday.getContentEdt().setText(sdf.format(date));
                                user.setBirthday(sdf.format(date));
                            }
                        }).setDate(selectedDate).setCancelColor(Color.GRAY).build();
                        pvTime.show();
                        break;
                    case R.id.tv_save:
                        user.save();
                        Log.d("数据保存:","成功");
                        ToastUtil.showMsg(EditInfoActivity.this,"保存成功");

                }

            }

    }
    //页面刷新
    void flashInfoView()
    {
        Bitmap bitmap;
        bitmap=BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/mipmap/default_header.png"));
//        if(user.getHeader()==null)
//        {
//            bitmap=BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/mipmap/default_header.png"));
//        }
//        else {
//            bitmap=photoUtils.Bytes2Bimap(user.getHeader());
//        }
        userheader.setImageBitmap(bitmap);
        //user.setHeader(photoUtils.bitmap2byte(bitmap));
        //userheader.setImageBitmap((photoUtils.Bytes2Bimap(user.getHeader())));
        igUsername.flashContext(user.getUsername());
        igEditNickname.flashContext(user.getNickname());
        igEditCategory.flashContext(user.getCategory());
        igEditGender.flashContext(user.getGender());
        igEditBirthday.flashContext(user.getBirthday());
        igEditBirthday.flashContext(user.getBirthday());

    }
    //展示修改头像的选择框，并设置选择框的监听器
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
                    if(ContextCompat.checkSelfPermission( EditInfoActivity.this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(EditInfoActivity.this, new String[]{Manifest.permission.CAMERA},
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
                if(ContextCompat.checkSelfPermission(EditInfoActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(EditInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
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
                    userheader.setImageBitmap(bitmap);
                    user.setHeader(photoUtils.bitmap2byte(bitmap));
//                    try {
//                        //将拍摄的图片展示并更新数据库
//                        Bitmap bitmap = BitmapFactory.decodeStream((getContentResolver().openInputStream(imageUri)));
//                        userheader.setImageBitmap(bitmap);
//                        user.setHeader(bitmap);
//                    }catch (FileNotFoundException e){
//                        e.printStackTrace();
//                    }
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
                    userheader.setImageBitmap(bitmap);
                    user.setHeader(photoUtils.bitmap2byte(bitmap));
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
            if (ContextCompat.checkSelfPermission(EditInfoActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //如果用户已经拒绝过一次权限申请，该方法返回true
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        EditInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //提示用户这一权限的重要性
                    Toast.makeText(EditInfoActivity.this, "读取SD卡功能是本应用的核心"
                                    + "功能，如果不授予权限，程序是无法正常工作！",
                            Toast.LENGTH_SHORT).show();
                }
                //请求权限
                ActivityCompat.requestPermissions(EditInfoActivity.this,
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
                        Toast.makeText(EditInfoActivity.this, "未授予权限的情况下，程序无法正常工作",Toast.LENGTH_SHORT).show();
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
            if (DocumentsContract.isDocumentUri(EditInfoActivity.this, uri)) {
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
                userheader.setImageBitmap(bitmap);
            } else {
                Toast.makeText(EditInfoActivity.this, "failed to get image", Toast.LENGTH_SHORT).show();
            }
        }
    }


}