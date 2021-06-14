package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.finalproject.RDatabase.Rdb;
import com.example.finalproject.RDatabase.UserDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

public class SecondActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    GPSTracker gps;
    Rdb rdb;
    String en1,en2,en3;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        gps = new GPSTracker(this);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this, WebActivity.class));
            }
        });
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_chat,R.id.navigation_user, R.id.navigation_news)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        rdb = Room.databaseBuilder(this, Rdb.class, "MYROOM").allowMainThreadQueries().build();
        List<UserDatabase> dummy = rdb.myDao().getAllData();
        if (dummy.size()==0) {
            Toast.makeText(this, "Please add the profile data first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, EmrgencyActivity.class));
        } else {
            en1 = dummy.get(0).getEn1();
            en2 = dummy.get(0).getEn2();
            en3 = dummy.get(0).getEn3();
        }
        reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menus,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bot:
                startActivity(new Intent(this, BotActivity.class));
                break;
            case R.id.profile:
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                boolean dialogShown = settings.getBoolean("dialogShown", false);
                if (!dialogShown) {
                    startActivity(new Intent(this, EmrgencyActivity.class));
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("dialogShown", true);
                    editor.commit();
                } else {
                    startActivity(new Intent(this, ProfileActivity.class));
                }
                break;
            case R.id.alert:

                String num[] = {en1, en2, en3};
                String sms = "i am in danger :";
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                SmsManager smsManager = SmsManager.getDefault();
                for (int j = 0; j < 3; j++) {
                    smsManager.sendTextMessage(""+num[j], null, sms + "https://www.google.com/maps/@" + latitude + "," + longitude + ",18z", null, null);
                    Toast.makeText(this, "Alert message sent!", Toast.LENGTH_SHORT).show();
                }
                break;

        }

        return super.onOptionsItemSelected(item);
    }
        private void status(String status){
        reference  = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("status",status);
            reference.updateChildren(hashMap);

        }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}



