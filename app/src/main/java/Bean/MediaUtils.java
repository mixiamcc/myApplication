package Bean;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import Bean.MediaExtraBean;
//这里面为了行文方便没有编写set和get方法，所以下文中进行测试时候需要注意

public class MediaUtils {

    public Context mContext;
    public MediaUtils(Context context)
    {
        mContext=context;
    }
    public MediaExtraBean getRingDuring(Uri mUri){
        Log.e("YM","文件路径:"+mUri);
        String duration=null;
        MediaExtraBean mediaExtraBean = new MediaExtraBean();
        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();

        try {
            ParcelFileDescriptor parcelFileDescriptor = mContext.getContentResolver().openFileDescriptor(mUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            HashMap<String, String> headers=null;
            headers = new HashMap<String, String>();
            headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
//                mmr.setDataSource(mUri, headers);
            mmr.setDataSource(fileDescriptor);
            duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
            mediaExtraBean.setDuration(Integer.parseInt(duration));
            Log.e("YM","时长:"+duration);
            Bitmap bitmap = mmr.getFrameAtTime();//获取第一帧,然后保存到本地
// Load thumbnail of a specific media item.
//            Bitmap thumbnail =
//                    ImProvider.context.getApplicationContext().getContentResolver().loadThumbnail(
//                            mUri, new Size(640, 480), null);
            Uri uri = createImageFile();
            mContext.grantUriPermission(mContext.getPackageName(), uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                ParcelFileDescriptor imageFd = mContext.getContentResolver().openFileDescriptor(uri, "w");
                FileDescriptor thumb = imageFd.getFileDescriptor();
                FileOutputStream fileOutputStream = new FileOutputStream(thumb);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                mediaExtraBean.setThumbPath(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mmr.release();
        }
        return mediaExtraBean;
    }

    private Uri createImageFile(){
        Uri uri;
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = String.format("JPEG_%s.png", timeStamp);
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,imageFileName);
        contentValues.put(MediaStore.Audio.Media.MIME_TYPE, "image/png");//修改文件类型，默认为jpg
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            uri =mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        } else {
            uri = mContext.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, contentValues);
        }
        Log.e("YM","文件路径:"+uri.toString());
        return uri;
    }



}



