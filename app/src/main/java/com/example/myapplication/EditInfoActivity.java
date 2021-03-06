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


        //??????????????????
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
                                //??????????????????
                                igEditNickname.flashContext(nickname);
                                ToastUtil.showMsg(EditInfoActivity.this,"????????????");

                            }
                        });
                        builder2.setTitle("????????????").setView(view2).show();
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
                                {//???????????????
                                    if(p1.equals(p2)) {//??????????????????
                                        user.setPassword(p1);
                                        ToastUtil.showMsg(EditInfoActivity.this,"??????????????????");
                                    }
                                    else{//???????????????
                                    ToastUtil.showMsg(EditInfoActivity.this,"???????????????????????????????????????");
                                    etOldPassword.setText("");
                                    etPassword1.setText("");
                                    etPassword2.setText("");
                                    }
                                }
                                else//??????????????????
                                {
                                    ToastUtil.showMsg(EditInfoActivity.this,"??????????????????");

                                }

                            }
                        });
                        builder3.setTitle("????????????").setView(view3).show();
                        break;
                    case R.id.ig_edit_category:
                        final String[] array3 = new String[]{"?????????", "??????"};
                        AlertDialog.Builder builder4=new AlertDialog.Builder(EditInfoActivity.this);
                        builder4.setTitle("????????????").setSingleChoiceItems(array3, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                user.setCategory(array3[i]);
                                ToastUtil.showMsg(EditInfoActivity.this, array3[i]);
                                igEditCategory.flashContext(array3[i]);
                                dialogInterface.dismiss();//?????????????????????
                            }
                        }).show();
                        break;
                    case R.id.ig_edit_gender:
                        final String[] array4 = new String[]{"???", "???","??????"};
                        AlertDialog.Builder builder5=new AlertDialog.Builder(EditInfoActivity.this);
                        builder5.setTitle("????????????").setSingleChoiceItems(array4, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                user.setGender(array4[i]);
                                ToastUtil.showMsg(EditInfoActivity.this, array4[i]);
                                igEditGender.flashContext(array4[i]);
                                dialogInterface.dismiss();//?????????????????????
                            }
                        }).show();
                        break;
                    //????????????????????????
                    case R.id.ig_edit_birthday:
                        //???????????????
                        //???????????????????????????????????????????????????????????????????????????????????????????????????
                        Calendar selectedDate = Calendar.getInstance();
                        if (user.getBirthday() != null){
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy???MM???dd???");
                                selectedDate.setTime(sdf.parse(user.getBirthday()));
                            }catch (ParseException e){
                                e.printStackTrace();
                            }
                        }
                        //?????????picker???show
                        TimePickerView pvTime = new TimePickerBuilder(EditInfoActivity.this, new OnTimeSelectListener() {
                            @Override
                            public void onTimeSelect(Date date, View v) {
                                //???????????????????????????user??????????????????????????????
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy???MM???dd???");
                                igEditBirthday.getContentEdt().setText(sdf.format(date));
                                user.setBirthday(sdf.format(date));
                            }
                        }).setDate(selectedDate).setCancelColor(Color.GRAY).build();
                        pvTime.show();
                        break;
                    case R.id.tv_save:
                        user.save();
                        Log.d("????????????:","??????");
                        ToastUtil.showMsg(EditInfoActivity.this,"????????????");

                }

            }

    }
    //????????????
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
    //???????????????????????????????????????????????????????????????
    private void show_popup_windows(){
        /**
         * ???????????????????????????popupWindow ???AlertDialog ?????????
         * AlertDialog ?????????????????????????????? ??????????????????????????????
         * popupWindow ??????????????????????????????
         */
        RelativeLayout layout_photo_selected = (RelativeLayout) getLayoutInflater().inflate(R.layout.photo_select,null);
        if(popupWindow==null){
            popupWindow = new PopupWindow(layout_photo_selected, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        }
        //??????popupwindows
        popupWindow.showAtLocation(layout_photo_selected, Gravity.CENTER, 0, 0);
        //???????????????
        TextView take_photo =  (TextView) layout_photo_selected.findViewById(R.id.take_photo);
        TextView from_albums = (TextView)  layout_photo_selected.findViewById(R.id.from_albums);
        LinearLayout cancel = (LinearLayout) layout_photo_selected.findViewById(R.id.cancel);
        //??????????????????
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow != null && popupWindow.isShowing()) {
                    //????????????????????????????????????imageUri?????????outputImage???
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
                    //???????????????
                    popupWindow.dismiss();
                }
            }
        });
        //??????????????????
        from_albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //????????????
                if(ContextCompat.checkSelfPermission(EditInfoActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(EditInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    //????????????
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent, FROM_ALBUMS);
                }
                //???????????????
                popupWindow.dismiss();
            }
        });
        //??????????????????
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
    }



    //????????????????????????
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        switch (requestCode){
            //??????????????????
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    //??????????????????????????????????????????
                    Bundle bundle = data.getExtras();
                    //intent???????????????????????????data?????????Bitmap???????????????????????????
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    //Bitmap bitmap = BitmapFactory.decodeStream((getContentResolver().openInputStream(imageUri)));
                    userheader.setImageBitmap(bitmap);
                    user.setHeader(photoUtils.bitmap2byte(bitmap));
//                    try {
//                        //??????????????????????????????????????????
//                        Bitmap bitmap = BitmapFactory.decodeStream((getContentResolver().openInputStream(imageUri)));
//                        userheader.setImageBitmap(bitmap);
//                        user.setHeader(bitmap);
//                    }catch (FileNotFoundException e){
//                        e.printStackTrace();
//                    }
                }
                break;
            //????????????????????????
            case FROM_ALBUMS:
                if(resultCode == RESULT_OK){
                    //?????????????????????
                    if(Build.VERSION.SDK_INT >= 19){
                        photoUtils.handleImageOnKitKat( data);
                    }else {
                        photoUtils.handleImageBeforeKitKat( data);
                    }
                }
                if(imagePath != null){
                    //??????????????????????????????????????????
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    userheader.setImageBitmap(bitmap);
                    user.setHeader(photoUtils.bitmap2byte(bitmap));
                }else{
                    Log.d("food","??????????????????");
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

        //????????????
        private void getPermission() {
            //?????????????????????Android6.0?????????????????????????????????????????????????????????
            if (ContextCompat.checkSelfPermission(EditInfoActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //???????????????????????????????????????????????????????????????true
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        EditInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //????????????????????????????????????
                    Toast.makeText(EditInfoActivity.this, "??????SD??????????????????????????????"
                                    + "???????????????????????????????????????????????????????????????",
                            Toast.LENGTH_SHORT).show();
                }
                //????????????
                ActivityCompat.requestPermissions(EditInfoActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else { //?????????????????????????????????
                openAlbum();
            }
        }

        //??????????????????????????????????????????????????????????????????????????????????????????
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
                case 1:
                    if (grantResults.length > 0 && grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED) {
                        //??????????????????????????????
                        openAlbum();
                    } else {
                        //???????????????
                        Toast.makeText(EditInfoActivity.this, "??????????????????????????????????????????????????????",Toast.LENGTH_SHORT).show();
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

        //??????
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            if (requestCode == 1) {
                // ??????????????????
                if (Build.VERSION.SDK_INT >= 19) {
                    // 4.4?????????????????????????????????????????????
                    handleImageOnKitKat(data);
                } else {
                    // 4.4??????????????????????????????????????????
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
                // ?????????document?????????Uri????????????document id??????
                String docId = DocumentsContract.getDocumentId(uri);
                Log.d("TAG", "handleImageOnKitKat: docId" + docId);

                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1]; // ????????????????????????id
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
                    imagePath = getImagePath(contentUri, null);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                // ?????????content?????????Uri??????????????????????????????
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                // ?????????file?????????Uri?????????????????????????????????
                imagePath = uri.getPath();
            }
            displayImage(imagePath); // ??????????????????????????????
        }

        private void handleImageBeforeKitKat(Intent data) {
            Uri uri = data.getData();
            String imagePath = getImagePath(uri, null);
            displayImage(imagePath);
        }

        private String getImagePath(Uri uri, String selection) {
            String path = null;
            // ??????Uri???selection??????????????????????????????
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