package com.example.zuoye.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.zuoye.R;

public class MsgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        WebView wv = (WebView) findViewById(R.id.wv_web);
        wv.loadUrl(url);

    }
}
