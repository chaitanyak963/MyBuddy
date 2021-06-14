package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalproject.RDatabase.Rdb;
import com.example.finalproject.RDatabase.UserDatabase;
import com.example.finalproject.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    Rdb rdb;
    TextView tv,tv1,tv2,tv3,tv4,tv5;
    List<UserDatabase> list;
    CircleImageView civ;
    DatabaseReference reference;
    FirebaseUser fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        list = new ArrayList<>();
        tv = findViewById(R.id.name);
        tv1 = findViewById(R.id.mail);
        tv2 = findViewById(R.id.num);
        tv3 = findViewById(R.id.enum1);
        tv4 = findViewById(R.id.enum2);
        tv5 = findViewById(R.id.enum3);
        civ = findViewById(R.id.dpic);
        rdb = Room.databaseBuilder(this, Rdb.class, "MYROOM").allowMainThreadQueries().build();
        List<UserDatabase> dummy = rdb.myDao().getAllData();
        if (dummy.size()==0) {
            Toast.makeText(this, "Please add the profile data first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, EmrgencyActivity.class));
        } else {
            tv.setText(dummy.get(0).getName());
            tv1.setText(dummy.get(0).getNum());
            tv2.setText(dummy.get(0).getMail());
            tv3.setText(dummy.get(0).getEn1());
            tv4.setText(dummy.get(0).getEn2());
            tv5.setText(dummy.get(0).getEn3());

        }
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getImageURL().equals("default")){
                    civ.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(ProfileActivity.this).load(user.getImageURL()).into(civ);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit:
                Intent i = new Intent(this, EmrgencyActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
