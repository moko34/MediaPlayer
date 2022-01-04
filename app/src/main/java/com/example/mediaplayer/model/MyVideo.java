package com.example.mediaplayer.model;

import android.net.Uri;

public class MyVideo {
    private final String name;
    private final  int duration;
    private final Uri uri;

    public MyVideo(String name, int duration, Uri uri) {
        this.name = name;
        this.duration = duration;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public Uri getUri() {
        return uri;
    }
}
