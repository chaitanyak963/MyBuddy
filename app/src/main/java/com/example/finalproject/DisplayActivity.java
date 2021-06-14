package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayActivity extends AppCompatActivity {
    ImageView iv;
    TextView s, s1, s2, s3, s4;
    Button b;
    ArrayList<Pojo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        iv = findViewById(R.id.image);
        s = findViewById(R.id.tv);
        s1 = findViewById(R.id.tv1);
        s2 = findViewById(R.id.tv2);
        s3 = findViewById(R.id.tv3);
        s4 = findViewById(R.id.tv4);
      //  lb = findViewById(R.id.lb);
        b = findViewById(R.id.b);
        DateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String c1 = getIntent().getStringExtra("name");
        String c2 = getIntent().getStringExtra("urlToImage");
        String c3 = getIntent().getStringExtra("title");
        String c4 = getIntent().getStringExtra("author");
        String c5 = getIntent().getStringExtra("description");
        String c7 = getIntent().getStringExtra("publishedAt");
        String p = getIntent().getStringExtra("position");
        s.setText(c1);
        s1.setText(c4);
        s2.setText(c3);
        s3.setText(c5);
        s4.setText(c7);
        Glide.with(this).load(c2).into(iv);


    }

    public void readmore(View view) {
        String c6=getIntent().getStringExtra("url");
        Intent i=new Intent(this,BrowserActivity.class);
        i.putExtra("key",c6);
        startActivity(i);
    }
}
