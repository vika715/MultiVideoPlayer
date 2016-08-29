package com.example.multivideoplayer;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.exoplayer.util.Util;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //List<>
        Uri uri = Uri.parse("http://0.s3.envato.com/h264-video-previews/80fad324-9db4-11e3-bf3d-0050569255a8/490527.mp4");
        Uri uri2 = Uri.parse("https://d2zihajmogu5jn.cloudfront.net/bipbop-advanced/bipbop_16x9_variant.m3u8");
        //VideoPlayerFragment fragment = VideoPlayerFragment.newInstance(uri, Util.TYPE_OTHER);
        VideoPlayerFragment fragment2 = VideoPlayerFragment.newInstance(uri2, Util.TYPE_HLS);
        //VideoPlayerFragment fragment3 = VideoPlayerFragment.newInstance(uri, Util.TYPE_OTHER);
        VideoPlayerFragment fragment4 = VideoPlayerFragment.newInstance(uri2, Util.TYPE_HLS);
        //getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment2).commit();
        //getSupportFragmentManager().beginTransaction().add(R.id.container, fragment3).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment4).commit();
    }
}
