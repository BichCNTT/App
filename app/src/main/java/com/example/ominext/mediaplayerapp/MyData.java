package com.example.ominext.mediaplayerapp;

/**
 * Created by Ominext on 7/25/2017.
 */

public class MyData {

    String url;
    String name;
    String time;

    public MyData() {

    }

    public MyData(String url, String name, String time) {
        this.url = url;
        this.name = name;
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String toString(){
        return name;
    }
}
