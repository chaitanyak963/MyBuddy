package com.example.finalproject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;


public class BrowserActivity extends AppCompatActivity {
    WebView wv;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);



        wv = findViewById(R.id.web);
        wv.setWebViewClient(new WebViewClient());
        wv.loadUrl(getIntent().getStringExtra("key"));
        wv.getSettings().setJavaScriptEnabled(true);



    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        if (wv.canGoBack()){
            wv.goBack();
        }else{
        super.onBackPressed();
    }}
}
