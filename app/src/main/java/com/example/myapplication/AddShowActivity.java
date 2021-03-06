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

        //????????????back???????????????
        ivShowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //???????????????
        //????????????
        btnUpShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //????????????
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
                if(ContextCompat.checkSelfPermission(AddShowActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AddShowActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
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
                    userShow.setPhoto(photoUtils.bitmap2byte(bitmap));
                    ivUpShowPhoto.setImageBitmap(bitmap);
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
                    ivUpShowPhoto.setImageBitmap(bitmap);
                    userShow.setPhoto(photoUtils.bitmap2byte(bitmap));
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
            if (ContextCompat.checkSelfPermission(AddShowActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //???????????????????????????????????????????????????????????????true
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        AddShowActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //????????????????????????????????????
                    Toast.makeText(AddShowActivity.this, "??????SD??????????????????????????????"
                                    + "???????????????????????????????????????????????????????????????",
                            Toast.LENGTH_SHORT).show();
                }
                //????????????
                ActivityCompat.requestPermissions(AddShowActivity.this,
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
                        Toast.makeText(AddShowActivity.this, "??????????????????????????????????????????????????????",Toast.LENGTH_SHORT).show();
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
            if (DocumentsContract.isDocumentUri(AddShowActivity.this, uri)) {
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
                ivUpShowPhoto.setImageBitmap(bitmap);
            } else {
                Toast.makeText(AddShowActivity.this, "failed to get image", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
