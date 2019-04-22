package com.example.photoapp;

import android.app.Application;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    public MyApplication() {
    }

    List<Uri> uriList = new ArrayList<>();

    public MyApplication(List<Uri> uriList) {
        this.uriList = uriList;
    }

    public List<Uri> getUriList() {
        return uriList;
    }

    public void setUriList(List<Uri> uriList) {
        this.uriList = uriList;
    }
}
