package com.example.myapplication.data;

import android.net.Uri;

import org.litepal.crud.LitePalSupport;

public class Course extends LitePalSupport {
    String courseId;
    String courseName;
    String courseCategory;
    String courseUrl;

    Uri coursePhoto;
    //byte[] coursePhoto=new byte[];
    String authorId;


//    public void setCoursePhoto(byte[] coursePhoto) {
//        this.coursePhoto = coursePhoto;
//    }
//
//    public byte[] getCoursePhoto() {
//        return coursePhoto;
//    }


    public void setCoursePhoto(Uri coursePhoto) {
        this.coursePhoto = coursePhoto;
    }

    public Uri getCoursePhoto() {
        return coursePhoto;
    }

    public void setCourseId(String courseId)
    {
        this.courseId=courseId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseCategory(String courseCategory) {
        this.courseCategory = courseCategory;
    }

    public String getCourseCategory() {
        return courseCategory;
    }

    public void setCourseUrl(String courseUrl) {
        this.courseUrl = courseUrl;
    }
    public String getCourseUrl() {
        return courseUrl;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorId() {
        return authorId;
    }
}

