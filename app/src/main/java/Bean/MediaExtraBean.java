package Bean;

import android.net.Uri;

public class MediaExtraBean {
    public Uri thumbPath;//缩略图路径
    public String localPath = "";//路径
    public String title  = "";//视频名称
    public int duration;//视频时长
    public long size;//视频大小
    public String imagePath;//视频缩略图路径

    public void setThumbPath(Uri thumbPath) {
        this.thumbPath = thumbPath;
    }

    public Uri getThumbPath() {
        return thumbPath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

}
