package com.example.myapplication.data;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class UserShow extends  LitePalSupport {

    private String showId;
    private String text;
    private byte[] photo;
    private Date showtime;

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getShowId() {
        return showId;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setShowtime(Date showtime) {
        this.showtime = showtime;
    }

    public Date getShowtime() {
        return showtime;
    }
}
