package com.uart.hbapp.bean;

/**
 * Created by admin on 2019/9/16.
 */

public class MusicBean {
    public String musicName;
    public String musicTime;
    public String musicState;
    public String singer;
    public String path;
    public int duration;
    public long size;
    public long id;
    public long albumId;
    public boolean musicCheck;

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicTime() {
        return musicTime;
    }

    public void setMusicTime(String musicTime) {
        this.musicTime = musicTime;
    }

    public String getMusicState() {
        return musicState;
    }

    public void setMusicState(String musicState) {
        this.musicState = musicState;
    }

    public boolean isMusicCheck() {
        return musicCheck;
    }

    public void setMusicCheck(boolean musicCheck) {
        this.musicCheck = musicCheck;
    }
}
