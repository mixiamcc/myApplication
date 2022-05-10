package com.example.myapplication.data;


import android.graphics.Bitmap;
import android.media.Image;

import org.litepal.crud.LitePalSupport;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Friend extends LitePalSupport {

    private String username;//用户名，唯一
    private String password;
    //private Bitmap header_photo;
    private byte[] header_photo;
    private String nickname="匿名用户";//昵称
    private String category="未知";//身份
    private String gender="未知";//性别
    private String  birthday="未知";
    private List<Course> courseList=new ArrayList<Course>();
    //private List<UserData> friends=  new ArrayList<UserData>();
    private List<UserShow> shows =new ArrayList<UserShow>();

    // public UserData()
//    {
//        friends.add(this);
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getHeader() {
        return header_photo;
    }

    public void setHeader(byte[] header) {
        header_photo = header;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender=gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


//    public List<UserData> getFriends()
//    {
//        return friends;
//    }
//
//    public void setFriends(List<UserData> friends) {
//        this.friends = friends;
//    }

    public List<UserShow> getShows() {
        return shows;
    }

    public void setShows(List<UserShow> shows) {
        this.shows = shows;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public byte[] getHeader_photo() {
        return header_photo;
    }

    public void setHeader_photo(byte[] header_photo) {
        this.header_photo = header_photo;
    }
}

